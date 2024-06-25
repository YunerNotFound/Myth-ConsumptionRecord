package me.consumptionrecord.Command;

import me.consumptionrecord.ConsumptionRecord;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = "plugman reload ConsumptionRecord";
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
        sender.sendMessage("插件已重载,请查看控制台信息");
        return true;
    }
}
