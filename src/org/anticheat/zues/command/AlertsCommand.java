package org.anticheat.zues.command;

import org.anticheat.zues.util.Color;
import org.anticheat.zues.util.ConfigManager;
import org.anticheat.zues.util.SetupPlugin;
import org.anticheat.zues.util.command.Command;
import org.anticheat.zues.util.command.CommandArgs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AlertsCommand {

    @Command(name = "alerts",
        inGameOnly = true)

    public void ZuesCommand(CommandArgs ca) {
        String c = ca.getCommand().getName();
        Player p = ca.getPlayer();
        String[] args = ca.getArgs();
        FileConfiguration msg = ConfigManager.messages.getConfiguration();

        if (args.length < 1) {
            if (!SetupPlugin.alertsToggled.containsKey(p)) {
                SetupPlugin.alertsToggled.put(p, true);
                p.sendMessage(Color.translate(msg.getString("commands.alerts.toggled")
                        .replace("$state", (SetupPlugin.alertsToggled.containsKey(p) ? "§a" : "§c") + SetupPlugin.alertsToggled.containsKey(p))));
                return;
            }
            SetupPlugin.alertsToggled.remove(p);
            p.sendMessage(Color.translate(msg.getString("commands.alerts.toggled")
                    .replace("$state", (SetupPlugin.alertsToggled.containsKey(p) ? "§a" : "§c") + SetupPlugin.alertsToggled.containsKey(p))));
        }
    }
}