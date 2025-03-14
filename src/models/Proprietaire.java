package models;

public class Proprietaire {
    private int id_proprietaire; // Identifiant unique pour chaque propriétaire
    private String nom;          // Le nom du propriétaire
    private String prenom;       // Le prénom du propriétaire
    private String adresse;      // L'adresse du propriétaire
    private String cp;           // Le code postal du propriétaire
    private String ville;        // La ville du propriétaire

    // Constructeur
    public Proprietaire(int id_proprietaire, String nom, String prenom, String adresse, String cp, String ville) {
        this.id_proprietaire = id_proprietaire;  // Initialisation des variables avec les valeurs données
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.cp = cp;
        this.ville = ville;
    }

    // Getters et Setters : Permet d'obtenir ou de modifier les valeurs des attributs
    public int getId_proprietaire() {
        return id_proprietaire;
    }

    public void setId_proprietaire(int id_proprietaire) {
        this.id_proprietaire = id_proprietaire;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
