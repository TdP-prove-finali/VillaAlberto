package it.polito.tdp.crimes.model;

import java.util.LinkedList;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	List<Ordinazione> listaOrdinazioniDelTavolo;
	List<Tavolo> tavoliPerRicorsione;
	List<Ordinazione> ordinazioniPerRicorsione;

	public Model() {
		dao = new EventsDao();
		listaOrdinazioniDelTavolo = new LinkedList<Ordinazione>();
	}

	public List<Pasta> listAllPasta() {
		return dao.listAllPasta();
	}

	public List<Condimento> listAllCondimenti() {
		return dao.listAllCondimento();
	}

	public List<Piatto> listtAllPiatti() {
		return dao.listAllPiatto();
	}

	public void avviaSimulazione(int numeroAgenti, int giorno, int mese, int anno) {
//		System.out.println("Sono entrato in avvia Simulazione\n");
//		sim= new Simula(numeroAgenti, grafo, anno, mese, giorno);
//		sim.AvviaSimulazione();

	}

	public void aggiungiInListaTavolo(Ordinazione ordinazione) {
		listaOrdinazioniDelTavolo.add(ordinazione);
		System.out.println(listaOrdinazioniDelTavolo.toString());
	}

	public List<Ordinazione> dammiLista() {
		return listaOrdinazioniDelTavolo;
	}

	public void pulisciListaTavolo() {
		listaOrdinazioniDelTavolo.clear();

	}

	public boolean salvaTavolo(LocalDate giorno, LocalTime oraArrivo, LocalTime oraOrdinazione, LocalTime oraServizio,
			LocalTime oraScontrino, double importo) {

		LocalDateTime arrivo = LocalDateTime.of(giorno, oraArrivo);
		LocalDateTime ordinazione = LocalDateTime.of(giorno, oraOrdinazione);
		LocalDateTime servizio = LocalDateTime.of(giorno, oraServizio);
		LocalDateTime scontrino = LocalDateTime.of(giorno, oraScontrino);

		if (dao.salvaTavolo(importo, arrivo, ordinazione, servizio, scontrino)
				&& dao.salvaListaOrdiniTavolo(listaOrdinazioniDelTavolo)) {
			listaOrdinazioniDelTavolo.clear();
			return true;
		} else
			return false;

		// sentenza di ritorno se tutto è andato a buon fine

	}

	public void prendiDati(int numGiorni) {
		tavoliPerRicorsione = new LinkedList<Tavolo>();
		ordinazioniPerRicorsione = new LinkedList<Ordinazione>();
		dao.prendiDati(tavoliPerRicorsione, ordinazioniPerRicorsione, numGiorni);
//		System.out.println(tavoliPerRicorsione+"\n");
//		System.out.println(ordinazioniPerRicorsione+"\n");

	}
	
	public int giorniEffettivi(int numGiorni) {
		return dao.giorniEffettivi(numGiorni);
	}
	
	public int numeroDiClienti() {
		return ordinazioniPerRicorsione.size();
	}

	public Double fatturato() {
		double fatturato=0.0;
		for(Tavolo t: tavoliPerRicorsione)
			fatturato+=t.getImporto()/100;
		return fatturato;
	}
	
	public int tempoMedioInterarrivo(int numGiorni) {
		int tempo=0;
		for (int i=1; i<tavoliPerRicorsione.size(); i++)
		{	
			LocalDateTime t1= tavoliPerRicorsione.get(i).getTempoArrivo();
			LocalDateTime t2=tavoliPerRicorsione.get(i-1).getTempoArrivo();
			if (t1.getDayOfYear()==t2.getDayOfYear()){
			int min= (int) ChronoUnit.MINUTES.between(t2, t1);
			tempo+=min;
			}
		}
		return tempo/(tavoliPerRicorsione.size()-giorniEffettivi(numGiorni));
	}
	
	public int tempoMedioPresaComanda() {
		int tempo=0;
		for (Tavolo t:tavoliPerRicorsione)
		{
			LocalDateTime t1=t.getTempoArrivo();
			LocalDateTime t2=t.getTempoOrdinazione();
			int min= (int) ChronoUnit.MINUTES.between(t1, t2);
			tempo+=min;
		}
		return tempo/tavoliPerRicorsione.size();
	}
	
	public int tempoMedioCucina() {
		int tempo=0;
		for (Tavolo t:tavoliPerRicorsione)
		{
			LocalDateTime t1=t.getTempoOrdinazione();
			LocalDateTime t2=t.getTempoServizio();
			int min= (int) ChronoUnit.MINUTES.between(t1, t2);
			tempo+=min;
		}
		return tempo/tavoliPerRicorsione.size();
	}
	
	public int tempoMedioConsumazione() {
		int tempo=0;
		for (Tavolo t:tavoliPerRicorsione)
		{
			LocalDateTime t1=t.getTempoServizio();
			LocalDateTime t2=t.getTempoScontrino();
			int min= (int) ChronoUnit.MINUTES.between(t1, t2);
			tempo+=min;
		}
		return tempo/tavoliPerRicorsione.size();
	}
	
	public int porzioniDiPastaServite() {
		int pasta=0;
		for (Ordinazione o: ordinazioniPerRicorsione)
			if (o.isDaCondire())
				pasta++;
		return pasta;
	}
	
	public double percentualePasta() {
		return porzioniDiPastaServite()*100.0/numeroDiClienti();}
	
	public double percentualePiatto() {
		return (numeroDiClienti()-porzioniDiPastaServite())*100.0/numeroDiClienti();}
	
}
