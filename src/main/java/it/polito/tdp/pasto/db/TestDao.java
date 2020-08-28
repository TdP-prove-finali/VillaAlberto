package it.polito.tdp.pasto.db;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.pasto.model.Condimento;
import it.polito.tdp.pasto.model.Ordinazione;
import it.polito.tdp.pasto.model.Pasta;

public class TestDao {

	public static void main(String[] args) {
		EventsDao dao = new EventsDao();
		dao.salvaTavolo(15.00, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
		List<Ordinazione> listaOrdinazioniDelTavolo = new LinkedList<Ordinazione>();
		listaOrdinazioniDelTavolo.add(new Ordinazione(1, 1, true, null, new Pasta(1, "nome", 1, 1, "descrizione"),
				new Condimento(1, "nome", 1, "descrizione")));
		listaOrdinazioniDelTavolo.add(new Ordinazione(1, 1, true, null, new Pasta(1, "nome", 1, 1, "descrizione"),
				new Condimento(1, "nome", 1, "descrizione")));
		dao.salvaListaOrdiniTavolo(listaOrdinazioniDelTavolo);
	}

}
