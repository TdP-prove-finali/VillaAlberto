package it.polito.tdp.crimes.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.db.EventsDao;

public class Simula{
	
//	private EventsDao dao;
//	private Graph<Integer, DefaultWeightedEdge> grafo;
//	private int numeroAgenti;
//	private int anno;
//	private int mese;
//	private int giorno;
//	private int centrale=-1;
//	private Queue<Evento> coda;
//	private Map<Integer, Agente> mappaAgenti;
//	private int casiTotali=-1;
//	private int insoddisfatti=-1;
//	
//	public Simula(int numeroAgenti, Graph<Integer, DefaultWeightedEdge> grafo, int anno, int mese, int giorno) {
//		dao= new EventsDao();
//		this.grafo=grafo;
//		this.anno=anno;
//		this.mese=mese;
//		this.giorno=giorno;
//		this.numeroAgenti=numeroAgenti;
//		
//	}
//	
//	public void AvviaSimulazione() {
//		//Lista di ogni crimine ordinate per data conoscendo giorno mese e anno
//		coda= new PriorityQueue<Evento>();
//		casiTotali=0;
//		for (Event e: dao.listAllEventsbyDate(anno, mese, giorno))
//		{
//			System.out.println(e);
//			coda.add(new Evento(e.getDistrict_id(), TipoDiEvento.ASSEGNAZIONE_CASO, e.getReported_date(), e.getOffense_type_id(), e));
//			casiTotali++;
//		}
//		
//		//Calcolo qual è il distretto in cui colloco la centrale (DAO) --> collocata nel centro
//		centrale=dao.getCentrale(anno);
//		System.out.println("La centrale è "+centrale);
//		
//		//Creo la squadra di agenti
//		mappaAgenti= new TreeMap<Integer, Agente>();
//		for (int i=0; i<numeroAgenti; i++)
//		{
//			mappaAgenti.put(i,(new Agente(i, centrale)));
//		}
//		
//		//Inizializzo le mie variabili
//		insoddisfatti=0;
//		
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
//		
//	}
//
//
//	private LocalDateTime calcolaIstanteFinale(LocalDateTime istante, Duration d, String crimine) {
//		int tempo=0;
//		if (crimine.compareTo("all_other_crimes")==0)
//		{
//			if (Math.random()<0.5)
//				tempo=60;
//			else tempo=120;
//		}
//		else tempo=120;
//		Duration du= Duration.ofMinutes(tempo);
//		return istante.plus(d).plus(du);
//	}
//
//	private Duration tempoViaggio(int distretto, int luogo) {
//		DefaultWeightedEdge e= grafo.getEdge(distretto, luogo);
//		double distanza=grafo.getEdgeWeight(e);
//		double ore= distanza/60*60;
//		Duration d= Duration.ofMinutes((long) ore);
//		return d;
//	}
//
//	private Integer agenteVicino(int distretto) {
//		Integer bestAgente=null;
//		Double bestDistance=5000000000000.0;
//		for (Agente a: mappaAgenti.values())
//		{
//			DefaultWeightedEdge e= grafo.getEdge(distretto, a.getLuogo());
//			if (grafo.getEdgeWeight(e)<bestDistance)
//			{
//				bestAgente=a.getId();
//				bestDistance=grafo.getEdgeWeight(e);
//			}
//		}
//		return bestAgente;
//	}
//
//	public int getInsoddisfatti() {
//		return insoddisfatti;
//	}
//	
//	public int getTotali() {
//		return casiTotali;
//	}
//	
//	
//	
//	//Scelgo agente libero piu'vicino
//	
//	//60 km/h -->guarda distanza tra i due distretti
//	
//	//Tempo di permanenza 1/2 h se allOtherCrimes
//	//Altrimenti due ore per tutti gli altri eventi
//	//Evento mal gestito se piu'di 15 minuti di ritardo
//	//Calcola eventi totali, eventi mal gestiti
}
