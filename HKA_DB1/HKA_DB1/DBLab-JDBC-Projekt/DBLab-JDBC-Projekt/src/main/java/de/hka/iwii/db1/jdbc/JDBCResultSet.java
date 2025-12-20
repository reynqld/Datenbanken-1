package de.hka.iwii.db1.jdbc;

import java.sql.*;

public class JDBCResultSet {

    public static String mapSQLType(int type) {
        return switch (type) {
            case Types.INTEGER, Types.SMALLINT, Types.BIGINT, Types.NUMERIC, Types.DECIMAL -> "number";
            case Types.CHAR, Types.VARCHAR, Types.LONGVARCHAR -> "char";
            case Types.DATE, Types.TIME, Types.TIMESTAMP -> "date";
            case Types.FLOAT, Types.REAL, Types.DOUBLE -> "float";
            default -> "other";
        };
    }

    // helper function for Statements
    public static void resultSet(Connection connection, String sql) {
        try ( Statement stmt = connection.createStatement(); ) {
            ResultSet resultSet = stmt.executeQuery(sql);
            resultSet(connection, resultSet);

            // Close ResultSet & Statement
            stmt.close();
            resultSet.close();
        }
        catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
        }
    }

    // helper function for PreparedStatements
    public static void resultSet(Connection connection, PreparedStatement prepStmt) {
        try {
            ResultSet resultSet = prepStmt.executeQuery();
            resultSet(connection, resultSet);

            // Close resultSet
            resultSet.close();
        }
        catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
        }
    }

    // main function
    private static void resultSet(Connection connection, ResultSet resultSet) {
        try {
            ResultSetMetaData meta = resultSet.getMetaData();

            int colCount = meta.getColumnCount();

            // Maximum column widths
            int[] colWidths = new int[colCount];
            for (int i = 1; i <= colCount; i++) {
                colWidths[i-1] = meta.getColumnDisplaySize(i);
                //System.out.println("Maximum Width of Column " + i + ": " + colWidths[i-1]);
            }
            System.out.println();

            // Table name
            System.out.println("Table: " + meta.getTableName(1) + "\n");

            // Column names
            for (int i = 1; i <= colCount; i++) {
                System.out.printf("%-" + colWidths[i - 1] + "s", meta.getColumnName(i));
                if (i < colCount) {
                    System.out.print(" | ");
                }
            }
            System.out.println();

            // Column types
            for (int i = 1; i <= colCount; i++) {
                String result = mapSQLType(meta.getColumnType(i));
                System.out.printf("%-" + colWidths[i - 1] + "s", result);
                if (i < colCount) {
                    System.out.print(" | ");
                }
            }
            System.out.println();

            // Divider between column name and its contents
            for (int i = 1; i <= colCount; i++) {
                String divider = String.format("%-" + colWidths[i-1] + "s", "-")
                        .replace(" ", "-");
                System.out.print(divider);
                if (i < colCount) { System.out.print("-+-"); };
            }
            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= colCount; i++) {
                    String colVal = resultSet.getString(i);

                    if (colVal == null) {

                        // print empty column if null
                        System.out.print(String.format("%-" + colWidths[i - 1] + "s",  ""));

                    } else {

                        // right-aligned if number
                        try {
                            int numInt = Integer.parseInt(colVal);
                            double numDouble = Double.parseDouble(colVal);

                            if (numDouble - numInt != 0) {
                                System.out.print(String.format("%" + colWidths[i - 1] + "f", numDouble));
                            } else {
                                System.out.print(String.format("%" + colWidths[i - 1] + "d", numInt));
                            }
                        } catch (NumberFormatException e) {

                        // left-aligned if string
                            System.out.printf("%-" + colWidths[i - 1] + "s", colVal);
                        }

                    }

                    if (i < colCount) {
                        System.out.print(" | ");
                    }

                }
                System.out.println();
            }
            System.out.println();

        }
        catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
        }
    }

    public static void main(String[] args) {
        JDBCTest dbTest = new JDBCTest();

        try {

            Connection connection = dbTest.getPostgreSQLConnection();

            resultSet(connection, "SELECT persnr, name, ort, aufgabe FROM personal");
            //"SELECT * FROM kunde";

            dbTest.closePostgreSQLConnection(connection);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            JDBCTest.dumpSQLException(ex);
        }
    }
}
