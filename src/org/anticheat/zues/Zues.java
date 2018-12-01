package org.anticheat.zues;

import org.anticheat.zues.util.ConfigManager;
import org.anticheat.zues.util.ServerUtils;
import org.anticheat.zues.util.SetupPlugin;
import org.anticheat.zues.util.hwid.HWIDChecker;
import org.bukkit.plugin.java.JavaPlugin;

public class Zues extends JavaPlugin {

    public static Zues instance;
    public static String HWIDConfig;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.instance.setup();
        SetupPlugin.instance.setup(this);

        /**
         * Checks if the plugin.yml is valid, and ready to go ;P
         */
        if (!checkPluginYML()) {
            ServerUtils.logPluginError(1, "114MJ^kCbD");
            return;
        }
        /**
         * Same as the plugin.yml shit, but for HWID :3
         */
        HWIDConfig = ConfigManager.settings.getConfiguration().getString("plugin_license");
        if (!new HWIDChecker(this).check(HWIDConfig) || HWIDConfig.equals("")) {
            ServerUtils.logPluginError(0, "2DWbXMHJ$J");
        } else {
            new HWIDChecker(this);
            ServerUtils.logPluginSuccess(0);
        }
    }

    @Override
    public void onDisable() {
        SetupPlugin.instance.saveChecks();
    }

    private Boolean checkPluginYML() {
        if (getDescription().getName().equals("AntiCheat")) {
            if (getDescription().getVersion().equals("0.1")) {
                if (getDescription().getAuthors().contains("Zibb and Rainnny")) {
                    return true;
                }
            }
        }
        return false;
    }
}