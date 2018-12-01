package org.anticheat.zues.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.anticheat.zues.Zues;
import org.anticheat.zues.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerUtils {

    public static final double PLAYER_WIDTH = .6;
    public static List<Player> noChats = new ArrayList<Player>();
    private static ImmutableSet<Material> ground = Sets.immutableEnumSet(Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK,
            Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH,
            Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT,
            Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS,
            Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL,
            Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
            Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST,
            Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE,
            Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN,
            Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER);

    static {
        String bukkit = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        if (bukkit.contains("1_8") || bukkit.contains("1_9") | bukkit.contains("1_1")) {
            ground = Sets.immutableEnumSet(Material.SUGAR_CANE, Material.SUGAR_CANE_BLOCK,
                    Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH,
                    Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT,
                    Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS,
                    Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL,
                    Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF,
                    Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
                    Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST,
                    Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE,
                    Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN,
                    Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER,
                    Material.getMaterial("ARMOR_STAND"), Material.getMaterial("BANNER"), Material.getMaterial("STANDING_BANNER")
                    , Material.getMaterial("WALL_BANNER"));
        }
    }

    public static boolean onGround2(Player p) {
        if (p.getLocation().getBlock().getType() == Material.AIR) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isOnGround4(Player player) {
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        if (a.getBlock().getType() != Material.AIR) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        return a.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR || isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN),
                new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER});
    }


    public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList();
        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
            for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if (l.distance(loc) <= radius) {
                        blocks.add(l.getBlock());
                    }
                }
            }
        }
        return blocks;
    }

    public static int getDistanceToGround(Player p) {
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid()) break;
            distance++;
        }
        return distance;
    }


    private static boolean isGround(Material material) {
        return ground.contains(material);
    }

    public static boolean isOnGround(Location loc) {
        double diff = .3;

        return !isGround(loc.clone().add(0, -.1, 0).getBlock().getType())
                || !isGround(loc.clone().add(diff, -.1, 0).getBlock().getType())
                || !isGround(loc.clone().add(-diff, -.1, 0).getBlock().getType())
                || !isGround(loc.clone().add(0, -.1, diff).getBlock().getType())
                || !isGround(loc.clone().add(0, -.1, -diff).getBlock().getType())
                || !isGround(loc.clone().add(diff, -.1, diff).getBlock().getType())
                || !isGround(loc.clone().add(diff, -.1, -diff).getBlock().getType())
                || !isGround(loc.clone().add(-diff, -.1, diff).getBlock().getType())
                || !isGround(loc.clone().add(-diff, -.1, -diff).getBlock().getType())
                || (BlockUtils.getBlockHeight(loc.clone().subtract(0.0D, 0.5D, 0.0D).getBlock()) != 0.0D &&
                (!isGround(loc.clone().add(diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, 0).getBlock().getType())
                        || !isGround(loc.clone().add(-diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, 0).getBlock().getType())
                        || !isGround(loc.clone().add(0, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType())
                        || !isGround(loc.clone().add(0, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())
                        || !isGround(loc.clone().add(diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType())
                        || !isGround(loc.clone().add(diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())
                        || !isGround(loc.clone().add(-diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType())
                        || !isGround(loc.clone().add(-diff, BlockUtils.getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())));
    }


    public static boolean isOnGround(Player player) {
        Object box = ServerUtils.getBoundingBox(player);
        Object outcome = ServerUtils.getMethodValue(ServerUtils.getMethod(box.getClass(), "grow", double.class, double.class, double.class), box, 0D, 0.1D, 0D);
        return ServerUtils.inBlock(player, outcome);
    }

    public static boolean hasPistonNear(Player player) {
        Object box = ServerUtils.getMethodValue(ServerUtils.getMethod(ServerUtils.getBoundingBox(player).getClass(), "grow", double.class, double.class, double.class), ServerUtils.getBoundingBox(player), 2D, 3D, 2D);

        Collection<?> collidingBlocks = ServerUtils.getCollidingBlocks(player, box);

        for (Object object : collidingBlocks) {
            double x = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "a"), object);
            double y = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "b"), object);
            double z = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "c"), object);

            Block block = new Location(player.getWorld(), x, y, z).getBlock();
            if (BlockUtils.isPiston(block)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasIceNear(Player player) {
        Object box = ServerUtils.getMethodValue(ServerUtils.getMethod(ServerUtils.getBoundingBox(player).getClass(), "grow", double.class, double.class, double.class), ServerUtils.getBoundingBox(player), 0D, 1.5D, 0D);

        Collection<?> collidingBlocks = ServerUtils.getCollidingBlocks(player, box);

        for (Object object : collidingBlocks) {
            double x = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "a"), object);
            double y = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "b"), object);
            double z = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "c"), object);

            Block block = new Location(player.getWorld(), x, y, z).getBlock();

            if (BlockUtils.isIce(block)) {
                return true;
            }
        }
        return false;
    }

    public static boolean wasOnSlime(Player player) {
        PlayerData user = SetupPlugin.instance.getDataManager().getData(player);

        if (user != null
                && user.getSetbackLocation() != null) {
            Location location = user.getSetbackLocation().clone().subtract(0.0D, 1.0D, 0.0D);

            if (location.getBlock().getTypeId() == 165) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasSoulsandNear(Player player) {
        Object box = ServerUtils.getMethodValue(ServerUtils.getMethod(ServerUtils.getBoundingBox(player).getClass(), "grow", double.class, double.class, double.class), ServerUtils.getBoundingBox(player), -0.3D, 1D, -0.3D);

        Collection<?> collidingBlocks = ServerUtils.getCollidingBlocks(player, box);

        for (Object object : collidingBlocks) {
            double x = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "a"), object);
            double y = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "b"), object);
            double z = (double) ServerUtils.getFieldValue(ServerUtils.getFieldByName(object.getClass(), "c"), object);

            Block block = new Location(player.getWorld(), x, y, z).getBlock();

            if (block.getType().equals(Material.SOUL_SAND)) {
                return true;
            }
        }
        return false;
    }


    public static boolean blocksNear(Player player) {
        Object box = ServerUtils.getMethodValue(ServerUtils.getMethod(ServerUtils.getBoundingBox(player).getClass(), "grow", double.class, double.class, double.class), ServerUtils.getBoundingBox(player), 0.2D, 0D, 0.2D);

        return ServerUtils.inBlock(player, box);
    }

    public static boolean isOnGround3(Player player) {
        Object box = ServerUtils.getBoundingBox(player);
        Object outcome = ServerUtils.getMethodValue(ServerUtils.getMethod(box.getClass(), "grow", double.class, double.class, double.class), box, 0D, 0.3D, 0D);
        return ServerUtils.inBlock(player, outcome);
    }

    public static Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public static double P(double Yaw1, double Yaw2) {
        Scanner in = new Scanner(System.in);
        double perc_value = Yaw2;
        double numb_value = Yaw1;
        double rslt_value = 0;
        rslt_value = (perc_value * numb_value) / 100;
        rslt_value = (1000 * rslt_value) / 1000;
        return Double.parseDouble(String.valueOf(rslt_value));
    }

    public static String getDirection(Player player) {
        int degrees = (Math.round(player.getLocation().getYaw()) + 270) % 360;
        if (degrees <= 22) return "North";
        if (degrees <= 67) return "North-east";
        if (degrees <= 112) return "East";
        if (degrees <= 157) return "South-east";
        if (degrees <= 202) return "South";
        if (degrees <= 247) return "South-west";
        if (degrees <= 292) return "West";
        if (degrees <= 337) return "North-west";
        if (degrees <= 359) return "North";
        return null;
    }

    public static boolean isSlab(Block block) {
    	Material type = block.getType();
    	switch (type) {
    	case STEP:
    	case WOOD_STEP:
    		return true;
    	}
    	return false;
    }
    
    public static boolean isClimbableBlock(Block block) {
    	return block.getType() == Material.VINE || (block.getType() == Material.LADDER)
    			|| (block.getType() == Material.WATER) || (block.getType() == Material.STATIONARY_WATER);
    }
    
    public static boolean isStair(Block block) {
    	Material type = block.getType();
    	switch (type) {
    	case COMMAND:
        case COBBLESTONE_STAIRS:
        case BRICK_STAIRS:
        case ACACIA_STAIRS:
        case BIRCH_WOOD_STAIRS:
        case DARK_OAK_STAIRS:
        case JUNGLE_WOOD_STAIRS:
        case NETHER_BRICK_STAIRS:
        case QUARTZ_STAIRS:
        case SANDSTONE_STAIRS:
        case SMOOTH_STAIRS:
        case SPRUCE_WOOD_STAIRS:
        case WOOD_STAIRS:
            return true;
        }
        return false;
    }

    public static boolean cantStandAtWater(Block block) {
        Block otherBlock = block.getRelative(BlockFace.DOWN);

        boolean isHover = block.getType() == Material.AIR;
        boolean n = (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER);
        boolean s = (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER);
        boolean e = (otherBlock.getRelative(BlockFace.EAST).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER);
        boolean w = (otherBlock.getRelative(BlockFace.WEST).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER);
        boolean ne = (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER);
        boolean nw = (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER);
        boolean se = (otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER);
        boolean sw = (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER)
                || (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER);

        return (n) && (s) && (e) && (w) && (ne) && (nw) && (se) && (sw) && (isHover);
    }

    public static boolean cantStandAtLava(Block block) {
        Block otherBlock = block.getRelative(BlockFace.DOWN);

        boolean isHover = block.getType() == Material.AIR;
        boolean n = (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_LAVA);
        boolean s = (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_LAVA);
        boolean e = (otherBlock.getRelative(BlockFace.EAST).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_LAVA);
        boolean w = (otherBlock.getRelative(BlockFace.WEST).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_LAVA);
        boolean ne = (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_LAVA);
        boolean nw = (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_LAVA);
        boolean se = (otherBlock.getRelative(BlockFace.SOUTH_EAST).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_LAVA);
        boolean sw = (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.LAVA)
                || (otherBlock.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_LAVA);

        return (n) && (s) && (e) && (w) && (ne) && (nw) && (se) && (sw) && (isHover);
    }

    public static boolean isInWater(Location loc) {
        double diff = .3;
        return BlockUtils.isLiquid(loc.clone().add(0, 0D, 0).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(diff, 0D, 0).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(-diff, 0D, 0).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(0, 0D, diff).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(0, 0D, -diff).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(diff, 0D, diff).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(diff, 0D, -diff).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(-diff, 0D, diff).getBlock())
                || BlockUtils.isLiquid(loc.clone().add(-diff, 0D, -diff).getBlock())
                || (BlockUtils.getBlockHeight(loc.clone().subtract(0.0D, 0.5D, 0.0D).getBlock()) != 0.0D &&
                (BlockUtils.isLiquid(loc.clone().add(diff, 0D, 0).getBlock())
                        || BlockUtils.isLiquid(loc.clone().add(-diff, 0D, 0).getBlock())
                        || BlockUtils.isLiquid(loc.clone().add(0, 0D, diff).getBlock())
                        || BlockUtils.isLiquid(loc.clone().add(0, 0D, -diff).getBlock())
                        || BlockUtils.isLiquid(loc.clone().add(diff, 0D, diff).getBlock())
                        || BlockUtils.isLiquid(loc.clone().add(diff, 0D, -diff).getBlock())
                        || BlockUtils.isLiquid(loc.clone().add(-diff, 0D, diff).getBlock())
                        || BlockUtils.isLiquid(loc.clone().add(-diff, 0D, -diff).getBlock())));
    }

    public static boolean isOnSlab(Location loc) {
        double diff = 0.3;
        return BlockUtils.isSlab(loc.clone().add(0, 0D, 0).getBlock())
                || BlockUtils.isSlab(loc.clone().add(diff, 0D, 0).getBlock())
                || BlockUtils.isSlab(loc.clone().add(-diff, 0D, 0).getBlock())
                || BlockUtils.isSlab(loc.clone().add(0, 0D, diff).getBlock())
                || BlockUtils.isSlab(loc.clone().add(0, 0D, -diff).getBlock())
                || BlockUtils.isSlab(loc.clone().add(diff, 0D, diff).getBlock())
                || BlockUtils.isSlab(loc.clone().add(diff, 0D, -diff).getBlock())
                || BlockUtils.isSlab(loc.clone().add(-diff, 0D, diff).getBlock())
                || BlockUtils.isSlab(loc.clone().add(-diff, 0D, -diff).getBlock());
    }

    public static boolean isOnStair(Location loc) {
        double diff = 0.3;
        return BlockUtils.isStair(loc.clone().add(0, 0D, 0).getBlock())
                || BlockUtils.isStair(loc.clone().add(diff, 0D, 0).getBlock())
                || BlockUtils.isStair(loc.clone().add(-diff, 0D, 0).getBlock())
                || BlockUtils.isStair(loc.clone().add(0, 0D, diff).getBlock())
                || BlockUtils.isStair(loc.clone().add(0, 0D, -diff).getBlock())
                || BlockUtils.isStair(loc.clone().add(diff, 0D, diff).getBlock())
                || BlockUtils.isStair(loc.clone().add(diff, 0D, -diff).getBlock())
                || BlockUtils.isStair(loc.clone().add(-diff, 0D, diff).getBlock())
                || BlockUtils.isStair(loc.clone().add(-diff, 0D, -diff).getBlock());
    }

    public static boolean isInsideCauldron(Player player) {
        return player.getLocation().getBlock().getType().equals(Material.CAULDRON);
    }

    public static boolean isOnLilyPad(Player player) {
        Block block = player.getLocation().getBlock();
        Material lily = Material.WATER_LILY;

        return (block.getType() == lily) || (block.getRelative(BlockFace.NORTH).getType() == lily)
                || (block.getRelative(BlockFace.SOUTH).getType() == lily)
                || (block.getRelative(BlockFace.EAST).getType() == lily)
                || (block.getRelative(BlockFace.WEST).getType() == lily);
    }


    public static boolean isHoveringOverWater(Location player, int blocks) {
        for (int i = player.getBlockY(); i > player.getBlockY() - blocks; i--) {
            Block newloc = new Location(player.getWorld(), player.getBlockX(), i, player.getBlockZ()).getBlock();
            if (newloc.getType() != Material.AIR) {
                return newloc.isLiquid();
            }
        }
        return false;
    }

    public static boolean isHoveringOverWater(Location player) {
        return isHoveringOverWater(player, 25);
    }

    public static boolean isFullyInWater(Location player) {
        return player.getBlock().isLiquid() && player.clone().add(0.0D, 1.0D, 0.0D).getBlock().isLiquid();
    }

    public static boolean isCompletelyInWater(Location player) {
        return player.getBlock().isLiquid() && player.clone().add(0.0D, 1.35D, 0.0D).getBlock().isLiquid();
    }

    public static boolean hasSlabsNear(Location location) {
        for (Block block : BlockUtils.getSurroundingXZ(location.getBlock(), true)) {
            if (BlockUtils.isSlab(block)) {
                return true;
            }
        }
        return false;
    }


    public static boolean hasBlocksNear(Player player) {
        for (Block block : BlockUtils.getSurroundingXZ(player.getLocation().getBlock())) {
            if (block.getType().isSolid()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnCactus(Player player) {
        return player.getLocation().clone().subtract(0D, 0.8D, 0D).getBlock().getType().equals(Material.CACTUS);
    }

    public static boolean hasBlocksNear(Location location) {
        for (Block block : BlockUtils.getSurroundingXZ(location.getBlock())) {
            if (block.getType().isSolid()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnClimbable(Player player, int blocks) {
        if (blocks == 0) {
            for (Block block : getSurrounding(player.getLocation().getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        } else {
            for (Block block : getSurrounding(player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock(),
                    false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        return player.getLocation().getBlock().getType() == Material.LADDER
                || player.getLocation().getBlock().getType() == Material.VINE;
    }

    public static boolean isInWeb(Player player) {
        if (player.getLocation().getBlock().getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.WEB && player.getLocation().getBlock().getRelative(BlockFace.UP).getType() != Material.WEB) {
            return false;
        }
        return true;
    }

    public static ArrayList<Block> getSurrounding(Block block, boolean diagonals) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if ((x != 0) || (y != 0) || (z != 0)) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        } else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }

    public static ArrayList<Block> getSurroundingC(Block block, boolean diagonals) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        if ((x != 0) || (y != 0) || (z != 0)) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        } else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }

    public static boolean hasPotionEffect(Player player, PotionEffectType type) {
        if (player.getActivePotionEffects().size() > 0) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType() == type) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInWater(Player player) {
        Material m = player.getLocation().getBlock().getType();
        return m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.LAVA || m == Material.STATIONARY_LAVA;
    }

    public static boolean hasWaterAround(Player player) {
        for (Block block : BlockUtils.getSurroundingXZ(player.getLocation().getBlock())) {
            Material m = block.getType();
            return m == Material.STATIONARY_WATER || m == Material.WATER || m == Material.LAVA || m == Material.STATIONARY_LAVA;
        }
        return false;
    }

    public static boolean hasChestNear(Player player) {
        for (Block block : BlockUtils.getSurroundingXZ(player.getLocation().clone().subtract(0.0, 0.8, 0.0D).getBlock())) {
            if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) {
                return true;
            }
        }
        for (Block block : BlockUtils.getSurroundingXZ(player.getLocation().clone().subtract(0.0, 2.0, 0.0D).getBlock())) {
            if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST)) {
                return true;
            }
        }
        return false;
    }

    public static Location getEyeLocation(final Player player) {
        final Location eye = player.getLocation();
        eye.setY(eye.getY() + player.getEyeHeight());
        return eye;
    }

    public static double offset(Vector a, Vector b) {
        return a.subtract(b).length();
    }

    public static boolean isReallyOnground(Player p) {
        Location l = p.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Location b = new Location(p.getWorld(), x, y - 1, z);

        if (p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB
                && !b.getBlock().isLiquid()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasDepthStrider(Player player) {
        if (player.getInventory().getBoots() != null
                && player.getInventory().getBoots().getEnchantmentLevel(Enchantment.getByName("DEPTH_STRIDER")) > 0) {
            return true;
        }
        return false;
    }

    public static boolean isOnGroundVanilla(Player p) {
        Location l = p.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Location b = new Location(p.getWorld(), x, y - 0.3, z);

        if (p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB
                && !b.getBlock().isLiquid()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean flaggyStuffNear(Location loc) {
        boolean nearBlocks = false;
        for (Block bl : getSurrounding(loc.getBlock(), true)) {
            if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
                    || (bl.getType().equals(Material.BED)) || (bl.getType().equals(Material.WOOD_DOUBLE_STEP))
                    || (bl.getType().equals(Material.WOOD_STEP))) {
                nearBlocks = true;
                break;
            }
        }
        for (Block bl : getSurrounding(loc.getBlock(), false)) {
            if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
                    || (bl.getType().equals(Material.BED)) || (bl.getType().equals(Material.WOOD_DOUBLE_STEP))
                    || (bl.getType().equals(Material.WOOD_STEP))) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.STEP, Material.BED,
                Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP})) {
            nearBlocks = true;
        }
        return nearBlocks;
    }

    public static boolean isBlock(Block block, Material[] materials) {
        Material type = block.getType();
        Material[] arrayOfMaterial;
        int j = (arrayOfMaterial = materials).length;
        for (int i = 0; i < j; i++) {
            Material m = arrayOfMaterial[i];
            if (m == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAir(final Player player) {
        final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        return b.getType().equals((Object) Material.AIR)
                && b.getRelative(BlockFace.WEST).getType().equals((Object) Material.AIR)
                && b.getRelative(BlockFace.NORTH).getType().equals((Object) Material.AIR)
                && b.getRelative(BlockFace.EAST).getType().equals((Object) Material.AIR)
                && b.getRelative(BlockFace.SOUTH).getType().equals((Object) Material.AIR);
    }

    private static Vector getRotation(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(dy, distanceXZ) * 180.0D / 3.141592653589793D);
        return new Vector(yaw, pitch, 0.0F);
    }

    private static double clamp180(double dub) {
        dub %= 360.0D;
        if (dub >= 180.0D) {
            dub -= 360.0D;
        }
        if (dub < -180.0D) {
            dub += 360.0D;
        }
        return dub;
    }

    private static double getDistance3D(Location one, Location two) {
        double toReturn = 0.0D;
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    private static double[] getOffsetsOffCursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0D, entity.getEyeHeight(), 0.0D);
        Location playerLoc = player.getLocation().add(0.0D, player.getEyeHeight(), 0.0D);

        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0F);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);

        double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = clamp180(playerRotation.getY() - expectedRotation.getY());

        double horizontalDistance = MathUtils.getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);

        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;

        return new double[]{Math.abs(offsetX), Math.abs(offsetY)};
    }

    public static double getOffsetOffCursor(Player player, LivingEntity entity) {
        double offset = 0.0D;
        double[] offsets = getOffsetsOffCursor(player, entity);

        offset += offsets[0];
        offset += offsets[1];

        return offset;
    }

    public static boolean isGliding(Player p) {
        if (!ServerUtils.isBukkitVerison("1_9") && !ServerUtils.isBukkitVerison("1_1")) {
            return false;
        }

        boolean isGliding = false;
        try {
            isGliding = (boolean) p.getClass().getMethod("isGliding").invoke(p);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isGliding;

    }

    public static int getPotionEffectLevel(Player player, PotionEffectType pet) {
        for (PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }

    public static final Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public static boolean slabsNear(Location loc) {
        boolean nearBlocks = false;
        for (Block bl : getSurrounding(loc.getBlock(), true)) {
            if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
                    || (bl.getType().equals(Material.WOOD_DOUBLE_STEP)) || (bl.getType().equals(Material.WOOD_STEP))) {
                nearBlocks = true;
                break;
            }
        }
        for (Block bl : getSurrounding(loc.getBlock(), false)) {
            if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
                    || (bl.getType().equals(Material.WOOD_DOUBLE_STEP)) || (bl.getType().equals(Material.WOOD_STEP))) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.STEP, Material.DOUBLE_STEP,
                Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }

    public static boolean blocksNear(final Location loc) {
        boolean nearBlocks = false;
        for (Block block : getSurrounding(loc.getBlock(), true)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        for (final Block block : getSurrounding(loc.getBlock(), false)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        loc.setY(loc.getY() - 0.5);
        if (loc.getBlock().getType() != Material.AIR) {
            nearBlocks = true;
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN),
                new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
}
