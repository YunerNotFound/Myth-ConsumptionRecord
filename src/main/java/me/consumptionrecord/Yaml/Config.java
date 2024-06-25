package me.consumptionrecord.Yaml;


import me.consumptionrecord.ConsumptionRecord;
import org.bukkit.Bukkit;

public class Config {
    public static String Prefix;
    public static boolean QSDebug;
    public static String MissParam;
    public static String InvalidParam;
    public static String InvalidPlayer;
    public static String ExistPlayer;
    public static String AddSucceed;
    public static String SetSucceed;
    public static String TakeSucceed;
    public static String GetSucceed;
    public static String CreateSucceed;
    public static String TopHead;
    public static String TopEnd;
    public static String TopFormat;
    public static String PlayerColor;
    public static String AmountColor;
    public static String BetweenPlayerAndAmountFormat;
    public static String InvalidNum;
    public static String Error;
    public static Long RefreshInterval;

    static {
        loadConfig();
    }

    public static void loadConfig() {
        Prefix = getConfigString("Prefix");
        QSDebug = Boolean.parseBoolean(getConfigString("QSDebug"));
        Error = getConfigString("Message.Error");
        MissParam = getConfigString("Message.MissParam");
        InvalidParam = getConfigString("Message.InvalidParam");
        InvalidPlayer = getConfigString("Message.InvalidPlayer");
        ExistPlayer = getConfigString("Message.ExistPlayer");
        AddSucceed = getConfigString("Message.AddSucceed");
        SetSucceed = getConfigString("Message.SetSucceed");
        TakeSucceed = getConfigString("Message.TakeSucceed");
        GetSucceed = getConfigString("Message.GetSucceed");
        CreateSucceed = getConfigString("Message.CreateSucceed");
        TopHead = getConfigString("Message.TopHead");
        TopEnd = getConfigString("Message.TopEnd");
        TopFormat = getConfigString("Message.TopFormat");
        PlayerColor = getConfigString("Placeholder.PlayerColor");
        AmountColor = getConfigString("Placeholder.AmountColor");
        BetweenPlayerAndAmountFormat = getConfigString("Placeholder.BetweenPlayerAndAmountFormat");
        InvalidNum = getConfigString("Placeholder.InvalidNum");
        RefreshInterval = Long.parseLong(getConfigString("Placeholder.RefreshInterval"));
    }

    public static String getConfigString(String key) {
        String result = null;
        try {
            result = ConsumptionRecord.getInstance().getConfig().getString(key);
        } catch (NullPointerException e) {
            Bukkit.getLogger().warning("配置文件加载失败! 内容 = " + key);
        }
        assert result != null;
        return result;
    }
}