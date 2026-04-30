package cinema.DAO;

import cinema.BO.Cinema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CinemaDAO extends DAO<Cinema> {

    private final LogDAO logDAO = new LogDAO();

    @Override
    public boolean create(Cinema obj) {
        String sql = "INSERT INTO cinema (denomination, adresse, ville, id_franchise) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getDenomination());
            ps.setString(2, obj.getAdresse());
            ps.setString(3, obj.getVille());
            ps.setInt(4, obj.getIdFranchise());

            if (ps.executeUpdate() > 0) {
                logDAO.log(
                        "cinema",
                        "INSERT",
                        "",
                        decrire(obj)
                );
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Cinema obj) {
        String sql = "DELETE FROM cinema WHERE id_cinema = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, obj.getIdCinema());

            if (ps.executeUpdate() > 0) {
                logDAO.log(
                        "cinema",
                        "DELETE",
                        decrire(obj),
                        ""
                );
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Cinema obj) {
        // On récupère l'ancien état avant d'écraser
        Cinema avant = find(obj.getIdCinema());

        String sql = "UPDATE cinema SET denomination = ?, adresse = ?, ville = ?, id_franchise = ? WHERE id_cinema = ?";
        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getDenomination());
            ps.setString(2, obj.getAdresse());
            ps.setString(3, obj.getVille());
            ps.setInt(4, obj.getIdFranchise());
            ps.setInt(5, obj.getIdCinema());

            if (ps.executeUpdate() > 0) {
                logDAO.log(
                        "cinema",
                        "UPDATE",
                        avant != null ? decrire(avant) : "",
                        decrire(obj)
                );
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Cinema> findAll() {
        List<Cinema> liste = new ArrayList<>();
        String sql = "SELECT * FROM cinema";
        try (PreparedStatement ps = this.connect.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(hydrate(rs));
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
                if (rs.next()) return hydrate(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ----------------------------------------------------------------
    // Helpers privés
    // ----------------------------------------------------------------

    private Cinema hydrate(ResultSet rs) throws SQLException {
        return new Cinema(
                rs.getInt("id_cinema"),
                rs.getString("denomination"),
                rs.getString("adresse"),
                rs.getString("ville"),
                rs.getInt("id_franchise")
        );
    }

    /**
     * Produit une description lisible d'un cinéma pour le log.
     * Exemple : "ID:1 | Nom:CinéMax Étoile | Adresse:5 Place de l'Étoile | Ville:Paris | Franchise ID:1"
     */
    private String decrire(Cinema c) {
        return "ID:" + c.getIdCinema()
                + " | Nom:" + c.getDenomination()
                + " | Adresse:" + c.getAdresse()
                + " | Ville:" + c.getVille()
                + " | Franchise ID:" + c.getIdFranchise();
    }
}