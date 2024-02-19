package com.epf.rentmanager.ui.cli;

import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.models.Vehicule;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import com.epf.rentmanager.utils.IOUtils;

import java.time.LocalDate;
import java.util.List;

public class CommandLineInterface {

    private static final ClientService clientService = ClientService.getInstance();
    private static final VehicleService vehicleService = VehicleService.getInstance();

    public static void main(String[] args) {
        showMenu();
    }

    private static void showMenu() {
        System.out.println("Bienvenue dans l'interface en ligne de commande !");
        System.out.println("Veuillez choisir une option :");
        System.out.println("1. Créer un client");
        System.out.println("2. Lister tous les clients");
        System.out.println("3. Créer un véhicule");
        System.out.println("4. Lister tous les véhicules");
        System.out.println("5. Bonus : Supprimer un client");
        System.out.println("6. Bonus : Supprimer un véhicule");
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
        String email = IOUtils.readString("Email : ", true);
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
            System.out.println("Le client a été supprimé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression du client : " + e.getMessage());
        }
    }
    private static void deleteVehicle() {
        long id = IOUtils.readInt("ID du véhicule à supprimer : ");
        try {
            Vehicule vehicle = VehicleService.getInstance().findById(id);
            vehicleService.delete(vehicle);
            System.out.println("Le véhicule a été supprimé avec succès !");
        } catch (ServiceException e) {
            System.out.println("Erreur lors de la suppression du véhicule : " + e.getMessage());
        }
    }
}
