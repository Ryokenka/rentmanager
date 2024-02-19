package com.epf.rentmanager.models;
import java.time.LocalDate;
public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate birthdate;

    public Client(){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.birthdate = birthdate;
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

