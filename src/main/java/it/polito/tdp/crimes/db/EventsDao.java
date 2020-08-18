package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Condimento;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.Ordinazione;
import it.polito.tdp.crimes.model.Pasta;
import it.polito.tdp.crimes.model.Piatto;

public class EventsDao {

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

}
