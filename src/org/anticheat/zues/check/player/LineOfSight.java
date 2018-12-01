package org.anticheat.zues.check.player;

import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.util.LineOfSight_Utils.BlockPathFinder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Mr_JaVa_ on 2018-04-08 Package  org.anticheat.zues.check.player
 */
public class LineOfSight extends Check {
    public LineOfSight() {
        super("LineOfSight", CheckType.WORLD, true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST || e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                Player p = e.getPlayer();
                if ((e.getClickedBlock().getLocation().distance(p.getPlayer().getEyeLocation()) > 2)
                        && !BlockPathFinder.line(p.getPlayer().getEyeLocation(), e.getClickedBlock().getLocation()).contains(e.getClickedBlock()) && !e.isCancelled()) {
                    flag(p, "ChestAura/GhostHand", null);
                    e.setCancelled(true);
                }
            }
        }
    }
}
