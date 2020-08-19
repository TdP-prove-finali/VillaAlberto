package it.polito.tdp.crimes.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model= new Model();
		model.prendiDati(3);
		System.out.println(model.giorniEffettivi(3));
		System.out.println(model.numeroDiClienti());
		System.out.println(model.fatturato());
		System.out.println(model.tempoMedioInterarrivo(3));
		System.out.println(model.tempoMedioPresaComanda());
		System.out.println(model.tempoMedioCucina());
		System.out.println(model.tempoMedioConsumazione());
		System.out.println(model.porzioniDiPastaServite());
		System.out.println(model.percentualePasta()+"%");
		System.out.println(model.percentualePiatto()+"%");
	}

}
