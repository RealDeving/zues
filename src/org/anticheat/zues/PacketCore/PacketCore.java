package org.anticheat.zues.PacketCore;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.anticheat.zues.Zues;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.events.PluginEvents.PacketAttackEvent;
import org.anticheat.zues.events.PluginEvents.PacketPlayerEvent;
import org.anticheat.zues.util.SetupPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mr_JaVa_ on 2018-04-08 Package  org.anticheat.zues.PacketCore
 */
public class PacketCore {
    public static Map<UUID, Integer> movePackets;

    public static void init() {
        movePackets = new HashMap<>();

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Zues.instance, PacketType.Play.Server.POSITION) {
            public void onPacketSending(final PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }

                movePackets.put(player.getUniqueId(), movePackets.getOrDefault(player.getUniqueId(), 0) + 1);
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(Zues.instance, PacketType.Play.Client.USE_ENTITY) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        PacketContainer packet = event.getPacket();
                        Player player = event.getPlayer();
                        if (player == null) {
                            return;
                        }

                        EnumWrappers.EntityUseAction type;
                        try {
                            type = packet.getEntityUseActions().read(0);
                        } catch (Exception ex) {
                            return;
                        }

                        Entity entity = event.getPacket().getEntityModifier(player.getWorld()).read(0);

                        if (entity == null) {
                            return;
                        }

                        if (type == EnumWrappers.EntityUseAction.ATTACK) {
                            Bukkit.getServer().getPluginManager().callEvent(new PacketAttackEvent(player, entity, PacketTypes.USE));
                        }
                    }
                });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Zues.instance, PacketType.Play.Client.LOOK) {
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), packetEvent.getPacket().getFloat().read(0), packetEvent.getPacket().getFloat().read(1), PacketTypes.LOOK));

                PlayerData data = SetupPlugin.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Zues.instance, PacketType.Play.Client.POSITION) {
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, packetEvent.getPacket().getDoubles().read(0), packetEvent.getPacket().getDoubles().read(1), packetEvent.getPacket().getDoubles().read(2), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.POSITION));

                PlayerData data = SetupPlugin.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Zues.instance, PacketType.Play.Client.POSITION_LOOK) {
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }

                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, packetEvent.getPacket().getDoubles().read(0), packetEvent.getPacket().getDoubles().read(1), packetEvent.getPacket().getDoubles().read(2), packetEvent.getPacket().getFloat().read(0), packetEvent.getPacket().getFloat().read(1), PacketTypes.POSLOOK));

                PlayerData data = SetupPlugin.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastKillauraPitch(packetEvent.getPacket().getFloat().read(1));
                    data.setLastKillauraYaw(packetEvent.getPacket().getFloat().read(0));
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(Zues.instance, PacketType.Play.Client.FLYING) {
            public void onPacketReceiving(PacketEvent packetEvent) {
                Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketTypes.FLYING));

                PlayerData data = SetupPlugin.instance.getDataManager().getData(player);

                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
    }
}

