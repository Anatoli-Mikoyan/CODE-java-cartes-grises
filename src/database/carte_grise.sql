-- Supprimer la base de données si elle existe déjà
DROP DATABASE IF EXISTS carte_grise;

-- Créer la base de données
CREATE DATABASE carte_grise;

-- Utiliser la base de données nouvellement créée
USE carte_grise;

-- Table MARQUE
CREATE TABLE MARQUE (
    id_marque INT AUTO_INCREMENT PRIMARY KEY,
    nom_marque VARCHAR(255) NOT NULL
);

-- Table MODELE
CREATE TABLE MODELE (
    id_modele INT AUTO_INCREMENT PRIMARY KEY,
    nom_modele VARCHAR(255) NOT NULL,
    id_marque INT NOT NULL,
    FOREIGN KEY (id_marque) REFERENCES MARQUE(id_marque) ON DELETE CASCADE
);

-- Table PROPRIETAIRE
CREATE TABLE PROPRIETAIRE (
    id_proprietaire INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    adresse VARCHAR(255) NOT NULL,
    cp VARCHAR(10) NOT NULL,
    ville VARCHAR(255) NOT NULL
);

-- Table VEHICULE
CREATE TABLE VEHICULE (
    id_vehicule INT AUTO_INCREMENT PRIMARY KEY,
    matricule VARCHAR(50) NOT NULL,
    annee_sortie YEAR NOT NULL,
    poids INT NOT NULL,
    puissance_chevaux INT NOT NULL,
    puissance_fiscale INT NOT NULL,
    id_modele INT NOT NULL,
    FOREIGN KEY (id_modele) REFERENCES MODELE(id_modele) ON DELETE CASCADE
);

-- Table POSSEDER
CREATE TABLE POSSEDER (
    id_proprietaire INT NOT NULL,
    id_vehicule INT NOT NULL,
    date_debut_propriete DATE NOT NULL,
    date_fin_propriete DATE,
    PRIMARY KEY (id_proprietaire, id_vehicule),
    FOREIGN KEY (id_proprietaire) REFERENCES PROPRIETAIRE(id_proprietaire) ON DELETE CASCADE,
    FOREIGN KEY (id_vehicule) REFERENCES VEHICULE(id_vehicule) ON DELETE CASCADE
);
