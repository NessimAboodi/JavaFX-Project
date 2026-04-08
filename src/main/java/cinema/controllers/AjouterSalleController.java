package cinema.controllers;

import cinema.BO.Cinema;
import cinema.BO.Salle; // Import ajouté
import cinema.DAO.CinemaDAO;
import cinema.DAO.SalleDAO; // Import ajouté
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;

import java.util.List;

public class AjouterSalleController extends MenuController {

    @FXML
    private TextField tfNumSalle;
    @FXML
    private TextField tfCapacite;
    @FXML
    private ComboBox<Cinema> cbCinema;
    @FXML
    private Button bEnregistrer;
    @FXML
    private Button bRetour;

    @FXML
    public void initialize() {
        // On remplit la ComboBox avec la liste des cinémas de la BDD
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> listeCinemas = cinemaDAO.findAll();
        cbCinema.setItems(FXCollections.observableArrayList(listeCinemas));

        // Grâce au toString() que nous avons ajouté à Cinema.java,
        // les noms des cinémas s'afficheront proprement ici !
    }

    @FXML
    void bEnregistrerClick(ActionEvent event) {
        // 1. Récupération des données saisies
        String numSalle = tfNumSalle.getText();
        String capaciteText = tfCapacite.getText();
        Cinema cinemaSelectionne = cbCinema.getSelectionModel().getSelectedItem();

        // 2. Validation : On vérifie que rien n'est vide
        if (numSalle.isEmpty() || capaciteText.isEmpty() || cinemaSelectionne == null) {
            System.out.println("Erreur : Veuillez remplir tous les champs et sélectionner un cinéma.");
            return;
        }

        try {
            // 3. Conversion de la capacité en nombre entier
            int capacite = Integer.parseInt(capaciteText);

            // 4. Création de l'objet Salle
            // On utilise l'ID du cinéma récupéré depuis la ComboBox
            Salle nouvelleSalle = new Salle(numSalle, capacite, cinemaSelectionne.getIdCinema());

            // 5. Appel au DAO pour l'insertion en base de données
            SalleDAO salleDAO = new SalleDAO();
            boolean succes = salleDAO.create(nouvelleSalle);

            if (succes) {
                System.out.println("Enregistrement réussi pour la salle : " + numSalle);
                // 6. Retour à la liste des salles uniquement si l'enregistrement a réussi
                bRetourClick(event);
            } else {
                System.out.println("Erreur technique lors de l'enregistrement en base de données.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Erreur : La capacité doit être un nombre valide.");
        }
    }

    @FXML
    void bRetourClick(ActionEvent event) {
        Window currentWindow = bRetour.getScene().getWindow();
        Navigation.goTo("/cinema/views/page_liste_salle.fxml", "Liste Salles", nameUti, currentWindow);
    }
}