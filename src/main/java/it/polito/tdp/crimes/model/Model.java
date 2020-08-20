package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
	Map<LocalDate, Giornata> mappaGiornatePerRicorsione;
	List<Giornata> parziale;
	List<Giornata> best;
	int clienti;
	double fatturato;
	int tempi;

	public Model() {
		dao = new EventsDao();
		listaOrdinazioniDelTavolo = new ArrayList<Ordinazione>();
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
		tavoliPerRicorsione = new ArrayList<Tavolo>();
		ordinazioniPerRicorsione = new ArrayList<Ordinazione>();
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
		double fatturato = 0.0;
		for (Tavolo t : tavoliPerRicorsione)
			fatturato += t.getImporto() / 100;
		return fatturato;
	}

	public int tempoMedioInterarrivo(int numGiorni) {
		int tempo = 0;
		for (int i = 1; i < tavoliPerRicorsione.size(); i++) {
			LocalDateTime t1 = tavoliPerRicorsione.get(i).getTempoArrivo();
			LocalDateTime t2 = tavoliPerRicorsione.get(i - 1).getTempoArrivo();
			if (t1.getDayOfYear() == t2.getDayOfYear()) {
				int min = (int) ChronoUnit.MINUTES.between(t2, t1);
				tempo += min;
			}
		}
		return tempo / (tavoliPerRicorsione.size() - giorniEffettivi(numGiorni));
	}

	public int tempoMedioPresaComanda() {
		int tempo = 0;
		for (Tavolo t : tavoliPerRicorsione) {
			LocalDateTime t1 = t.getTempoArrivo();
			LocalDateTime t2 = t.getTempoOrdinazione();
			int min = (int) ChronoUnit.MINUTES.between(t1, t2);
			tempo += min;
		}
		return tempo / tavoliPerRicorsione.size();
	}

	public int tempoMedioCucina() {
		int tempo = 0;
		for (Tavolo t : tavoliPerRicorsione) {
			LocalDateTime t1 = t.getTempoOrdinazione();
			LocalDateTime t2 = t.getTempoServizio();
			int min = (int) ChronoUnit.MINUTES.between(t1, t2);
			tempo += min;
		}
		return tempo / tavoliPerRicorsione.size();
	}

	public int tempoMedioConsumazione() {
		int tempo = 0;
		for (Tavolo t : tavoliPerRicorsione) {
			LocalDateTime t1 = t.getTempoServizio();
			LocalDateTime t2 = t.getTempoScontrino();
			int min = (int) ChronoUnit.MINUTES.between(t1, t2);
			tempo += min;
		}
		return tempo / tavoliPerRicorsione.size();
	}

	public int porzioniDiPastaServite() {
		int pasta = 0;
		for (Ordinazione o : ordinazioniPerRicorsione)
			if (o.isDaCondire())
				pasta++;
		return pasta;
	}

	public double percentualePasta() {
		return porzioniDiPastaServite() * 100.0 / numeroDiClienti();
	}

	public double percentualePiatto() {
		return (numeroDiClienti() - porzioniDiPastaServite()) * 100.0 / numeroDiClienti();
	}

	public void doRicorsione(boolean maxClienti, boolean maxFatturato, boolean maxTempi) {
		ottieniSintesi();
		parziale = new ArrayList<Giornata>();
		best = new ArrayList<Giornata>();
		if (maxClienti)
			clienti = 0;
		else
			clienti = 1000000000;
		if (maxFatturato)
			fatturato = 0.0;
		else
			fatturato = 10000000000.0;
		if (maxTempi)
			tempi = 0;
		else
			tempi = 1000000000;

		ricorri(maxClienti, maxFatturato, maxTempi);
	}

	public void ricorri(boolean maxClienti, boolean maxFatturato, boolean maxTempi) {
		if (parziale.size() == 6) {
			int cli = calcolaClienti();
			// System.out.println(cli);
			// System.out.println(clienti);
			double fat = calcolaFatturato();
			// System.out.println(fat);
			int tem = calcolaTempi();
			// System.out.println(tem);

			// Controllo sui clienti
			if (maxClienti) {
				if (cli > clienti) {
					clienti = cli;

					if (maxFatturato) {
						if (fat > fatturato) {
							fatturato = fat;

							if (maxTempi) {
								if (tem > tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
							} else {
								if (tem < tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
								return;
							}
							return;
						}
					} else {
						if (fat < fatturato) {
							fatturato = fat;
							if (maxTempi) {
								if (tem > tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
							} else {
								if (tem < tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
								return;
							}
							return;
						}
						return;
					}
					return;
				}
			} else {
				if (cli < clienti) {
					clienti = cli;
					if (maxFatturato) {
						if (fat > fatturato) {
							fatturato = fat;
							if (maxTempi) {
								if (tem > tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
							} else {
								if (tem < tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
								return;
							}
							return;
						}
					} else {
						if (fat < fatturato) {
							fatturato = fat;
							if (maxTempi) {
								if (tem > tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
							} else {
								if (tem < tempi) {
									tempi = tem;
									best = new ArrayList<Giornata>(parziale);
									return;
								}
								return;
							}
							return;
						}
						return;
					}
					return;
				}
				return;
			}
			return;
		}

		for (Giornata g : mappaGiornatePerRicorsione.values()) {
			if (!parziale.contains(g)) {
				parziale.add(g);
				ricorri(maxClienti, maxFatturato, maxTempi);
				parziale.remove(g);
			}

		}
	}

	private int calcolaTempi() {
		int t = 0;
		for (Giornata g : parziale)
			t += g.getTempiPreparazione();
		return t;
	}

	private double calcolaFatturato() {
		double f = 0.0;
		for (Giornata g : parziale)
			f += g.getFatturato();
		return f;
	}

	private int calcolaClienti() {
		int c = 0;
		for (Giornata g : parziale)
			c += g.getNumClienti();
		return c;
	}

	public void ottieniSintesi() {
		mappaGiornatePerRicorsione = new TreeMap<LocalDate, Giornata>();
		// Ottieni sintesi di ogni giornata

		for (Tavolo t : tavoliPerRicorsione) {

			int numClienti = 0;
			int tempiDiPreparazione = 0;
			for (Ordinazione o : ordinazioniPerRicorsione) {

				if (o.getTavolo().compareTo(t.getId()) == 0) {
					numClienti++;
					// Aggiungo tempi di preparazione solo se è un piatto che transita in cucina
					if (o.isDaCondire()) {
						tempiDiPreparazione += o.getPasta().getCotturaMax();
						tempiDiPreparazione += o.getCondimento().getTempo();
					}
				}
			}
			// Se non c'è già in mappa la giornata aggiungo
			if (!mappaGiornatePerRicorsione.containsKey(t.getTempoScontrino().toLocalDate())) {
				mappaGiornatePerRicorsione.put(t.getTempoScontrino().toLocalDate(),
						new Giornata(numClienti, t.getImporto(), tempiDiPreparazione));
				// aggiungo il tavolo in lista
				mappaGiornatePerRicorsione.get(t.getTempoScontrino().toLocalDate()).aggiungiTavoloAGiornata(t);
				mappaGiornatePerRicorsione.get(t.getTempoScontrino().toLocalDate())
						.setGiorno(t.getTempoScontrino().toLocalDate());

			}
			// Altrimenti aggiorno una giornata presente
			else {
				// aggiungo il tavolo in lista
				Giornata g = mappaGiornatePerRicorsione.get(t.getTempoScontrino().toLocalDate());
				g.aggiungiTavoloAGiornata(t);
				// Aggiorno i dati della giornata
				g.aggiornaFatturato(t.getImporto());
				g.aggiornaNumClienti(numClienti);
				g.aggiornaTempi(tempiDiPreparazione);
			}

		}
	}

	public int clientidaSimulare() {
		int clienti=0;
		for (Giornata g: best)
			clienti+=g.getNumClienti();
		return clienti;
	}

	public double fatturatoDaSimulare() {
		double fatturato=0.0;
		for (Giornata g:best)
		fatturato+=g.getFatturato();
		return fatturato/100;
	}

	public int tempoDaSimulare() {
		int tempo=0;
		for (Giornata g: best)
			tempo+=g.getTempiPreparazione();
		return tempo;
	}

}
