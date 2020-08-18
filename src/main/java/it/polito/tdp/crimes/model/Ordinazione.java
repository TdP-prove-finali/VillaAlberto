package it.polito.tdp.crimes.model;

public class Ordinazione {
	private Integer id;
	private Integer tavolo;
	private boolean daCondire;
	private Piatto piatto;
	private Pasta pasta;
	private Condimento condimento;
	public Ordinazione(Integer id, Integer tavolo, boolean daCondire, Piatto piatto, Pasta pasta,
			Condimento condimento) {
		super();
		this.id = id;
		this.tavolo = tavolo;
		this.daCondire = daCondire;
		this.piatto = piatto;
		this.pasta = pasta;
		this.condimento = condimento;
	}
	public Integer getId() {
		return id;
	}
	public Integer getTavolo() {
		return tavolo;
	}
	public boolean isDaCondire() {
		return daCondire;
	}
	public Piatto getPiatto() {
		return piatto;
	}
	public Pasta getPasta() {
		return pasta;
	}
	public Condimento getCondimento() {
		return condimento;
	}
	@Override
	public String toString() {
		if (daCondire)
		return pasta.getNome()+" + "+condimento.getNome();
		else return piatto.getNome();
	}
	
}
