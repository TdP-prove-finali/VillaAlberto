package it.polito.tdp.pasto.model;

public class Pasta {

	private Integer id;
	private String nome;
	private Integer cotturaMax;
	private Integer cotturaMin;
	private String descrizione;

	public Pasta(Integer id, String nome, Integer cotturaMax, Integer cotturaMin, String descrizione) {
		this.id = id;
		this.nome = nome;
		this.cotturaMax = cotturaMax;
		this.cotturaMin = cotturaMin;
		this.descrizione = descrizione;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Integer getCotturaMax() {
		return cotturaMax;
	}

	public Integer getCotturaMin() {
		return cotturaMin;
	}

	public String getDescrizione() {
		return descrizione;
	}

	@Override
	public String toString() {
		return nome;
	}

}
