package com.orbitgis.entities;

import java.time.LocalDate;

public class KalenderEntry implements Comparable<KalenderEntry> {
	private LocalDate datum;
	private String entry;
	private Gebruiker gebruiker;
	
	public KalenderEntry(LocalDate datum, String entry, Gebruiker gebruiker) {
		this.datum = datum;
		this.entry = entry;
		this.gebruiker = gebruiker;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Gebruiker getGebruiker() {
		return gebruiker;
	}

	public void setGebruiker(Gebruiker gebruiker) {
		this.gebruiker = gebruiker;
	}

	public LocalDate getDatum() {
		return datum;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((gebruiker == null) ? 0 : gebruiker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KalenderEntry other = (KalenderEntry) obj;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (gebruiker == null) {
			if (other.gebruiker != null)
				return false;
		} else if (!gebruiker.equals(other.gebruiker))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(KalenderEntry kalenderEntry) {
		if (this.equals(kalenderEntry)) {
			return 0;
		}
		if (!(this.datum.equals(kalenderEntry.getDatum()))) {
			return this.datum.compareTo(kalenderEntry.getDatum());
		} else {
			return this.gebruiker.compareTo(kalenderEntry.getGebruiker());
		}
	}
		
}