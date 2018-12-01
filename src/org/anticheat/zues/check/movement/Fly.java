package org.anticheat.zues.check.movement;

import org.anticheat.zues.Zues;
import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.util.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * Created by Funkemunky on 2018-04-08 Package  org.anticheat.zues.check.movement
 */
public class Fly extends Check {
    public Fly() {
        super("Flight", CheckType.MOVEMENT, true);
    }

    private static void setBackPlayer(Player p) {
        SetBackSystem.setBack(p);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
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

        if (data == null) {
            return;
        }
        //Ascension Check
        if (!NEW_Velocity_Utils.didTakeVel(p) && !PlayerUtils.wasOnSlime(p)) {
            Vector vec = new Vector(to.getX(), to.getY(), to.getZ());
            double Distance = vec.distance(new Vector(from.getX(), from.getY(), from.getZ()));
            if (p.getFallDistance() == 0.0f && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && p.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                if (Distance > 0.50 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ() && !VelocityUtils.didTakeVelocity(p)) {
                    flag(p, "Ascension", "Distance: " + Distance);
                    setBackPlayer(p);
                } else if (Distance > 0.90 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                    flag(p, "Ascension", "Distance: " + Distance);
                    setBackPlayer(p);
                } else if (Distance > 1.0 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                    flag(p, "Ascension", "Distance: " + Distance);
                    setBackPlayer(p);
                } else if (Distance > 3.24 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                    flag(p, "Ascension", "Distance: " + Distance);
                    setBackPlayer(p);
                }
            }
        }

        //Hover check
        if (!PlayerUtils.isOnGround(p)) {
            double distanceToGround = getDistanceToGround(p);
            double yDiff = MathUtils.getVerticalDistance(e.getFrom(), e.getTo());
            int verbose = data.getFlyHoverVerbose();

            if (distanceToGround > 2) {
                verbose = yDiff == 0 ? verbose + 6 : yDiff < 0.06 ? verbose + 4 : 0;
            } else if (data.getAirTicks() > 7
                    && yDiff < 0.001) {
                verbose += 2;
            } else {
                verbose = 0;
            }

            if (verbose > 17) {
                flag(p, "Hover", "Verbose: " + verbose);
                setBackPlayer(p);
                verbose = 0;
            }
            data.setFlyHoverVerbose(verbose);
        }

        //Glide check
        if (PlayerUtils.getDistanceToGround(p) > 3) {
            double OffSet = e.getFrom().getY() - e.getTo().getY();
            long Time = System.currentTimeMillis();
            if (OffSet <= 0.0 || OffSet > 0.16) {
                data.setGlideTicks(0);
                return;
            }
            if (data.getGlideTicks() != 0) {
                Time = data.getGlideTicks();
            }
            long Millis = System.currentTimeMillis() - Time;
            if (Millis > 200L) {
                if (BlockUtils.isNearLiquid(p)
                        || PlayerUtils.wasOnSlime(p))
                    return;
                flag(p, "Glide", "Time: " + Time);
                setBackPlayer(p);
                data.setGlideTicks(0);
            }
            data.setGlideTicks(Time);
        } else {
            data.setGlideTicks(0);
        }

        //Velocity Diff check
        double diffY = Math.abs(from.getY() - to.getY());
        double lastDiffY = data.getLastVelocityFlyY();
        int verboseC = data.getFlyVelocityVerbose();

        double finalDifference = Math.abs(diffY - lastDiffY);

        //Bukkit.broadcastMessage(Math.abs(diffY - lastDiffY) + ", " + PlayerUtils.isOnGround(p));

        if (finalDifference < 0.08
                && e.getFrom().getY() < e.getTo().getY()
                && !PlayerUtils.isOnGround(p) && !p.getLocation().getBlock().isLiquid() && !BlockUtils.isNearLiquid(p)
                && !NEW_Velocity_Utils.didTakeVel(p) && !VelocityUtils.didTakeVelocity(p)) {
            if (++verboseC > 2) {
                flag(p, "Vertical", "Verbose: " + verboseC);
                SetBackSystem.setBack(p);
                verboseC = 0;
            }
        } else {
            verboseC = verboseC > 0 ? verboseC - 1 : 0;
        }
        data.setLastVelocityFlyY(diffY);
        data.setFlyVelocityVerbose(verboseC);
    }

    private int getDistanceToGround(Player p) {
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid()) break;
            distance++;
        }
        return distance;
    }
}