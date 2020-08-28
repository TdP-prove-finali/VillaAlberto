package it.polito.tdp.pasto.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento> {

	public enum EventType {
		ARRIVA_TAVOLO, ACCOMODA_TAVOLO, SERVITO_TAVOLO, SCONTRINO_TAVOLO, INIZIO_PREPARAZIONE_PIATTO,
		INIZIO_CONDIMENTO_PIATTO, FINE_PREPARAZIONE_PIATTO
	}

	public LocalDateTime istante;
	public EventType tipo;
	public Tavolo tavolo;

	public Evento(LocalDateTime istante, EventType tipo, Tavolo tavolo) {

		this.istante = istante;
		this.tipo = tipo;
		this.tavolo = tavolo;
	}

	@Override
	public String toString() {
		return "istante:" + istante;// + " tipo:" + tipo + " tavolo:" + tavolo;
	}

	public LocalDateTime getIstante() {
		return istante;
	}

	public EventType getTipo() {
		return tipo;
	}

	public Tavolo getTavolo() {
		return tavolo;
	}

	@Override
	public int compareTo(Evento o) {
		return this.getIstante().compareTo(o.istante);
	}

}
