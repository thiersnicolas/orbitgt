package com.orbitgis.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.orbitgis.entities.*;

public class KalenderCreator {

	public static Kalender maakKalenderVanZondagen(LocalDate beginDatum, LocalDate eindDatum, List<Gebruiker> gebruikers) {
		Map<LocalDate, Set<KalenderEntry>> kalenderMap = new TreeMap<>();

		Set<LocalDate> datumsSet = new TreeSet<LocalDate>();
		LocalDate kalenderDatum = beginDatum;
		Set<KalenderEntry> legeKalenderEntrySet = new TreeSet<>();

		do {
			if (kalenderDatum.getDayOfWeek().getValue() == 7) {
				datumsSet.add(kalenderDatum);
				kalenderMap.put(kalenderDatum, legeKalenderEntrySet);
				kalenderDatum = kalenderDatum.plusDays(7);
			} else {
				kalenderDatum = kalenderDatum.plusDays(1);
			}
		} while (kalenderDatum.isBefore(eindDatum));

		Kalender kalender = new Kalender(kalenderMap, gebruikers);
		return kalender;
	}

	public static Kalender takenVerdelen(Kalender kalender, List<Taak> taken) {
		Map<LocalDate, Set<KalenderEntry>> kalenderMap = kalender.getKalenderMap();
		List<Gebruiker> gebruikers = kalender.getGebruikers();
		for (LocalDate key : kalenderMap.keySet()) {
			kalenderMap.put(key, dagTakenVerdelen(key, gebruikers, taken));
		}
		return kalender;
	}

	private static Set<KalenderEntry> dagTakenVerdelen(LocalDate kalenderDatum, List<Gebruiker> gebruikers,
			List<Taak> taken) {
		Set<KalenderEntry> kalenderEntrySet = new TreeSet<>();
		List<Gebruiker> beschikbareGebruikersLijst = new ArrayList<>();
		for (Gebruiker gebruiker : gebruikers) {
			if (gebruiker.heeftVerlof(kalenderDatum)) {
				kalenderEntrySet.add(new KalenderEntry(kalenderDatum, "verlof", gebruiker));
			} else {
				beschikbareGebruikersLijst.add(gebruiker);
			}
		}
		// int aantalTaken = taken.size();
		List<Gebruiker> gebruikersShuffled = Util.shuffleList(gebruikers);

		if (taken.size() == beschikbareGebruikersLijst.size()) {
			for (int i = 0; i < taken.size(); i++) {
				kalenderEntrySet
						.add(new KalenderEntry(kalenderDatum, taken.get(i).getNaam(), gebruikersShuffled.get(i)));
			}
			return kalenderEntrySet;
		} else {
			if (taken.size() > gebruikers.size() && taken.size() != 0 && gebruikers.size() != 0) {
				int aantalTaken = taken.size() / gebruikers.size();
				int restTaken = taken.size() % gebruikers.size();
				String toeTeVoegenTaken = "";
				for (Gebruiker gebruiker : gebruikersShuffled) {
					for (int i = 1; i < aantalTaken && restTaken != 0; i++) {
						toeTeVoegenTaken = toeTeVoegenTaken + taken.get(i - 1).getNaam() + ",";
						if ((i % aantalTaken) == 0) {
							if (restTaken > 0) {
								toeTeVoegenTaken = toeTeVoegenTaken + taken.get(taken.size() - restTaken).getNaam()
										+ ",";
								kalenderEntrySet.add(new KalenderEntry(kalenderDatum, toeTeVoegenTaken, gebruiker));
								restTaken--;
							}
						}
					}
				}
				return kalenderEntrySet;
			} else {
				int i = 0;
				for (Gebruiker gebruiker : gebruikersShuffled) {
					kalenderEntrySet.add(new KalenderEntry(kalenderDatum, taken.get(i).getNaam(), gebruiker));
					i++;
					if (i == taken.size()) {
						break;
					}
				}
				return kalenderEntrySet;
			}

		}

	}
}
