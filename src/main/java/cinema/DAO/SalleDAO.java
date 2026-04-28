package cinema.DAO;

import cinema.BO.Salle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO extends DAO<Salle> {

    @Override
    public boolean create(Salle obj) {
        // CORRECTION : nb_places au lieu de capacite
        String sql = "INSERT INTO salle (numero, nb_places, description, id_cinema) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getNumero());
            ps.setInt(2, obj.getCapacite());
            ps.setString(3, obj.getDescription());
            ps.setInt(4, obj.getIdCinema());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ERREUR SQL EXCEPTION (create salle) : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Salle obj) {
        String sql = "DELETE FROM salle WHERE id_salle = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, obj.getIdSalle());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(Salle obj) {
        // CORRECTION : nb_places au lieu de capacite
        String sql = "UPDATE salle SET numero = ?, nb_places = ?, description = ?, id_cinema = ? WHERE id_salle = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getNumero());
            ps.setInt(2, obj.getCapacite());
            ps.setString(3, obj.getDescription());
            ps.setInt(4, obj.getIdCinema());
            ps.setInt(5, obj.getIdSalle());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Salle find(int id) {
        String sql = "SELECT * FROM salle WHERE id_salle = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // CORRECTION : rs.getInt("nb_places")
                    return new Salle(rs.getInt("id_salle"), rs.getString("numero"), rs.getInt("nb_places"), rs.getString("description"), rs.getInt("id_cinema"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Salle> findAll() {
        List<Salle> liste = new ArrayList<>();
        String sql = "SELECT * FROM salle";
        try (PreparedStatement ps = this.connect.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // CORRECTION : rs.getInt("nb_places")
                liste.add(new Salle(rs.getInt("id_salle"), rs.getString("numero"), rs.getInt("nb_places"), rs.getString("description"), rs.getInt("id_cinema")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }
}