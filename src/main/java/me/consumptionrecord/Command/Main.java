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
import java.util.Objects;

public class Main implements CommandExecutor, TabCompleter {
    public static String action;
    public static String player;
    public static Double amount;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(Config.MissParam);
            return false;
        }
        try {
            action = args[0];
            player = args[1];
            amount = Double.valueOf(args[2]);
        } catch(NumberFormatException e) {
            sender.sendMessage(Config.Prefix + "§c数值只能为DOUBLE！ 例如28.22 / 11");
            return false;
        }
        switch (action) {
            case "add":
                try {
                    if (SQLiteManager.playerExists(player)) {
                        Double old = SQLiteManager.getData(player);
                        Double renew = old + amount;
                        SQLiteManager.setData(player, renew);
                        String meg = Config.AddSucceed.replace("%p", player).replace("%new", String.valueOf(renew)).replace("%old", String.valueOf(old));
                        sender.sendMessage(Config.Prefix + meg);
                    } else {
                        sender.sendMessage(Config.Prefix + Config.InvalidPlayer);
                    }
                } catch (Exception e) {
                    sender.sendMessage(Config.Prefix + Config.Error);
                }
                break;
            case "set":
                try {
                    if (SQLiteManager.playerExists(player)) {
                        Double old = SQLiteManager.getData(player);
                        SQLiteManager.setData(player, amount);
                        String meg = Config.SetSucceed.replace("%p", player).replace("%new", String.valueOf(amount)).replace("%old", String.valueOf(old));
                        sender.sendMessage(Config.Prefix + meg);
                    } else {
                        sender.sendMessage(Config.Prefix + Config.InvalidPlayer);
                    }
                } catch (Exception e) {
                    sender.sendMessage(Config.Prefix + Config.Error);
                }
                break;
            case "take":
                try {
                    if (SQLiteManager.playerExists(player)) {
                        Double old = SQLiteManager.getData(player);
                        Double renew = old - amount;
                        SQLiteManager.setData(player, renew);
                        String meg = Config.TakeSucceed.replace("%p", player).replace("%new", String.valueOf(renew)).replace("%old", String.valueOf(old));
                        sender.sendMessage(Config.Prefix + meg);
                    } else {
                        sender.sendMessage(Config.Prefix + Config.InvalidPlayer);
                    }
                } catch (Exception e) {
                    sender.sendMessage(Config.Prefix + Config.Error);
                }
                break;
            case "create":
                try {
                    if (!SQLiteManager.playerExists(player)) {
                        SQLiteManager.insertData(player, amount);
                        String meg = Config.CreateSucceed.replace("%p", player).replace("%new", String.valueOf(amount));
                        sender.sendMessage(Config.Prefix + meg);
                    } else {
                        sender.sendMessage(Config.Prefix + Config.ExistPlayer);
                    }
                } catch (Exception e) {
                    sender.sendMessage(Config.Prefix + Config.Error);
                }
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("add");
            completions.add("set");
            completions.add("take");
        }
        if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }
        if (args.length == 3) {
            completions.add("[额度]");
        }
        return completions;
    }

}