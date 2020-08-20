package it.polito.tdp.crimes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Giornata {
	
	private LocalDate giorno;
	


	private int numClienti;
	private double fatturato;
	private int tempiPreparazione;
	private LinkedList<Tavolo> listaTavoli;
	
	public Giornata(int numClienti, double fatturato, int tempiPreparazione) {
		
		this.listaTavoli= new LinkedList<Tavolo>();
		this.numClienti = numClienti;
		this.fatturato = fatturato;
		this.tempiPreparazione = tempiPreparazione;
	}

	
	public void setGiorno(LocalDate localDate) {
		this.giorno = localDate;
	}
	
	public LinkedList<Tavolo> getListaTavoli() {
		return listaTavoli;
	}

	public int getNumClienti() {
		return numClienti;
	}

	public double getFatturato() {
		return fatturato;
	}

	public int getTempiPreparazione() {
		return tempiPreparazione;
	}
	
	public void aggiungiTavoloAGiornata(Tavolo t) {
		listaTavoli.add(t);
	}

	public LocalDate getGiorno() {
		return giorno;
	}
	
	public void aggiornaFatturato(double fat) {
		fatturato+=fat;
	}
	
	public void aggiornaNumClienti(int cli) {
		numClienti+=cli;
	}
	
	public void aggiornaTempi(int tem) {
		tempiPreparazione+=tem;
	}

	@Override
	public String toString() {
		return "giorno:" + giorno + " numClienti:" + numClienti + " fatturato:" + fatturato + " tempiPreparazione:"
				+ tempiPreparazione+"\n";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((giorno == null) ? 0 : giorno.hashCode());
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
		Giornata other = (Giornata) obj;
		if (giorno == null) {
			if (other.giorno != null)
				return false;
		} else if (!giorno.equals(other.giorno))
			return false;
		return true;
	}
	
}
