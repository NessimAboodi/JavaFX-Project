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
    private TextArea taLibSec; // Champ de texte pour la dénomination du cinéma

    private int idSec; // Identifiant du cinéma à modifier

    @FXML
    private Button bRetour, bEnregistrer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Récupération du nom de l'utilisateur pour le menu
        nameUti = Navigation.getParam("nameUti");

        // 2. RÉCUPÉRATION DE L'ID : On récupère l'ID passé par ListeCinemaController
        Object param = Navigation.getParam("idCinema");
        if (param != null) {
            this.idSec = (Integer) param;
            // 3. CHARGEMENT : On remplit le champ de texte avec le nom actuel du cinéma
            setAttrinuts();
        }
    }

    /**
     * Charge les données du cinéma depuis la base de données pour les afficher
     */
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
        // Retour à la liste via la classe Navigation
        Navigation.goTo("/cinema/views/page_liste_cinema.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    private void bEnregistrerClick(ActionEvent event) {
        String denomination = taLibSec.getText();

        if (!denomination.trim().isEmpty()) {
            CinemaDAO cinemaDAO = new CinemaDAO();

            // 1. PROTECTION DES DONNÉES : On récupère l'objet complet pour ne pas perdre l'adresse et la ville
            Cinema cinemaExistant = cinemaDAO.find(idSec);

            if (cinemaExistant != null) {
                // 2. INSTANCIATION CORRECTE : On modifie uniquement la dénomination
                Cinema cinemaModifie = new Cinema(
                        idSec,
                        denomination,
                        cinemaExistant.getAdresse(),
                        cinemaExistant.getVille(),
                        cinemaExistant.getIdFranchise()
                );

                // 3. MISE À JOUR : Persistence dans la base PostgreSQL
                boolean controle = cinemaDAO.update(cinemaModifie);

                if (controle) {
                    Window currentWindow = bEnregistrer.getScene().getWindow();
                    Navigation.goTo("/cinema/views/page_liste_cinema.fxml", "nameUti", nameUti, currentWindow);
                }
            }
        } else {
            // Remplacement de l'ancien code d'école par un message d'erreur console
            System.out.println("Erreur : Le champ de dénomination ne peut pas être vide.");
        }
    }
}