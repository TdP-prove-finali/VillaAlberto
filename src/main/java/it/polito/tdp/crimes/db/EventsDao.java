package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.crimes.model.Condimento;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.Ordinazione;
import it.polito.tdp.crimes.model.Pasta;
import it.polito.tdp.crimes.model.Piatto;
import it.polito.tdp.crimes.model.Tavolo;

public class EventsDao {
	
	Map<Integer, Pasta> mappaPasta= new TreeMap<Integer, Pasta>();
	Map<Integer, Piatto> mappaPiatto= new TreeMap<Integer, Piatto>();
	Map<Integer,Condimento> mappaCondimento= new TreeMap<Integer, Condimento>();

	public List<Pasta> listAllPasta() {
		String sql = "SELECT * FROM pasta";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Pasta> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {

				Pasta pasta = new Pasta(res.getInt(1), res.getString(2), res.getInt(5), res.getInt(3),
						res.getString(4));
				list.add(pasta);
				mappaPasta.put(pasta.getId(), pasta);
				//mappaPasta.put(-1, new Pasta(-1, null, null, null, null));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Condimento> listAllCondimento() {
		String sql = "SELECT * FROM condimento";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Condimento> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {

				Condimento condimento = new Condimento(res.getInt(1), res.getString(2), res.getInt(3),
						res.getString(4));
				list.add(condimento);
				mappaCondimento.put(condimento.getId(), condimento);
				//mappaCondimento.put(-1, new Condimento(-1, null, null, null));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Piatto> listAllPiatto() {
		String sql = "SELECT * FROM piatto";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Piatto> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {

				Piatto piatto = new Piatto(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4));
				list.add(piatto);
				mappaPiatto.put(piatto.getId(), piatto);
				//mappaPiatto.put(-1, new Piatto(-1, null, 0, null));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean salvaTavolo(double importo, LocalDateTime arrivo, LocalDateTime ordinazione, LocalDateTime servizio,
			LocalDateTime scontrino) {
		// Salvo l'intestazione del tavolo nel db
		String sql = "INSERT INTO tavolo (importo,arrivo, ordinazione, servizio, scontrino) VALUES(?,?,?,?,?)";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, importo * 100);
			st.setObject(2, arrivo);
			st.setObject(3, ordinazione);
			st.setObject(4, servizio);
			st.setObject(5, scontrino);

			st.executeQuery();

			conn.close();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean salvaListaOrdiniTavolo(List<Ordinazione> listaOrdinazioniDelTavolo) {

		String sql = "INSERT INTO ordinazione (ZORDERHEADER, DaCondire, Piatto, Pasta, Condimento) VALUES(?,?,?,?,?)";
		for (Ordinazione o : listaOrdinazioniDelTavolo) {
			try {
				Connection conn = DBConnect.getConnection();

				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1, dimmiNumeroUltimoTavolo());
				if (o.isDaCondire())
					{
				st.setInt(2, 1);
				st.setInt(3, -1);
				st.setInt(4, o.getPasta().getId());
				st.setInt(5, o.getCondimento().getId());
					}
				else
				{
					st.setInt(2, 0);
					st.setInt(3, o.getPiatto().getId());
					st.setInt(4, -1);
					st.setInt(5, -1);
				}
				st.executeQuery();

				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private int dimmiNumeroUltimoTavolo() {
		String sql = "SELECT Z_PK FROM tavolo ORDER BY Z_PK DESC LIMIT 1";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);


			ResultSet res = st.executeQuery();

			 if (res.next()) {

				return res.getInt(1);
			}

			conn.close();
			return -1;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void prendiDati(List<Tavolo> tavoliPerRicorsione, List<Ordinazione> ordinazioniPerRicorsione,int numGiorni) {
				//Pulisco le strutture dati utilizzate
				tavoliPerRicorsione.clear();
				ordinazioniPerRicorsione.clear();
				
				//Prendo i tavoli
				String sql="SELECT * FROM tavolo WHERE DATE(scontrino) IN ( SELECT *FROM (SELECT DISTINCT DATE(scontrino) FROM tavolo ORDER BY DATE(scontrino) DESC LIMIT ?) AS t1);";
				try {
					Connection conn = DBConnect.getConnection();

					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, numGiorni);

					ResultSet res = st.executeQuery();

					while (res.next()) {

						Tavolo tavolo = new Tavolo(res.getInt(1), res.getDouble(3), res.getTimestamp(4).toLocalDateTime() , res.getTimestamp(6).toLocalDateTime(), res.getTimestamp(5).toLocalDateTime(), res.getTimestamp(7).toLocalDateTime());
						tavoliPerRicorsione.add(tavolo);
					}

					conn.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				//Prendo tutte le ordinazioni relative a questi tavoli
				
	
				for (Tavolo t: tavoliPerRicorsione)
				{
					String sql1="SELECT * FROM ordinazione WHERE ZORDERHEADER =?";
					try {
						Connection conn = DBConnect.getConnection();

						PreparedStatement st = conn.prepareStatement(sql1);
						st.setInt(1, t.getId());
//						System.out.println(t.getId());
						ResultSet res = st.executeQuery();

						while (res.next()) {
//							System.out.println(t.getId()+"TAVOLO");
							boolean daCondire=false;
							if (res.getInt(5)==1)
								daCondire=true;
							listAllCondimento();
							listAllPasta();
							listAllPiatto();
//							System.out.println(mappaPasta);
//							System.out.println(mappaCondimento);
//							System.out.println(mappaPiatto);
//							System.out.println(res.getInt(2)+"ORDINAZIONE");
//							System.out.println(res.getInt(1));
//							System.out.println(res.getInt(2));
//							System.out.println(daCondire);
//							System.out.println(res.getInt(6)+"idPiatto");
//							System.out.println(res.getInt(7)+"idPasta");
//							System.out.println(res.getInt(8)+"idCondimento");
//							System.out.println(mappaPiatto.get(res.getInt(6)));
//							System.out.println(mappaPasta.get(res.getInt(7)));
//							System.out.println(mappaCondimento.get(res.getInt(8)));
							Ordinazione ordinazione= new Ordinazione(res.getInt(1), res.getInt(2), daCondire, mappaPiatto.get(res.getInt(6)), mappaPasta.get(res.getInt(7)), mappaCondimento.get(res.getInt(8)));
							ordinazioniPerRicorsione.add(ordinazione);
//							System.out.println(ordinazione);
//							System.out.println(ordinazione.getId());
//							System.out.println(ordinazioniPerRicorsione.size()+"\n");
						}

						conn.close();

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
	}
	
	public Piatto dammiPiatto(int id) {
		String sql = "SELECT * FROM piatto WHERE id=?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, id);

			ResultSet res = st.executeQuery();

			if (res.next()) {

				Piatto piatto = new Piatto(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4));
				return piatto;
			}

			conn.close();
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Pasta dammiPasta(int id) {
		String sql = "SELECT * FROM pasta WHERE id=?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, id);

			ResultSet res = st.executeQuery();

			if (res.next()) {

				Pasta pasta = new Pasta(res.getInt(1), res.getString(2), res.getInt(5), res.getInt(3),
						res.getString(4));
				return pasta;
			}

			conn.close();
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Condimento dammiCondimento(int id) {
		String sql = "SELECT * FROM condimento WHERE id=?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, id);

			ResultSet res = st.executeQuery();

			if (res.next()) {

				Condimento condimento = new Condimento(res.getInt(1), res.getString(2), res.getInt(3),
						res.getString(4));
				return condimento;
			}

			conn.close();
			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int giorniEffettivi(int numGiorni) {
		String sql = "SELECT COUNT(DISTINCT DATE(scontrino)) FROM tavolo WHERE DATE(scontrino) IN ( SELECT * FROM ( SELECT DISTINCT DATE(scontrino) FROM tavolo ORDER BY DATE(scontrino) DESC LIMIT ? ) AS t1);";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, numGiorni);

			ResultSet res = st.executeQuery();

			if (res.next()) {

				return res.getInt(1);
			}

			conn.close();
			return -1;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
