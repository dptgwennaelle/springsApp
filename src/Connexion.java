import java.sql.*;

public class Connexion {

    //déclaration des variables
    private final String DB_NAME = "bibliotheque";
    private final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection connection;

    // constructeur de la classe connexion
    public Connexion() {
        //essai de connexion
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (Exception e) { // si ça marche pas, envoi l'erreur
            e.printStackTrace();
        }
    }

    // Fermeture de la connexion à la base de données
    public void close() {
        try {
            connection.close();
        } catch (Exception e) { // si ça marche pas, envoi l'erreur
            e.printStackTrace();
        }
    }

    // Exécution d'une requête SQL de type SELECT
    public ResultSet executeQuery(String query, String... params) {
        ResultSet result = null;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i+1, params[i]);
            }
            result = statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Exécution d'une requête SQL de type INSERT, UPDATE ou DELETE
    public int executeUpdate(String sql, String... params) {
        int result = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i+1, params[i]);
            }
            result = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
