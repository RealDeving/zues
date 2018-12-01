package org.anticheat.zues.util;

import org.anticheat.zues.Zues;

public class ConfigManager {

    public static ConfigManager instance;

    static {
        instance = new ConfigManager();
    }

    public static Config settings;
    public static Config messages;

    public void setup() {
        settings = new Config(Zues.instance, "settings.yml", null);
        messages = new Config(Zues.instance, "messages.yml", null);
        settings.saveDefaultConfig();
        messages.saveDefaultConfig();
    }
}
