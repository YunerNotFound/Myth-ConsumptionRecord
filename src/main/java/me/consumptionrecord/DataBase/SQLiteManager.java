package me.consumptionrecord.DataBase;

import me.consumptionrecord.Yaml.Config;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.*;
import java.text.DecimalFormat;

public class SQLiteManager {

    public static Connection connection;

    public SQLiteManager(String dbPath) {
        try {
            String url = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(url);
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Player TEXT," +
                "Consumption DOUBLE" +
                ")";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(String playerName, Double Consumption) {
        String sql = "INSERT INTO player_data (Player, Consumption) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(playerName));
            statement.setDouble(2, Consumption);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void setData(String playerName, Double renew) {

        DecimalFormat df = new DecimalFormat("#.##");
        String formattedRenew = df.format(renew);
        String query = "UPDATE player_data SET Consumption = " + formattedRenew + " WHERE Player = '" + playerName + "'";

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateData(String playerName, Double newConsumption) {
        String sql = "UPDATE player_data SET Consumption = ? WHERE Player = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, newConsumption);
            statement.setString(2, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Double getData(String playerName) {
        String sql = "SELECT Consumption FROM player_data WHERE Player = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("Consumption");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean playerExists(String playerName) {
        String sql = "SELECT * FROM player_data WHERE Player = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String getTopTenCount() {
        String sql = "SELECT Player, Consumption FROM player_data ORDER BY Consumption DESC LIMIT 10";
        StringBuilder result = new StringBuilder();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String playerName = resultSet.getString("Player");
                int count = resultSet.getInt("Consumption");

                // Append playerName and count to result with comma separation
                result.append(Config.PlayerColor).append(playerName).append(Config.BetweenPlayerAndAmountFormat + Config.AmountColor).append(count).append(",");
            }

            // Remove the trailing comma, if any

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
//    public static String getPlayerNameByPosition(int position) {
//        String sql = "\n" +
//                "SELECT Player, Consumption FROM player_data ORDER BY Consumption DESC LIMIT 1 OFFSET ?";
//        try {
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, position-1);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getString("Player");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return Config.InvalidNum;
//    }
//
//    public static double getAmountByPosition(int position) {
//        String sql = "SELECT Consumption FROM player_data ORDER BY Consumption DESC LIMIT 1 OFFSET ?";
//        try {
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, position-1);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getDouble("Consumption");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0.0;
//    }
    public static int getPlayerRank(String playerName) {
        int rank = 1;
        String sql = "\n" +
                "SELECT COUNT(*) AS rank FROM player_data WHERE Consumption > (SELECT Consumption FROM player_data WHERE Player = ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                rank += resultSet.getInt("rank");
                return rank;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Player not found in database
    }
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}