package org.anticheat.zues.check.movement;

import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.util.MathUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class NoSlowdown extends Check {

    public static Map<UUID, Map.Entry<Integer, Long>> speedTicks;

    public NoSlowdown() {
        super("NoSlowdown", CheckType.MOVEMENT, true);

        speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (speedTicks.containsKey(e.getPlayer().getUniqueId())) {
            speedTicks.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void BowShoot(final EntityShootBowEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        if (player.isInsideVehicle()) {
            return;
        }
        if (player.isSprinting()) {
            flag(player, "Bow", null);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo().getX() == e.getFrom().getX() && e.getFrom().getY() == e.getTo().getY()
                && e.getTo().getZ() == e.getFrom().getZ()) {
            return;
        }
        Player player = e.getPlayer();
        double OffsetXZ = MathUtils.offset(MathUtils.getHorizontalVector(e.getFrom().toVector()),
                MathUtils.getHorizontalVector(e.getTo().toVector()));

        if (!player.getLocation().getBlock().getType().equals(Material.WEB)) {
            return;
        }

        if (OffsetXZ < 0.2) {
            return;
        }

        flag(player, "Web", null);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && event.getItem() != null) {
            if (event.getItem().equals(Material.EXP_BOTTLE) || event.getItem().getType().equals(Material.GLASS_BOTTLE)
                    || event.getItem().getType().equals(Material.POTION)) {
                return;
            }
            Player player = event.getPlayer();

            long Time = System.currentTimeMillis();
            int level = 0;
            if (speedTicks.containsKey(player.getUniqueId())) {
                level = speedTicks.get(player.getUniqueId()).getKey().intValue();
                Time = speedTicks.get(player.getUniqueId()).getValue().longValue();
            }
            double diff = System.currentTimeMillis() - Time;
            level = diff >= 2.0
                    ? (diff <= 51.0 ? (level += 2)
                    : (diff <= 100.0 ? (level += 0) : (diff <= 500.0 ? (level -= 6) : (level -= 12))))
                    : ++level;
            int max = 50;
            if (level > max * 0.9D && diff <= 100.0D) {
                flag(player, "Experimental", "Level: " + level);

                if (level > max) {
                    level = max / 4;
                }
            } else if (level < 0) {
                level = 0;
            }
            speedTicks.put(player.getUniqueId(),
                    new AbstractMap.SimpleEntry<Integer, Long>(level, System.currentTimeMillis()));
        }
    }
}