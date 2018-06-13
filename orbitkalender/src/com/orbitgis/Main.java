package com.orbitgis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.orbitgis.entities.Gebruiker;
import com.orbitgis.entities.Kalender;
import com.orbitgis.entities.Taak;
import com.orbitgis.repository.KalenderRepository;
import com.orbitgis.util.KalenderCreator;
import com.orbitgis.util.KalenderFileRW;
import com.orbitgis.util.Util;

public class Main {

	public static void main(String[] args) {
		List<Gebruiker> gebruikers = new ArrayList<>();
		gebruikers.add(new Gebruiker("Mark", LocalDate.of(2018, 7, 15), LocalDate.of(2018, 7, 29)));
		gebruikers.add(new Gebruiker("Jef", LocalDate.of(2018, 7, 22), LocalDate.of(2018, 8, 12)));
		gebruikers.add(new Gebruiker("Paul", LocalDate.of(2018, 8, 5), LocalDate.of(2018, 8, 26)));
		gebruikers.add(new Gebruiker("Jan", LocalDate.of(2018, 8, 19), LocalDate.of(2018, 9, 9)));

		List<Taak> taken = new ArrayList<>();
		taken.add(new Taak("Hagen scheren"));
		taken.add(new Taak("Gras maaien"));
		taken.add(new Taak("Terras vegen"));
		taken.add(new Taak("Planten water geven"));

		LocalDate beginDatum = LocalDate.of(2018, 7, 01);
		LocalDate eindDatum = LocalDate.of(2018, 9, 30);

		Kalender kalender = KalenderCreator
				.takenVerdelen(KalenderCreator.maakKalenderVanZondagen(beginDatum, eindDatum, gebruikers, taken));
		
		Util.kalenderPrinten(kalender);
		
		KalenderRepository kalenderRepository = new KalenderRepository();
		kalenderRepository.kalenderNaarDatabase(kalender);
		
		Kalender kalenderUitDatabase = kalenderRepository.getKalenderUitDatabaseViaDriverManager();
		Util.kalenderPrinten(kalenderUitDatabase);
		
		for (Taak taak:kalender.getTaken()) {
			System.out.println(taak.getNaam());
		}
		for (Gebruiker gebruiker:kalender.getGebruikers()) {
			System.out.println(gebruiker.getNaam() + " - " + gebruiker.getBeginVerlof() + " - " + gebruiker.getEindeVerlof());
		}
		
		String locatie = "C:\\Users\\nicolas.thiers\\"
                + "Desktop\\kalender.xls";
		System.out.println(KalenderFileRW.kalenderNaarExcel(kalenderUitDatabase, locatie));
	}

}
