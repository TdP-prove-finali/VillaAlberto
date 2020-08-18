package it.polito.tdp.crimes.model;

public class Condimento {
	
	private Integer id;
	private String nome;
	private Integer tempo;
	private String descrizione;
	public Condimento(Integer id, String nome, Integer tempo, String descrizione) {
		super();
		this.id = id;
		this.nome = nome;
		this.tempo = tempo;
		this.descrizione = descrizione;
	}
	public Integer getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public Integer getTempo() {
		return tempo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	@Override
	public String toString() {
		return "id:" + id + " nome:" + nome + " tempo:" + tempo + " descrizione:" + descrizione;
	}
	

	
	
	

}
