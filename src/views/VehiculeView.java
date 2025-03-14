package views;

import controllers.VehiculeController;
import models.Vehicule;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VehiculeView extends JFrame {
    private VehiculeController controller;

    public VehiculeView() {
        controller = new VehiculeController();  // Initialisation du contrôleur.

        setTitle("Gestion des Véhicules");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));  // Utilisation de BoxLayout pour une disposition en colonne

        List<Vehicule> vehicules = controller.getAllVehicules();  // Récupération des véhicules depuis la base de données
        for (Vehicule vehicule : vehicules) {
            String nomModele = controller.getModeleNameById(vehicule.getIdModele());  // Récupérer le modèle du véhicule par son ID

            // Panneau pour chaque véhicule
            JPanel vehiculePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel vehiculeLabel = new JLabel(vehicule.getMatricule() + " (Modèle : " + nomModele + ")");

            // Bouton pour modifier le véhicule
            JButton modifyButton = new JButton("Modifier");
            modifyButton.addActionListener(e -> {
                // Créer des champs de texte pour tous les champs du véhicule
                JTextField matriculeField = new JTextField(vehicule.getMatricule());
                JTextField anneeField = new JTextField(String.valueOf(vehicule.getAnneeSortie()));
                JTextField poidsField = new JTextField(String.valueOf(vehicule.getPoids()));
                JTextField puissanceChevauxField = new JTextField(String.valueOf(vehicule.getPuissanceChevaux()));
                JTextField puissanceFiscaleField = new JTextField(String.valueOf(vehicule.getPuissanceFiscale()));
                JTextField modeleField = new JTextField(nomModele);

                // Demander à l'utilisateur de modifier tous les champs
                Object[] message = {
                        "Matricule:", matriculeField,
                        "Année de sortie:", anneeField,
                        "Poids:", poidsField,
                        "Puissance (chevaux):", puissanceChevauxField,
                        "Puissance fiscale:", puissanceFiscaleField,
                        "Modèle:", modeleField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Modifier un Véhicule", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    try {
                        // Récupérer les valeurs modifiées
                        String newMatricule = matriculeField.getText();
                        int newAnneeSortie = Integer.parseInt(anneeField.getText());
                        double newPoids = Double.parseDouble(poidsField.getText());
                        int newPuissanceChevaux = Integer.parseInt(puissanceChevauxField.getText());
                        int newPuissanceFiscale = Integer.parseInt(puissanceFiscaleField.getText());
                        String newModele = modeleField.getText();  // Nouveau modèle

                        // Appeler la méthode de modification dans le contrôleur
                        controller.updateVehicule(vehicule.getIdVehicule(), newMatricule, newAnneeSortie, newPoids,
                                newPuissanceChevaux, newPuissanceFiscale, newModele);
                        refreshView();  // Rafraîchir la vue après modification
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la modification du véhicule.");
                    }
                }
            });

            // Bouton pour supprimer le véhicule
            JButton deleteButton = new JButton("Supprimer");
            deleteButton.addActionListener(e -> {
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Êtes-vous sûr de vouloir supprimer ce véhicule ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    try {
                        controller.deleteVehicule(vehicule.getIdVehicule());
                        refreshView();  // Rafraîchir la vue après suppression
                    } catch (Exception ex) {
                        showErrorMessage("Une erreur est survenue lors de la suppression du véhicule.");
                    }
                }
            });

            // Ajouter les composants au panneau du véhicule
            vehiculePanel.add(vehiculeLabel);
            vehiculePanel.add(modifyButton);
            vehiculePanel.add(deleteButton);
            panel.add(vehiculePanel);
        }

        // Panneau pour les boutons "Ajouter" et "Retour"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Bouton "Ajouter un Véhicule"
        JButton addButton = new JButton("Ajouter un Véhicule");
        addButton.addActionListener(e -> {
            // Créer des champs de texte pour saisir les informations du véhicule
            JTextField matriculeField = new JTextField();
            JTextField anneeField = new JTextField();
            JTextField poidsField = new JTextField();
            JTextField puissanceChevauxField = new JTextField();
            JTextField puissanceFiscaleField = new JTextField();
            JTextField modeleField = new JTextField();

            // Demander à l'utilisateur de saisir les informations du nouveau véhicule
            Object[] message = {
                    "Matricule 00-AAA-00:", matriculeField,
                    "Année de sortie:", anneeField,
                    "Poids:", poidsField,
                    "Puissance (chevaux):", puissanceChevauxField,
                    "Puissance fiscale:", puissanceFiscaleField,
                    "Modèle:", modeleField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Ajouter un Véhicule", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    // Récupérer les valeurs saisies pour le nouveau véhicule
                    String matricule = matriculeField.getText();
                    int anneeSortie = Integer.parseInt(anneeField.getText());
                    double poids = Double.parseDouble(poidsField.getText());
                    int puissanceChevaux = Integer.parseInt(puissanceChevauxField.getText());
                    int puissanceFiscale = Integer.parseInt(puissanceFiscaleField.getText());
                    String modele = modeleField.getText();

                    // Appeler la méthode d'ajout dans le contrôleur
                    controller.addVehicule(matricule, anneeSortie, poids, puissanceChevaux, puissanceFiscale, modele);
                    refreshView();  // Rafraîchir la vue après ajout
                } catch (Exception ex) {
                    showErrorMessage("Une erreur est survenue lors de l'ajout du véhicule.");
                }
            }
        });

        // Bouton "Retour" pour fermer la fenêtre
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(e -> dispose());

        // Ajouter les boutons au panneau
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel);

        // Ajouter un JScrollPane pour gérer le défilement de la liste des véhicules
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setVisible(true);
    }

    // Méthode pour rafraîchir la vue
    private void refreshView() {
        dispose();
        new VehiculeView();  // Recharge la fenêtre avec les données actualisées
    }

    // Méthode pour afficher un message d'erreur
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
