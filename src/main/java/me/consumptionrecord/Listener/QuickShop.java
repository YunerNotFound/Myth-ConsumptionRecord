package me.consumptionrecord.Listener;

import com.ghostchu.quickshop.api.event.ShopPurchaseEvent;
import com.ghostchu.quickshop.api.obj.QUser;
import me.consumptionrecord.ConsumptionRecord;
import me.consumptionrecord.DataBase.SQLiteManager;
import me.consumptionrecord.Yaml.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuickShop implements Listener {

    private final Economy economy;
    private double ownMoney;

    public QuickShop(Economy economy) {
        this.economy = economy;
    }

    @EventHandler
    public void BuyInAdminShop(ShopPurchaseEvent e) {
        if (e.getShop().isUnlimited()) {
            Double price = e.getTotal();
            String playerName = String.valueOf(e.getPurchaser());
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

            ownMoney = economy.getBalance(offlinePlayer);

            if (ownMoney > price) {
                Double old = SQLiteManager.getData(playerName);
                Double renew = old + price;
                SQLiteManager.setData(playerName, renew);
                if (Config.QSDebug) {

                    ConsumptionRecord.getInstance().getLogger().info("[QuickShop监听] " + playerName + " 当前购买金额: " + price);
                }
            }
        }
    }
}
