package org.anticheat.zues.util;

import org.anticheat.zues.PacketCore.PacketCore;
import org.anticheat.zues.check.Check;
import org.anticheat.zues.command.AlertsCommand;
import org.anticheat.zues.data.DataManager;
import org.anticheat.zues.events.UtilityJoinQuitEvent;
import org.anticheat.zues.events.UtilityMoveEvent;
import org.anticheat.zues.util.command.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class SetupPlugin {

    public static SetupPlugin instance;

    private DataManager dataManager;
    public PacketCore packet;
    public static HashMap<Player,Boolean> alertsToggled = new HashMap<>();

    static {
        instance = new SetupPlugin();
    }

    public void setup(JavaPlugin plugin) {

        dataManager = new DataManager();
        packet = new PacketCore();
        PacketCore.init();
        loadChecks();
        addDataPlayers();
        CommandManager.instance.setupCommands(plugin);

        // Registering events
        getServer().getPluginManager().registerEvents(new UtilityMoveEvent(), plugin);
        getServer().getPluginManager().registerEvents(new UtilityJoinQuitEvent(), plugin);
        getServer().getPluginManager().registerEvents(new SetBackSystem(), plugin);
        getServer().getPluginManager().registerEvents(new VelocityUtils(), plugin);
        getServer().getPluginManager().registerEvents(new NEW_Velocity_Utils(), plugin);
    }

    public void loadChecks() {
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        for (Check check : getDataManager().getChecks()) {
            if (config.get("checks." + check.getName() + ".enabled") != null) {
                check.setEnabled(config.getBoolean("checks." + check.getName() + ".enabled"));
            } else {
                config.set("checks." + check.getName() + ".enabled", check.isEnabled());
                ConfigManager.settings.saveConfig();
            }
            ServerUtils.logDebug(null,"Loaded checks: " + check);
        }
    }

    public void saveChecks() {
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        for (Check check : getDataManager().getChecks()) {
            config.set("checks." + check.getName() + ".enabled", check.isEnabled());
            ConfigManager.settings.saveConfig();
            ServerUtils.logDebug(null,"Saved checks: " + check);
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void addDataPlayers() {
        for (Player playerLoop : getServer().getOnlinePlayers()) {
            instance.getDataManager().addPlayerData(playerLoop);
            ServerUtils.logDebug(null,"Added data players: " + playerLoop);
        }
    }
}
