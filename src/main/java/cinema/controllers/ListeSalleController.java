package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cinema.BO.Salle;
import cinema.DAO.SalleDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

public class ListeSalleController extends MenuController implements Initializable {

    @FXML
    private TableView<Salle> tvSalle;

    @FXML
    private TableColumn<Salle, String> tcNumero;

    @FXML
    private TableColumn<Salle, Integer> tcCapacite;

    @FXML
    private TableColumn<Salle, Integer> tcCinema;

    @FXML
    private TableColumn<Salle, Void> tcModif, tcSupp;

    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Récupérer le nom de l'utilisateur pour le menu
        nameUti = Navigation.getParam("nameUti");

        // Liaison des colonnes avec les attributs de la classe Salle.java
        tcNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tcCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        tcCinema.setCellValueFactory(new PropertyValueFactory<>("idCinema"));

        // Charger les données dans le tableau
        refreshTable();

        // Création des boutons d'action
        btnModif();
        btnSupp();
    }

    private void refreshTable() {
        SalleDAO salleDAO = new SalleDAO();
        List<Salle> mesSalles = salleDAO.findAll();
        ObservableList<Salle> data = FXCollections.observableArrayList(mesSalles);
        tvSalle.setItems(data);
    }

    @FXML
    public void bRetourClick(ActionEvent actionEvent) {
        Window currentWindow = bRetour.getScene().getWindow();
        Navigation.goTo("/cinema/views/page_accueil.fxml", "nameUti", nameUti, currentWindow);
    }

    private void btnModif() {
        tcModif.setCellFactory(column -> new TableCell<Salle, Void>() {
            private final Button btn = new Button("Modifier");

            {
                btn.getStyleClass().add("action-button");

                btn.setOnAction(event -> {
                    Salle salle = getTableView().getItems().get(getIndex());

                    // ÉTAPE CRUCIALE POUR QUE ÇA MARCHE :
                    // On transmet l'objet salle au contrôleur de modification
                    ModifierSalleController.salleAModifier = salle;

                    Window currentWindow = btn.getScene().getWindow();
                    // On navigue vers la page de modification
                    Navigation.goTo("/cinema/views/page_modif_salle.fxml", "nameUti", nameUti, currentWindow);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void btnSupp() {
        tcSupp.setCellFactory(col -> new TableCell<Salle, Void>() {
            private final Button btn = new Button("Supprimer");

            {

                btn.setOnAction(event -> {
                    Salle salle = getTableView().getItems().get(getIndex());

                    // Fenêtre de confirmation avant suppression
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer la salle " + salle.getNumero() + " ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        SalleDAO salleDAO = new SalleDAO();
                        boolean estSupprime = salleDAO.delete(salle);

                        if (estSupprime) {
                            tvSalle.getItems().remove(salle); // Mise à jour visuelle
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
}