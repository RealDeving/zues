package org.anticheat.zues.data;

import org.anticheat.zues.check.Check;
import org.anticheat.zues.check.combat.*;
import org.anticheat.zues.check.movement.*;
import org.anticheat.zues.check.movement.NoSlowdown;
import org.anticheat.zues.check.other.VapeListener;
import org.anticheat.zues.check.player.GroundSpoofCheck;
import org.anticheat.zues.check.player.ImpossiblePitch;
import org.anticheat.zues.check.player.LineOfSight;
import org.anticheat.zues.check.player.MorePackets;
import net.minecraft.util.com.mojang.authlib.UserType;
import org.anticheat.zues.util.SetupPlugin;
import org.bukkit.entity.Player;

import java.util.*;

public class DataManager {

    public List<Check> checks;
    public List<PlayerData> players;
    private Map<Player, Map<Check, Integer>> violations;

    public DataManager() {
        checks = new ArrayList<>();
        violations = new WeakHashMap<>();
        players = new ArrayList<>();
        addChecks();
    }

    private void addChecks() {

        // Combat
        addCheck(new Criticals());
        addCheck(new FastBow());
        addCheck(new KillAuraA());
        addCheck(new Reach());
        addCheck(new Regen());

        // Movement
        addCheck(new Fly());
        addCheck(new Gravity());
        addCheck(new Jesus());
        addCheck(new NoSlowdown());
        addCheck(new Phase());
        addCheck(new Speed());
        addCheck(new Step());
        addCheck(new FastLadder());

        // Player
        addCheck(new GroundSpoofCheck());
        addCheck(new ImpossiblePitch());
        addCheck(new LineOfSight());
        addCheck(new MorePackets());

        // Other
        addCheck(new VapeListener());
    }

    public void removeCheck(Check check) {
        if (checks.contains(check)) checks.remove(check);
    }

    public boolean isCheck(Check check) {
        return checks.contains(check);
    }

    public Check getCheckByName(String checkName) {
        for (Check checkLoop : Collections.synchronizedList(checks)) {
            if (checkLoop.getName().equalsIgnoreCase(checkName)) return checkLoop;
        }

        return null;
    }

    public Map<Player, Map<Check, Integer>> getViolationsMap() {
        return violations;
    }

    public int getViolatonsPlayer(Player player, Check check) {
        if (violations.containsKey(player)) {
            Map<Check, Integer> vlMap = violations.get(player);

            return vlMap.getOrDefault(check, 0);
        }
        return 0;
    }

    public void addViolation(Player player, Check check) {
        if (violations.containsKey(player)) {
            Map<Check, Integer> vlMap = violations.get(player);

            vlMap.put(check, vlMap.getOrDefault(check, 0) + 1);
            violations.put(player, vlMap);
        } else {
            Map<Check, Integer> vlMap = new HashMap<>();

            vlMap.put(check, 1);

            violations.put(player, vlMap);
        }
    }

    public void resetViolation(Player player) {
        if (violations.containsKey(player)) {
            Map<Check, Integer> vlMap = violations.get(player);

            for (Check check : SetupPlugin.instance.getDataManager().getChecks()) {
                vlMap.put(check, 0);
            }
            violations.put(player, vlMap);
        } else {
            Map<Check, Integer> vlMap = new HashMap<>();

            for (Check check : SetupPlugin.instance.getDataManager().getChecks()) {
                vlMap.put(check, 1);
            }
            violations.put(player, vlMap);
        }
    }

    public void addPlayerData(Player player) {
        players.add(new PlayerData(player));
    }

    public PlayerData getData(Player player) {
        for (PlayerData dataLoop : Collections.synchronizedList(players)) {
            if (dataLoop.getPlayer() == player) {
                return dataLoop;
            }
        }
        return null;
    }

    public void removePlayerData(Player player) {
        for (PlayerData dataLoop : Collections.synchronizedList(players)) {
            if (dataLoop.getPlayer() == player) {
                players.remove(dataLoop);
                break;
            }
        }
    }

    public void addCheck(Check check) {
        if (!checks.contains(check)) checks.add(check);
    }

    public List<Check> getChecks() {
        return checks;
    }

    public UserType getUser(UUID uniqueId) {
        // TODO Auto-generated method stub
        return null;
    }

    public DataPlayer getDataPlayer(Player player) {
        // TODO Auto-generated method stub
        return null;
    }
}
