package me.consumptionrecord.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.consumptionrecord.ConsumptionRecord;
import me.consumptionrecord.DataBase.SQLiteManager;
import me.consumptionrecord.Yaml.Config;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlaceholderHook extends PlaceholderExpansion {
    private ConsumptionRecord plugin;

    public PlaceholderHook() {
    }

    @Override
    public String getIdentifier() {
        return "cr";
    }

    @Override
    public String getAuthor() {
        return "404Yuner";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        String[] parts = params.split("_");
        if (parts.length == 1 && Objects.equals(parts[0], "me")) {
            String name = player.getName();
            int rank = SQLiteManager.getPlayerRank(name);
            return String.valueOf(rank);
        }
        if (parts.length == 1 && Objects.equals(parts[0], "record")) {
            return String.valueOf(SQLiteManager.getData(player.getName()));
        }
        if (parts.length == 2) {
            try {
                int position = Integer.parseInt(parts[1]);
           //     String cacheKey = "placeholder_" + position;
                String cachedResult = PlaceholderCache.getCache();


                if (cachedResult != null) {
                    String[] topList = cachedResult.split(",");
                    if (topList.length > position) {
                        return topList[position-1];
                    } else {
                        return "---";
                    }
                } else {
                    String top = SQLiteManager.getTopTenCount();
                    PlaceholderCache.putCache(top);
                    String[] topList = top.split(",");
                    if (topList.length > position) {
                        return topList[position-1];
                    } else {
                        return "---";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return String.valueOf(Config.RefreshInterval);
            }
        } else {
            return "错误length";
        }
    }
}