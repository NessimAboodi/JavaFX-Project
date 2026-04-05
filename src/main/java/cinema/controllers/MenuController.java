package cinema.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Window;

public class MenuController {

    @FXML
    protected MenuItem bListeFranchise, bAjouterFranchise, bListeCinema, bAjouterCinema, bQuitter, bAccueil,
            bListeSalle,
            bAjouterSalle;

    protected String nameUti;

    @FXML
    public void bQuitterClick(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void bAccueilClick(ActionEvent event) {
        Window currentWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        Navigation.goTo("/cinema/views/page_accueil.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    public void bListFranchiseClick(ActionEvent event) {
        Window currentWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        Navigation.goTo("/cinema/views/page_liste_franchise.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    public void bAjouterFranchiseClick(ActionEvent event) {
        Window currentWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        Navigation.goTo("/cinema/views/page_ajout_franchise.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    public void bListeCinemaClick(ActionEvent event) {
        Window currentWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        // Correction de la faute de frappe (cinemaa -> cinema)
        Navigation.goTo("/cinema/views/page_liste_cinema.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    public void bAjouterCinemaClick(ActionEvent event) {
        Window currentWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        // Modification du lien (section -> cinema)
        Navigation.goTo("/cinema/views/page_ajout_cinema.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    public void bListeSalleClick(ActionEvent event) {
        Window currentWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        // Modification du lien (cours -> salle)
        Navigation.goTo("/cinema/views/page_liste_salle.fxml", "nameUti", nameUti, currentWindow);
    }

    @FXML
    public void bAjouterSalleClick(ActionEvent event) {
        Window currentWindow = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        // Modification du lien (cours -> salle)
        Navigation.goTo("/cinema/views/page_ajout_salle.fxml", "nameUti", nameUti, currentWindow);
    }

    public void setName(String nameUti) {
        this.nameUti = nameUti;
    }
}