package org.anticheat.zues.check.combat;

import org.anticheat.zues.PacketCore.PacketTypes;
import org.anticheat.zues.Zues;
import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.events.PluginEvents.PacketAttackEvent;
import org.anticheat.zues.util.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Criticals extends Check {

    public Criticals() {
        super("Criticals", CheckType.COMBAT, true);
    }

    public static Map<UUID, Map.Entry<Integer, Long>> CritTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    public static Map<UUID, Double> FallDistance = new HashMap<UUID, Double>();

    public Map<UUID,Long> LastVelocity = new HashMap<>();

    public void Velocity(PlayerVelocityEvent event) {
        this.LastVelocity.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    public Map<UUID, Long> getLastVelocity() {
        return this.LastVelocity;
    }

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (CritTicks.containsKey(uuid)) {
            CritTicks.remove(uuid);
        }
        if (FallDistance.containsKey(uuid)) {
            CritTicks.remove(uuid);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)
                || !e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }

        Player player = (Player) e.getDamager();
        if (player.getAllowFlight()
                || LastVelocity.containsKey(player.getUniqueId())
                || PlayerUtils.slabsNear(player.getLocation())) {
            return;
        }

        Location pL = player.getLocation().clone();
        pL.add(0.0, player.getEyeHeight() + 1.0, 0.0);
        if (PlayerUtils.blocksNear(pL)) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (CritTicks.containsKey(player.getUniqueId())) {
            Count = CritTicks.get(player.getUniqueId()).getKey();
            Time = CritTicks.get(player.getUniqueId()).getValue();
        }
        if (!FallDistance.containsKey(player.getUniqueId())) {
            return;
        }
        double realFallDistance = FallDistance.get(player.getUniqueId());
        Count = player.getFallDistance() > 0.0 && !player.isOnGround() && realFallDistance == 0.0 ? ++Count : 0;
        if (CritTicks.containsKey(player.getUniqueId()) && ServerUtils.elapsed(Time, 10000)) {
            Count = 0;
            Time = ServerUtils.nowlong();
        }
        if (Count >= 2) {
            Count = 0;
            flag(player, "Fall Distance", "Fall Distance: " + realFallDistance);
        }
        CritTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void Move(PlayerMoveEvent e) {
        Player Player2 = e.getPlayer();
        double Falling = 0.0;
        if (!Player2.isOnGround() && e.getFrom().getY() > e.getTo().getY()) {
            if (FallDistance.containsKey(Player2.getUniqueId())) {
                Falling = FallDistance.get(Player2.getUniqueId());
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        FallDistance.put(Player2.getUniqueId(), Falling);
    }
}