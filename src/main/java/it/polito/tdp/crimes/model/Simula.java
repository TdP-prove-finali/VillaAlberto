package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import it.polito.tdp.crimes.model.Evento.EventType;

public class Simula {

//	private EventsDao dao;

//  Variabili del mondo
	private int numeroCoperti = 10;
	private int numeroBollitori = 4;
	private int maniChef = 2;
	private int numeroCopertiDehors = 6;
	private int giorniSole = 0;
	private int copertiTotali = 0;
	private int postiDisponibili = 0;
	private int giorniDehors;
	private double percAsporto;
	private double perFreddo;
	private double tempoRiassetto;
	private double tempoSanificazione;
	private int frequenzaErrore;
	private int tempoAttesa;
	private boolean pastaFresca;

// Variabili output
	private int soddisfatti;
	private int insoddisfatti;
	private List<Integer> tempiAttesa;
	private int piattiAsporto;
	private int piattiPreparati;
	private double fatturato;
	private List<Giornata> giornate;
	private List<Ordinazione> ordinazioni;

	// Coda degli eventi
	private Queue<Evento> coda;

	public Simula(int giorniDehors2, double percAsporto, double perFreddo, double tempoRiassetto2,
			double tempoSanificazione2, int frequenzaErrore, int tempoAttesa, boolean pastaFresca, List<Giornata> best,
			List<Ordinazione> ordinazioniPerRicorsione) {
		// Prendo tutto quello che mi serve per fare la simulazione
		this.giornate = new LinkedList<Giornata>(best);
		this.ordinazioni = new LinkedList<Ordinazione>(ordinazioniPerRicorsione);
		this.giorniDehors = giorniDehors2;
		this.percAsporto = percAsporto;
		this.perFreddo = perFreddo;
		this.tempoRiassetto = tempoRiassetto2;
		this.tempoSanificazione = tempoSanificazione2;
		this.frequenzaErrore = frequenzaErrore;
		this.tempoAttesa = tempoAttesa;
		this.pastaFresca = pastaFresca;

	}

	public void AvviaSimulazione() {

		// Inizializzo le variabili di output
		soddisfatti = 0;
		insoddisfatti = 0;
		tempiAttesa = new LinkedList<Integer>();
		piattiAsporto = 0;
		piattiPreparati = 0;
		fatturato = 0.0;

		// Ciclo sulle giornate della simulazione (che sono 6)
		 for (Giornata g : giornate) {
		giorniSole++;
		if (giorniSole <= giorniDehors)
			copertiTotali = numeroCoperti + numeroCopertiDehors;
		else
			copertiTotali = numeroCoperti;

		postiDisponibili = copertiTotali;

		System.out.println(String.format("\nInizia una nuova giornata con %d coperti", copertiTotali));
		// System.out.println(g);

		// Prendo per la giornata tutti i tavoli
		LinkedList<Tavolo> tavoliDaServire = new LinkedList<Tavolo>(g.getListaTavoli());
		// Inizializzo e popolo la coda degli eventi
		System.out.println(tavoliDaServire.size());
		coda = new PriorityQueue<Evento>();
		for (Tavolo t : tavoliDaServire) {
			coda.add(new Evento(t.getTempoArrivo(), EventType.ARRIVA_TAVOLO, t));
		}
		// System.out.println(coda);

		// Eseguo la simulazione vera e propria

		while (!coda.isEmpty()) {
			Evento e = coda.poll();
//				System.out.println(e);
			switch (e.getTipo()) {

			case ARRIVA_TAVOLO: {
				//System.out.println("Arrivato tavolo\n");
				List<Ordinazione> ordinazioniDelTavolo = new LinkedList<Ordinazione>(
						dimmiOrdinazioni(e.getTavolo().getId()));
				LocalDateTime tempoMassimaPazienza = e.getTavolo().getTempoArrivo().plusMinutes(tempoAttesa);
				if (ordinazioniDelTavolo.size() > 0) {
					if (ordinazioniDelTavolo.size() <= postiDisponibili) // Ho abbastanza posti per fare accomodare il
																			// tavolo
					{
						if (tempoMassimaPazienza.compareTo(e.getIstante()) >= 0) {// I clienti hanno ancora pazienza e
																					// accettano di accomodarsi
							postiDisponibili -= dimmiPariSuperioriore(ordinazioniDelTavolo.size());
							System.out.println("Tavolo seduto");
							System.out.println("Posti disponibili: " + postiDisponibili);
							// TODO CREA NUOVO EVENTO PER LA PREPARAZIONE
							coda.add(new Evento(e.getIstante(), EventType.ACCOMODA_TAVOLO, e.getTavolo()));
							break;
						} else {// I clienti non hanno piu'voglia di aspettare e se ne vanno
							insoddisfatti += dimmiPariSuperioriore(ordinazioniDelTavolo.size());
							System.out.println("Clienti persi");
							break;
						}
					} else // Non ho abbastanza posti per fare accomodare il tavolo
					{
						if ((e.getTavolo().getTempoArrivo().compareTo(e.getIstante()) == 0)
								&& (Math.random() < percAsporto)) {// Tavolo accetta asporto
							fatturato += 0.5 * e.getTavolo().getImporto(); // Sommo la metà del valore dello scontrino
							soddisfatti += ordinazioniDelTavolo.size();
							piattiAsporto++;
							System.out.println("Clienti asporto");
							break;
						} else { // Tavolo non accetta asporto
							coda.add(new Evento(e.getIstante().plusSeconds(30), e.getTipo(), e.getTavolo()));
							//System.out.println("Clienti rimessi in lista");
//							System.err.println(e.getIstante());
//							System.err.println(e.getIstante().plusSeconds(30));

							break;
						}
					}
				}
			}
			case ACCOMODA_TAVOLO:
				System.out.println("Tavolo accomodato e prende ordine\n");
				// Calcolo dopo quanto tempo è presa l'ordinazione del tavolo, dopodichè la
				// comanda va in cucina
				LocalDateTime oraPresaComanda = e.getIstante().plusMinutes(ChronoUnit.MINUTES
						.between(e.getTavolo().getTempoOrdinazione(), e.getTavolo().getTempoArrivo()));
				coda.add(new Evento(oraPresaComanda, EventType.INIZIO_PREPARAZIONE_PIATTO, e.getTavolo()));
				break;

			case INIZIO_PREPARAZIONE_PIATTO:
				System.out.println("La cucina inizia a processare il piatto\n");
				List<Ordinazione> ordinazioni = new LinkedList<Ordinazione>(dimmiOrdinazioni(e.getTavolo().getId()));
				int PiattiFreddi = 0;
				for (Ordinazione o : ordinazioni) {
					if (!o.isDaCondire()) {
						PiattiFreddi++;
						piattiPreparati++;
					}
				}

				if (PiattiFreddi == ordinazioni.size()) {
					int tempo = 0;
					for (Ordinazione o : ordinazioni) {
						tempo += o.getPiatto().getPreparazione();
					}
					coda.add(new Evento(e.getIstante().plusSeconds(tempo), EventType.SERVITO_TAVOLO, e.getTavolo()));
					break;
				} else {
					// Provo a sostituire il piatto da Cucinare con un piattoFreddo a scelta del
					// cliente per decongestionare la cucina
					if (Math.random() > perFreddo) {
						coda.add(new Evento(e.getIstante().plusSeconds(100 * ordinazioni.size()),
								EventType.SERVITO_TAVOLO, e.getTavolo()));// Ho invitato tutti i clienti a prendere
																			// piatti freddi e hanno accettato
						break;
					}

					if (ordinazioni.size() - PiattiFreddi > numeroBollitori) {
						coda.add(new Evento(e.getIstante().plusSeconds(600), EventType.INIZIO_CONDIMENTO_PIATTO,
								e.getTavolo()));
						break;
					}

					if (numeroBollitori > (ordinazioni.size() - PiattiFreddi)) {
						coda.add(new Evento(e.getIstante().plusSeconds(dimmiMaxTempoCottura(ordinazioni, pastaFresca)), // Tutte
																														// le
																														// paste
																														// sono
																														// penalizzate
																														// al
																														// tempo
																														// piu'lungo
								EventType.INIZIO_CONDIMENTO_PIATTO, e.getTavolo()));
						numeroBollitori = -(ordinazioni.size() - PiattiFreddi);// Sottraggo i bollitori dalle risorse
																				// disponibili
						break;
					} else {// Rimetto in coda dopo 30 secondi
						new Evento(e.getIstante().plusSeconds(30), EventType.FINE_PREPARAZIONE_PIATTO, e.getTavolo());
					}

				}

				break;
			case INIZIO_CONDIMENTO_PIATTO:
				System.out.println("La cucina si occupa del condimento del piatto\n");
				int tempo = 0;
				List<Ordinazione> ordinazioni1 = new LinkedList<Ordinazione>(dimmiOrdinazioni(e.getTavolo().getId()));
				for (Ordinazione o : ordinazioni1) {
					if (o.isDaCondire()) {
						tempo +=  o.getCondimento().getTempo();
						numeroBollitori++;
					}
				}

				if (maniChef > 0) {
					maniChef--;
					coda.add(new Evento(e.getIstante().plusSeconds(tempo), EventType.FINE_PREPARAZIONE_PIATTO,
							e.getTavolo()));
					tempiAttesa.add((int) ChronoUnit.MINUTES.between(e.getIstante(), e.getTavolo().getTempoArrivo()));
					break;
				} else {
					coda.add(new Evento(e.getIstante().plusSeconds(30), EventType.INIZIO_CONDIMENTO_PIATTO,
							e.getTavolo()));// Rimetto in coda dopo 30 secondi
					break;
				}
			case FINE_PREPARAZIONE_PIATTO:
				System.out.println("La cucina si occupa del riassetto\n");
				coda.add(new Evento(e.getIstante().plusSeconds((long) (tempoRiassetto * 60)), EventType.SERVITO_TAVOLO,
						e.getTavolo()));
				maniChef++;
				break;

			case SERVITO_TAVOLO:
				System.out.println("Il cliente consuma\n");
				LocalDateTime oraFineConsumazione = e.getIstante().plusMinutes(ChronoUnit.MINUTES
						.between(e.getTavolo().getTempoScontrino(), e.getTavolo().getTempoServizio()));
				coda.add(new Evento(oraFineConsumazione, EventType.SCONTRINO_TAVOLO, e.getTavolo()));
				break;

			case SCONTRINO_TAVOLO:
				postiDisponibili += dimmiPariSuperioriore(dimmiOrdinazioni(e.getTavolo().getId()).size());
				soddisfatti+=dimmiOrdinazioni(e.getTavolo().getId()).size();
				fatturato += e.getTavolo().getImporto();
				System.out.println("Il cliente se ne va e libera le risorse\n");
				break;
			default:
				throw new Error("Errore!");
			}
		}
		System.out.println("Fatturato:" + fatturato/100);
		System.out.println("PiattiAsporto: " + piattiAsporto);
		System.out.println("PiattiGiàPreparati: " + piattiPreparati);
		System.out.println("Soddisfatti: " + soddisfatti);
		System.out.println("Insoddisfatti: " + insoddisfatti);
		 }

	}

	private long dimmiMaxTempoCottura(List<Ordinazione> ordinazioni2, boolean pastaFresca2) {
		int maxTempo = 0;
		for (Ordinazione o : ordinazioni2) {
			if (o.isDaCondire()) {
				if (pastaFresca2) {
					if (o.getPasta().getCotturaMin() > maxTempo) {
						maxTempo = o.getPasta().getCotturaMin();
					}
				} else {
					if (o.getPasta().getCotturaMax() > maxTempo) {
						maxTempo = o.getPasta().getCotturaMax();
					}
				}
			}
		}

		return maxTempo;
	}

	private int dimmiPariSuperioriore(int size) {
		if (size % 2 != 0)
			return size + 1;
		return size;
	}

	private List<Ordinazione> dimmiOrdinazioni(Integer id) {
		List<Ordinazione> ls = new LinkedList<Ordinazione>();
		for (Ordinazione o : ordinazioni)
			if (o.getTavolo().compareTo(id) == 0)
				ls.add(o);
		return ls;
	}

}
