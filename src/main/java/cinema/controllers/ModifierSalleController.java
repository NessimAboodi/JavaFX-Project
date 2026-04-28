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

public class ModifierSalleController extends MenuController {

    // Cette variable statique est remplie par ListeSalleController avant d'arriver ici
    public static Salle salleAModifier;

    @FXML private TextField tfNumSalle;
    @FXML private TextField tfCapacite;
    @FXML private TextField tfDescription; // Ajout du champ description
    @FXML private ComboBox<Cinema> cbCinema;
    @FXML private Button bEnregistrer;
    @FXML private Button bRetour;

    @FXML
    public void initialize() {
        // 1. Charger la liste des cinémas
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> listeCinemas = cinemaDAO.findAll();
        cbCinema.setItems(FXCollections.observableArrayList(listeCinemas));

        // 2. Remplir les champs avec les infos de la salle actuelle
        if (salleAModifier != null) {
            tfNumSalle.setText(salleAModifier.getNumero());
            tfCapacite.setText(String.valueOf(salleAModifier.getCapacite()));
            tfDescription.setText(salleAModifier.getDescription()); // Affichage de la description

            // Sélectionner le bon cinéma dans la liste
            for (Cinema c : listeCinemas) {
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
            // Récupération des saisies
            String num = tfNumSalle.getText();
            int cap = Integer.parseInt(tfCapacite.getText());
            String desc = tfDescription.getText();
            Cinema cine = cbCinema.getSelectionModel().getSelectedItem();

            if (num.isEmpty() || cine == null) {
                afficherMessage("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Mise à jour de l'objet
            salleAModifier.setNumero(num);
            salleAModifier.setCapacite(cap);
            salleAModifier.setDescription(desc);
            salleAModifier.setIdCinema(cine.getIdCinema());

            // Enregistrement en BDD
            SalleDAO dao = new SalleDAO();
            if (dao.update(salleAModifier)) {
                afficherMessage("Succès", "La salle a été modifiée !");
                bRetourClick(event);
            } else {
                afficherMessage("Erreur", "Échec de la mise à jour en base de données.");
            }

        } catch (NumberFormatException e) {
            afficherMessage("Erreur", "La capacité doit être un nombre.");
        }
    }

    @FXML
    void bRetourClick(ActionEvent event) {
        Window currentWindow = bRetour.getScene().getWindow();
        Navigation.goTo("/cinema/views/page_liste_salle.fxml", "Liste Salles", nameUti, currentWindow);
    }

    private void afficherMessage(String titre, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(titre);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}