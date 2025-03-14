package models;

/**
 * Classe représentant la table "VEHICULE" de la base de données.
 * Les noms des attributs correspondent aux colonnes de la table.
 */
public class Vehicule {
    
    // Attributs correspondant aux colonnes de la table "VEHICULE"
    private int id_vehicule; // Identifiant unique du véhicule
    private String matricule; // Matricule du véhicule (plaque d'immatriculation)
    private int annee_sortie; // Année de sortie du véhicule
    private double poids; // Poids du véhicule (en kilogrammes)
    private int puissance_chevaux; // Puissance en chevaux du véhicule
    private int puissance_fiscale; // Puissance fiscale du véhicule (en fonction de la législation)
    private int id_modele; // Identifiant du modèle du véhicule (clé étrangère vers la table "MODELE")

    /**
     * Constructeur de la classe Vehicule.
     * Ce constructeur permet d'initialiser un véhicule avec toutes les informations nécessaires.
     *
     * @param id_vehicule Identifiant unique du véhicule
     * @param matricule Matricule du véhicule
     * @param annee_sortie Année de sortie du véhicule
     * @param poids Poids du véhicule
     * @param puissance_chevaux Puissance en chevaux du véhicule
     * @param puissance_fiscale Puissance fiscale du véhicule
     * @param id_modele Identifiant du modèle associé au véhicule
     */
    public Vehicule(int id_vehicule, String matricule, int annee_sortie, double poids, 
                    int puissance_chevaux, int puissance_fiscale, int id_modele) {
        this.id_vehicule = id_vehicule; // Initialisation de l'identifiant du véhicule
        this.matricule = matricule; // Initialisation du matricule
        this.annee_sortie = annee_sortie; // Initialisation de l'année de sortie
        this.poids = poids; // Initialisation du poids du véhicule
        this.puissance_chevaux = puissance_chevaux; // Initialisation de la puissance en chevaux
        this.puissance_fiscale = puissance_fiscale; // Initialisation de la puissance fiscale
        this.id_modele = id_modele; // Initialisation de l'identifiant du modèle
    }

    // Getters et Setters permettant d'accéder et de modifier les attributs
    
    public int getIdVehicule() {
        return id_vehicule; // Retourne l'identifiant du véhicule
    }

    public void setIdVehicule(int id_vehicule) {
        this.id_vehicule = id_vehicule; // Modifie l'identifiant du véhicule
    }

    public String getMatricule() {
        return matricule; // Retourne le matricule du véhicule
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule; // Modifie le matricule du véhicule
    }

    public int getAnneeSortie() {
        return annee_sortie; // Retourne l'année de sortie du véhicule
    }

    public void setAnneeSortie(int annee_sortie) {
        this.annee_sortie = annee_sortie; // Modifie l'année de sortie du véhicule
    }

    public double getPoids() {
        return poids; // Retourne le poids du véhicule
    }

    public void setPoids(double poids) {
        this.poids = poids; // Modifie le poids du véhicule
    }

    public int getPuissanceChevaux() {
        return puissance_chevaux; // Retourne la puissance en chevaux du véhicule
    }

    public void setPuissanceChevaux(int puissance_chevaux) {
        this.puissance_chevaux = puissance_chevaux; // Modifie la puissance en chevaux du véhicule
    }

    public int getPuissanceFiscale() {
        return puissance_fiscale; // Retourne la puissance fiscale du véhicule
    }

    public void setPuissanceFiscale(int puissance_fiscale) {
        this.puissance_fiscale = puissance_fiscale; // Modifie la puissance fiscale du véhicule
    }

    public int getIdModele() {
        return id_modele; // Retourne l'identifiant du modèle associé au véhicule
    }

    public void setIdModele(int id_modele) {
        this.id_modele = id_modele; // Modifie l'identifiant du modèle du véhicule
    }

    /**
     * Méthode toString pour afficher une représentation textuelle du véhicule.
     * Cette méthode est utile pour l'affichage des objets dans des listes ou des logs.
     *
     * @return Une chaîne de caractères représentant le véhicule (matricule et modèle)
     */
    @Override
    public String toString() {
        // Retourne une chaîne avec le matricule et l'identifiant du modèle
        return "Véhicule [Matricule=" + matricule + ", Modèle ID=" + id_modele + "]";
    }
}
