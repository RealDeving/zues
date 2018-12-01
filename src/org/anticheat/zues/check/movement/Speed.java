package org.anticheat.zues.check.movement;

import org.anticheat.zues.Zues;
import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Speed extends Check {

    public Speed() {
        super("Speed", CheckType.MOVEMENT, true);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (p.hasPotionEffect(PotionEffectType.SPEED)) {
            return;
        }
        final PlayerData data = SetupPlugin.instance.getDataManager().getData(p);
        final Location to = e.getTo();
        final Location from = e.getFrom();
        if ((to.getX() == from.getX() && to.getY() == from.getY() && to.getZ() == from.getZ()) || p.getAllowFlight()) {
            return;
        }
        if (data != null) {
            if (data.isSpeed_PistonExpand_Set() && TimerUtils.elapsed(data.getSpeed_PistonExpand_MS(), 9900L)) {
                data.setSpeed_PistonExpand_Set(false);
            }

            final double speed = MathUtils.getHorizontalDistance(from, to);
            if (MathUtils.elapsed(data.getLastVelMS(), 250L)) {
                int verbose = data.getSpeedAVerbose();
                final double speedEffect = PlayerUtils.getPotionEffectLevel(p, PotionEffectType.SPEED);
                double speedAThreshold = ((data.getAirTicks() > 0) ? ((data.getAirTicks() >= 6) ? ((data.getAirTicks() == 13) ? 0.466 : 0.35) : (0.345 * Math.pow(0.986938064, data.getAirTicks()))) : ((data.getGroundTicks() > 5) ? 0.362 : ((data.getGroundTicks() == 3) ? 0.62 : 0.4))) + ((data.getAirTicks() > 0) ? (-0.001 * data.getAirTicks() + 0.014) : ((0.018 - ((data.getGroundTicks() >= 6) ? 0.0 : (data.getGroundTicks() * 0.001))) * speedEffect));
                speedAThreshold = ((data.getAboveBlockTicks() > 0) ? (speedAThreshold + 0.25) : speedAThreshold);
                speedAThreshold = ((data.getIceTicks() > 0) ? (speedAThreshold + 0.14) : speedAThreshold);
                speedAThreshold = ((data.getSlimeTicks() > 0) ? (speedAThreshold + 0.1) : speedAThreshold);
                speedAThreshold = ((data.getIceTicks() > 0 && data.getAboveBlockTicks() > 0) ? (speedAThreshold + 0.24) : speedAThreshold);
                if (BlockUtils.isHalfBlock(p.getLocation().add(0.0, -1.5, 0.0).getBlock()) || BlockUtils.isNearHalfBlock(p) || BlockUtils.isStair(p.getLocation().add(0.0, 1.5, 0.0).getBlock()) || BlockUtils.isNearStair(p) || NEW_Velocity_Utils.didTakeVel(p) || PlayerUtils.wasOnSlime(p)) {
                    return;
                }
                if (speed > speedAThreshold) {
                    verbose += 8;
                } else {
                    verbose = ((verbose > 0) ? (verbose - 1) : 0);
                }
                if (verbose > 38) {
//                    SetBackSystem.setBack(p);
                    verbose = 0;
                    new BukkitRunnable() {
                        public void run() {
                            flag(p, "Horizontal", "Speed: " + speed);
                        }
                    }.runTask((Plugin) Zues.instance);
                }
                data.setSpeedAVerbose(verbose);
            } else {
                data.setSpeedAVerbose(0);
            }
            final Location l = p.getLocation();
            final int x = l.getBlockX();
            final int y = l.getBlockY();
            final int z = l.getBlockZ();
            final Location blockLoc = new Location(p.getWorld(), (double) x, (double) (y - 1), (double) z);
            final Location loc = new Location(p.getWorld(), (double) x, (double) y, (double) z);
            final Location loc2 = new Location(p.getWorld(), (double) x, (double) (y + 1), (double) z);
            final Location above = new Location(p.getWorld(), (double) x, (double) (y + 2), (double) z);
            final Location above2 = new Location(p.getWorld(), (double) (x - 1), (double) (y + 2), (double) (z - 1));
            double MaxAirSpeed = 0.4;
            double maxSpeed = 0.42;
            double MaxSpeedNEW = 0.75;
            if (data.isNearIce()) {
                MaxSpeedNEW = 1.0;
            }
            final double Max = 0.28;
            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                final int level = PlayerUtils.getPotionEffectLevel(p, PotionEffectType.SPEED);
                if (level > 0) {
                    maxSpeed *= level * 20 * 0.011 + 1.0;
                    MaxAirSpeed *= level * 20 * 0.011 + 1.0;
                    maxSpeed *= level * 20 * 0.011 + 1.0;
                    MaxSpeedNEW *= level * 20 * 0.011 + 1.0;
                }
            }
            MaxAirSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
            maxSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
            if (!PlayerUtils.isOnGround4(p) && speed >= MaxAirSpeed && !data.isNearIce() && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && blockLoc.getBlock().getType() != Material.AIR && !NEW_Velocity_Utils.didTakeVel(p) && !BlockUtils.isNearStair(p)) {
                if (!NEW_Velocity_Utils.didTakeVel(p) && PlayerUtils.getDistanceToGround(p) <= 1) {
                    if (data.getSpeed2Verbose() >= 8 || (!VelocityUtils.didTakeVelocity(p) && !NEW_Velocity_Utils.didTakeVel(p) && p.getLocation().add(0.0, 1.94, 0.0).getBlock().getType() != Material.AIR)) {
                        this.flag(p, "Vertical", "Verbose: " + data.getSpeed2Verbose());
                        SetBackSystem.setBack(p);
                    } else {
                        data.setSpeed2Verbose(data.getSpeed2Verbose() + 1);
                    }
                } else {
                    data.setSpeed2Verbose(0);
                }
            }
            final double onGroundDiff = to.getY() - from.getY();
            if (speed > Max && !PlayerUtils.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && e.getTo().getY() != e.getFrom().getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && data.getAboveBlockTicks() != 0) {
                this.flag(p, "Vertical", "" + data.getAboveBlockTicks());
                SetBackSystem.setBack(p);
            }
            if (speed > Max && !PlayerUtils.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && e.getTo().getY() != e.getFrom().getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && !NEW_Velocity_Utils.didTakeVel(p) && !VelocityUtils.didTakeVelocity(p) && !PlayerUtils.hasPistonNear(p) && p.getLocation().getBlock().getType() != Material.PISTON_MOVING_PIECE && p.getLocation().getBlock().getType() != Material.PISTON_BASE && p.getLocation().getBlock().getType() != Material.PISTON_STICKY_BASE && !BlockUtils.isNearPistion(p) && !data.isSpeed_PistonExpand_Set()) {
                if (!data.isSpeed_PistonExpand_Set()) {
                    if (data.getSpeed_C_3_Verbose() > 1) {
                        this.flag(p, "Ground", "" + data.getSpeed_C_3_Verbose());
                        SetBackSystem.setBack(p);
                    } else {
                        data.setSpeed_C_3_Verbose(data.getSpeed_C_3_Verbose() + 1);
                    }
                }
                if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                    final int level2 = PlayerUtils.getPotionEffectLevel(p, PotionEffectType.SPEED);
                    if (level2 > 0) {
                        maxSpeed *= level2 * 20 * 0.011 + 1.0;
                        MaxAirSpeed *= level2 * 20 * 0.011 + 1.0;
                        maxSpeed *= level2 * 20 * 0.011 + 1.0;
                        MaxSpeedNEW *= level2 * 20 * 0.011 + 1.0;
                    }
                }
                MaxAirSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                maxSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                if (!PlayerUtils.isOnGround4(p) && speed >= MaxAirSpeed && !data.isNearIce() && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && blockLoc.getBlock().getType() != Material.AIR && !NEW_Velocity_Utils.didTakeVel(p) && !BlockUtils.isNearStair(p)) {
                    if (!NEW_Velocity_Utils.didTakeVel(p) && PlayerUtils.getDistanceToGround(p) <= 4) {
                        if (data.getSpeed2Verbose() >= 8 || (!VelocityUtils.didTakeVelocity(p) && !NEW_Velocity_Utils.didTakeVel(p) && p.getLocation().add(0.0, 1.94, 0.0).getBlock().getType() != Material.AIR)) {
                            this.flag(p, "Packet", "" + data.getSpeed2Verbose());
                            SetBackSystem.setBack(p);
                        } else {
                            data.setSpeed2Verbose(data.getSpeed2Verbose() + 1);
                        }
                    } else {
                        data.setSpeed2Verbose(0);
                    }
                    boolean speedPot = false;
                    for (final PotionEffect effect : p.getActivePotionEffects()) {
                        if (effect.getType().equals((Object) PotionEffectType.SPEED)) {
                            speedPot = true;
                        }
                    }
                    if (speed > 0.29 && PlayerUtils.isOnGround(p) && !data.isNearIce() && !BlockUtils.isNearStair(p) && !NEW_Velocity_Utils.didTakeVel(p) && !speedPot) {
                        data.getSpeed_OnGround_Verbose();
                        if (speed > Max && !PlayerUtils.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && e.getTo().getY() != e.getFrom().getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && data.getIceTicks() == 0 && !PlayerUtils.hasIceNear(p)) {
                            this.flag(p, "Fall", null);
                            SetBackSystem.setBack(p);
                        }
                    }
                }
            }
        }
    }
}
