package cinema.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cinema.BO.Franchise;

public class FranchiseDAO extends DAO<Franchise> {

    private final LogDAO logDAO = new LogDAO();

    @Override
    public boolean create(Franchise obj) {
        boolean controle = false;
        String sql = "INSERT INTO franchise(nom_franchise, siege_social, id_gerant) VALUES (?,?,?)";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getNomFranchise());
            ps.setString(2, obj.getSiegeSocial());
            ps.setInt(3, obj.getIdGerant());

            if (ps.executeUpdate() > 0) {
                controle = true;
                logDAO.log(
                        "franchise",
                        "INSERT",
                        "",
                        decrire(obj)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controle;
    }

    @Override
    public boolean delete(Franchise obj) {
        boolean controle = false;
        String sql = "DELETE FROM franchise WHERE id_franchise = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, obj.getIdFranchise());

            if (ps.executeUpdate() > 0) {
                controle = true;
                logDAO.log(
                        "franchise",
                        "DELETE",
                        decrire(obj),
                        ""
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controle;
    }

    @Override
    public boolean update(Franchise obj) {
        boolean controle = false;

        // On récupère l'ancien état avant d'écraser
        Franchise avant = find(obj.getIdFranchise());

        String sql = "UPDATE franchise SET nom_franchise = ?, siege_social = ?, id_gerant = ? WHERE id_franchise = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getNomFranchise());
            ps.setString(2, obj.getSiegeSocial());
            ps.setInt(3, obj.getIdGerant());
            ps.setInt(4, obj.getIdFranchise());

            if (ps.executeUpdate() > 0) {
                controle = true;
                logDAO.log(
                        "franchise",
                        "UPDATE",
                        avant != null ? decrire(avant) : "",
                        decrire(obj)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return controle;
    }

    @Override
    public Franchise find(int id) {
        Franchise franchise = null;
        String sql = "SELECT * FROM franchise WHERE id_franchise = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    franchise = hydrate(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return franchise;
    }

    @Override
    public List<Franchise> findAll() {
        List<Franchise> mesFranchises = new ArrayList<>();
        String sql = "SELECT * FROM franchise ORDER BY id_franchise";

        try (Statement ps = this.connect.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) {
                mesFranchises.add(hydrate(rs));
            }
        } catch (SQLException e) {
            return null;
        }
        return mesFranchises;
    }

    public List<Franchise> getAllByGerant(int idGerant) {
        List<Franchise> mesFranchises = new ArrayList<>();
        String sql = "SELECT * FROM franchise WHERE id_gerant = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, idGerant);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mesFranchises.add(hydrate(rs));
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return mesFranchises;
    }

    public Integer getNbFranchiseByIdGerant(int idGerant) {
        int result = 0;
        String sql = "SELECT COUNT(*) FROM franchise WHERE id_gerant = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, idGerant);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // ----------------------------------------------------------------
    // Helpers privés
    // ----------------------------------------------------------------

    private Franchise hydrate(ResultSet rs) throws SQLException {
        return new Franchise(
                rs.getInt("id_franchise"),
                rs.getString("nom_franchise"),
                rs.getString("siege_social"),
                rs.getInt("id_gerant")
        );
    }

    /**
     * Produit une description lisible d'une franchise pour le log.
     * Exemple : "ID:2 | Nom:CinéMax | Siège:12 rue de la Paix, Paris | Gérant ID:1"
     */
    private String decrire(Franchise f) {
        return "ID:" + f.getIdFranchise()
                + " | Nom:" + f.getNomFranchise()
                + " | Siège:" + f.getSiegeSocial()
                + " | Gérant ID:" + f.getIdGerant();
    }
}