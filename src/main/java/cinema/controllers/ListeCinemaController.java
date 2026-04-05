package cinema.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cinema.BO.Cinema;
import cinema.DAO.CinemaDAO;
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
    private TableColumn<Cinema, String> tcDenomination, tcFranchise;

    @FXML
    private TableColumn<Cinema, Void> tcModif, tcSupp;

    @FXML
    private Button bRetour;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Récupérer le nom de l'utilisateur pour le menu
        nameUti = Navigation.getParam("nameUti");

        tcDenomination.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        // Remarque : Pour que tcFranchise affiche quelque chose, votre objet Cinema doit avoir un attribut "franchise"
        // Ou il faut faire une jointure dans votre CinemaDAO.
        tcFranchise.setCellValueFactory(new PropertyValueFactory<>("franchise"));

        ObservableList<Cinema> data = getCinema();
        tvCinema.setItems(data);

        // CORRECTION DE L'ANOMALIE : On appelle enfin les méthodes pour créer les boutons dans la table !
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
                // Appliquer le style CSS existant si nécessaire
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

                    // CORRECTION DE L'ANOMALIE LOGIQUE : On supprime directement le cinéma
                    // (L'ancien code vérifiait "getNbFranchiseByIdGerant" ce qui n'avait aucun sens ici)

                    CinemaDAO cinemaDAO = new CinemaDAO();
                    boolean estSupprime = cinemaDAO.delete(cinema);

                    if (estSupprime) {
                        tvCinema.getItems().remove(cinema); // Retire la ligne visuellement de la TableView
                    } else {
                        System.out.println("Erreur lors de la suppression du cinéma.");
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