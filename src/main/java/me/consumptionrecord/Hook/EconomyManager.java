package me.consumptionrecord.Hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private static Economy economy = null;

    public static Economy getEconomy() {
        if (economy == null) {
            // 尝试获取 Vault 的经济系统实例
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                economy = rsp.getProvider();
            }
        }
        return economy;
    }
}
