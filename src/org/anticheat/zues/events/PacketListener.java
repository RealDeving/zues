package org.anticheat.zues.events;

import org.anticheat.zues.Zues;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.events.PluginEvents.PacketPlayerEvent;
import org.anticheat.zues.util.SetupPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {
    @EventHandler
    public void onPacketPlayerEvent(PacketPlayerEvent e) {
        Player p = e.getPlayer();
        PlayerData data = SetupPlugin.instance.getDataManager().getData(p);
        if (data != null) {
            if (data.getLastPlayerPacketDiff() > 200) {
                data.setLastDelayedPacket(System.currentTimeMillis());
            }
            data.setLastPlayerPacket(System.currentTimeMillis());
        }
    }
}
