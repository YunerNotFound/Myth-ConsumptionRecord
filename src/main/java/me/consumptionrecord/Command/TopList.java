package me.consumptionrecord.Command;

import me.consumptionrecord.DataBase.SQLiteManager;
import me.consumptionrecord.Yaml.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String sql =  "SELECT Player, Consumption FROM player_data ORDER BY Consumption DESC LIMIT 10";
            statement = SQLiteManager.connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            int rank = 1;
            sender.sendMessage(Config.TopHead);
            while (resultSet.next()) {
                String player = resultSet.getString("Player");
                Double consumption = resultSet.getDouble("Consumption");
                String meg = Config.TopFormat.replace("%p", player).replace("%i", String.valueOf(rank)).replace("%num", String.valueOf(consumption));
                sender.sendMessage(meg);
                rank++;
            }
            sender.sendMessage(Config.TopEnd);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;

    }
}
