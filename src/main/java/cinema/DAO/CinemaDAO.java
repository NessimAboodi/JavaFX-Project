package cinema.DAO;

import cinema.BO.Cinema;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CinemaDAO extends DAO<Cinema> {

    @Override
    public boolean create(Cinema obj) {
        String sql = "INSERT INTO cinema (denomination, adresse, ville, id_franchise) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getDenomination());
            ps.setString(2, obj.getAdresse());
            ps.setString(3, obj.getVille());
            ps.setInt(4, obj.getIdFranchise());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Cinema obj) {
        String sql = "DELETE FROM cinema WHERE id_cinema = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, obj.getIdCinema());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Cinema obj) {
        String sql = "UPDATE cinema SET denomination = ?, adresse = ?, ville = ?, id_franchise = ? WHERE id_cinema = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getDenomination());
            ps.setString(2, obj.getAdresse());
            ps.setString(3, obj.getVille());
            ps.setInt(4, obj.getIdFranchise());
            ps.setInt(5, obj.getIdCinema());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Cinema> findAll() {
        List<Cinema> liste = new ArrayList<>();
        String sql = "SELECT * FROM cinema";
        try (PreparedStatement ps = this.connect.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Cinema(
                        rs.getInt("id_cinema"),
                        rs.getString("denomination"),
                        rs.getString("adresse"),
                        rs.getString("ville"),
                        rs.getInt("id_franchise")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public Cinema find(int id) {
        String sql = "SELECT * FROM cinema WHERE id_cinema = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cinema(rs.getInt("id_cinema"), rs.getString("denomination"), rs.getString("adresse"), rs.getString("ville"), rs.getInt("id_franchise"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}