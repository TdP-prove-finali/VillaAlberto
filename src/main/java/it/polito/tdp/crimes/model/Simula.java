package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.Evento.EventType;

public class Simula{
	
//	private EventsDao dao;

//  Variabili del mondo
	private int numeroCoperti=16;
	private int numeroBollitori=4;
	private int maniChef=2;
	private int numeroCopertiDehors=6;
	private int i=0;
	private int copertiTotali=0;
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

	//Coda degli eventi
	private Queue<Evento> coda;

	
	public Simula(int giorniDehors2, double percAsporto, double perFreddo, double tempoRiassetto2, double tempoSanificazione2, int frequenzaErrore, int tempoAttesa, boolean pastaFresca, List<Giornata> best, List<Ordinazione> ordinazioniPerRicorsione) {
		//Prendo tutto quello che mi serve per fare la simulazione
		this.giornate= new LinkedList<Giornata>(best);
		this.ordinazioni= new LinkedList<Ordinazione>(ordinazioniPerRicorsione);
		this.giorniDehors=giorniDehors2;
		this.percAsporto=percAsporto;
		this.perFreddo=perFreddo;
		this.tempoRiassetto=tempoRiassetto2;
		this.tempoSanificazione=tempoSanificazione2;
		this.frequenzaErrore=frequenzaErrore;
		this.tempoAttesa=tempoAttesa;
		this.pastaFresca=pastaFresca;
	
	}
	
	public void AvviaSimulazione() {
		
		//Inizializzo le variabili di output
		soddisfatti=0;
		insoddisfatti=0;
		tempiAttesa= new LinkedList<Integer>();
		piattiAsporto=0;
		piattiPreparati=0;
		fatturato=0.0;
		
		//Ciclo sulle giornate della simulazione (che sono 6)
		for (Giornata g: giornate)
		{	i++;
			if (i<=giorniDehors)
				copertiTotali=numeroCoperti+numeroCopertiDehors;
			else
				copertiTotali=numeroCoperti;
			
			System.out.println(String.format("\nInizia una nuova giornata con %d coperti", copertiTotali));
			System.out.println(g);
			
			//Prendo per la giornata tutti i tavoli
			LinkedList<Tavolo> tavoliDaServire= new LinkedList<Tavolo>(g.getListaTavoli());
			//Inizializzo e popolo la coda degli eventi
			coda= new PriorityQueue<Evento>();
			for (Tavolo t: tavoliDaServire)
			{
				coda.add(new Evento(t.getTempoArrivo(), EventType.ARRIVA_TAVOLO, t));
			}
			System.out.println(coda);
		}
	


//		//Eseguo la simulazione vera e propria
//		
//		while(!coda.isEmpty())
//		{
//			Evento e= coda.poll();
//			switch(e.getTipo()) {
//			case ASSEGNAZIONE_CASO:
//				//Cerco l'agente piu'vicino
//				Integer agenteVicino= agenteVicino(e.getDistretto());
//				if (agenteVicino!=null)
//				{
//					//Guardo se l'agente puo'arrivare puntuale
//					Duration d=tempoViaggio(e.getDistretto(), mappaAgenti.get(agenteVicino).getLuogo());
//					if(d.getSeconds()>15*60)
//					{
//						System.out.println(mappaAgenti.get(agenteVicino).getId());
//						mappaAgenti.remove(agenteVicino);
//						LocalDateTime istante=calcolaIstanteFinale(e.getIstante(), d, e.getCrimine());
//						Evento nuovo= new Evento(e.getDistretto(), TipoDiEvento.RISOLUZIONE_CONFLITTO, istante, e.getCrimine(), e.getEvento());
//						nuovo.setAgente(mappaAgenti.get(agenteVicino));
//						System.out.println(nuovo.getAgente());
//						coda.add(nuovo);
//				}
//					else insoddisfatti++;}
//				else insoddisfatti++;
//				break;
//			case RISOLUZIONE_CONFLITTO:
//				//System.out.println(e.getAgente());
//				mappaAgenti.put(e.getAgente().getId(), e.getAgente());
//				break;
//			}
//		}
//		
//		
		
	}


}
