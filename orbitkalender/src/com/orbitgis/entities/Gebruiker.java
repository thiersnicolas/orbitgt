package com.orbitgis.entities;

import java.time.LocalDate;

public class Gebruiker implements Comparable<Gebruiker>{
	private String naam;
	private LocalDate beginVerlof;
	private LocalDate eindeVerlof;

	public Gebruiker(String naam, LocalDate beginVerlof, LocalDate eindeVerlof) {
		this.naam = naam;
		this.beginVerlof = beginVerlof;
		this.eindeVerlof = eindeVerlof;
	}

	public LocalDate getBeginVerlof() {
		return beginVerlof;
	}

	public void setBeginVerlof(LocalDate beginVerlof) {
		this.beginVerlof = beginVerlof;
	}

	public LocalDate getEindeVerlof() {
		return eindeVerlof;
	}

	public void setEindeVerlof(LocalDate eindeVerlof) {
		this.eindeVerlof = eindeVerlof;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}
	
	public boolean heeftVerlof(LocalDate datum) {
		if (datum.isAfter(this.beginVerlof)&& datum.isBefore(this.eindeVerlof)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((naam == null) ? 0 : naam.hashCode());
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
		Gebruiker other = (Gebruiker) obj;
		if (naam == null) {
			if (other.naam != null)
				return false;
		} else if (!naam.equals(other.naam))
			return false;
		return true;
	}

	@Override
	public int compareTo(Gebruiker gebruiker) {
		return this.naam.compareTo(gebruiker.getNaam());
	}
	
	
	
}
