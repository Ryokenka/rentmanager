package com.epf.rentmanager.models;

public class Vehicule {
    private int id;
    private String constructeur;
    private String modele;
    private int nb_places;
    // Constructor
    public Vehicule() {
        this.id = -1;
        this.constructeur = "Unknown";
        this.modele = "Unknown";
        this.nb_places = 0;
    }
    public Vehicule(int id, String constructeur, String modele, int nb_places) {
        this.id = id;
        this.constructeur = constructeur;
        this.modele = modele;
        this.nb_places = nb_places;
    }

    public Vehicule(String constructeur, String modele, int nbPlaces) {
        this.constructeur = constructeur;
        this.modele = modele;
        this.nb_places = nbPlaces;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getConstructeur() {
        return constructeur;
    }

    public String getModele() {
        return modele;
    }

    public int getNb_places() {
        return nb_places;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setConstructeur(String constructeur) {
        this.constructeur = constructeur;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public void setNb_places(int nb_places) {
        this.nb_places = nb_places;
    }
    @Override
    public String toString() {
        return "Vehicule{" +
                "id=" + id +
                ", constructeur='" + constructeur + '\'' +
                ", modele='" + modele + '\'' +
                ", nb_places=" + nb_places +
                '}';
    }
}

