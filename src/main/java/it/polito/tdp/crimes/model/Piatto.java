package it.polito.tdp.crimes.model;

public class Piatto {
	private Integer id;
	private String nome;
	private int preparazione;
	private String descrizione;
	
	
	public Piatto(Integer id, String nome, int preparazione, String descrizione) {
		super();
		this.id = id;
		this.nome = nome;
		this.preparazione = preparazione;
		this.descrizione = descrizione;
	}


	public Integer getId() {
		return id;
	}


	public String getNome() {
		return nome;
	}


	public int getPreparazione() {
		return preparazione;
	}


	public String getDescrizione() {
		return descrizione;
	}


	@Override
	public String toString() {
		return "id:" + id + " nome:" + nome + " preparazione:" + preparazione + " descrizione:" + descrizione;
	}
	
	
}
