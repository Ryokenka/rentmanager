package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private ClientDao() {}


	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES (?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id = ?;";
	private static final String FIND_CLIENT_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE id = ?;";
	private static final String FIND_ALL_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String COUNT_CLIENTS_QUERY = "SELECT COUNT(*) FROM Client";

	public long create(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, java.sql.Date.valueOf(client.getNaissance()));

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DaoException("La création du client a échoué, aucune ligne affectée.");
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new DaoException("La création du client a échoué, aucun ID généré.");
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la création du client.", e);
		}
	}

	public void delete(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)) {

			ps.setLong(1, client.getId());

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DaoException("La suppression du client a échoué, aucun client avec l'ID : " + client.getId());
			}

		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la suppression du client.", e);
		}
	}

	public Client findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY)) {

			ps.setLong(1, id);

			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					return extractClientFromResultSet(resultSet);
				} else {
					throw new DaoException("Le client avec l'ID : " + id + " est introuvable.");
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche du client avec l'ID : " + id, e);
		}
	}

	public List<Client> findAll() throws DaoException {
		List<Client> clients = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_ALL_CLIENTS_QUERY);
			 ResultSet resultSet = ps.executeQuery()) {

			while (resultSet.next()) {
				Client client = extractClientFromResultSet(resultSet);
				clients.add(client);
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la récupération de tous les clients.", e);
		}
		return clients;
	}

	private Client extractClientFromResultSet(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt("id");
		String nom = resultSet.getString("nom");
		String prenom = resultSet.getString("prenom");
		String email = resultSet.getString("email");
		LocalDate birthdate = resultSet.getDate("naissance").toLocalDate();

		return new Client(id, nom, prenom, email, birthdate);
	}
	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_CLIENTS_QUERY);
			 ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new DaoException("Aucun résultat trouvé lors du comptage des clients.");
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors du comptage des clients.", e);
		}
	}
	private static final String FIND_CLIENT_BY_EMAIL_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE email = ?;";
	public Optional<Client> findByEmail(String email) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_BY_EMAIL_QUERY)) {

			ps.setString(1, email);

			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					return Optional.of(extractClientFromResultSet(resultSet));
				} else {
					return Optional.empty();
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche du client avec l'email : " + email, e);
		}
	}
	public Client update(Client updatedClient) throws DaoException {
		final String UPDATE_CLIENT_QUERY = "UPDATE Client SET nom = ?, prenom = ?, email = ?, naissance = ? WHERE id = ?;";

		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(UPDATE_CLIENT_QUERY)) {

			ps.setString(1, updatedClient.getNom());
			ps.setString(2, updatedClient.getPrenom());
			ps.setString(3, updatedClient.getEmail());
			ps.setDate(4, java.sql.Date.valueOf(updatedClient.getNaissance()));
			ps.setLong(5, updatedClient.getId());

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DaoException("Updating the client failed, no rows affected.");
			}

			return rowsAffected > 0 ? updatedClient : null; // Return null if no rows affected
		} catch (SQLException e) {
			throw new DaoException("An error occurred while updating the client.", e);
		}
	}

}

