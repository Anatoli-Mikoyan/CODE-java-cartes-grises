package models;

/**
 * Classe représentant la table "MODELE" de la base de données.
 * Les noms des attributs correspondent aux colonnes de la table.
 */
public class Modele {
    private int id_modele; // Identifiant unique du modèle
    private String nom_modele; // Nom du modèle
    private int id_marque; // Identifiant de la marque associée (clé étrangère)

    // Constructeur
    public Modele(int id_modele, String nom_modele, int id_marque) {
        this.id_modele = id_modele;
        this.nom_modele = nom_modele;
        this.id_marque = id_marque;
    }

    // Getters et Setters
    public int getId_modele() {
        return id_modele;
    }

    public void setId_modele(int id_modele) {
        this.id_modele = id_modele;
    }

    public String getNom_modele() {
        return nom_modele;
    }

    public void setNom_modele(String nom_modele) {
        this.nom_modele = nom_modele;
    }

    public int getId_marque() {
        return id_marque;
    }

    public void setId_marque(int id_marque) {
        this.id_marque = id_marque;
    }

    @Override
    public String toString() {
        return nom_modele;
    }
}
