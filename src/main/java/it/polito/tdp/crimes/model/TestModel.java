package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model= new Model();
		model.prendiDati(12);
		model.doRicorsione(true, false, true);
		//best
		model.avviaSimulazione(6, 1, 1, 0, 0, 100000, 100000, true);
		//worst
		model.avviaSimulazione(0, 0, 0, 10, 4, 5, 5, false);
		//mia
		model.avviaSimulazione(3, 0.5, 0.5, 5, 2, 25, 25, true);
	}

}
