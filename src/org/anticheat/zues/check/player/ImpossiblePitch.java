package org.anticheat.zues.check.player;

import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by Mr_JaVa_ on 2018-04-08 Package  org.anticheat.zues.check.player
 */
public class ImpossiblePitch extends Check {
    public ImpossiblePitch() {
        super("Twitch", CheckType.MOVEMENT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        double Pitch = e.getPlayer().getLocation().getPitch();
        if (Pitch > 90 || Pitch < -90) {
            flag(e.getPlayer(), "Derp/Head Roll", "Pitch: " + Pitch);
        }
    }
}
