package org.anticheat.zues.util.command;

import org.anticheat.zues.command.AlertsCommand;
import org.anticheat.zues.command.ZuesCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {

    public static CommandManager instance;

    static {
        instance = new CommandManager();
    }

    CommandFramework framework;

    public void setupCommands(JavaPlugin plugin) {
        framework = new CommandFramework(plugin);
        framework.registerCommands(new ZuesCommand());
        framework.registerCommands(new AlertsCommand());
        framework.registerHelp();
    }
}