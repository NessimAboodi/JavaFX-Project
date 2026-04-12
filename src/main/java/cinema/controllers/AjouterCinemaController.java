package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cinema.BO.Cinema;
import cinema.BO.Franchise;
import cinema.DAO.CinemaDAO;
import cinema.DAO.FranchiseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class AjouterCinemaController extends MenuController implements Initializable {

    @FXML
    private TextField tfDenomination, tfAdresse, tfVille;

    @FXML
    private ListView<Franchise> lvFranchise;

    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // On récupère le nom de l'utilisateur pour la navigation
        nameUti = Navigation.getParam("nameUti");

        // On charge la liste des franchises pour que l'utilisateur puisse en choisir une
        FranchiseDAO franchiseDAO = new FranchiseDAO();
        List<Franchise> franchises = franchiseDAO.findAll();
        ObservableList<Franchise> list = FXCollections.observableArrayList(franchises);
        lvFranchise.setItems(list);
    }

    @FXML
    public void bEnregistrerClick(ActionEvent event) {
        String denomination = tfDenomination.getText();
        String adresse = tfAdresse.getText();
        String ville = tfVille.getText();

        Franchise franchiseSelectionnee = lvFranchise.getSelectionModel().getSelectedItem();

        if (franchiseSelectionnee == null || denomination.isEmpty()) {
            System.out.println("Erreur : Veuillez remplir au moins le nom et choisir une franchise.");
            return;
        }

        // Création de l'objet (l'id 0 est géré par l'AUTO_INCREMENT de la DB)
        Cinema nouveauCinema = new Cinema(0, denomination, adresse, ville, franchiseSelectionnee.getIdFranchise());

        CinemaDAO cinemaDAO = new CinemaDAO();
        boolean succes = cinemaDAO.create(nouveauCinema);

        if (succes) {
            System.out.println("Cinéma ajouté avec succès !");

            // REDIRECTION AUTOMATIQUE vers la liste pour voir le résultat
            Window currentWindow = bRetour.getScene().getWindow();
            Navigation.goTo("/cinema/views/page_liste_cinema.fxml", "nameUti", nameUti, currentWindow);
        } else {
            System.out.println("Erreur lors de l'insertion en base de données.");
        }
    }

    @FXML
    public void bEffacerClick(ActionEvent event) {
        tfDenomination.clear();
        tfAdresse.clear();
        tfVille.clear();
        lvFranchise.getSelectionModel().clearSelection();
    }

    @FXML
    public void bRetourClick(ActionEvent event) {
        Window currentWindow = bRetour.getScene().getWindow();
        Navigation.goTo("/cinema/views/page_liste_cinema.fxml", "nameUti", nameUti, currentWindow);
    }
}