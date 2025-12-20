package de.hka.iwii.db1.jdbc;

import java.sql.Connection;
import java.sql.*;
import java.util.Properties;

public class JDBCTest {

    public Connection getPostgreSQLConnection() throws ClassNotFoundException, SQLException {
        // PostgreSQL
        Class.forName("org.postgresql.Driver");

        // 2. Verbinden mit Anmelde-Daten
        Properties props = new Properties();
        props.put("user", "g16");
        props.put("password", "g8vqhNFhQC");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://datenbanken1.ddns.net:3690/" + "g16", props);
        System.out.println("Connection established: " + connection.getCatalog() + "\n");
        return connection;
    }

    public void getDatabaseInfo(Connection connection) throws ClassNotFoundException, SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        // Informationen zur Datenbank sowie zum Treiber und dem angemeldeten user
        System.out.println("Database: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
        System.out.println("Database Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());
        System.out.println("Database User: " + metaData.getUserName());
    }

    public void closePostgreSQLConnection(Connection connection) throws SQLException {
        connection.close();
        if (connection.isClosed()) { System.out.println("Connection closed."); };
    }


    public static void main(String[] args){
        JDBCTest dbTest = new JDBCTest();
        try {
            Connection connection = dbTest.getPostgreSQLConnection();
            dbTest.getDatabaseInfo(connection);
            dbTest.closePostgreSQLConnection(connection);
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (SQLException ex) {
            dumpSQLException(ex);
        }

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
