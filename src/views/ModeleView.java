package views;

import controllers.ModeleController;
import models.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ModeleView extends JFrame {
    private ModeleController controller;

    public ModeleView() {
        controller = new ModeleController();

        setTitle("Gestion des Modèles");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposition verticale

        // Charger les modèles
        List<Modele> modeles = controller.getAllModeles();
        for (Modele modele : modeles) {
            String nomMarque = controller.getNomMarqueById(modele.getId_marque());

            // Créer un panel pour chaque modèle avec un FlowLayout
            JPanel modelePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel modeleLabel = new JLabel(modele.getNom_modele() + " (Marque : " + nomMarque + ")");

            // Bouton Modifier
            JButton modifyButton = new JButton("Modifier");
            modifyButton.addActionListener(e -> {
                JTextField nomField = new JTextField(modele.getNom_modele());
                JTextField marqueField = new JTextField(nomMarque);
                Object[] message = {
                        "Nom du modèle :", nomField,
                        "Nom de la marque associée :", marqueField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Modifier un Modèle", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        String newNom = nomField.getText();
                        String newNomMarque = marqueField.getText(); // Nom de la marque
                        controller.updateModele(modele.getId_modele(), newNom, newNomMarque);
                        refreshView();
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la modification du modèle.");
                    }
                }
            });

            // Bouton Supprimer
            JButton deleteButton = new JButton("Supprimer");
            deleteButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Êtes-vous sûr de vouloir supprimer ce modèle ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        controller.deleteModele(modele.getId_modele());
                        refreshView();
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la suppression du modèle.");
                    }
                }
            });

            // Ajouter les composants au panel
            modelePanel.add(modeleLabel);
            modelePanel.add(modifyButton);
            modelePanel.add(deleteButton);
            panel.add(modelePanel);
        }

        // Panel pour les boutons "Ajouter" et "Retour"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Bouton "Ajouter un Modèle"
        JButton addButton = new JButton("Ajouter un Modèle");
        addButton.addActionListener(e -> {
            JTextField nomField = new JTextField();
            JTextField marqueField = new JTextField();
            Object[] message = {
                    "Nom du modèle :", nomField,
                    "Nom de la marque associée :", marqueField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Ajouter un Modèle", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String nom = nomField.getText();
                    String nomMarque = marqueField.getText(); // Utiliser le nom de la marque
                    controller.addModele(nom, nomMarque);
                    refreshView();
                } catch (Exception ex) {
                    showErrorMessage("Une erreur est survenue lors de l'ajout du modèle.");
                }
            }
        });

        // Bouton "Retour"
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(e -> dispose());

        // Ajouter les boutons au panel
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        // Ajouter le panel des boutons au panneau principal
        panel.add(buttonPanel);

        // Ajouter un JScrollPane pour la barre de défilement
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        // Rendre visible
        setVisible(true);
    }

    // Méthode pour rafraîchir la vue
    private void refreshView() {
        dispose();
        new ModeleView(); // Recharger la vue avec les nouveaux modèles
    }

    // Afficher un message d'erreur
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
