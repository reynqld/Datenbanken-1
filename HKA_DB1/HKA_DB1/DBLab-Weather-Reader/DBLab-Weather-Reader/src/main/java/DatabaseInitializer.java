import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.*;

import java.io.IOException;

public class DatabaseInitializer {

    // get connection to local database
    private static Connection getPostgreSQLConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/DB1_BonusAufgabe_WeatherReader";
        String user = "postgres";
        String password = "Reynald256";

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connection to Weather Forecasts Database established: " + connection.getCatalog() + "\n");
        return connection;
    }

    // initialize table in database
    public Connection initialize() throws SQLException, IOException {
        Connection connection = getPostgreSQLConnection();
        Path sqlPath = Path.of("sql", "Forecasts.sql");
        String sql = Files.readString(sqlPath);
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        return connection;
    }

    public void closePostgreSQLConnection(Connection connection) throws SQLException {
        connection.close();
        if (connection.isClosed()) { System.out.println("Connection closed."); };
    }

    public static void dumpSQLException(SQLException ex) {
        System.out.println("SQLException: " + ex.getLocalizedMessage());
        SQLException nextException = ex.getNextException();
        while (nextException != null) {
            System.out.println("SQLException: " + nextException.getLocalizedMessage());
            nextException = nextException.getNextException();
        }
        ex.printStackTrace();
    }
}
