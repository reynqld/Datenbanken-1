import java.io.IOException;
import java.sql.*;
import java.time.*;

import de.hka.iwii.db1.weather.model.Weather;
import de.hka.iwii.db1.weather.model.WeatherForecast;
import de.hka.iwii.db1.weather.reader.WeatherReader;

public class WeatherTest {

    private static void insertForecastData(Connection connection, int[] stationIds) throws SQLException {
        try {

            //WeatherReader reader = new WeatherReader("proxy.hs-karlsruhe.de", 8888, "g16", "g8vqhNFhQC");
            WeatherReader reader = new WeatherReader();

            WeatherForecast[] forecasts = new WeatherForecast[stationIds.length];
            for (int i = 0; i < stationIds.length; i++) {
                //System.out.println("Reading");
                forecasts[i] = reader.readWeatherForecast(stationIds[i]);
                //System.out.println("Finished");
            }

            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("insert into forecasts (station_id, date_weather, min_temp, max_temp, precipitation, sunshine, date_retrieved) values (?, ?, ?, ?, ?, ?, ?)");

            for (int i = 0; i < stationIds.length; i++) {
                if (forecasts[i] != null) {
                    for (Weather weather : forecasts[i].getWeather()) {
                        ps.setInt(1, stationIds[i]);
                        ps.setTimestamp(2, new Timestamp(weather.getDate().getTime()));
                        ps.setFloat(3, weather.getMinTemp());
                        ps.setFloat(4, weather.getMaxTemp());
                        ps.setInt(5, weather.getPrecipitation());
                        ps.setInt(6, weather.getSunshine());
                        ps.setTimestamp(7, Timestamp.from(Instant.now()));

                        ps.addBatch();
                    }
                }
            }

            ps.executeBatch();
            connection.commit();

            ps.close();

            System.out.println("Inserted Forecasts into Table.\n\n");
        }
        catch (SQLException ex) {
            connection.rollback();
            DatabaseInitializer.dumpSQLException(ex);
        }
    }

    private static void filterStationId(Connection connection, int stationId) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from forecasts as f where f.station_id = ?");
            ps.setInt(1, stationId);

            ResultSet resultSet = ps.executeQuery();
            ResultSetMetaData meta = resultSet.getMetaData();
            int colCount = meta.getColumnCount();

            int idx = 1;
            while (resultSet.next()) {
                if (idx == 1) {
                    System.out.println("Station-ID: " + resultSet.getString(2) + "\n");
                }
                System.out.println(idx++ + ")");

                for (int i = 3; i <= colCount; i++) {
                    System.out.println(meta.getColumnName(i) + ": " + resultSet.getString(i));
                }
                System.out.print("\n");
            }

            ps.close();
            resultSet.close();

            System.out.println("Filtered by Station-ID: " + stationId + ".\n\n");
        }
        catch (SQLException ex) {
            connection.rollback();
            DatabaseInitializer.dumpSQLException(ex);
        }
    }

    private static void filterTempDate(Connection connection, int minTemp, int maxTemp, OffsetDateTime date) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("select distinct station_id from forecasts as f where f.min_temp >= ? AND f.max_temp <= ? AND f.date_weather = ?");
            ps.setFloat(1, minTemp);
            ps.setFloat(2, maxTemp);
            ps.setObject(3, date);

            ResultSet resultSet = ps.executeQuery();

            int idx = 1;
            while (resultSet.next()) {
                System.out.println(idx++ + ") Station ID: " + resultSet.getString(1));
            }
            if (idx == 1) { System.out.println("(No Stations matched the filter.)"); }
            ps.close();
            resultSet.close();

            System.out.println("Filtered by Temperatures (" + minTemp + ") - (" + maxTemp + ") and Date " + date + ".\n\n");
        }
        catch (SQLException ex) {
            connection.rollback();
            DatabaseInitializer.dumpSQLException(ex);
        }
    }

    public static void main(String[] args) throws IOException {
        DatabaseInitializer dbInit = new DatabaseInitializer();
        int[] stationIds = {10520, 10519, 10321, 10521, 10038};
        //int[] stationIds = {10519};

        try (Connection connection = dbInit.initialize()){
            insertForecastData(connection, stationIds);

            filterStationId(connection, 10519);

            filterTempDate(connection, -10, 30, OffsetDateTime.of(2026, 1, 10, 1, 0, 0, 0, ZoneOffset.ofHours(1)));

            dbInit.closePostgreSQLConnection(connection);
        }
        catch (SQLException ex) {
            DatabaseInitializer.dumpSQLException(ex);
        }
    }
}