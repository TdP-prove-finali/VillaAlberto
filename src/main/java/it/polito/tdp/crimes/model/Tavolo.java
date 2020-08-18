package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;

public class Tavolo {
	
	private Integer id;
	private Double importo;
	private LocalDateTime tempoArrivo;
	private LocalDateTime tempoOrdinazione;
	private LocalDateTime tempoServizio;
	private LocalDateTime tempoScontrino;
	public Tavolo(Integer id, Double importo, LocalDateTime tempoArrivo, LocalDateTime tempoOrdinazione,
			LocalDateTime tempoServizio, LocalDateTime tempoScontrino) {
		super();
		this.id = id;
		this.importo = importo;
		this.tempoArrivo = tempoArrivo;
		this.tempoOrdinazione = tempoOrdinazione;
		this.tempoServizio = tempoServizio;
		this.tempoScontrino = tempoScontrino;
	}
	public Integer getId() {
		return id;
	}
	public Double getImporto() {
		return importo;
	}
	public LocalDateTime getTempoArrivo() {
		return tempoArrivo;
	}
	public LocalDateTime getTempoOrdinazione() {
		return tempoOrdinazione;
	}
	public LocalDateTime getTempoServizio() {
		return tempoServizio;
	}
	public LocalDateTime getTempoScontrino() {
		return tempoScontrino;
	}
	
	

}
