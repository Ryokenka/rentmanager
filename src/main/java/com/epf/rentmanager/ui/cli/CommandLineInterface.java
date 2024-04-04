package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.configuration.AppConfiguration;
import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.models.Vehicule;
import com.epf.rentmanager.models.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.utils.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class CommandLineInterface {
    static ApplicationContext context = new
            AnnotationConfigApplicationContext(AppConfiguration.class);
    static ClientService clientService = context.getBean(ClientService.class);
    static VehicleService vehicleService = context.getBean(VehicleService.class);
    static ReservationService reservationService = context.getBean(ReservationService.class);


    public static void main(String[] args) {
        System.out.println("Bienvenue dans l'interface en ligne de commande !");
        while(true) {
            showMenu();
        }
    }

    private static void showMenu() {
        System.out.println("Veuillez choisir une option :");
        System.out.println("1. Créer un client");
        System.out.println("2. Lister tous les clients");
        System.out.println("3. Créer un véhicule");
        System.out.println("4. Lister tous les véhicules");
        System.out.println("5. Supprimer un client");
        System.out.println("6. Supprimer un véhicule");
        System.out.println("7. Créer une réservation");
        System.out.println("8. Lister toutes les réservations");
        System.out.println("9. Lister toutes les réservations associées à un client");
        System.out.println("10. Lister toutes les réservations associées à un véhicule");
        System.out.println("11. Supprimer une réservation");
        System.out.println("0. Quitter");

        int choice = IOUtils.readInt("Votre choix : ");

        switch (choice) {
            case 1:
                createClient();
                break;
            case 2:
                listAllClients();
                break;
            case 3:
                createVehicle();
                break;
            case 4:
                listAllVehicles();
                break;
            case 5:
                deleteClient();
                break;
            case 6:
                deleteVehicle();
                break;
            case 7:
                createReservation();
                break;
            case 8:
                listAllReservations();
                break;
            case 9:
                listReservationsByClientId();
                break;
            case 10:
                listReservationsByVehicleId();
                break;
            case 11:
                deleteReservation();
                break;
            case 0:
                System.out.println("Au revoir !");
                System.exit(0);
            default:
                System.out.println("Choix invalide !");
                showMenu();
        }
    }

    private static void createClient() {
        System.out.println("Création d'un nouveau client :");
        String nom = IOUtils.readString("Nom : ", true);
        String prenom = IOUtils.readString("Prénom : ", true);
        String email = "";
        boolean validEmail = false;

        // Validate email format
        while (!validEmail) {
            email = IOUtils.readString("Email : ", true);
            if (isValidEmail(email)) {
                validEmail = true;
            } else {
                System.out.println("Format d'email incorrect. Veuillez saisir une adresse email valide.");
            }
        }

        LocalDate naissance = IOUtils.readDate("Date de naissance (dd/MM/yyyy) : ", true);

        try {
            Client client = new Client(nom, prenom, email, naissance);
            clientService.create(client);
            System.out.println("Le client a été créé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la création du client : " + e.getMessage());
        }
    }


    private static void listAllClients() {
        try {
            List<Client> clients = clientService.findAll();
            System.out.println("Liste de tous les clients :");
            for (Client client : clients) {
                System.out.println(client);
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des clients : " + e.getMessage());
        }
    }

    private static void createVehicle() {
        System.out.println("Création d'un nouveau véhicule :");
        String constructeur = IOUtils.readString("Constructeur : ", true);
        String modele = IOUtils.readString("Modèle : ", true);
        int nbPlaces = IOUtils.readInt("Nombre de places : ");

        try {
            Vehicule vehicle = new Vehicule(constructeur, modele, nbPlaces);
            vehicleService.create(vehicle);
            System.out.println("Le véhicule a été créé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la création du véhicule : " + e.getMessage());
        }
    }

    private static void listAllVehicles() {
        try {
            List<Vehicule> vehicles = vehicleService.findAll();
            System.out.println("Liste de tous les véhicules :");
            for (Vehicule vehicle : vehicles) {
                System.out.println(vehicle);
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des véhicules : " + e.getMessage());
        }
    }

    private static void deleteClient() {
        long id = IOUtils.readInt("ID du client à supprimer : ");
        try {
            Client client = clientService.findById(id);
            clientService.delete(client);
            System.out.println("Le client et ses réservations ont été supprimés avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression du client et de ses réservations : " + e.getMessage());
        }
    }

    private static void deleteVehicle() {
        long id = IOUtils.readInt("ID du véhicule à supprimer : ");
        try {
            Vehicule vehicle = vehicleService.findById(id);
            vehicleService.delete(vehicle);
            System.out.println("Le véhicule et ses réservations associées ont été supprimés avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression du véhicule et de ses réservations associées: " + e.getMessage());
        }
    }
    private static void createReservation() {
        System.out.println("Création d'une nouvelle réservation :");
        long clientId = IOUtils.readInt("ID du client : ");
        long vehicleId = IOUtils.readInt("ID du véhicule : ");
        LocalDate debut = IOUtils.readDate("Date de début (dd/MM/yyyy) : ", true);
        LocalDate fin = IOUtils.readDate("Date de fin (dd/MM/yyyy) : ", true);

        try {
            Reservation reservation = new Reservation((int) clientId, (int) vehicleId, debut, fin);
            reservationService.create(reservation);
            System.out.println("La réservation a été créée avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la création de la réservation : " + e.getMessage());
        }
    }
    private static void listAllReservations() {
        try {
            List<Reservation> reservations = reservationService.findAll();
            System.out.println("Liste de toutes les réservations :");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }
    private static void listReservationsByClientId() {
        long clientId = IOUtils.readInt("ID du client : ");
        try {
            List<Reservation> reservations = reservationService.findReservationsByClientId(clientId);
            System.out.println("Liste des réservations associées au client " + clientId + " :");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }
    private static void listReservationsByVehicleId() {
        long vehicleId = IOUtils.readInt("ID du véhicule : ");
        try {
            List<Reservation> reservations = reservationService.findReservationsByVehicleId(vehicleId);
            System.out.println("Liste des réservations associées au véhicule " + vehicleId + " :");
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
    }
    private static void deleteReservation() {
        long reservationId = IOUtils.readInt("ID de la réservation à supprimer : ");
        try {
            reservationService.delete(reservationId);
            System.out.println("La réservation a été supprimée avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }


    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
