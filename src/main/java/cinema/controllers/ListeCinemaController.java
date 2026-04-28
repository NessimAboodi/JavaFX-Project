package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import cinema.BO.Cinema;
import cinema.DAO.CinemaDAO;
import cinema.DAO.SalleDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

public class ListeCinemaController extends MenuController implements Initializable {

    @FXML
    private TableView<Cinema> tvCinema;

    @FXML
    private TableColumn<Cinema, String> tcDenomination;

    @FXML
    private TableColumn<Cinema, Integer> tcFranchise;

    @FXML
    private TableColumn<Cinema, String> tcVp;

    @FXML
    private TableColumn<Cinema, Void> tcModif, tcSupp;

    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Récupérer le nom de l'utilisateur pour le menu
        nameUti = Navigation.getParam("nameUti");

        tcDenomination.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        // CORRECTION ICI : "idFranchise" correspond exactement à l'attribut dans votre classe Cinema.java
        tcFranchise.setCellValueFactory(new PropertyValueFactory<>("idFranchise"));

        // Colonne "salle" : affiche les salles séparées par des virgules
        SalleDAO salleDAO = new SalleDAO();
        tcVp.setCellValueFactory(cellData -> {
            int idCinema = cellData.getValue().getIdCinema();
            String salles = salleDAO.findAll().stream()
                    .filter(s -> s.getIdCinema() == idCinema)
                    .map(s -> "Salle " + s.getNumero())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(salles.isEmpty() ? "Aucune salle" : salles);
        });

        tvCinema.setItems(getCinema());
        btnModif();
        btnSupp();
    }

    private ObservableList<Cinema> getCinema() {
        CinemaDAO cinemaDAO = new CinemaDAO();
        List<Cinema> mesCinemas = cinemaDAO.findAll();
        return FXCollections.observableArrayList(mesCinemas);
    }

    @FXML
    public void bRetourClick(ActionEvent actionEvent) {
        Window currentWindow = bRetour.getScene().getWindow();
        // Utilisation propre de Navigation vers l'accueil
        Navigation.goTo("/cinema/views/page_accueil.fxml", "nameUti", nameUti, currentWindow);
    }

    private void btnModif() {
        tcModif.setCellFactory(column -> new TableCell<Cinema, Void>() {
            private final Button btn = new Button("Modifier");
            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(event -> {
                    Cinema cinema = getTableView().getItems().get(getIndex());
                    // On sauvegarde l'ID du cinéma cliqué pour le passer à ModifierCinemaController
                    Navigation.setParam("idCinema", cinema.getIdCinema());
                    Window currentWindow = btn.getScene().getWindow();
                    Navigation.goTo("/cinema/views/page_modif_cinema.fxml", "nameUti", nameUti, currentWindow);
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
        tcSupp.setCellFactory(col -> new TableCell<Cinema, Void>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.getStyleClass().add("action-button");
                btn.setOnAction(event -> {
                    Cinema cinema = getTableView().getItems().get(getIndex());
                    CinemaDAO cinemaDAO = new CinemaDAO();
                    if (cinemaDAO.delete(cinema)) {
                        tvCinema.getItems().remove(cinema);
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