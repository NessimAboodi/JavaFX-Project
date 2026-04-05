package cinema.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import cinema.BO.Cinema;
import cinema.DAO.CinemaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Window;

public class ModifierCinemaController extends MenuController implements Initializable {

    @FXML
    private TextArea taLibSec; // On garde le même nom si votre FXML utilise encore ce fx:id

    private int idSec; // Identifiant du cinéma en cours de modification

    @FXML
    private Button bRetour, bEnregistrer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Le nom de l'utilisateur peut être récupéré via la Navigation si besoin
        nameUti = Navigation.getParam("nameUti");
    }

    public void setIdSec(int idSec) {
        this.idSec = idSec;
    }

    public void setAttrinuts() {
        CinemaDAO cinemaDAO = new CinemaDAO();
        Cinema cinema = cinemaDAO.find(idSec);
        if (cinema != null) {
            taLibSec.setText(cinema.getDenomination());
        }
    }

    @FXML
    private void bRetourClick(ActionEvent event) {
        Window currentWindow = bRetour.getScene().getWindow();
        // Utilisation de Navigation au lieu de FXMLLoader
        Navigation.goTo("/cinema/views/page_liste_cinema.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    private void bEnregistrerClick(ActionEvent event) {
        String denomination = taLibSec.getText();

        if (!denomination.trim().isEmpty()) {
            CinemaDAO cinemaDAO = new CinemaDAO();

            // 1. CORRECTION MAJEURE : On récupère l'ancien cinéma pour ne pas perdre ses données
            Cinema cinemaExistant = cinemaDAO.find(idSec);

            if (cinemaExistant != null) {
                // 2. On instancie l'objet correctement en conservant l'adresse, la ville et la franchise d'origine
                Cinema cinemaModifie = new Cinema(
                        idSec,
                        denomination,
                        cinemaExistant.getAdresse(),
                        cinemaExistant.getVille(),
                        cinemaExistant.getIdFranchise()
                );

                // 3. Mise à jour dans la base
                boolean controle = cinemaDAO.update(cinemaModifie);

                if (controle) {
                    Window currentWindow = bEnregistrer.getScene().getWindow();
                    Navigation.goTo("/cinema/views/page_liste_cinema.fxml", "nameUti", nameUti, currentWindow);
                }
            }
        } else {
            // Suppression de l'appel absurde à "popup_ajout_etu.fxml"
            System.out.println("Erreur : Le champ de dénomination ne peut pas être vide.");
            // Optionnel : vous pouvez afficher un vrai message d'erreur à l'utilisateur ici
        }
    }
}