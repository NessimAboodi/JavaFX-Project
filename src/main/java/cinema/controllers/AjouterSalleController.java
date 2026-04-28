package cinema.controllers;

import cinema.BO.Cinema;
import cinema.BO.Salle;
import cinema.DAO.CinemaDAO;
import cinema.DAO.SalleDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import java.util.List;

public class AjouterSalleController extends MenuController {

    @FXML private TextField tfNumSalle;
    @FXML private TextField tfCapacite;
    @FXML private TextField tfDescription;
    @FXML private ComboBox<Cinema> cbCinema;
    @FXML private Button bEnregistrer;
    @FXML private Button bRetour;

    @FXML
    public void initialize() {
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> listeCinemas = cinemaDAO.findAll();
        cbCinema.setItems(FXCollections.observableArrayList(listeCinemas));
    }

    @FXML
    void bEnregistrerClick(ActionEvent event) {
        String numSalle = tfNumSalle.getText();
        String capaciteText = tfCapacite.getText();
        String description = tfDescription.getText();
        Cinema cinemaSelectionne = cbCinema.getSelectionModel().getSelectedItem();

        // 1. Vérification des champs vides
        if (numSalle == null || numSalle.isEmpty() || capaciteText == null || capaciteText.isEmpty() || cinemaSelectionne == null) {
            afficherErreur("Champs manquants", "Veuillez remplir le numéro, la capacité et sélectionner un cinéma.");
            return;
        }

        try {
            int capacite = Integer.parseInt(capaciteText);

            // L'objet Salle est créé
            Salle nouvelleSalle = new Salle(numSalle, capacite, description, cinemaSelectionne.getIdCinema());
            SalleDAO salleDAO = new SalleDAO();

            // 2. Vérification de l'enregistrement en base
            if (salleDAO.create(nouvelleSalle)) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setHeaderText("Succès");
                success.setContentText("La salle a bien été enregistrée !");
                success.showAndWait();

                bRetourClick(event); // Retour automatique
            } else {
                // Si la BDD refuse, on affiche un message (Probablement à cause du texte dans "numero")
                afficherErreur("Erreur Base de Données", "Impossible d'enregistrer la salle.\n\nASTUCE : Dans votre base de données, la colonne 'numero' est un entier (INTEGER). Assurez-vous de taper juste un chiffre (ex: '1') et non du texte (ex: 'Salle 1').");
            }

        } catch (NumberFormatException e) {
            afficherErreur("Erreur de format", "La capacité doit être un nombre valide (ex: 150).");
        }
    }

    @FXML
    void bRetourClick(ActionEvent event) {
        Window currentWindow = bRetour.getScene().getWindow();
        Navigation.goTo("/cinema/views/page_liste_salle.fxml", "Liste Salles", nameUti, currentWindow);
    }

    // Petite méthode utilitaire pour afficher les erreurs facilement
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.show();
    }
}