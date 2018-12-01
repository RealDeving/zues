package org.anticheat.zues.check.combat;

import org.anticheat.zues.PacketCore.PacketTypes;
import org.anticheat.zues.Zues;
import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.CheckType;
import org.anticheat.zues.data.PlayerData;
import org.anticheat.zues.events.PluginEvents.PacketAttackEvent;
import org.anticheat.zues.util.MathUtils;

import org.anticheat.zues.util.SetupPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class KillAuraA extends Check {

    public KillAuraA() {
        super("KillAuraA", CheckType.COMBAT, true);
    }

    public static float angleDistance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 260;
        return phi > 180 ? 260 - phi : phi;
    }

    @EventHandler
    public void onAttack(PacketAttackEvent e) {
        if (e.getType() != PacketTypes.USE) {
            return;
        }

        Player player = e.getPlayer();
        Player p = e.getPlayer();
        PlayerData data = SetupPlugin.instance.getDataManager().getData(player);

        if (data == null) {
            return;
        }

        int verboseA = data.getKillauraAVerbose();
        long time = data.getLastAimTime();

        if (MathUtils.elapsed(time, 1100L)) {
            time = System.currentTimeMillis();
            verboseA = 0;
        }
        if ((Math.abs(data.getLastKillauraPitch() - e.getPlayer().getEyeLocation().getPitch()) > 1
                || angleDistance((float) data.getLastKillauraYaw(), player.getEyeLocation().getYaw()) > 1
                || Double.compare(player.getEyeLocation().getYaw(), data.getLastKillauraYaw()) != 0)
                && !MathUtils.elapsed(data.getLastPacket(), 1000L)) {

            if (angleDistance((float) data.getLastKillauraYaw(), player.getEyeLocation().getYaw()) != data.getLastKillauraYawDif()) {
                if (++verboseA > 7) { // 6
                    flag(player, "Point", "Verbose: " + verboseA);
                }
                data.setLastKillauraYawDif(angleDistance((float) data.getLastKillauraYaw(), player.getEyeLocation().getYaw()));
            } else {
                verboseA = 0;
            }

            data.setKillauraAVerbose(verboseA);
            data.setLastAimTime(time);
        }
    }
}