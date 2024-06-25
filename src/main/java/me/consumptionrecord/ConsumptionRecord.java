package me.consumptionrecord;

import me.consumptionrecord.Command.Get;
import me.consumptionrecord.Command.Main;
import me.consumptionrecord.Command.Reload;
import me.consumptionrecord.Command.TopList;
import me.consumptionrecord.DataBase.SQLiteManager;
import me.consumptionrecord.Hook.EconomyManager;
import me.consumptionrecord.Hook.PlaceholderCache;
import me.consumptionrecord.Hook.PlaceholderHook;
import me.consumptionrecord.Listener.CreateData;
import me.consumptionrecord.Listener.QuickShop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ConsumptionRecord extends JavaPlugin {
    private static ConsumptionRecord instance;

    public static FileConfiguration config;

    private SQLiteManager sqliteManager;

    @Override
    public void onEnable() {
        instance = this;
        Economy economy = EconomyManager.getEconomy();
        if (economy != null) {
            Bukkit.getPluginManager().registerEvents(new QuickShop(economy), this);
        } else {
            getLogger().warning("Unable to find an economy plugin through Vault.Disabled the listener of QuickShop");
        }

        Bukkit.getPluginManager().registerEvents(new CreateData(), (Plugin)this);
        getCommand("ConsumptionRecord").setExecutor(new Main());
        getCommand("ConsumptionRecordTop").setExecutor(new TopList());
        getCommand("ConsumptionRecordReload").setExecutor(new Reload());
        getCommand("ConsumptionRecordGet").setExecutor(new Get());
        String serverVersion = Bukkit.getServer().getVersion();
        getLogger().info("\n" + "  ________ \n" +
                " / ___/ _ \\ 消费记录 By 404Yuner\n" +
                "/ /__/ , _/ " + serverVersion +"\n" +
                "\\___/_/|_| \n" +
                "           ");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PlaceholderHook placeholder = new PlaceholderHook();
            getLogger().info("Successfully hooked PlaceHolderAPI. Usage: %cr_me% and %cr_top_<rank>%");
            placeholder.register();

        } else {
            getLogger().warning("Unable to find PlaceholderAPI plugin. Disabled the related function.");
            getServer().getPluginManager().disablePlugin(this);
        }

        // 数据库文件创建并连接
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        String dbPath = dataFolder.getAbsolutePath() + File.separator + "database.db";

        // 检查数据库文件是否存在，如果不存在则重新创建数据库连接
        if (!new File(dbPath).exists()) {
            getLogger().info("数据库文件不存在，重新创建数据库连接...");
            sqliteManager = new SQLiteManager(dbPath);
            getLogger().info("已成功创建sql数据库连接");
        } else {
            // 如果数据库文件存在，则直接建立连接
            getLogger().info("已成功创建sql数据库连接");
            sqliteManager = new SQLiteManager(dbPath);
        }

        // config.yml创建
        File file = new File(getDataFolder(), "config.yml");
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            getLogger().info("插件文件夹不存在，正在创建中...");
        }
        if (!file.exists()) {
            saveDefaultConfig();
            getLogger().info("插件默认配置文件保存成功！");
            reloadConfig();
            getLogger().info("已经成功加载新的配置文件！");
        }
        config = load(file);

    }

    @Override
    public void onDisable() {
        if (sqliteManager != null) {
            sqliteManager.close();
            getLogger().info("消费记录数据库连接已关闭...");
        }
        PlaceholderHook placeholder = new PlaceholderHook();
        placeholder.unregister();
        getLogger().info("已取消注册 PlaceholderAPI");
        PlaceholderCache.close();
    }
    public static ConsumptionRecord getInstance() {
        return instance;
    }
    public FileConfiguration load(File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException iOException) {}
        return (FileConfiguration) YamlConfiguration.loadConfiguration(file);
    }
}
