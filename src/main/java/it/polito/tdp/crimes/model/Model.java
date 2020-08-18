package it.polito.tdp.crimes.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import java.lang.Object;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	List<Ordinazione> listaOrdinazioniDelTavolo;
	
	public Model () {
		dao= new EventsDao();
		listaOrdinazioniDelTavolo= new LinkedList<Ordinazione>();
	}

	
	public List <Pasta> listAllPasta() {
		return dao.listAllPasta();
	}
	
	public List	<Condimento> listAllCondimenti(){
		return dao.listAllCondimento();
	}
	
	public List <Piatto> listtAllPiatti(){
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
	
}
