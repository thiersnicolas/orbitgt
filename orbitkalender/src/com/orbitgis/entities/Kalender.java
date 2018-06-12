package com.orbitgis.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Kalender {
	private Map<LocalDate, Set<KalenderEntry>> kalenderMap;
	private List<Gebruiker> gebruikers;
	private List<Taak> taken;

	public Kalender(Map<LocalDate, Set<KalenderEntry>> kalenderMap, List<Gebruiker> gebruikers, List<Taak> taken) {
		this.kalenderMap = kalenderMap;
		this.gebruikers = gebruikers;
		this.taken = taken;
	}

	public List<Gebruiker> getGebruikers() {
		return gebruikers;
	}

	public void setGebruikers(List<Gebruiker> gebruikers) {
		this.gebruikers = gebruikers;
	}

	public Map<LocalDate, Set<KalenderEntry>> getKalenderMap() {
		return kalenderMap;
	}

	public void setKalenderMap(Map<LocalDate, Set<KalenderEntry>> kalender) {
		this.kalenderMap = kalender;
	}

	public List<Taak> getTaken() {
		return taken;
	}

	public void setTaken(List<Taak> taken) {
		this.taken = taken;
	}
}