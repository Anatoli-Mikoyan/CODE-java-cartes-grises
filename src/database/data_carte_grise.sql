-- Utiliser la base de données nouvellement créée
USE carte_grise;

-- Suppression des données existantes dans les tables
DELETE FROM POSSEDER;
DELETE FROM VEHICULE;
DELETE FROM PROPRIETAIRE;
DELETE FROM MODELE;
DELETE FROM MARQUE;

-- Réinitialisation des séquences d'auto-incrémentation pour les tables
ALTER TABLE MARQUE AUTO_INCREMENT = 1;
ALTER TABLE MODELE AUTO_INCREMENT = 1;
ALTER TABLE PROPRIETAIRE AUTO_INCREMENT = 1;
ALTER TABLE VEHICULE AUTO_INCREMENT = 1;

-- Insertion des données dans MARQUE
INSERT INTO MARQUE (nom_marque) VALUES 
('Peugeot'),
('Renault'),
('Toyota');

-- Insertion des données dans MODELE
INSERT INTO MODELE (nom_modele, id_marque) VALUES 
('208', 1), -- 208 appartient à Peugeot
('Clio', 2), -- Clio appartient à Renault
('Yaris', 3); -- Yaris appartient à Toyota

-- Insertion des données dans PROPRIETAIRE
INSERT INTO PROPRIETAIRE (nom, prenom, adresse, cp, ville) VALUES 
('Doe', 'John', '123 Rue de Paris', '75000', 'Paris'),
('Doe', 'Jane', '456 Avenue des Champs', '75008', 'Paris'),
('Smith', 'Alice', '789 Boulevard Haussmann', '75009', 'Paris');

-- Insertion des données dans VEHICULE
INSERT INTO VEHICULE (matricule, annee_sortie, poids, puissance_chevaux, puissance_fiscale, id_modele) VALUES 
('AB-123-CD', 2015, 1500, 100, 5, 1), -- Modèle ID : 1 (208)
('EF-456-GH', 2018, 1600, 110, 6, 2), -- Modèle ID : 2 (Clio)
('IJ-789-KL', 2020, 1400, 90, 4, 3);  -- Modèle ID : 3 (Yaris)

-- Insertion des données dans POSSEDER
INSERT INTO POSSEDER (id_proprietaire, id_vehicule, date_debut_propriete, date_fin_propriete) VALUES 
(1, 1, '2023-01-01', '2023-12-31'), -- John possède le véhicule 208
(2, 2, '2023-01-01', NULL),         -- Jane possède le véhicule Clio
(3, 3, '2023-06-01', NULL);         -- Alice possède le véhicule Yaris
