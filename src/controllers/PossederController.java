package controllers;

import models.Posseder;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contrôleur pour gérer les opérations CRUD liées aux relations de possession entre propriétaires et véhicules.
 * Cette classe fait l'interface entre la vue et la base de données pour les entités POSSEDER.
 */
public class PossederController {
    // Constantes pour les requêtes SQL
    private static final String SELECT_ALL_QUERY = "SELECT * FROM POSSEDER";
    private static final String INSERT_QUERY = "INSERT INTO POSSEDER (id_proprietaire, id_vehicule, date_debut_propriete, date_fin_propriete) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE POSSEDER SET date_debut_propriete = ?, date_fin_propriete = ? WHERE id_proprietaire = ? AND id_vehicule = ?";
    private static final String DELETE_QUERY = "DELETE FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ?";
    private static final String GET_PROPRIETAIRE_ID_QUERY = "SELECT id_proprietaire FROM PROPRIETAIRE WHERE nom = ?";
    private static final String GET_VEHICULE_ID_QUERY = "SELECT v.id_vehicule FROM VEHICULE v JOIN MODELE m ON v.id_modele = m.id_modele WHERE m.nom_modele = ?";
    private static final String GET_PROPRIETAIRE_NOM_QUERY = "SELECT nom FROM PROPRIETAIRE WHERE id_proprietaire = ?";
    private static final String GET_MODELE_NOM_QUERY = "SELECT m.nom_modele FROM MODELE m JOIN VEHICULE v ON m.id_modele = v.id_modele WHERE v.id_vehicule = ?";
    
    // Logger pour une gestion des erreurs plus professionnelle
    private static final Logger LOGGER = Logger.getLogger(PossederController.class.getName());

    /**
     * Constructeur par défaut.
     */
    public PossederController() {
        // Initialisation du logger
        LOGGER.setLevel(Level.INFO);
    }

    /**
     * Récupère toutes les relations POSSEDER de la base de données.
     * 
     * @return Liste des relations de possession
     * @throws SQLException Si une erreur de base de données survient
     */
    public List<Posseder> getAllPosseder() {
        List<Posseder> possederList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_QUERY)) {

            while (rs.next()) {
                Posseder posseder = new Posseder(
                    rs.getInt("id_proprietaire"),
                    rs.getInt("id_vehicule"),
                    rs.getDate("date_debut_propriete"),
                    rs.getDate("date_fin_propriete")
                );
                possederList.add(posseder);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des relations POSSEDER", e);
            throw new RuntimeException("Impossible de récupérer les relations de possession", e);
        }
        
        return possederList;
    }

    /**
     * Recherche des relations POSSEDER avec des filtres spécifiques.
     * 
     * @param idProprietaire ID du propriétaire (0 pour ignorer ce filtre)
     * @param idVehicule ID du véhicule (0 pour ignorer ce filtre)
     * @return Liste des relations de possession correspondant aux critères
     * @throws SQLException Si une erreur de base de données survient
     */
    public List<Posseder> searchPosseder(int idProprietaire, int idVehicule) {
        List<Posseder> possederList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM POSSEDER WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        
        if (idProprietaire > 0) {
            queryBuilder.append(" AND id_proprietaire = ?");
            parameters.add(idProprietaire);
        }
        
        if (idVehicule > 0) {
            queryBuilder.append(" AND id_vehicule = ?");
            parameters.add(idVehicule);
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
            
            // Définir les paramètres de la requête
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Posseder posseder = new Posseder(
                        rs.getInt("id_proprietaire"),
                        rs.getInt("id_vehicule"),
                        rs.getDate("date_debut_propriete"),
                        rs.getDate("date_fin_propriete")
                    );
                    possederList.add(posseder);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de relations POSSEDER", e);
            throw new RuntimeException("Impossible de rechercher les relations de possession", e);
        }
        
        return possederList;
    }

    /**
     * Ajoute une nouvelle relation POSSEDER à la base de données.
     * 
     * @param posseder L'objet Posseder à ajouter
     * @return true si l'ajout a réussi, false sinon
     * @throws SQLException Si une erreur de base de données survient
     */
    public boolean addPosseder(Posseder posseder) {
        if (posseder == null || posseder.getDateDebutPropriete() == null) {
            LOGGER.log(Level.WARNING, "Tentative d'ajout d'un objet Posseder invalide");
            throw new IllegalArgumentException("L'objet Posseder ou sa date de début ne peut pas être null");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_QUERY)) {
            
            pstmt.setInt(1, posseder.getIdProprietaire());
            pstmt.setInt(2, posseder.getIdVehicule());
            pstmt.setDate(3, new java.sql.Date(posseder.getDateDebutPropriete().getTime()));
            
            if (posseder.getDateFinPropriete() != null) {
                pstmt.setDate(4, new java.sql.Date(posseder.getDateFinPropriete().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Échec de l'ajout de la relation POSSEDER, aucune ligne affectée");
                return false;
            }
            
            LOGGER.log(Level.INFO, "Relation POSSEDER ajoutée avec succès: {0}-{1}", 
                    new Object[]{posseder.getIdProprietaire(), posseder.getIdVehicule()});
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout d'une relation POSSEDER", e);
            throw new RuntimeException("Impossible d'ajouter la relation de possession", e);
        }
    }

    /**
     * Met à jour une relation POSSEDER existante dans la base de données.
     * 
     * @param posseder L'objet Posseder avec les nouvelles valeurs
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException Si une erreur de base de données survient
     */
    public boolean updatePosseder(Posseder posseder) {
        if (posseder == null || posseder.getDateDebutPropriete() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'un objet Posseder invalide");
            throw new IllegalArgumentException("L'objet Posseder ou sa date de début ne peut pas être null");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_QUERY)) {
            
            pstmt.setDate(1, new java.sql.Date(posseder.getDateDebutPropriete().getTime()));
            
            if (posseder.getDateFinPropriete() != null) {
                pstmt.setDate(2, new java.sql.Date(posseder.getDateFinPropriete().getTime()));
            } else {
                pstmt.setNull(2, Types.DATE);
            }
            
            pstmt.setInt(3, posseder.getIdProprietaire());
            pstmt.setInt(4, posseder.getIdVehicule());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Échec de la mise à jour de la relation POSSEDER, aucune ligne affectée");
                return false;
            }
            
            LOGGER.log(Level.INFO, "Relation POSSEDER mise à jour avec succès: {0}-{1}", 
                    new Object[]{posseder.getIdProprietaire(), posseder.getIdVehicule()});
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour d'une relation POSSEDER", e);
            throw new RuntimeException("Impossible de mettre à jour la relation de possession", e);
        }
    }

    /**
     * Supprime une relation POSSEDER de la base de données.
     * 
     * @param idProprietaire L'identifiant du propriétaire
     * @param idVehicule L'identifiant du véhicule
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException Si une erreur de base de données survient
     */
    public boolean deletePosseder(int idProprietaire, int idVehicule) {
        if (idProprietaire <= 0 || idVehicule <= 0) {
            LOGGER.log(Level.WARNING, "Tentative de suppression avec des identifiants invalides: {0}-{1}", 
                    new Object[]{idProprietaire, idVehicule});
            throw new IllegalArgumentException("Les identifiants doivent être positifs");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_QUERY)) {
            
            pstmt.setInt(1, idProprietaire);
            pstmt.setInt(2, idVehicule);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                LOGGER.log(Level.WARNING, "Relation POSSEDER non trouvée pour la suppression: {0}-{1}", 
                        new Object[]{idProprietaire, idVehicule});
                return false;
            }
            
            LOGGER.log(Level.INFO, "Relation POSSEDER supprimée avec succès: {0}-{1}", 
                    new Object[]{idProprietaire, idVehicule});
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression d'une relation POSSEDER", e);
            throw new RuntimeException("Impossible de supprimer la relation de possession", e);
        }
    }

    /**
     * Récupère l'identifiant d'un propriétaire à partir de son nom.
     * 
     * @param nomProprietaire Le nom du propriétaire
     * @return L'identifiant du propriétaire ou -1 si non trouvé
     */
    public int getIdProprietaire(String nomProprietaire) {
        if (nomProprietaire == null || nomProprietaire.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de récupération d'ID avec un nom de propriétaire vide");
            return -1;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_PROPRIETAIRE_ID_QUERY)) {
            
            pstmt.setString(1, nomProprietaire.trim());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_proprietaire");
                } else {
                    LOGGER.log(Level.INFO, "Aucun propriétaire trouvé avec le nom: {0}", nomProprietaire);
                    return -1;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'ID du propriétaire", e);
            throw new RuntimeException("Impossible de récupérer l'identifiant du propriétaire", e);
        }
    }

    /**
     * Récupère l'identifiant d'un véhicule à partir du nom de son modèle.
     * 
     * @param nomModele Le nom du modèle du véhicule
     * @return L'identifiant du véhicule ou -1 si non trouvé
     */
    public int getIdVehiculeParModele(String nomModele) {
        if (nomModele == null || nomModele.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de récupération d'ID avec un nom de modèle vide");
            return -1;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_VEHICULE_ID_QUERY)) {
            
            pstmt.setString(1, nomModele.trim());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_vehicule");
                } else {
                    LOGGER.log(Level.INFO, "Aucun véhicule trouvé avec le modèle: {0}", nomModele);
                    return -1;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'ID du véhicule", e);
            throw new RuntimeException("Impossible de récupérer l'identifiant du véhicule", e);
        }
    }

    /**
     * Récupère le nom d'un propriétaire à partir de son identifiant.
     * 
     * @param idProprietaire L'identifiant du propriétaire
     * @return Le nom du propriétaire ou "Inconnu" si non trouvé
     */
    public String getNomProprietaire(int idProprietaire) {
        if (idProprietaire <= 0) {
            LOGGER.log(Level.WARNING, "Tentative de récupération de nom avec un ID de propriétaire invalide: {0}", idProprietaire);
            return "Inconnu";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_PROPRIETAIRE_NOM_QUERY)) {
            
            pstmt.setInt(1, idProprietaire);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom");
                } else {
                    LOGGER.log(Level.INFO, "Aucun propriétaire trouvé avec l'ID: {0}", idProprietaire);
                    return "Inconnu";
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du nom du propriétaire", e);
            return "Erreur";
        }
    }

    /**
     * Récupère le nom du modèle d'un véhicule à partir de son identifiant.
     * 
     * @param idVehicule L'identifiant du véhicule
     * @return Le nom du modèle du véhicule ou "Inconnu" si non trouvé
     */
    public String getNomModele(int idVehicule) {
        if (idVehicule <= 0) {
            LOGGER.log(Level.WARNING, "Tentative de récupération de nom avec un ID de véhicule invalide: {0}", idVehicule);
            return "Inconnu";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_MODELE_NOM_QUERY)) {
            
            pstmt.setInt(1, idVehicule);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_modele");
                } else {
                    LOGGER.log(Level.INFO, "Aucun modèle trouvé pour le véhicule avec l'ID: {0}", idVehicule);
                    return "Inconnu";
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du nom du modèle", e);
            return "Erreur";
        }
    }

    /**
     * Vérifie si une relation POSSEDER existe déjà dans la base de données.
     * 
     * @param idProprietaire L'identifiant du propriétaire
     * @param idVehicule L'identifiant du véhicule
     * @return true si la relation existe, false sinon
     */
    public boolean possederExists(int idProprietaire, int idVehicule) {
        String query = "SELECT 1 FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idProprietaire);
            pstmt.setInt(2, idVehicule);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'existence d'une relation POSSEDER", e);
            throw new RuntimeException("Impossible de vérifier l'existence de la relation", e);
        }
    }

    /**
     * Récupère une relation POSSEDER spécifique si elle existe.
     * 
     * @param idProprietaire L'identifiant du propriétaire
     * @param idVehicule L'identifiant du véhicule
     * @return Un Optional contenant l'objet Posseder s'il est trouvé, un Optional vide sinon
     */
    public Optional<Posseder> getPosseder(int idProprietaire, int idVehicule) {
        String query = "SELECT * FROM POSSEDER WHERE id_proprietaire = ? AND id_vehicule = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, idProprietaire);
            pstmt.setInt(2, idVehicule);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Posseder posseder = new Posseder(
                        rs.getInt("id_proprietaire"),
                        rs.getInt("id_vehicule"),
                        rs.getDate("date_debut_propriete"),
                        rs.getDate("date_fin_propriete")
                    );
                    return Optional.of(posseder);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération d'une relation POSSEDER spécifique", e);
            throw new RuntimeException("Impossible de récupérer la relation de possession", e);
        }
    }
}