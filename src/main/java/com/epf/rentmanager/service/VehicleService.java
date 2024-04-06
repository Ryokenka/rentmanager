package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.models.Vehicule;
import com.epf.rentmanager.models.Reservation;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

	private final VehicleDao vehicleDao;
	private final ReservationService reservationService;

	private VehicleService(VehicleDao vehicleDao, ReservationService reservationService) {
		this.vehicleDao = vehicleDao;
		this.reservationService = reservationService;
	}


	public long create(Vehicule vehicule) throws ServiceException {
		validateVehicule(vehicule);
		try {
			return vehicleDao.create(vehicule);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la création du véhicule");
		}
	}

	public Vehicule findById(long id) throws ServiceException {
		try {
			return vehicleDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la recherche du véhicule par id");
		}
	}

	public List<Vehicule> findAll() throws ServiceException {
		try {
			return vehicleDao.findAll();
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors du listage de l'ensemble des véhicules");
		}
	}

	private void validateVehicule(Vehicule vehicule) throws ServiceException {
		if (vehicule.getConstructeur().isEmpty() || vehicule.getNb_places() <= 0) {
			throw new ServiceException("Impossible de vérifier le constructeur et il faut un nombre de places strictement supérieur à 0");
		}
	}

	public void delete(Vehicule vehicle) throws ServiceException {
		try {
			long vehicleId = vehicle.getId();

			List<Reservation> reservations = reservationService.findReservationsByVehicleId(vehicleId);

			for (Reservation reservation : reservations) {
				reservationService.delete(reservation.getId());
			}

			vehicleDao.delete(vehicle);
		} catch (DaoException | ServiceException e) {
			throw new ServiceException("Erreur lors de la suppression du véhicule et de ses réservations", e);
		}
	}
	public int countVehicles() throws ServiceException {
		try {
			return vehicleDao.count();
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors du comptage des véhicules", e);
		}
	}
}

