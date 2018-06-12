package com.orbitgis.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.orbitgis.entities.*;

public class Util {
	public static List<Gebruiker> shuffleList(List<Gebruiker> list){
		List<Gebruiker> listShuffled = new ArrayList<>();
		SecureRandom random = new SecureRandom();
		int aantalGebruikers = list.size();
		 for(int i=0; i<aantalGebruikers; i++) {
			 int randomInt = random.nextInt(list.size());
			 listShuffled.add(list.get(randomInt));
			 list.remove(randomInt);
		 }
		 return listShuffled;
	 }
	
	public static Gebruiker getGebruiker(List<Gebruiker> gebruikers, String naam) {
		Gebruiker gebruiker = null;
		for(Gebruiker gebruiker2: gebruikers){
			if (gebruiker2.getNaam().equals(naam)) {
				gebruiker = gebruiker2;
			}
		}
		return gebruiker;
	}
	
	public static Taak getTaak(List<Taak> taken, String naam) {
		Taak taak = null;
		for(Taak taak2: taken){
			if (taak2.getNaam().equals(naam)) {
				taak = taak2;
			}
		}
		return taak;
	}
	
	public static void kalenderPrinten(Kalender kalender) {
		System.out.println("Kalender:");
		for (LocalDate key : kalender.getKalenderMap().keySet()) {
			System.out.print(key + ":");
			for (KalenderEntry kalenderentry : kalender.getKalenderMap().get(key)) {
				System.out.print("\t\t" + kalenderentry.getGebruiker().getNaam() + "-" + kalenderentry.getEntry());
			}
			System.out.print("\n");
		}
	}
}
