package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável pela conexão ao banco de dados MySQL
 */
public class ConnectionFactory {

    private static Connection cnx;
    private static ConnectionFactory db;

    private ConnectionFactory() {
        Properties properties = new Properties();

        properties.setProperty("user", "root");
        properties.setProperty("password", "password");
        properties.setProperty("useSSL", "false");

        String host = "127.0.0.1";
        String port = "3306";
        String dbname = "projeto1";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;

        try {
            cnx = DriverManager.getConnection(url, properties);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Faz a conexão em um banco de dados MySQL e retorna o objeto Connection
     *
     * @return conexão com um banco MySQL
     */
    public static synchronized Connection getDBConnection() {
        if (db == null) {
            db = new ConnectionFactory();
        }
        return cnx;
    }

}
