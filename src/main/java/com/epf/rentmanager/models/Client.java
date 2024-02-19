package com.epf.rentmanager.models;
import java.time.LocalDate;
import java.util.Optional;

public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate birthdate;

    public Client(int id, String nom, String prenom, String email, LocalDate birthdate){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.birthdate = birthdate;
    }

    public Client(String nom, String prenom, String email, LocalDate naissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.birthdate = naissance;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getNaissance() {
        return birthdate;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNaissance(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}

