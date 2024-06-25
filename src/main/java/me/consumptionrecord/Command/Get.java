package me.consumptionrecord.Command;

import me.consumptionrecord.DataBase.SQLiteManager;
import me.consumptionrecord.Yaml.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Get implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String player = args[0];
            if (SQLiteManager.playerExists(player)) {
                Double data = SQLiteManager.getData(player);
                String meg = Config.GetSucceed.replace("%data", String.valueOf(data)).replace("%p", player);
                sender.sendMessage(Config.Prefix + meg);
            } else {
                sender.sendMessage(Config.Prefix + Config.InvalidPlayer);
            }
            return true;
        } else {
            sender.sendMessage(Config.Prefix + Config.InvalidPlayer);
            return false;
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        return completions;
    }

}
