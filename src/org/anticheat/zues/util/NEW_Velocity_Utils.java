package org.anticheat.zues.util;

import org.anticheat.zues.Zues;
import org.anticheat.zues.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

/**
 * Mr_JaVa_ Helped with this
 */
public class NEW_Velocity_Utils implements Listener {
    public static boolean didTakeVel(Player p) {
        PlayerData data = SetupPlugin.instance.getDataManager().getData(p);
        if (data != null) {
            return data.isLastVelUpdateBoolean();
        } else {
            return false;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = SetupPlugin.instance.getDataManager().getData(p);
        if (data != null) {
            if (data.isLastVelUpdateBoolean()) {
                if (TimerUtils.elapsed(data.getLastVelUpdate(), ConfigValues.VelTimeReset_1_FORCE_RESET)) {
                    //FORCE Reset
                    data.setLastVelUpdateBoolean(false);
                }
                if (TimerUtils.elapsed(data.getLastVelUpdate(), ConfigValues.VelTimeReset_1)) {
                    if (!p.isOnGround()) {
                        data.setLastVelUpdate(TimerUtils.nowlong());
                    } else {
                        data.setLastVelUpdateBoolean(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onVelChange(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        PlayerData data = SetupPlugin.instance.getDataManager().getData(p);
        if (data != null) {
            if (p.getNoDamageTicks() > 0 == false) {
                if (!data.isLastVelUpdateBoolean()) {
                    data.setLastVelUpdateBoolean(true);
                    data.setLastVelUpdate(TimerUtils.nowlong());
                }
            }
        }
    }
}
