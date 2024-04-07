package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.models.Reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    private ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public long create(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la création de la réservation", e);
        }
    }

    public void delete(long id) throws ServiceException {
        try {
            // First, fetch the reservation by its ID
            Reservation reservation = reservationDao.findById(id);

            // Then, call the delete method with the retrieved reservation
            reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la suppression de la réservation", e);
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors du listage de l'ensemble des réservations", e);
        }
    }

    public List<Reservation> findReservationsByClientId(long clientId) throws ServiceException {
        try {
            return reservationDao.findReservationsByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la recherche des réservations par client", e);
        }
    }

    public List<Reservation> findReservationsByVehicleId(long vehicleId) throws ServiceException {
        try {
            return reservationDao.findReservationsByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la recherche des réservations par véhicule", e);
        }
    }
    public int count() throws ServiceException {
        try {
            List<Reservation> reservationList = reservationDao.findAll();
            return reservationList.size();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Reservation findById(long id) throws ServiceException {
        try {
            return reservationDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la recherche de la réservation par ID", e);
        }
    }

    public Reservation update(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.update(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Erreur lors de la mise à jour de la réservation", e);
        }
    }
    public boolean checkVehicleAvailability(int vehicleId, LocalDate debut, LocalDate fin) {
        try {
            List<Reservation> reservations = reservationDao.findReservationsByVehicleId(vehicleId);
            for (Reservation reservation : reservations) {
                if (debut.isBefore(reservation.getFin()) && fin.isAfter(reservation.getDebut())) {
                    return false;
                }
            }
            return true;
        } catch (DaoException e) {
            try {
                throw new ServiceException("Erreur lors de la vérification de la disponibilité du véhicule", e);
            } catch (ServiceException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

