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
        // ATTENTION : Vérifiez bien dans pgAdmin si la colonne s'appelle 'numero' ou 'numero_salle'
        String sql = "INSERT INTO salle (numero, capacite, id_cinema) VALUES (?, ?, ?)";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getNumero());
            ps.setInt(2, obj.getCapacite());
            ps.setInt(3, obj.getIdCinema());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Affichage de l'erreur précise dans la console pour le débogage
            System.err.println("ERREUR SQL EXCEPTION (create) : " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("ERREUR SQL EXCEPTION (delete) : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Salle obj) {
        String sql = "UPDATE salle SET numero = ?, capacite = ?, id_cinema = ? WHERE id_salle = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getNumero());
            ps.setInt(2, obj.getCapacite());
            ps.setInt(3, obj.getIdCinema());
            ps.setInt(4, obj.getIdSalle());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ERREUR SQL EXCEPTION (update) : " + e.getMessage());
            e.printStackTrace();
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
                    return new Salle(
                            rs.getInt("id_salle"),
                            rs.getString("numero"),
                            rs.getInt("capacite"),
                            rs.getInt("id_cinema")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("ERREUR SQL EXCEPTION (find) : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Salle> findAll() {
        List<Salle> liste = new ArrayList<>();
        String sql = "SELECT * FROM salle";
        try (PreparedStatement ps = this.connect.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Salle(
                        rs.getInt("id_salle"),
                        rs.getString("numero"),
                        rs.getInt("capacite"),
                        rs.getInt("id_cinema")
                ));
            }
        } catch (SQLException e) {
            System.err.println("ERREUR SQL EXCEPTION (findAll) : " + e.getMessage());
            e.printStackTrace();
        }
        return liste;


    }
}