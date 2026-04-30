package cinema.controllers;

import cinema.BO.Utilisateur;
import cinema.DAO.UtilisateurDAO;
import cinema.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class ConnexionController {

    @FXML
    private Button bConnexion;

    @FXML
    private PasswordField pfMdp;

    @FXML
    private TextField tfLogin;

    @FXML
    private Label lError;

    @FXML
    void bConnexionClick(ActionEvent event) {
        UtilisateurDAO dao = new UtilisateurDAO();
        Utilisateur user = dao.authenticate(tfLogin.getText(), pfMdp.getText());

        if (user != null) {
            // Sauvegarde de l'utilisateur connecté dans la Session
            Session.setUtilisateur(user);

            Window currentWindow = bConnexion.getScene().getWindow();
            Navigation.goTo("/cinema/views/page_accueil.fxml", "nameUti", user.getNom(), currentWindow);
        } else {
            showError("Identifiant ou mot de passe incorrect.");
        }
    }

    private void showError(String msg) {
        lError.setText(msg);
        lError.setStyle("-fx-text-fill: red;");
        lError.setVisible(true);
    }
}