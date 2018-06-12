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

	public static Kalender maakKalenderVanZondagen(LocalDate beginDatum, LocalDate eindDatum,
			List<Gebruiker> gebruikers, List<Taak> taken) {
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
		} while (kalenderDatum.isBefore(eindDatum.plusDays(1)));

		Kalender kalender = new Kalender(kalenderMap, gebruikers, taken);
		return kalender;
	}

	public static Kalender takenVerdelen(Kalender kalender) {
		Map<LocalDate, Set<KalenderEntry>> kalenderMap = kalender.getKalenderMap();
		List<Gebruiker> gebruikers = kalender.getGebruikers();
		List<Taak> taken = kalender.getTaken();
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
		List<Gebruiker> beschikbareGebruikersShuffled = Util.shuffleList(beschikbareGebruikersLijst);

		if (taken.size() == beschikbareGebruikersLijst.size()) {
			for (int i = 0; i < taken.size(); i++) {
				kalenderEntrySet.add(
						new KalenderEntry(kalenderDatum, taken.get(i).getNaam(), beschikbareGebruikersShuffled.get(i)));
			}
			return kalenderEntrySet;
		} else {
			if (taken.size() > beschikbareGebruikersShuffled.size() && taken.size() != 0 && gebruikers.size() != 0) {
				int aantalTakenConstant = taken.size() / beschikbareGebruikersShuffled.size();
				int restTaken = taken.size() % beschikbareGebruikersShuffled.size();
				List<Taak> takenUitTeDelen = new ArrayList<>();
				for (Taak taak : taken) {
					takenUitTeDelen.add(taak);
				}
				for (Gebruiker gebruiker : beschikbareGebruikersShuffled) {
					int aantalTaken = aantalTakenConstant;
					String toeTeVoegenTaken = "";
					for (int i = 1; i <= aantalTaken; i++) {
						if (i==1) {
							toeTeVoegenTaken = toeTeVoegenTaken + takenUitTeDelen.get(0).getNaam();
						} else {
							toeTeVoegenTaken = toeTeVoegenTaken + "," + takenUitTeDelen.get(0).getNaam();
						}
						takenUitTeDelen.remove(0);
						if ((i % aantalTaken) == 0) {
							if (restTaken > 0) {
								toeTeVoegenTaken = toeTeVoegenTaken + "," + takenUitTeDelen.get(0).getNaam();
								takenUitTeDelen.remove(0);
								restTaken--;
							}
							kalenderEntrySet.add(new KalenderEntry(kalenderDatum, toeTeVoegenTaken, gebruiker));
						}
					}
				}
				return kalenderEntrySet;
			} else {
				int i = 0;
				for (Gebruiker gebruiker : beschikbareGebruikersShuffled) {
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
