package cinema.DAO;

import cinema.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    private final Connection connect = DBManager.getInstance();

    private static final String SQL_INSERT =
            "INSERT INTO log (tableName, operation, loginUtilisateur, ancienContenu, nouveauContenu) " +
                    "VALUES (?, ?, ?, ?, ?)";

    /**
     * Enregistre une action dans le journal.
     *
     * @param tableName      Table concernée ("franchise", "cinema")
     * @param operation      "INSERT", "UPDATE" ou "DELETE"
     * @param ancienContenu  État avant modification (vide pour INSERT)
     * @param nouveauContenu État après modification  (vide pour DELETE)
     */
    public void log(String tableName, String operation,
                    String ancienContenu, String nouveauContenu) {

        String login = "inconnu";
        if (Session.getUtilisateur() != null && Session.getUtilisateur().getLogin() != null) {
            login = Session.getUtilisateur().getLogin();
        }

        try (PreparedStatement ps = connect.prepareStatement(SQL_INSERT)) {
            ps.setString(1, tableName);
            ps.setString(2, operation);
            ps.setString(3, login);
            ps.setString(4, ancienContenu  != null ? ancienContenu  : "");
            ps.setString(5, nouveauContenu != null ? nouveauContenu : "");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[LogDAO] Erreur lors de l'écriture du log : " + e.getMessage());
        }
    }

    /**
     * Retourne tous les logs, du plus récent au plus ancien.
     */
    public List<String[]> findAll() {
        List<String[]> liste = new ArrayList<>();
        String sql = "SELECT idlog, tableName, operation, dateAction, loginUtilisateur, " +
                "ancienContenu, nouveauContenu FROM log ORDER BY dateAction DESC";
        try (PreparedStatement ps = connect.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new String[]{
                        String.valueOf(rs.getInt("idlog")),
                        rs.getString("tableName"),
                        rs.getString("operation"),
                        rs.getTimestamp("dateAction").toString(),
                        rs.getString("loginUtilisateur"),
                        rs.getString("ancienContenu"),
                        rs.getString("nouveauContenu")
                });
            }
        } catch (SQLException e) {
            System.err.println("[LogDAO] Erreur lecture logs : " + e.getMessage());
        }
        return liste;
    }
}