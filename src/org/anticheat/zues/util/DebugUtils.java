package org.anticheat.zues.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Mr_JaVa_ on 2018-04-09 Package me.oneaddictions.raven.util
 */
public class DebugUtils {
    public static boolean enabled = true;

    public static void Send(Player p, String string) {
        if (enabled) {
            p.sendMessage(ChatColor.AQUA + "DEBUG: " + ChatColor.GRAY + string);
        }
    }
}
