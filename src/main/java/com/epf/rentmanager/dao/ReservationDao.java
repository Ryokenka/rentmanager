package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.models.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ReservationDao {

	private static ReservationDao instance = null;

	private ReservationDao() {}

	public static ReservationDao getInstance() {
		if (instance == null) {
			instance = new ReservationDao();
		}
		return instance;
	}

	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES (?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id = ?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE client_id = ?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE vehicle_id = ?;";
	private static final String FIND_ALL_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";

	public long create(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setLong(1, reservation.getClient_id());
			ps.setLong(2, reservation.getVehicle_id());
			ps.setDate(3, java.sql.Date.valueOf(reservation.getDebut()));
			ps.setDate(4, java.sql.Date.valueOf(reservation.getFin()));

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DaoException("La création de la réservation a échoué, aucune ligne affectée.");
			}

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getLong(1);
				} else {
					throw new DaoException("La création de la réservation a échoué, aucun ID généré.");
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la création de la réservation.", e);
		}
	}

	public void delete(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY)) {

			ps.setLong(1, reservation.getId());

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				throw new DaoException("La suppression de la réservation a échoué, aucune réservation avec l'ID : " + reservation.getId());
			}

		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la suppression de la réservation.", e);
		}
	}

	public List<Reservation> findReservationsByClientId(long clientId) throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)) {

			ps.setLong(1, clientId);

			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					Reservation reservation = extractReservationFromResultSet(resultSet);
					reservations.add(reservation);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche des réservations par client.", e);
		}
		return reservations;
	}

	public List<Reservation> findReservationsByVehicleId(long vehicleId) throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)) {

			ps.setLong(1, vehicleId);

			try (ResultSet resultSet = ps.executeQuery()) {
				while (resultSet.next()) {
					Reservation reservation = extractReservationFromResultSet(resultSet);
					reservations.add(reservation);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la recherche des réservations par véhicule.", e);
		}
		return reservations;
	}

	public List<Reservation> findAll() throws DaoException {
		List<Reservation> reservations = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement ps = connection.prepareStatement(FIND_ALL_RESERVATIONS_QUERY);
			 ResultSet resultSet = ps.executeQuery()) {

			while (resultSet.next()) {
				Reservation reservation = extractReservationFromResultSet(resultSet);
				reservations.add(reservation);
			}
		} catch (SQLException e) {
			throw new DaoException("Une erreur est survenue lors de la récupération de toutes les réservations.", e);
		}
		return reservations;
	}

	private Reservation extractReservationFromResultSet(ResultSet resultSet) throws SQLException {
		Reservation reservation = new Reservation();
		reservation.setId((int) resultSet.getLong("id"));
		reservation.setClient_id((int) resultSet.getLong("client_id"));
		reservation.setVehicle_id((int) resultSet.getLong("vehicle_id"));
		reservation.setDebut(resultSet.getDate("debut").toLocalDate());
		reservation.setFin(resultSet.getDate("fin").toLocalDate());
		return reservation;
	}
}

