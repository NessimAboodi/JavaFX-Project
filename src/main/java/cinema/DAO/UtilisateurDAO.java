package cinema.DAO;

import cinema.BO.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO extends DAO<Utilisateur> {

    @Override
    public boolean create(Utilisateur obj) {
        boolean result = false;
        String sql = "INSERT INTO utilisateur(login, mdp) VALUES(?,?)";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getLogin());
            ps.setString(2, BCrypt.hashpw(obj.getMdp(), BCrypt.gensalt())); // hachage
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(Utilisateur obj) {
        boolean result = false;
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, obj.getIdUtilisateur());

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean update(Utilisateur obj) {
        boolean result = false;
        String sql = "UPDATE Utilisateur SET login=?, mdp=? WHERE id_utilisateur = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, obj.getLogin());
            ps.setString(2, obj.getMdp());
            ps.setInt(3, obj.getIdUtilisateur());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Utilisateur hydrate(ResultSet resultSet) throws SQLException {
        return new Utilisateur(resultSet.getInt("id_utilisateur"),
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getString("login"),
                resultSet.getString("mdp"));
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> mesUtilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Statement statement = this.connect.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                mesUtilisateurs.add(hydrate(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mesUtilisateurs;
    }

    @Override
    public Utilisateur find(int idUtilisateur) {
        Utilisateur user = null;
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    user = hydrate(result);
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return user;
    }

    public Utilisateur authenticate(String login, String motDePasse) {
        Utilisateur user = null;
        // On cherche uniquement par login, le mot de passe se vérifie en Java
        String sql = "SELECT * FROM utilisateur WHERE login = ?";

        try (PreparedStatement ps = this.connect.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    Utilisateur candidat = hydrate(result);
                    // BCrypt compare le mot de passe saisi avec le hash stocké
                    if (BCrypt.checkpw(motDePasse, candidat.getMdp())) {
                        user = candidat;
                    }
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return user;
    }
}