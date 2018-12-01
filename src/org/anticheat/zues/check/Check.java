package org.anticheat.zues.check;

import org.anticheat.zues.Zues;
import org.anticheat.zues.util.Color;
import org.anticheat.zues.util.ConfigManager;
import org.anticheat.zues.util.ServerUtils;
import org.anticheat.zues.util.SetupPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class Check implements Listener {
    public Map<Player, Integer> violations = new WeakHashMap<>();

    private String name;
    private CheckType type;
    private boolean enabled;

    private CraftPlayer p;

    public Check(String name, CheckType type, boolean enabled) {
        this.name = name;
        this.type = type;
        this.enabled = enabled;

        if (enabled) Bukkit.getPluginManager().registerEvents(this, Zues.instance);
    }
    
    protected void flag(Player p, String data, String debugData) {
        SetupPlugin.instance.getDataManager().addViolation(p, this);
        Integer vl = SetupPlugin.instance.getDataManager().getViolatonsPlayer(p, this);
        String checkOut = vl.toString();

        if (ConfigManager.settings.getConfiguration().getBoolean("check_colors")) {
            if (vl <= 9) {
                checkOut = Color.translate(ConfigManager.messages.getConfiguration().getString("severity1")) + getName();
            } else if (vl <= 19) {
                checkOut = Color.translate(ConfigManager.messages.getConfiguration().getString("severity2")) + getName();
            } else if (vl >= 20) {
                checkOut = Color.translate(ConfigManager.messages.getConfiguration().getString("severity3")) + getName();
            }
        }

        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            FileConfiguration msg = ConfigManager.messages.getConfiguration();
            if (staff.hasPermission("zues.alerts")) {
                if (SetupPlugin.alertsToggled.containsKey(staff)) {
                    staff.sendMessage(Color.translate(msg.getString("alert"))
                            .replace("$prefix", msg.getString("prefix"))
                            .replace("$player", p.getName())
                            .replace("$check", checkOut)
                            .replace("$data", data != null ? data : "")
                            .replace("$vl", "" + vl)
                            .replace("$ping", "9999ms"));
                }
            }
        }
        ServerUtils.logDebug(null, p.getName() + " flagged " + this.name + " (" + debugData != null ? debugData : "NO DATA" + ") [x" + vl + "] Ping: " + "0" + "ms");

        HashMap<Player,Boolean> queuedForBan = new HashMap<>();

        if (vl >= ConfigManager.settings.getConfiguration().getInt("ban.ban_count")) {
            if (queuedForBan.containsKey(p)) {
                return;
            }
            queuedForBan.put(p, true);

            Bukkit.getScheduler().runTask(Zues.instance, new Runnable() {
                @Override
                public void run() {
                    if (p.isOnline()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.settings.getConfiguration().getString("ban.command").replace("$player", p.getName()));
                    }
                }
            });

            for (String msg : ConfigManager.messages.getConfiguration().getStringList("ban.broadcast")) {
                if (p.isOnline()) {
                    Bukkit.broadcastMessage(Color.translate(msg).replace("$player", p.getName()));
                }
            }
            ServerUtils.logDebug(null, p.getName() + " has been banned. Check: " + this.name + " (" + data + ") [x" + vl + "]");
            ServerUtils.logDebug(null, debugData);

            new BukkitRunnable() {
                public void run() {
                    queuedForBan.remove(p);
                }
            }.runTaskLater(Zues.instance, 3 * 20);
        }
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (this.enabled) {
            Bukkit.getPluginManager().registerEvents(this, Zues.instance);
        } else {
            HandlerList.unregisterAll(this);
        }
    }

    public CheckType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    protected void onEvent(Event event) {
        // TODO Auto-generated method stub

    }
}