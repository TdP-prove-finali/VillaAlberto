package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model= new Model();
		model.prendiDati(24);
		model.doRicorsione(false, false, true);
	}

}
