package cinema.controllers;

import cinema.BO.Utilisateur;
import cinema.DAO.UtilisateurDAO;
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
        // On tente de récupérer l'utilisateur avec les identifiants saisis
        Utilisateur user = dao.authenticate(tfLogin.getText(), pfMdp.getText());

        // On vérifie que l'utilisateur n'est pas null avant de l'utiliser
        if (user != null) {
            Window currentWindow = bConnexion.getScene().getWindow();
            // Si la connexion réussit, on va vers l'accueil
            Navigation.goTo("/cinema/views/page_accueil.fxml", "nameUti", user.getNom(), currentWindow);
        } else {
            // Si le mot de passe ou le login est faux, on affiche l'erreur sans crasher
            showError("Identifiant ou mot de passe incorrect.");
        }
    }

    // Méthode pour afficher le message d'erreur en rouge
    private void showError(String msg) {
        lError.setText(msg);
        lError.setStyle("-fx-text-fill: red;");
        lError.setVisible(true);
    }
}