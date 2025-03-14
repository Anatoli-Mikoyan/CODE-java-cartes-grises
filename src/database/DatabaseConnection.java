package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/carte_grise";
    private static final String USER = "root"; // Remplacer par votre utilisateur MySQL
    private static final String PASSWORD = "root"; // Remplacer par votre mot de passe MySQL

    static {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Affichage d'une alerte en pop-up
            JOptionPane.showMessageDialog(null, "Erreur : Le driver MySQL n'a pas pu être chargé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Driver MySQL introuvable.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Affichage d'une alerte en pop-up
            JOptionPane.showMessageDialog(null, "Erreur : Impossible de se connecter à la base de données.\nVérifiez vos identifiants ou l'état du serveur MySQL.", "Erreur", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
}
