package views;

import controllers.MarqueController;
import models.Marque;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MarqueView extends JFrame {
    private MarqueController controller;

    public MarqueView() {
        controller = new MarqueController();

        setTitle("Liste des Marques");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Charger les marques
        List<Marque> marques = controller.getAllMarques();
        for (Marque marque : marques) {
            JPanel marquePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel marqueLabel = new JLabel(marque.getNomMarque());

            JButton modifyButton = new JButton("Modifier");
            modifyButton.addActionListener(e -> {
                String newName = JOptionPane.showInputDialog(this, "Nouveau nom :", marque.getNomMarque());
                if (newName != null && !newName.isEmpty()) {
                    controller.updateMarque(marque.getIdMarque(), newName);
                    refreshView();
                }
            });

            JButton deleteButton = new JButton("Supprimer");
            deleteButton.addActionListener(e -> {
                controller.deleteMarque(marque.getIdMarque());
                refreshView();
            });

            marquePanel.add(marqueLabel);
            marquePanel.add(modifyButton);
            marquePanel.add(deleteButton);
            panel.add(marquePanel);
        }

        // Panel pour les boutons "Ajouter" et "Retour"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Bouton d'ajout
        JButton addButton = new JButton("Ajouter une marque");
        addButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this, "Nom de la nouvelle marque :");
            if (newName != null && !newName.isEmpty()) {
                controller.addMarque(newName);
                refreshView();
            }
        });

        // Bouton retour
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(e -> dispose());

        // Ajouter les boutons au panel
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);

        panel.add(buttonPanel);

        // Scroller
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        setVisible(true);
    }

    // Rafraîchir la vue
    private void refreshView() {
        dispose();
        new MarqueView();
    }
}
