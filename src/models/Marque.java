package models;

// Modèle pour la table MARQUE
public class Marque {
    private int id_marque;
    private String nom_marque;

    // Constructeur
    public Marque(int id_marque, String nom_marque) {
        this.id_marque = id_marque;
        this.nom_marque = nom_marque;
    }

    // Getters et setters
    public int getIdMarque() {
        return id_marque;
    }

    public void setIdMarque(int id_marque) {
        this.id_marque = id_marque;
    }

    public String getNomMarque() {
        return nom_marque;
    }

    public void setNomMarque(String nom_marque) {
        this.nom_marque = nom_marque;
    }
}
