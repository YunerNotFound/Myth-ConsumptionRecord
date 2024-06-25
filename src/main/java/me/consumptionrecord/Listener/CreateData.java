package me.consumptionrecord.Listener;

import me.consumptionrecord.ConsumptionRecord;
import me.consumptionrecord.DataBase.SQLiteManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class CreateData implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (!player.hasPlayedBefore() || !SQLiteManager.playerExists(name)) {
            SQLiteManager.insertData(name, 0.0);
            ConsumptionRecord.getInstance().getLogger().info("已为玩家 " + name + " 创建新的消费累计数据");
        }
    }
}
