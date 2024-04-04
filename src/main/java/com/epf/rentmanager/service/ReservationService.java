package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.models.Reservation;
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
}

