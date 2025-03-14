package views;

import controllers.ProprietaireController;
import models.Proprietaire;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProprietaireView extends JFrame {
    private ProprietaireController controller;

    public ProprietaireView() {
        controller = new ProprietaireController();

        setTitle("Liste des Propriétaires");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Charger les propriétaires
        List<Proprietaire> proprietaires = controller.getAllProprietaires();
        for (Proprietaire proprietaire : proprietaires) {
            JPanel proprietairePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel proprietaireLabel = new JLabel(
                    proprietaire.getNom() + " " + proprietaire.getPrenom() +
                            " (" + proprietaire.getAdresse() + ", " +
                            proprietaire.getCp() + " " + proprietaire.getVille() + ")");

            JButton modifyButton = new JButton("Modifier");
            modifyButton.addActionListener(e -> {
                JTextField nomField = new JTextField(proprietaire.getNom());
                JTextField prenomField = new JTextField(proprietaire.getPrenom());
                JTextField adresseField = new JTextField(proprietaire.getAdresse());
                JTextField cpField = new JTextField(proprietaire.getCp());
                JTextField villeField = new JTextField(proprietaire.getVille());

                Object[] message = {
                        "Nom :", nomField,
                        "Prénom :", prenomField,
                        "Adresse :", adresseField,
                        "Code Postal :", cpField,
                        "Ville :", villeField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Modifier le Propriétaire",
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    controller.updateProprietaire(
                            proprietaire.getId_proprietaire(),
                            nomField.getText(),
                            prenomField.getText(),
                            adresseField.getText(),
                            cpField.getText(),
                            villeField.getText());
                    refreshView();
                }
            });

            JButton deleteButton = new JButton("Supprimer");
            deleteButton.addActionListener(e -> {
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Êtes-vous sûr de vouloir supprimer ce propriétaire ?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    controller.deleteProprietaire(proprietaire.getId_proprietaire());
                    refreshView();
                }
            });

            proprietairePanel.add(proprietaireLabel);
            proprietairePanel.add(modifyButton);
            proprietairePanel.add(deleteButton);
            panel.add(proprietairePanel);
        }

        // Panel pour les boutons Ajouter et Retour
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Bouton d'ajout
        JButton addButton = new JButton("Ajouter un propriétaire");
        addButton.addActionListener(e -> {
            JTextField nomField = new JTextField();
            JTextField prenomField = new JTextField();
            JTextField adresseField = new JTextField();
            JTextField cpField = new JTextField();
            JTextField villeField = new JTextField();

            Object[] message = {
                    "Nom :", nomField,
                    "Prénom :", prenomField,
                    "Adresse :", adresseField,
                    "Code Postal :", cpField,
                    "Ville :", villeField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Ajouter un Propriétaire",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                controller.addProprietaire(
                        nomField.getText(),
                        prenomField.getText(),
                        adresseField.getText(),
                        cpField.getText(),
                        villeField.getText());
                refreshView();
            }
        });

        // Bouton Retour
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(e -> dispose());

        // Ajouter les boutons au panel d'action
        actionPanel.add(addButton);
        actionPanel.add(backButton);

        // Ajouter le panel d'action au panel principal
        panel.add(actionPanel);

        // Scroller
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setVisible(true);
    }

    // Rafraîchir la vue
    private void refreshView() {
        dispose();
        new ProprietaireView();
    }
}
