package models;

import java.util.Date;

/**
 * Classe représentant la table "POSSEDER" de la base de données.
 * Les noms des attributs correspondent aux colonnes de la table.
 */
public class Posseder {
    private int id_proprietaire; // Identifiant unique du propriétaire (clé étrangère)
    private int id_vehicule; // Identifiant du véhicule associé (clé étrangère)
    private Date date_debut_propriete; // Date de début de la propriété du véhicule
    private Date date_fin_propriete; // Date de fin de la propriété du véhicule

    // Constructeur
    public Posseder(int id_proprietaire, int id_vehicule, Date date_debut_propriete, Date date_fin_propriete) {
        this.id_proprietaire = id_proprietaire;
        this.id_vehicule = id_vehicule;
        this.date_debut_propriete = date_debut_propriete;
        this.date_fin_propriete = date_fin_propriete;
    }

    // Getters et Setters
    public int getIdProprietaire() {
        return id_proprietaire;
    }

    public void setIdProprietaire(int id_proprietaire) {
        this.id_proprietaire = id_proprietaire;
    }

    public int getIdVehicule() {
        return id_vehicule;
    }

    public void setIdVehicule(int id_vehicule) {
        this.id_vehicule = id_vehicule;
    }

    public Date getDateDebutPropriete() {
        return date_debut_propriete;
    }

    public void setDateDebutPropriete(Date date_debut_propriete) {
        this.date_debut_propriete = date_debut_propriete;
    }

    public Date getDateFinPropriete() {
        return date_fin_propriete;
    }

    public void setDateFinPropriete(Date date_fin_propriete) {
        this.date_fin_propriete = date_fin_propriete;
    }

    // Méthode toString() : Affiche un résumé des informations du propriétaire et du véhicule.
    @Override
    public String toString() {
        return "Propriétaire ID: " + id_proprietaire + ", Véhicule ID: " + id_vehicule;
    }

    // Méthode pour vérifier si la propriété du véhicule est encore valide
    public boolean estProprietaireActuel() {
        Date today = new Date(); // Récupère la date actuelle
        
        // Vérifie si la date actuelle est après la date de début de propriété et avant la date de fin de propriété
        return today.after(date_debut_propriete) && (date_fin_propriete == null || today.before(date_fin_propriete));
    }
}
