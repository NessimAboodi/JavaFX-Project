package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import cinema.BO.Cinema;
import cinema.BO.Salle;
import cinema.DAO.CinemaDAO;
import cinema.DAO.SalleDAO;
import javafx.beans.property.SimpleStringProperty;
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

    @FXML private TableView<Salle> tvSalle;
    @FXML private TableColumn<Salle, String> tcNumero;
    @FXML private TableColumn<Salle, Integer> tcCapacite;

    // NOUVELLES COLONNES : String au lieu de Integer pour afficher du texte
    @FXML private TableColumn<Salle, String> tcDescription;
    @FXML private TableColumn<Salle, String> tcCinema;

    @FXML private TableColumn<Salle, Void> tcModif, tcSupp;
    @FXML private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameUti = Navigation.getParam("nameUti");

        // Récupération des cinémas pour afficher leurs noms
        CinemaDAO cinemaDAO = new CinemaDAO();
        Map<Integer, Cinema> cinemas = cinemaDAO.findAll().stream()
                .collect(Collectors.toMap(Cinema::getIdCinema, c -> c));

        tcNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tcCapacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Liaison de l'ID du cinéma au Nom du cinéma
        tcCinema.setCellValueFactory(cellData -> {
            Cinema cinema = cinemas.get(cellData.getValue().getIdCinema());
            return new SimpleStringProperty(cinema != null ? cinema.getDenomination() : "Inconnu");
        });

        refreshTable();
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
                    ModifierSalleController.salleAModifier = salle;
                    Window currentWindow = btn.getScene().getWindow();
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
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer la salle " + salle.getNumero() + " ?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        if (new SalleDAO().delete(salle)) {
                            tvSalle.getItems().remove(salle);
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