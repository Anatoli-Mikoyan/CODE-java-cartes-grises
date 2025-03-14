package controllers;

import database.DatabaseConnection;
import models.Modele;

import javax.swing.JOptionPane;  // Pour afficher un message d'erreur sous forme de pop-up
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour gérer les modèles dans la base de données.
 */
public class ModeleController {

    // Récupère tous les modèles
    public List<Modele> getAllModeles() {
        List<Modele> modeles = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM MODELE")) {

            while (rs.next()) {
                modeles.add(new Modele(
                        rs.getInt("id_modele"),
                        rs.getString("nom_modele"),
                        rs.getInt("id_marque")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modeles;
    }

    // Ajouter un modèle
    public void addModele(String nomModele, String nomMarque) {
        int idMarque = getMarqueIdByName(nomMarque);  // Récupère l'ID de la marque à partir de son nom
        if (idMarque == -1) {
            // Si la marque n'existe pas, afficher un message d'erreur
            JOptionPane.showMessageDialog(null, "Erreur : La marque '" + nomMarque + "' n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifie l'existence du modèle
        if (existsModele(nomModele, idMarque)) {
            JOptionPane.showMessageDialog(null, "Erreur : Un modèle avec ce nom existe déjà pour cette marque.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO MODELE (nom_modele, id_marque) VALUES (?, ?)")) {

            ps.setString(1, nomModele);
            ps.setInt(2, idMarque);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier un modèle
    public void updateModele(int idModele, String newNom, String nomMarque) {
        int idMarque = getMarqueIdByName(nomMarque);  // Récupère l'ID de la marque à partir de son nom
        if (idMarque == -1) {
            // Si la marque n'existe pas, afficher un message d'erreur
            JOptionPane.showMessageDialog(null, "Erreur : La marque '" + nomMarque + "' n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifie l'existence du modèle
        if (existsModele(newNom, idMarque)) {
            JOptionPane.showMessageDialog(null, "Erreur : Un modèle avec ce nom existe déjà pour cette marque.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE MODELE SET nom_modele = ?, id_marque = ? WHERE id_modele = ?")) {

            ps.setString(1, newNom);
            ps.setInt(2, idMarque);
            ps.setInt(3, idModele);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un modèle
    public void deleteModele(int idModele) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM MODELE WHERE id_modele = ?")) {

            ps.setInt(1, idModele);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vérifier si un modèle existe déjà pour une marque
    private boolean existsModele(String nomModele, int idMarque) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM MODELE WHERE nom_modele = ? AND id_marque = ?")) {

            ps.setString(1, nomModele);
            ps.setInt(2, idMarque);

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

    // Méthode pour récupérer le nom de la marque à partir de l'ID
public String getNomMarqueById(int idMarque) {
    String query = "SELECT nom_marque FROM marque WHERE id_marque = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setInt(1, idMarque);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("nom_marque");  // Retourne le nom de la marque
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;  // Si la marque n'est pas trouvée
}

    // Vérifie si une marque existe dans la base de données et retourne son ID
    private int getMarqueIdByName(String nomMarque) {
        String query = "SELECT id_marque FROM marque WHERE nom_marque = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nomMarque);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_marque");  // Retourne l'ID de la marque
                } else {
                    return -1;  // Marque non trouvée
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
