package de.hka.iwii.db1.jdbc;

import com.mysql.cj.protocol.Resultset;

import java.sql.*;

public class JDBCKundenLieferanten {

    public static void searchCustomer(Connection connection, String search) {

        String sql = "select distinct k.name as kunde, k.nr as knr, la.name as lieferant, la.nr as lnr from kunde as k " +
                "left join auftrag as a on k.nr = a.kundnr left join auftragsposten as ap on a.auftrnr = ap.auftrnr " +
                "left join lieferung as lu on ap.teilnr = lu.teilnr " +
                "left join lieferant as la on lu.liefnr = la.nr " +
                "where k.name like ? " +
                "order by k.name, la.name";

        try {
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, search);
            JDBCResultSet.resultSet(connection, prepStmt);

            // Close PreparedStatement
            prepStmt.close();
        }
        catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
        }
    }

    public static void main(String[] args) {
        JDBCTest dbTest = new JDBCTest();

        try {

            Connection connection = dbTest.getPostgreSQLConnection();

            searchCustomer(connection, "Rafa%");

            dbTest.closePostgreSQLConnection(connection);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
        }

    }
}
