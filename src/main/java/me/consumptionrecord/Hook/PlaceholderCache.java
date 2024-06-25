package me.consumptionrecord.Hook;

import me.consumptionrecord.Yaml.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlaceholderCache {
    private static String cache;
    private static Long expiration;

    public static void putCache(String value) {
        cache = value;
        expiration = System.currentTimeMillis() + Config.RefreshInterval;
    }

    public static String getCache() {
      //  if (expiration > System.currentTimeMillis()) {
            return cache;
//        } else {
//            cache = null;
//            expiration = null;
//        }

  //      return null;
    }
    public static void close() {
        cache = null;
        expiration = null;
    }
}
