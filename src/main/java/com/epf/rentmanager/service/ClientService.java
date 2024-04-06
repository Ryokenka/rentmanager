package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.models.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

	private final ClientDao clientDao;
	private final ReservationService reservationService;

	private ClientService(ClientDao clientDao, ReservationService reservationService) {
		this.clientDao = clientDao;
		this.reservationService = reservationService;
	}

	public long create(Client client) throws ServiceException {
		validateClient(client);
		client.setNom(client.getNom().toUpperCase());
		try {
			return clientDao.create(client);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la création d'un client");
		}
	}

	public Client findById(long id) throws ServiceException {
		try {
			return clientDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la recherche d'un client par id");
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			return clientDao.findAll();
		} catch (DaoException e) {
			throw new ServiceException("Error lors du listage de l'ensemble des clients");
		}
	}

	private void validateClient(Client client) throws ServiceException {
		if (client.getNom().isEmpty() || client.getPrenom().isEmpty()) {
			throw new ServiceException("Le prénom ou nom du client ne peut être vide");
		}
	}
	public void delete(Client client) throws ServiceException {
		try {
			long clientId = client.getId();

			List<Reservation> reservations = reservationService.findReservationsByClientId(clientId);

			for (Reservation reservation : reservations) {
				reservationService.delete(reservation.getId());
			}

			clientDao.delete(client);
		} catch (DaoException | ServiceException e) {
			throw new ServiceException("Erreur lors de la suppression du client et de ses réservations", e);
		}
	}
	public int countClients() throws ServiceException {
		try {
			return clientDao.count();
		} catch (DaoException e) {
			throw new ServiceException("Erreur lors de la récupération du nombre de clients");
		}
	}

}