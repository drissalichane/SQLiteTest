package com.example.sqlitetest.classes;

public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private String dateNaissance; // New field for Date of Birth
    private String imagePath; // New field for Image Path

    // Default Constructor
    public Etudiant() {
    }

    // Constructor for creating a new student (without ID)
    public Etudiant(String nom, String prenom, String dateNaissance, String imagePath) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.imagePath = imagePath;
    }

    // Constructor for retrieving a student (with ID)
    public Etudiant(int id, String nom, String prenom, String dateNaissance, String imagePath) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
