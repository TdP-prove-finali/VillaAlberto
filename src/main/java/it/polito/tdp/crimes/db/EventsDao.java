package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Condimento;
import it.polito.tdp.crimes.model.Event;
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
					
					Pasta pasta= new Pasta(res.getInt(1), res.getString(2), res.getInt(5), res.getInt(3), res.getString(4));
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
					
					Condimento condimento= new Condimento(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4));
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
					
					Piatto piatto= new Piatto(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4));
					list.add(piatto);
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
