package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.models.Vehicule;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleDao {

	private VehicleDao() {}

	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES (?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id = ?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id = ?;";
	private static final String FIND_ALL_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String COUNT_VEHICLES_QUERY = "SELECT COUNT(*) FROM Vehicle";

	public long create(Vehicule vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_VEHICLE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, vehicle.getConstructeur());
			ps.setString(2, vehicle.getModele());
			ps.setInt(3, vehicle.getNb_places());

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DaoException("La création du véhicule a échoué, aucune ligne affectée.");
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new DaoException("La création du véhicule a échoué, aucun ID généré.");
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la création du véhicule.", e);
		}
	}

	public long delete(Vehicule vehicle) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(DELETE_VEHICLE_QUERY)) {

			ps.setLong(1, vehicle.getId());

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DaoException("La suppression du véhicule a échoué, aucun véhicule avec l'ID : " + vehicle.getId());
			}

			// Return any appropriate value indicating success
			return rowsAffected; // For example, return the number of rows affected
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la suppression du véhicule.", e);
		}
	}

	public Vehicule findById(long id) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_VEHICLE_QUERY)) {

			ps.setLong(1, id);

			try (ResultSet resultSet = ps.executeQuery()) {
				if (resultSet.next()) {
					return extractVehicleFromResultSet(resultSet);
				} else {
					throw new DaoException("Aucun véhicule trouvé avec l'ID : " + id);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche du véhicule par ID.", e);
		}
	}

	public List<Vehicule> findAll() throws DaoException {
		List<Vehicule> vehicles = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_ALL_VEHICLES_QUERY);
			 ResultSet resultSet = ps.executeQuery()) {

			while (resultSet.next()) {
				Vehicule vehicle = extractVehicleFromResultSet(resultSet);
				vehicles.add(vehicle);
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la récupération de tous les véhicules.", e);
		}
		return vehicles;
	}

	private Vehicule extractVehicleFromResultSet(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt("id");
		String constructeur = resultSet.getString("constructeur");
		String modele = resultSet.getString("modele");
		int nb_places = resultSet.getInt("nb_places");

		return new Vehicule(id, constructeur, modele, nb_places );
	}

	public int count() throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(COUNT_VEHICLES_QUERY);
			 ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new DaoException("Aucun résultat trouvé lors du comptage des véhicules.");
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors du comptage des véhicules.", e);
		}
	}

}
