package org.anticheat.zues.data;

import com.google.common.collect.Lists;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public class DataPlayer {

    public Player player;
    public boolean onGround, inLiquid, onStairSlab, onIce, onClimbable, underBlock;
    public int airTicks, groundTicks, iceTicks, liquidTicks, blockTicks;
    public long lastVelocityTaken, lastAttack;
    public LivingEntity lastHitEntity;

    /**
     * NoSlowdown
     **/
    public List<Float> patterns = Lists.newArrayList();
    public float lastRange;

    /**
     * Thresholds
     **/
    public int speedThreshold;

    public DataPlayer(Player player) {
        this.player = player;
    }

}