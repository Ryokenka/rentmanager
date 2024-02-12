package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.models.Client;

public class ClientDao {
	private static ClientDao instance = null;

	private ClientDao() {
	}

	public static ClientDao getInstance() {
		if (instance == null) {
			instance = new ClientDao();
		}
		return instance;
	}

	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";

	public long create(Client client) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();
			PreparedStatement ps =
					connection.prepareStatement(CREATE_CLIENT_QUERY);

			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, Date.valueOf(client.getNaissance()));

			ps.executeUpdate();

			// Retrieve the generated keys to get the inserted client's ID
			//ResultSet generatedKeys = ps.getGeneratedKeys();
			//long clientId = -1;
			//if (generatedKeys.next()) {
				//clientId = generatedKeys.getLong(1);
			//}
			ps.close();
			connection.close();

			//return clientId;
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la création du client", e);;
		}


		public long delete (Client client) throws DaoException {
			return 0;
		}

		public Client findById ( long id) throws DaoException {
			return new Client();
		}

		public List<Client> findAll () throws DaoException {
			return new ArrayList<Client>();
		}

	}
}
