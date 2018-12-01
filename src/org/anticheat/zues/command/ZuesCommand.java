package org.anticheat.zues.command;

import org.anticheat.zues.check.Check;
import org.anticheat.zues.data.DataManager;
import org.anticheat.zues.util.Color;
import org.anticheat.zues.util.Config;
import org.anticheat.zues.util.ConfigManager;
import org.anticheat.zues.util.SetupPlugin;
import org.anticheat.zues.util.command.Command;
import org.anticheat.zues.util.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZuesCommand {

    @Command(name = "zues",
        inGameOnly = true)

    public void ZuesCommand(CommandArgs ca) {
        String c = ca.getCommand().getName();
        Player p = ca.getPlayer();
        String[] args = ca.getArgs();
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        FileConfiguration msg = ConfigManager.messages.getConfiguration();

        if (args.length < 1) {
            sendHelp(p);
            return;
        }
        if (args[0].equalsIgnoreCase("help") && args.length == 1) {
            sendHelp(p);
            return;
        }
        if (args[0].equalsIgnoreCase("list") && args.length == 1) {
            List<String> checkNames = new ArrayList<>();

            for (Check checkLoop : SetupPlugin.instance.getDataManager().getChecks()) {
                checkNames.add((checkLoop.isEnabled() ? "§a" + checkLoop.getName() : "§c" + checkLoop.getName()) + "§7");
            }
            for (String msg2 : msg.getStringList("commands.zues.list")) {
                p.sendMessage(Color.translate(msg2).replace("$checks", checkNames.toString()));
            }
            return;
        }
        if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
            p.sendMessage(Color.translate(msg.getString("commands.zues.reloading")));
            long before;
            long after;
            before = System.currentTimeMillis();
            try {
                ConfigManager.settings.reloadConfig();
                ConfigManager.messages.reloadConfig();
                after = System.currentTimeMillis();
                long reloadTime = after - before;
                p.sendMessage(Color.translate(msg.getString("commands.zues.reload_success").replace("$time", "" + reloadTime)));
            } catch (Exception ex) {
                p.sendMessage(Color.translate(msg.getString("commands.zues.reload_failed")));
                ex.printStackTrace();
            }
            return;
        }
        if (args[0].equalsIgnoreCase("toggle") && args.length == 2) {
            Check check = SetupPlugin.instance.getDataManager().getCheckByName(args[1]);
            if (check != null) {
                check.setEnabled(!check.isEnabled());
                p.sendMessage(Color.translate(msg.getString("commands.zues.toggled_check")
                        .replace("$check", args[1])
                        .replace("$state", (check.isEnabled() ? "§a" : "§c") + check.isEnabled())));
                return;
            }
            p.sendMessage(Color.translate(msg.getString("commands.zues.invalid_check").replace("$check", args[1])));
            return;
        }
        if (args[0].equalsIgnoreCase("debug") && args.length == 1) {
            if (config.getBoolean("debug")) {
                config.set("debug", false);
                ConfigManager.settings.saveConfig();
                p.sendMessage(Color.translate(msg.getString("commands.zues.toggled_debug").replace("$state", (config.getBoolean("debug") ? "§a" : "§c") + config.getBoolean("debug"))));
                return;
            }
            config.set("debug", true);
            ConfigManager.settings.saveConfig();
            p.sendMessage(Color.translate(msg.getString("commands.zues.toggled_debug").replace("$state", (config.getBoolean("debug") ? "§a" : "§c") + config.getBoolean("debug"))));
            return;
        }
        if (args[0].equalsIgnoreCase("resetvio") && args.length == 2) {
            Player pp = Bukkit.getPlayer(args[1]);
            if (pp != null) {
                try {
                    SetupPlugin.instance.getDataManager().resetViolation(pp);
                    p.sendMessage(Color.translate(msg.getString("commands.zues.resetviolations_success").replace("$player", pp.getName())));
                } catch (Exception ex) {
                    p.sendMessage(Color.translate(msg.getString("commands.zues.resetviolations_failed").replace("$player", pp.getName())));
                    ex.printStackTrace();
                }
                return;
            }
            p.sendMessage(Color.translate(msg.getString("not_online").replace("$player", args[1])));
            return;
        }
        sendHelp(p);
    }

    private void sendHelp(Player p) {
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
        for (String msg2 : msg.getStringList("commands.zues.help")) {
            p.sendMessage(Color.translate(msg2));
        }
    }
}
