package cinema.controllers;

import cinema.BO.Cinema;
import cinema.BO.Salle;
import cinema.DAO.CinemaDAO;
import cinema.DAO.SalleDAO;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import java.util.List;

public class ModifierSalleController extends MenuController {

    // Cette variable permet de recevoir la salle cliquée depuis la liste
    public static Salle salleAModifier;

    @FXML private TextField tfNumSalle;
    @FXML private TextField tfCapacite;
    @FXML private ComboBox<Cinema> cbCinema;
    @FXML private Button bEnregistrer;
    @FXML private Button bRetour;

    @FXML
    public void initialize() {
        // 1. Charger les cinémas pour la ComboBox
        CinemaDAO cineDAO = new CinemaDAO();
        List<Cinema> cinemas = cineDAO.findAll();
        cbCinema.setItems(FXCollections.observableArrayList(cinemas));

        // 2. Pré-remplir les champs si une salle a été transmise
        if (salleAModifier != null) {
            tfNumSalle.setText(salleAModifier.getNumero());
            tfCapacite.setText(String.valueOf(salleAModifier.getCapacite()));

            // Sélectionner automatiquement le cinéma actuel dans la liste
            for (Cinema c : cinemas) {
                if (c.getIdCinema() == salleAModifier.getIdCinema()) {
                    cbCinema.getSelectionModel().select(c);
                    break;
                }
            }
        }
    }

    @FXML
    void bEnregistrerClick(ActionEvent event) {
        try {
            // Mettre à jour l'objet avec les nouvelles saisies
            salleAModifier.setNumero(tfNumSalle.getText());
            salleAModifier.setCapacite(Integer.parseInt(tfCapacite.getText()));
            salleAModifier.setIdCinema(cbCinema.getValue().getIdCinema());

            // Envoi à la BDD via le DAO
            SalleDAO dao = new SalleDAO();
            if (dao.update(salleAModifier)) {
                System.out.println("Mise à jour réussie !");
                bRetourClick(event);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @FXML
    void bRetourClick(ActionEvent event) {
        Window currentWindow = bRetour.getScene().getWindow();
        Navigation.goTo("/cinema/views/page_liste_salle.fxml", "Liste Salles", nameUti, currentWindow);
    }
}