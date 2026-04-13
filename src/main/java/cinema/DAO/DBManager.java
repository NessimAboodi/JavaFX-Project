package cinema.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {

    private static Connection connect;

    public static Connection getInstance() {
        if (connect == null) {
            try {
                Properties props = new Properties();

                // Chargement du fichier de config depuis le classpath
                InputStream input = DBManager.class
                        .getResourceAsStream("/cinema/database.properties");

                if (input == null) {
                    throw new RuntimeException(
                            "Fichier database.properties introuvable !");
                }

                props.load(input);

                String url  = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");

                Class.forName("org.postgresql.Driver");
                connect = DriverManager.getConnection(url, user, pass);

            } catch (IOException | SQLException | ClassNotFoundException e) {
                throw new RuntimeException(
                        "Erreur de connexion à la base de données : " + e.getMessage(), e);
            }
        }
        return connect;
    }
}