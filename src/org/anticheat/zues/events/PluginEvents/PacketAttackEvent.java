package org.anticheat.zues.events.PluginEvents;

import org.anticheat.zues.PacketCore.PacketTypes;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketAttackEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Entity entity;
    private PacketTypes type;

    public PacketAttackEvent(Player player, Entity entity, PacketTypes type) {
        this.player = player;
        this.entity = entity;
        this.type = type;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Entity getEntity() {
        return entity;
    }

    public PacketTypes getType() {
        return type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
