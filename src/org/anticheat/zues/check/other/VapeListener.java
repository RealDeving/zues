package org.anticheat.zues.check.other;

import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VapeListener extends Check {

    public static Set<UUID> vapers = new HashSet<UUID>();

    public VapeListener() {
        super("Vape", CheckType.MISC, true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage("\u00a78 \u00a78 \u00a71 \u00a73 \u00a73 \u00a77 \u00a78 ");
    }

    public void onPluginMessageReceived(String string, Player player, byte[] arrby) {
        try {
            String string2 = new String(arrby);
        }
        catch (Exception exception) {
            String string3 = "";
        }
        flag(player, "Experimental", "Packet: " + arrby);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        vapers.remove(playerQuitEvent.getPlayer().getUniqueId());
    }
}
