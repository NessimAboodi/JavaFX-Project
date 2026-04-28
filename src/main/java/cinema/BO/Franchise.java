package cinema.BO;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * BO Franchise - Style JavaFX Properties (complet)
 */
public class Franchise {

    private int idFranchise;
    private SimpleStringProperty nomFranchise = new SimpleStringProperty();
    private SimpleStringProperty siegeSocial = new SimpleStringProperty();
    private int idGerant;
    // ÉVOLUTION : Propriété pour lier le nom du gérant au TableView
    private SimpleStringProperty nomCompletGerant = new SimpleStringProperty();

    public Franchise() {}

    public Franchise(int idFranchise, String nomFranchise, String siegeSocial, int idGerant) {
        this.idFranchise = idFranchise;
        this.nomFranchise.set(nomFranchise);
        this.siegeSocial.set(siegeSocial);
        this.idGerant = idGerant;
    }

    // Getters/Setters standards
    public int getIdFranchise() { return idFranchise; }
    public void setIdFranchise(int idFranchise) { this.idFranchise = idFranchise; }

    public int getIdGerant() { return idGerant; }
    public void setIdGerant(int idGerant) { this.idGerant = idGerant; }

    // Getters/Setters de type String (utilisés par l'appli)
    public String getNomFranchise() { return nomFranchise.get(); }
    public void setNomFranchise(String nomFranchise) { this.nomFranchise.set(nomFranchise); }

    public String getSiegeSocial() { return siegeSocial.get(); }
    public void setSiegeSocial(String siegeSocial) { this.siegeSocial.set(siegeSocial); }

    // ÉVOLUTION : Accès au nom du gérant
    public String getNomCompletGerant() { return nomCompletGerant.get(); }
    public void setNomCompletGerant(String nomCompletGerant) { this.nomCompletGerant.set(nomCompletGerant); }

    // Accès aux Properties (pour le binding JavaFX)
    public StringProperty nomFranchiseProperty() { return nomFranchise; }
    public StringProperty siegeSocialProperty() { return siegeSocial; }
    public StringProperty nomCompletGerantProperty() { return nomCompletGerant; }

    @Override
    public String toString() { return this.nomFranchise.get(); }
}