package org.anticheat.zues.check.combat;

import org.anticheat.zues.PacketCore.PacketTypes;
import org.anticheat.zues.Zues;
import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.events.PluginEvents.PacketAttackEvent;
import org.anticheat.zues.util.MathUtils;
import org.anticheat.zues.util.SetupPlugin;
import org.anticheat.zues.util.TimerUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class Reach extends Check {
    public Reach() {
        super("Reach", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onAttack(final PacketAttackEvent e) {
        final Player p = e.getPlayer();
        if (p.getGameMode().equals((Object) GameMode.CREATIVE)) {
            return;
        }
        if (e.getType() != PacketTypes.USE || e.getEntity() == null) {
            return;
        }
        final Player player = e.getPlayer();
        final Entity entity = e.getEntity();
        final Date date = null;
        final double distance = MathUtils.getHorizontalDistance(player.getLocation(), entity.getLocation()) - 0.3;
        double maxReach = 4.25;
        final double yawDifference = 0.01 - Math.abs(Math.abs(player.getEyeLocation().getYaw()) - Math.abs(entity.getLocation().getYaw()));
        maxReach += Math.abs(player.getVelocity().length() + entity.getVelocity().length()) * 2.5;
        maxReach += yawDifference * 0.01;
        if (maxReach < 4.25) {
            maxReach = 4.25;
        }
        if (distance > maxReach) {
            new BukkitRunnable() {
                public void run() {
                    final PlayerData data = SetupPlugin.instance.getDataManager().getData(p);
                    if (data != null && TimerUtils.Passed(data.lastReachFlag, 500L)) {
                        data.lastReachFlag = TimerUtils.nowlong();
                        flag(player, "Heuristic", "Reach: " + MathUtils.trim(3, distance));
                    }
                }
            }.runTask((Plugin) Zues.instance);
        }
    }
}

