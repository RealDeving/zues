package org.anticheat.zues.events;


import org.anticheat.zues.Zues;
import org.anticheat.zues.util.SetupPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UtilityJoinQuitEvent implements Listener {


    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        SetupPlugin.instance.getDataManager().addPlayerData(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerQuitEvent e) {
        SetupPlugin.instance.getDataManager().removePlayerData(e.getPlayer());
    }
}
