package cinema.BO;

public class Salle {
    private int idSalle;
    private String numero;
    private int capacite;
    private String description; // NOUVEAU CHAMP
    private int idCinema;

    // Constructeur pour la création
    public Salle(String numero, int capacite, String description, int idCinema) {
        this.numero = numero;
        this.capacite = capacite;
        this.description = description;
        this.idCinema = idCinema;
    }

    // Constructeur complet (pour le DAO)
    public Salle(int idSalle, String numero, int capacite, String description, int idCinema) {
        this.idSalle = idSalle;
        this.numero = numero;
        this.capacite = capacite;
        this.description = description;
        this.idCinema = idCinema;
    }

    // Getters et Setters
    public int getIdSalle() { return idSalle; }
    public void setIdSalle(int idSalle) { this.idSalle = idSalle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getIdCinema() { return idCinema; }
    public void setIdCinema(int idCinema) { this.idCinema = idCinema; }

    @Override
    public String toString() {
        return "Salle n°" + this.numero;
    }
}