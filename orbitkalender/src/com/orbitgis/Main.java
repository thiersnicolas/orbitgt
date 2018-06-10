package com.orbitgis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.orbitgis.entities.*;
import com.orbitgis.util.KalenderCreator;

public class Main {

	public static void main(String[] args) {
		List<Gebruiker> gebruikers = new ArrayList<>();
		gebruikers.add(new Gebruiker("Mark", LocalDate.of(2018, 7, 15), LocalDate.of(29, 7, 2018)));
		gebruikers.add(new Gebruiker("Jef", LocalDate.of(2018, 7, 22), LocalDate.of(12, 8, 2018)));
		gebruikers.add(new Gebruiker("Paul", LocalDate.of(2018, 8, 5), LocalDate.of(26, 8, 2018)));
		gebruikers.add(new Gebruiker("Jan", LocalDate.of(2018, 8, 19), LocalDate.of(9, 9, 2018)));
		
		List<Taak> taken = new ArrayList<>();
		taken.add(new Taak("Hagen scheren"));
		taken.add(new Taak("Gras maaien"));
		taken.add(new Taak("Tarras vegen"));
		taken.add(new Taak("Planten water geven"));
		
		LocalDate beginDatum = LocalDate.of(2018, 7, 01);
		LocalDate eindDatum = LocalDate.of(2018, 9, 30);
		
		Kalender kalender = KalenderCreator.maakKalenderVanZondagen(beginDatum, eindDatum, gebruikers);
		kalender = KalenderCreator.takenVerdelen(kalender, taken);
		
		
	}

}
