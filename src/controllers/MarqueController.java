package controllers;

import database.DatabaseConnection;
import models.Marque;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarqueController {

    public List<Marque> getAllMarques() {
        List<Marque> marques = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM MARQUE")) {

            while (rs.next()) {
                marques.add(new Marque(rs.getInt("id_marque"), rs.getString("nom_marque")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marques;
    }

    public void addMarque(String nomMarque) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (existsByNomMarque(conn, nomMarque)) {
                showAlert("Erreur", "La marque '" + nomMarque + "' existe déjà !");
                return;
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO MARQUE (nom_marque) VALUES (?)")) {
                ps.setString(1, nomMarque);
                ps.executeUpdate();
                showAlert("Succès", "La marque '" + nomMarque + "' a été ajoutée avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout de la marque.");
        }
    }

    public void updateMarque(int idMarque, String newNom) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (existsByNomMarqueExcludingId(conn, newNom, idMarque)) {
                showAlert("Erreur", "Une autre marque porte déjà le nom '" + newNom + "' !");
                return;
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE MARQUE SET nom_marque = ? WHERE id_marque = ?")) {
                ps.setString(1, newNom);
                ps.setInt(2, idMarque);
                ps.executeUpdate();
                showAlert("Succès", "La marque a été mise à jour avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour de la marque.");
        }
    }

    public void deleteMarque(int idMarque) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Récupérer le nom de la marque pour confirmation
            String nomMarque = getMarqueNameById(conn, idMarque);
            if (nomMarque == null) {
                showAlert("Erreur", "La marque à supprimer n'existe pas.");
                return;
            }

            // Demander confirmation à l'utilisateur
            int confirmation = showConfirmation("Confirmation de suppression", 
                "Êtes-vous sûr de vouloir supprimer la marque '" + nomMarque + "' ?");

            if (confirmation == JOptionPane.YES_OPTION) {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM MARQUE WHERE id_marque = ?")) {
                    ps.setInt(1, idMarque);
                    ps.executeUpdate();
                    showAlert("Succès", "La marque '" + nomMarque + "' a été supprimée avec succès !");
                }
            } else {
                showAlert("Annulé", "La suppression a été annulée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la suppression de la marque.");
        }
    }

    private String getMarqueNameById(Connection conn, int idMarque) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT nom_marque FROM MARQUE WHERE id_marque = ?")) {
            ps.setInt(1, idMarque);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_marque");
                }
            }
        }
        return null;
    }

    private boolean existsByNomMarque(Connection conn, String nomMarque) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM MARQUE WHERE nom_marque = ?")) {
            ps.setString(1, nomMarque);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean existsByNomMarqueExcludingId(Connection conn, String nomMarque, int idMarque) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM MARQUE WHERE nom_marque = ? AND id_marque <> ?")) {
            ps.setString(1, nomMarque);
            ps.setInt(2, idMarque);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private int showConfirmation(String title, String message) {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }
}
