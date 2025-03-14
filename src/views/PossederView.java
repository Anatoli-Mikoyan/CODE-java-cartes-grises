package views;

import controllers.PossederController;
import models.Posseder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interface graphique pour la gestion des relations de possession entre propriétaires et véhicules.
 * Cette vue permet d'afficher, ajouter, modifier et supprimer des relations POSSEDER.
 */
public class PossederView extends JFrame {
    // Constantes pour améliorer la lisibilité et faciliter les modifications
    private static final String TITLE = "Gestion des Propriétés de Véhicules";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 500;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String[] COLUMN_NAMES = {"Nom Propriétaire", "Modèle Véhicule", "Date Début", "Date Fin"};
    
    // Composants de l'interface graphique
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final PossederController possederController;
    
    // Formatter pour la manipulation des dates
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

    /**
     * Constructeur de la classe PossederView.
     * Initialise l'interface graphique et configure les écouteurs d'événements.
     */
    public PossederView() {
        // Initialisation du contrôleur
        possederController = new PossederController();
        
        // Configuration de la fenêtre principale
        setTitle(TITLE);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Création du modèle de table non éditable
        tableModel = new NonEditableTableModel(COLUMN_NAMES, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Ajout d'un panneau de défilement pour la table
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Création des boutons et du panneau de boutons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Chargement des données initiales
        refreshTable();
        
        // Affichage de la fenêtre
        setVisible(true);
    }

    /**
     * Crée et configure le panneau de boutons.
     * @return Le panneau de boutons configuré
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        
        // Création des boutons avec des icônes
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton backButton = new JButton("Retour");
        JButton refreshButton = new JButton("Actualiser");
        
        // Configuration des écouteurs d'événements
        addButton.addActionListener(e -> showAddDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> deleteSelectedRow());
        backButton.addActionListener(e -> dispose());
        refreshButton.addActionListener(e -> refreshTable());
        
        // Ajout des boutons au panneau
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        
        return buttonPanel;
    }

    /**
     * Affiche la boîte de dialogue pour ajouter une nouvelle relation POSSEDER.
     */
    private void showAddDialog() {
        // Création des champs de saisie
        JTextField proprietaireField = new JTextField();
        JTextField modeleField = new JTextField();
        JTextField dateDebutField = new JTextField();
        JTextField dateFinField = new JTextField();
        
        // Création du panneau de formulaire
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Nom Propriétaire:"));
        panel.add(proprietaireField);
        panel.add(new JLabel("Modèle Véhicule:"));
        panel.add(modeleField);
        panel.add(new JLabel("Date Début (" + DATE_FORMAT + "):"));
        panel.add(dateDebutField);
        panel.add(new JLabel("Date Fin (" + DATE_FORMAT + ", optionnel):"));
        panel.add(dateFinField);
        
        // Affichage de la boîte de dialogue
        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une propriété", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        // Traitement du résultat
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Récupération des données saisies
                String nomProprietaire = proprietaireField.getText().trim();
                String nomModele = modeleField.getText().trim();
                
                // Validation des données
                if (nomProprietaire.isEmpty() || nomModele.isEmpty() || dateDebutField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Les champs nom propriétaire, modèle véhicule et date début sont obligatoires.");
                }
                
                // Parsing des dates
                Date dateDebut = dateFormatter.parse(dateDebutField.getText().trim());
                Date dateFin = dateFinField.getText().trim().isEmpty() ? null : dateFormatter.parse(dateFinField.getText().trim());
                
                // Validation de la cohérence des dates
                if (dateFin != null && dateDebut.after(dateFin)) {
                    throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
                }
                
                // Récupération des identifiants
                int idProprietaire = possederController.getIdProprietaire(nomProprietaire);
                int idVehicule = possederController.getIdVehiculeParModele(nomModele);
                
                if (idProprietaire == -1) {
                    throw new IllegalArgumentException("Propriétaire introuvable: " + nomProprietaire);
                }
                
                if (idVehicule == -1) {
                    throw new IllegalArgumentException("Modèle de véhicule introuvable: " + nomModele);
                }
                
                // Création et ajout de la relation
                possederController.addPosseder(new Posseder(idProprietaire, idVehicule, dateDebut, dateFin));
                refreshTable();
                
                JOptionPane.showMessageDialog(this, "Relation de propriété ajoutée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez le format " + DATE_FORMAT, "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Affiche la boîte de dialogue pour modifier une relation POSSEDER existante.
     */
    private void showUpdateDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Récupération des données de la ligne sélectionnée
            String originalNomProprietaire = table.getValueAt(selectedRow, 0).toString();
            String originalNomModele = table.getValueAt(selectedRow, 1).toString();
            String originalDateDebut = table.getValueAt(selectedRow, 2).toString();
            String originalDateFin = table.getValueAt(selectedRow, 3).toString();
            
            // Récupération des identifiants
            int originalIdProprietaire = possederController.getIdProprietaire(originalNomProprietaire);
            int originalIdVehicule = possederController.getIdVehiculeParModele(originalNomModele);
            
            // Création des champs de saisie pré-remplis
            JTextField proprietaireField = new JTextField(originalNomProprietaire);
            JTextField modeleField = new JTextField(originalNomModele);
            JTextField dateDebutField = new JTextField(originalDateDebut);
            JTextField dateFinField = new JTextField(originalDateFin.equals("N/A") ? "" : originalDateFin);
            
            // Création du panneau de formulaire
            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Nom Propriétaire:"));
            panel.add(proprietaireField);
            panel.add(new JLabel("Modèle Véhicule:"));
            panel.add(modeleField);
            panel.add(new JLabel("Date Début (" + DATE_FORMAT + "):"));
            panel.add(dateDebutField);
            panel.add(new JLabel("Date Fin (" + DATE_FORMAT + ", optionnel):"));
            panel.add(dateFinField);
            
            // Affichage de la boîte de dialogue
            int result = JOptionPane.showConfirmDialog(this, panel, "Modifier une propriété", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            // Traitement du résultat
            if (result == JOptionPane.OK_OPTION) {
                // Récupération des données saisies
                String nomProprietaire = proprietaireField.getText().trim();
                String nomModele = modeleField.getText().trim();
                
                // Validation des données
                if (nomProprietaire.isEmpty() || nomModele.isEmpty() || dateDebutField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Les champs nom propriétaire, modèle véhicule et date début sont obligatoires.");
                }
                
                // Parsing des dates
                Date dateDebut = dateFormatter.parse(dateDebutField.getText().trim());
                Date dateFin = dateFinField.getText().trim().isEmpty() ? null : dateFormatter.parse(dateFinField.getText().trim());
                
                // Validation de la cohérence des dates
                if (dateFin != null && dateDebut.after(dateFin)) {
                    throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début.");
                }
                
                // Récupération des nouveaux identifiants
                int idProprietaire = possederController.getIdProprietaire(nomProprietaire);
                int idVehicule = possederController.getIdVehiculeParModele(nomModele);
                
                if (idProprietaire == -1) {
                    throw new IllegalArgumentException("Propriétaire introuvable: " + nomProprietaire);
                }
                
                if (idVehicule == -1) {
                    throw new IllegalArgumentException("Modèle de véhicule introuvable: " + nomModele);
                }
                
                // Suppression de l'ancienne relation et ajout de la nouvelle
                possederController.deletePosseder(originalIdProprietaire, originalIdVehicule);
                possederController.addPosseder(new Posseder(idProprietaire, idVehicule, dateDebut, dateFin));
                
                refreshTable();
                JOptionPane.showMessageDialog(this, "Relation de propriété modifiée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez le format " + DATE_FORMAT, "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Supprime la relation POSSEDER correspondant à la ligne sélectionnée.
     */
    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Récupération des données de la ligne sélectionnée
            String nomProprietaire = table.getValueAt(selectedRow, 0).toString();
            String nomModele = table.getValueAt(selectedRow, 1).toString();
            
            // Confirmation de la suppression
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Êtes-vous sûr de vouloir supprimer la relation entre le propriétaire " + nomProprietaire + 
                " et le véhicule " + nomModele + "?", 
                "Confirmation de suppression", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                // Récupération des identifiants
                int idProprietaire = possederController.getIdProprietaire(nomProprietaire);
                int idVehicule = possederController.getIdVehiculeParModele(nomModele);
                
                // Suppression de la relation
                possederController.deletePosseder(idProprietaire, idVehicule);
                refreshTable();
                
                JOptionPane.showMessageDialog(this, "Relation de propriété supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Rafraîchit le tableau avec les données actuelles.
     */
    private void refreshTable() {
        try {
            List<Posseder> possederList = possederController.getAllPosseder();
            tableModel.setRowCount(0);
            
            for (Posseder p : possederList) {
                String nomProprietaire = possederController.getNomProprietaire(p.getIdProprietaire());
                String nomModele = possederController.getNomModele(p.getIdVehicule());
                addRowToTable(p, nomProprietaire, nomModele);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des données: " + ex.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Ajoute une ligne au tableau avec les données fournies.
     * @param p L'objet Posseder à ajouter
     * @param nomProprietaire Le nom du propriétaire
     * @param nomModele Le nom du modèle de véhicule
     */
    private void addRowToTable(Posseder p, String nomProprietaire, String nomModele) {
        tableModel.addRow(new Object[]{
            nomProprietaire,
            nomModele,
            dateFormatter.format(p.getDateDebutPropriete()),
            Optional.ofNullable(p.getDateFinPropriete())
                   .map(dateFormatter::format)
                   .orElse("N/A")
        });
    }

    /**
     * Classe interne pour empêcher l'édition directe du tableau.
     */
    private static class NonEditableTableModel extends DefaultTableModel {
        public NonEditableTableModel(String[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Point d'entrée du programme.
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        // Utilisation de SwingUtilities pour assurer que l'interface est créée dans l'EDT
        SwingUtilities.invokeLater(PossederView::new);
    }
}