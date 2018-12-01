package org.anticheat.zues.check.movement;

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

import java.util.Map;
import java.util.WeakHashMap;

public class FastLadder extends Check {

    public Map<Player, Integer> count;

    public FastLadder() {
        super("FastLadder", CheckType.MOVEMENT, true);

        count = new WeakHashMap<Player, Integer>();
    }

    @EventHandler
    public void checkFastLadder(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        /** False flag check **/
        if(e.isCancelled()
                || (e.getFrom().getY() == e.getTo().getY())
                || player.getAllowFlight()
                || !PlayerUtils.isOnClimbable(player, 1) ||
                !PlayerUtils.isOnClimbable(player, 0)) {
            return;
        }

        int Count = count.getOrDefault(player, 0);
        double OffsetY = MathUtils.offset(MathUtils.getVerticalVector(e.getFrom().toVector()),
                MathUtils.getVerticalVector(e.getTo().toVector()));
        double Limit = 0.13;

        double updown = e.getTo().getY() - e.getFrom().getY();
        if (updown <= 0) {
            return;
        }


        /** Checks if Y Delta is greater than Limit **/

        if (OffsetY > Limit) {
            Count++;
            ServerUtils.logDebug(null, "[Illegitmate] New Count: " + Count + " (+1); Speed: " + OffsetY + "; Max: " + Limit);
        } else {
            Count = Count > -2 ? Count - 1 : 0;
        }

        long percent = Math.round((OffsetY - Limit) * 120);

        /**If verbose count is greater than 11, flag **/
        if (Count > 11) {
            Count = 0;
            ServerUtils.logDebug(null, "FastLadder; Speed: " + OffsetY + "; Max: " + Limit + "; New Count: " + Count);
            flag(player, "Ladder", percent + "% faster than normal");
        }
        count.put(player, Count);
    }
}