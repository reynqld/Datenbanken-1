package de.hka.iwii.db1.jdbc;

import java.sql.*;
import java.util.Scanner;

public class JDBCModifyData {

    private static long getForeignKey(PreparedStatement ps) {
        try {
            ResultSet keys = ps.getGeneratedKeys();
            keys.next();

            long parentId = keys.getLong(1);
            keys.close();

            return parentId;
        }
        catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
            return 0;
        }
    }

    private static long editTable(Connection connection, String sql, long parentIdIn) {
        try {
            PreparedStatement prepStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepStmt.setLong(1, parentIdIn);
            prepStmt.executeUpdate();

            long parentIdOut = getForeignKey(prepStmt);

            prepStmt.close();

            return parentIdOut;
        }
        catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
            return 0;
        }
    }

    public static long insertData(Connection connection) throws SQLException {
        try {
            connection.setAutoCommit(false);

            // create Customer
            PreparedStatement psCustomer = connection.prepareStatement("insert into kunde values (7, 'Die Ritzlers', 'Oxfordstraße', 76131, 'Karlsruhe', 0)", Statement.RETURN_GENERATED_KEYS);
            psCustomer.executeUpdate();

            ResultSet keys = psCustomer.getGeneratedKeys();
            keys.next();
            long pIdCustomer = keys.getLong(1);

            keys.close();
            psCustomer.close();

            System.out.println("Added new customer.");

            // create Order
            long pIdOrder = editTable(connection, "insert into auftrag values (7, '2025-12-04', ?, 9)", pIdCustomer);
            System.out.println("Added new customer's order.");

            // create Order Item
            editTable(connection, "insert into auftragsposten values (71, ?, 200001, 2, 800)", pIdOrder);
            System.out.println("Added new customer's order item(s).");

            // Show tables after addition
            System.out.println("\n----AFTER INSERT----");
            JDBCResultSet.resultSet(connection, "select * from kunde");
            JDBCResultSet.resultSet(connection, "select * from auftrag");
            JDBCResultSet.resultSet(connection, "select * from auftragsposten");

            connection.commit();

            return pIdCustomer;
        }
        catch (SQLException ex) {
            connection.rollback();
            JDBCTest.dumpSQLException(ex);
            return 0;
        }
    }

    public static void updateSperre(Connection connection, long parentId) throws SQLException {
        try {
            connection.setAutoCommit(false);

            editTable(connection, "update kunde set sperre = 1 where nr = ?", parentId);
            System.out.println("Updated 'sperre' of new customer.");

            // Show customer table after update
            System.out.println("\n----AFTER UPDATE----");
            JDBCResultSet.resultSet(connection, "select * from kunde");

            connection.commit();
        }
        catch (SQLException ex) {
            connection.rollback();
            JDBCTest.dumpSQLException(ex);
        }
    }

    public static void deleteData(Connection connection, String sql, long parentId) throws SQLException {
        try {
            connection.setAutoCommit(false);

            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setLong(1, parentId);
            prepStmt.executeUpdate();

            prepStmt.close();

            connection.commit();
        }
        catch (SQLException ex) {
            connection.rollback();
            JDBCTest.dumpSQLException(ex);
        }
    }

    public static void main(String[] args) {

        JDBCTest dbTest = new JDBCTest();
        //JDBCBikeShop dbBShop = new JDBCBikeShop();

        try {

            Connection connection = dbTest.getPostgreSQLConnection();

            //dbBShop.reInitializeDB(connection);

            long pIdCustomer = insertData(connection);

            updateSperre(connection, pIdCustomer);

            // delete order
            deleteData(connection, "delete from auftrag where kundnr = ?", pIdCustomer);
            System.out.println("Deleted order.");

            // delete customer
            deleteData(connection, "delete from kunde where nr = ?" ,pIdCustomer);
            System.out.println("Deleted customer.");

            // delete order item
            deleteData(connection, "delete from auftragsposten where auftrnr = ?", 7);
            System.out.println("Deleted order item.");

            System.out.println("\n----AFTER DELETE----");
            JDBCResultSet.resultSet(connection, "select * from kunde");
            JDBCResultSet.resultSet(connection, "select * from auftrag");
            JDBCResultSet.resultSet(connection, "select * from auftragsposten");

            dbTest.closePostgreSQLConnection(connection);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
        }

    }
}
