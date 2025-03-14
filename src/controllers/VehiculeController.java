package controllers;

import database.DatabaseConnection;
import models.Vehicule;

import javax.swing.JOptionPane; // Pour afficher des messages d'erreur sous forme de pop-up
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour gérer les véhicules dans la base de données.
 */
public class VehiculeController {

    // Récupère tous les véhicules
    public List<Vehicule> getAllVehicules() {
        List<Vehicule> vehicules = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM VEHICULE")) {

            while (rs.next()) {
                vehicules.add(new Vehicule(
                        rs.getInt("id_vehicule"),
                        rs.getString("matricule"),
                        rs.getInt("annee_sortie"),
                        rs.getDouble("poids"),
                        rs.getInt("puissance_chevaux"),
                        rs.getInt("puissance_fiscale"),
                        rs.getInt("id_modele")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicules;
    }

    // Ajouter un véhicule
    public void addVehicule(String matricule, int anneeSortie, double poids, int puissanceChevaux, int puissanceFiscale,
            String nomModele) {
        int idModele = getModeleIdByName(nomModele);

        if (idModele == -1) {
            JOptionPane.showMessageDialog(null, "Erreur : Modèle invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (existsVehicule(matricule)) {
            JOptionPane.showMessageDialog(null, "Erreur : Un véhicule avec ce matricule existe déjà.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO VEHICULE (matricule, annee_sortie, poids, puissance_chevaux, puissance_fiscale, id_modele) "
                                +
                                "VALUES (?, ?, ?, ?, ?, ?)")) { // Ajout de l'id_marque dans l'insertion

            ps.setString(1, matricule);
            ps.setInt(2, anneeSortie);
            ps.setDouble(3, poids);
            ps.setInt(4, puissanceChevaux);
            ps.setInt(5, puissanceFiscale);
            ps.setInt(6, idModele);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier un véhicule
    public void updateVehicule(int idVehicule, String newMatricule, int anneeSortie, double poids, int puissanceChevaux,
            int puissanceFiscale, String nomModele) {
        int idModele = getModeleIdByName(nomModele);
        if (idModele == -1) {
            JOptionPane.showMessageDialog(null, "Erreur : Le modèle '" + nomModele + "' n'existe pas.", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE VEHICULE SET matricule = ?, annee_sortie = ?, poids = ?, puissance_chevaux = ?, puissance_fiscale = ?, id_modele = ? "
                                +
                                "WHERE id_vehicule = ?")) {

            ps.setString(1, newMatricule);
            ps.setInt(2, anneeSortie);
            ps.setDouble(3, poids);
            ps.setInt(4, puissanceChevaux);
            ps.setInt(5, puissanceFiscale);
            ps.setInt(6, idModele);
            ps.setInt(7, idVehicule);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un véhicule
    public void deleteVehicule(int idVehicule) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("DELETE FROM VEHICULE WHERE id_vehicule = ?")) {

            ps.setInt(1, idVehicule);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vérifie si un véhicule existe déjà (par matricule)
    private boolean existsVehicule(String matricule) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM VEHICULE WHERE matricule = ?")) {

            ps.setString(1, matricule);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Récupérer le nom d'un modèle à partir de son ID
    public String getModeleNameById(int idModele) {
        String query = "SELECT nom_modele FROM MODELE WHERE id_modele = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idModele);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_modele");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Récupérer l'ID d'un modèle à partir de son nom
    private int getModeleIdByName(String nomModele) {
        String query = "SELECT id_modele FROM MODELE WHERE nom_modele = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nomModele);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_modele");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Récupérer l'ID d'une marque à partir de son nom
    public int getMarqueIdByNom(String nomMarque) {
        String query = "SELECT id_marque FROM MARQUE WHERE nom_marque = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nomMarque);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_marque");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Récupérer le nom de la marque à partir de l'ID du modèle
    public String getMarqueNameByModeleId(int idModele) {
        String query = "SELECT MARQUE.nom_marque FROM MARQUE " +
                "JOIN MODELE ON MARQUE.id_marque = MODELE.id_marque " +
                "WHERE MODELE.id_modele = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, idModele);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_marque");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
