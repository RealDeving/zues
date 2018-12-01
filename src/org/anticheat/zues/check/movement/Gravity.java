package org.anticheat.zues.check.movement;

import org.anticheat.zues.Zues;
import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.util.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Mr_JaVa_ on 2018-04-08 Package  org.anticheat.zues.check.movement
 */
public class Gravity extends Check {
    public Gravity() {
        super("Gravity", CheckType.MOVEMENT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.CREATIVE)
                || p.getAllowFlight()
                || e.getPlayer().getVehicle() != null
                || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE
                || PlayerUtils.isOnClimbable(p, 0)
                || PlayerUtils.isOnClimbable(p, 1) || VelocityUtils.didTakeVelocity(p)) {
            return;
        }
        PlayerData data = SetupPlugin.instance.getDataManager().getData(p);
        if (data != null) {
            double diff = MathUtils.getVerticalDistance(e.getFrom(), e.getTo());
            double LastY = data.getLastY_Gravity();
            double MaxG = 5;
            if (PlayerUtils.wasOnSlime(p)) {
                data.setGravity_VL(0);
                return;
            }
            if (e.getTo().getY() < e.getFrom().getY()) {
                return;
            }
            if (BlockUtils.isHalfBlock(p.getLocation().add(0, -1.50, 0).getBlock()) || BlockUtils.isNearHalfBlock(p) || BlockUtils.isStair(p.getLocation().add(0, 1.50, 0).getBlock()) || BlockUtils.isNearStair(p) || NEW_Velocity_Utils.didTakeVel(p)
                    || PlayerUtils.wasOnSlime(p)) {
                data.setGravity_VL(0);
                return;
            }
            if (p.getLocation().getBlock().getType() != Material.CHEST &&
                    p.getLocation().getBlock().getType() != Material.TRAPPED_CHEST && p.getLocation().getBlock().getType() != Material.ENDER_CHEST && data.getAboveBlockTicks() == 0) {
                if (!PlayerUtils.onGround2(p) && !PlayerUtils.isOnGround3(p) && !PlayerUtils.isOnGround(p)) {
                    if ((((ServerUtils.isBukkitVerison("1_7") || ServerUtils.isBukkitVerison("1_8")) && Math.abs(p.getVelocity().getY() - LastY) > 0.000001)
                            || (!ServerUtils.isBukkitVerison("1_7") && !ServerUtils.isBukkitVerison("1_8") && Math.abs(p.getVelocity().getY() - diff) > 0.000001))
                            && !PlayerUtils.onGround2(p)
                            && e.getFrom().getY() < e.getTo().getY()
                            && (p.getVelocity().getY() >= 0 || p.getVelocity().getY() < (-0.0784 * 5)) && !VelocityUtils.didTakeVelocity(p) && p.getNoDamageTicks() == 0.0) {
                        if (data.getGravity_VL() >= MaxG) {
                            flag(p, "Vertical/Horizontal", "" + data.getGravity_VL());
                            SetBackSystem.setBack(p);
                        } else {
                            data.setGravity_VL(data.getGravity_VL() + 1);
                        }
                    } else {
                        data.setGravity_VL(0);
                    }
                }
            }
            data.setLastY_Gravity(diff);
        }
    }
}
