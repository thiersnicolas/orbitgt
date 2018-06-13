package com.orbitgis.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.orbitgis.entities.Gebruiker;
import com.orbitgis.entities.Kalender;
import com.orbitgis.entities.KalenderEntry;
import com.orbitgis.entities.Taak;
import com.orbitgis.util.Util;

public class KalenderRepository extends AbstractRepository {
	private static final String URL = "jdbc:mysql://localhost/orbitkalender?useSSL=false";
	private static final String USER = "cursist";
	private static final String PASSWORD = "cursist";

	private static final String DROP_TABLE_KALENDER = "drop table if exists kalender";
	private static final String CREATE_TABLE_KALENDER = "create table kalender (datum date not null)";

	private static final String ALTER_TABLE_GEBRUIKERS = "alter table kalender add ? varchar(?)"; // Gebruikers nog in
																									// te vullen

	private static final String INSERT_ENTRIES_KALENDER = "insert into kalender (?) values (?)"; // Gebruikers nog in te
																									// vullen

	private static final String SELECT_ALL = "select * from kalender";

	public final Kalender getKalenderUitDatabaseViaDataSource() {
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);

			Map<LocalDate, Set<KalenderEntry>> kalenderMap = new TreeMap<>();
			List<Gebruiker> gebruikers = new ArrayList<>();
			List<Taak> taken = new ArrayList<>();

			metaDateKolommenNaarGebruikers(resultSet, gebruikers);

			while (resultSet.next()) {
				resultSetRijnaarKalenderMap(resultSet, gebruikers, kalenderMap, taken);
			}

			connection.commit();
			Collections.sort(gebruikers);
			Kalender kalender = new Kalender(kalenderMap, gebruikers, taken);
			return kalender;

		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	public final Kalender getKalenderUitDatabaseViaDriverManager() {
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);

			Map<LocalDate, Set<KalenderEntry>> kalenderMap = new TreeMap<>();
			List<Gebruiker> gebruikers = new ArrayList<>();
			List<Taak> taken = new ArrayList<>();

			metaDateKolommenNaarGebruikers(resultSet, gebruikers);

			while (resultSet.next()) {
				resultSetRijnaarKalenderMap(resultSet, gebruikers, kalenderMap, taken);
			}

			connection.commit();
			Collections.sort(gebruikers);
			Kalender kalender = new Kalender(kalenderMap, gebruikers, taken);
			return kalender;

		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	private void takenInlezen(String entry, List<Taak> taken) {
		if (entry.contains(",")) {
			String[] entryArray = entry.split(",");
			for (int i = 0; i < entryArray.length; i++) {
				if (Util.getTaak(taken, entryArray[i]) == null) {
					taken.add(new Taak(entryArray[i]));
				}
			}
		}
		if (!(entry.isEmpty() || entry.equals("verlof"))) {
			if (Util.getTaak(taken, entry) == null) {
				taken.add(new Taak(entry));
			}
		}
	}

	private void resultSetRijnaarKalenderMap(ResultSet resultSet, List<Gebruiker> gebruikers,
			Map<LocalDate, Set<KalenderEntry>> kalenderMap, List<Taak> taken) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		Set<KalenderEntry> kalenderEntrySet = new TreeSet<>();
		LocalDate datum = resultSet.getDate(1).toLocalDate();
		for (int i = 2; i <= metaData.getColumnCount(); i++) {
			Gebruiker gebruiker = Util.getGebruiker(gebruikers, metaData.getColumnName(i));
			KalenderEntry kalenderEntry = new KalenderEntry(datum, resultSet.getString(i), gebruiker);
			kalenderEntrySet.add(kalenderEntry);
			vakantieInlezen(kalenderEntry, gebruikers);
			takenInlezen(resultSet.getString(i), taken);
		}
		kalenderMap.put(datum, kalenderEntrySet);
	}

	private void vakantieInlezen(KalenderEntry kalenderEntry, List<Gebruiker> gebruikers) {
		Gebruiker gebruiker = Util.getGebruiker(gebruikers, kalenderEntry.getGebruiker().getNaam());
		LocalDate beginVerlof = gebruiker.getBeginVerlof();
		LocalDate eindeVerlof = gebruiker.getEindeVerlof();
		LocalDate datum = kalenderEntry.getDatum();
		if (kalenderEntry.getEntry().equals("verlof")) {
			if (beginVerlof == null) {
				gebruiker.setBeginVerlof(datum);
				gebruiker.setEindeVerlof(datum);
			} else {
				if (beginVerlof.isAfter(datum)) {
					gebruiker.setBeginVerlof(datum);
				}
				if (eindeVerlof.isBefore(datum)) {
					gebruiker.setEindeVerlof(datum);
				}
			}
			gebruikers.remove(gebruiker);
			gebruikers.add(gebruiker);
		}
	}

	private void metaDateKolommenNaarGebruikers(ResultSet resultSet, List<Gebruiker> gebruikers) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		for (int i = 2; i <= metaData.getColumnCount(); i++) { // kolom 1 = datum
			gebruikers.add(new Gebruiker(metaData.getColumnName(i)));
		}
	}

	public final void kalenderNaarDatabase(Kalender kalender) {

		List<String> statementStringList = voegGebruikersToeAlterTable(kalender);
		String statementStringEntries = voegGebruikersToeInsertEntries(kalender);

		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement statement = connection.createStatement();
				PreparedStatement preparedStatement1 = connection.prepareStatement(statementStringEntries)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);

			statement.addBatch(DROP_TABLE_KALENDER);
			statement.addBatch(CREATE_TABLE_KALENDER);
			for (String statementString : statementStringList) {
				statement.addBatch(statementString);
			}

			entriesNaarBatch(kalender, preparedStatement1);

			statement.executeBatch();
			preparedStatement1.executeBatch();

			connection.commit();
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	private void entriesNaarBatch(Kalender kalender, PreparedStatement preparedStatement) throws SQLException {

		Set<LocalDate> datumsSet = kalender.getKalenderMap().keySet();
		for (LocalDate datum : datumsSet) {
			preparedStatement.setDate(1, Date.valueOf(datum));
			int i = 2;
			for (KalenderEntry kalenderEntry : kalender.getKalenderMap().get(datum)) {
				preparedStatement.setString(i, kalenderEntry.getEntry().toLowerCase());
				i++;
			}
			preparedStatement.addBatch();
		}
	}

	private List<String> voegGebruikersToeAlterTable(Kalender kalender) {
		int grootteVanTaken = 0;
		for (Set<KalenderEntry> kalenderEntrySet : kalender.getKalenderMap().values()) {
			for (KalenderEntry kalenderEntry : kalenderEntrySet) {
				if (kalenderEntry.getEntry().length() > grootteVanTaken) {
					grootteVanTaken = kalenderEntry.getEntry().length();
				}
			}
		}
		List<Gebruiker> gebruikers = kalender.getGebruikers();
		List<String> statementStrings = new ArrayList<>();
		for (Gebruiker gebruiker : gebruikers) {
			String statementString = ALTER_TABLE_GEBRUIKERS.replaceFirst("\\?", gebruiker.getNaam().toLowerCase());
			statementString = statementString.replaceFirst("\\?", String.valueOf(grootteVanTaken));
			statementStrings.add(statementString);
		}
		return statementStrings;
	}

	private String voegGebruikersToeInsertEntries(Kalender kalender) {
		List<Gebruiker> gebruikers = kalender.getGebruikers();
		Collections.sort(gebruikers);
		String gebruikersNamenString = "datum,";
		String aantalValuesString = "\\?";
		int i = 1;
		for (Gebruiker gebruiker : gebruikers) {
			if (i == 1) {
				gebruikersNamenString = gebruikersNamenString + gebruiker.getNaam();
			} else {
				gebruikersNamenString = gebruikersNamenString + "," + gebruiker.getNaam();
			}
			i++;
			aantalValuesString = aantalValuesString + ",\\?";
		}
		String statementStringEntries = INSERT_ENTRIES_KALENDER.replaceFirst("\\?", gebruikersNamenString);

		statementStringEntries = statementStringEntries.replaceFirst("\\?", aantalValuesString);

		return statementStringEntries;
	}
}
