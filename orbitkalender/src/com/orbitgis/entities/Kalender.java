package com.orbitgis.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Kalender {
	private Map<LocalDate, Set<KalenderEntry>> kalender;
	private List<Gebruiker> gebruikers;

	public Kalender(Map<LocalDate, Set<KalenderEntry>> kalender, List<Gebruiker> gebruikers) {
		this.kalender = kalender;
		this.gebruikers = gebruikers;
	}

	public List<Gebruiker> getGebruikers() {
		return gebruikers;
	}

	public void setGebruikers(List<Gebruiker> gebruikers) {
		this.gebruikers = gebruikers;
	}

	public Map<LocalDate, Set<KalenderEntry>> getKalenderMap() {
		return kalender;
	}

	public void setKalender(Map<LocalDate, Set<KalenderEntry>> kalender) {
		this.kalender = kalender;
	}
	
	

}