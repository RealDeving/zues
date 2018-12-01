package org.anticheat.zues.check.movement;

import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.util.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;


public class Step extends Check {

    public Step() {
        super("Step", CheckType.MOVEMENT, true);
    }

    public boolean isOnGround(Player player) {
        if (PlayerUtils.isOnClimbable(player, 0)) {
            return false;
        }
        if (player.getVehicle() != null) {
            return false;
        }
        Material type = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
        if ((type != Material.AIR) && (type.isBlock()) && (type.isSolid()) && (type != Material.LADDER)
                && (type != Material.VINE)) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5D);
        type = a.getBlock().getType();
        if ((type != Material.AIR) && (type.isBlock()) && (type.isSolid()) && (type != Material.LADDER)
                && (type != Material.VINE)) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5D);
        type = a.getBlock().getRelative(BlockFace.DOWN).getType();
        if ((type != Material.AIR) && (type.isBlock()) && (type.isSolid()) && (type != Material.LADDER)
                && (type != Material.VINE)) {
            return true;
        }
        if (PlayerUtils.isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN),
                new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER })) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (isOnGround(player)
                || player.getAllowFlight()
                || player.hasPotionEffect(PotionEffectType.JUMP)
                || PlayerUtils.isOnClimbable(player, 0)
                || PlayerUtils.slabsNear(player.getLocation())
                || player.getLocation().getBlock().getType().equals(Material.WATER)
                || player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
            return;
        }

        double yDist = event.getTo().getY() - event.getFrom().getY();
        if (yDist < 0) {
            return;
        }
        double YSpeed = MathUtils.offset(MathUtils.getVerticalVector(event.getFrom().toVector()),
                MathUtils.getVerticalVector(event.getTo().toVector()));
        if (yDist > 0.95) {
            flag(player, "Block", "Blocks: " + Math.round(yDist));
            return;
        }
        if (((YSpeed == 0.25D || (YSpeed >= 0.58D && YSpeed < 0.581D)) && yDist > 0.0D
                || (YSpeed > 0.2457D && YSpeed < 0.24582D) || (YSpeed > 0.329 && YSpeed < 0.33))
                && !player.getLocation().clone().subtract(0.0D, 0.1, 0.0D).getBlock().getType().equals(Material.SNOW)) {
            flag(player, "Block", "Blocks: " + player.getLocation().clone().subtract(0.0D, 0.1D, 0.0D).getBlock().getType().toString() + ". Speed: " + YSpeed);
            return;
        }
        ArrayList<Block> blocks = PlayerUtils.getBlocksAroundCenter(player.getLocation(), 1);
        for (Block block : blocks) {
            if (block.getType().isSolid()) {
                if ((YSpeed >= 0.321 && YSpeed < 0.322)) {
                    flag(player, "Block", "Speed: " + YSpeed);
                    return;
                }
            }
        }
    }
}