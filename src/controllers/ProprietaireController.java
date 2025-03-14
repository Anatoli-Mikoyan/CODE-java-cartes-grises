package controllers;

import database.DatabaseConnection;
import models.Proprietaire;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProprietaireController {

    // Récupérer tous les propriétaires
    public List<Proprietaire> getAllProprietaires() {
        List<Proprietaire> proprietaires = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM PROPRIETAIRE")) {

            while (rs.next()) {
                proprietaires.add(new Proprietaire(
                        rs.getInt("id_proprietaire"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("cp"),
                        rs.getString("ville")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proprietaires;
    }

    // Ajouter un propriétaire
    public void addProprietaire(String nom, String prenom, String adresse, String cp, String ville) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Vérifier les doublons
            if (isDuplicate(conn, nom, prenom, adresse, cp, ville)) {
                showAlert("Erreur", "Ce propriétaire existe déjà en base de données.");
                return;
            }

            // Insérer un nouveau propriétaire
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO PROPRIETAIRE (nom, prenom, adresse, cp, ville) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, adresse);
                ps.setString(4, cp);
                ps.setString(5, ville);
                ps.executeUpdate();
                showAlert("Succès", "Le propriétaire '" + nom + " " + prenom + "' a été ajouté avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du propriétaire.");
        }
    }

    // Mettre à jour un propriétaire
    public void updateProprietaire(int idProprietaire, String nom, String prenom, String adresse, String cp, String ville) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Vérifier les doublons
            if (isDuplicate(conn, nom, prenom, adresse, cp, ville)) {
                showAlert("Erreur", "Ce propriétaire existe déjà en base de données.");
                return;
            }

            // Mettre à jour le propriétaire
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE PROPRIETAIRE SET nom = ?, prenom = ?, adresse = ?, cp = ?, ville = ? WHERE id_proprietaire = ?")) {
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, adresse);
                ps.setString(4, cp);
                ps.setString(5, ville);
                ps.setInt(6, idProprietaire);
                ps.executeUpdate();
                showAlert("Succès", "Le propriétaire a été mis à jour avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour du propriétaire.");
        }
    }

    // Supprimer un propriétaire
    public void deleteProprietaire(int idProprietaire) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String nomProprietaire = getProprietaireNameById(conn, idProprietaire);
            if (nomProprietaire == null) {
                showAlert("Erreur", "Le propriétaire à supprimer n'existe pas.");
                return;
            }

            int confirmation = showConfirmation("Confirmation de suppression",
                    "Êtes-vous sûr de vouloir supprimer le propriétaire '" + nomProprietaire + "' ?");

            if (confirmation == JOptionPane.YES_OPTION) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM PROPRIETAIRE WHERE id_proprietaire = ?")) {
                    ps.setInt(1, idProprietaire);
                    ps.executeUpdate();
                    showAlert("Succès", "Le propriétaire '" + nomProprietaire + "' a été supprimé avec succès !");
                }
            } else {
                showAlert("Annulé", "La suppression a été annulée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la suppression du propriétaire.");
        }
    }

    // Vérifie si un enregistrement est un doublon
    private boolean isDuplicate(Connection conn, String nom, String prenom, String adresse, String cp, String ville) {
        String query = "SELECT COUNT(*) FROM PROPRIETAIRE WHERE nom = ? AND prenom = ? AND adresse = ? AND cp = ? AND ville = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, adresse);
            ps.setString(4, cp);
            ps.setString(5, ville);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retourne true si un doublon existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Récupérer le nom complet du propriétaire à partir de son ID
    private String getProprietaireNameById(Connection conn, int idProprietaire) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT CONCAT(nom, ' ', prenom) AS nom_complet FROM PROPRIETAIRE WHERE id_proprietaire = ?")) {
            ps.setInt(1, idProprietaire);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_complet");
                }
            }
        }
        return null;
    }

    // Afficher une alerte
    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Afficher une boîte de confirmation
    private int showConfirmation(String title, String message) {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }
}
