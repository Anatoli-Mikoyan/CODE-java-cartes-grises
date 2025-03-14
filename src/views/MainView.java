package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Fenêtre principale pour naviguer entre les différentes vues.
 */
public class MainView extends JFrame {
    public MainView() {
        setTitle("Gestion des entités");
        setSize(400, 400); // Augmenter la hauteur pour accueillir un bouton supplémentaire
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Bouton pour ouvrir la vue MarqueView
        JButton marqueButton = new JButton("Gérer les marques");
        marqueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new MarqueView();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur lors de l'ouverture de MarqueView : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bouton pour ouvrir la vue ModeleView
        JButton modeleButton = new JButton("Gérer les modèles");
        modeleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ModeleView();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur lors de l'ouverture de ModeleView : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bouton pour ouvrir la vue ProprietaireView
        JButton proprietaireButton = new JButton("Gérer les propriétaires");
        proprietaireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new ProprietaireView();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur lors de l'ouverture de ProprietaireView : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bouton pour ouvrir la vue VehiculeView
        JButton vehiculeButton = new JButton("Gérer les véhicules");
        vehiculeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new VehiculeView();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur lors de l'ouverture de VehiculeView : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bouton pour ouvrir la vue PossederView
        JButton possederButton = new JButton("Gérer les propriétés");
        possederButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PossederView possederView = new PossederView(); // Créer une instance de PossederView
                    possederView.setVisible(true); // Rendre la vue visible
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Erreur lors de l'ouverture de PossederView : " + ex.getMessage(),
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ajouter les boutons à la fenêtre
        setLayout(new GridLayout(5, 1, 10, 10)); // Disposition en grille avec 5 lignes
        add(marqueButton);
        add(modeleButton);
        add(proprietaireButton);
        add(vehiculeButton);
        add(possederButton);
        setVisible(true); // Rendre la fenêtre visible
    }

    public static void main(String[] args) {
        new MainView(); // Lancement de l'application
    }
}
