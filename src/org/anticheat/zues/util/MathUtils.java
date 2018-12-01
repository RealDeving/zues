package org.anticheat.zues.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Base64;

public class MathUtils {

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static long elapsed(long starttime) {
        return System.currentTimeMillis() - starttime;
    }

    public static double trim(int degree, double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = String.valueOf(format) + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String decrypt(String strEncrypted) {
        String strData = "";

        try {
            byte[] decoded = Base64.getDecoder().decode(strEncrypted);
            strData = (new String(decoded, "UTF-8") + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    public static double offset(final Vector one, final Vector two) {
        return one.subtract(two).length();
    }

    public static double getHorizontalDistance(Location to, Location from) {
        double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));

        return Math.sqrt(x * x + z * z);
    }

    public static float[] getRotations(Location one, Location two) {
        double diffX = two.getX() - one.getX();
        double diffZ = two.getZ() - one.getZ();
        double diffY = (two.getY() + 2.0D - 0.4D) - (
                one.getY() + 2.0D);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

        return new float[]{yaw, pitch};
    }

    public static float getYawChangeToEntity(Player player, Entity entity, Location from, Location to) {
        double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        double yawToEntity;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
                yawToEntity = -90.0D +
                        Math.toDegrees(Math.atan(deltaZ / deltaX));
            } else {
                yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
            }
        }
        return Float.valueOf((float) (-(getYawDifference(from, to)) - yawToEntity));
    }

    public static float getPitchChangeToEntity(Player player, Entity entity, Location from, Location to) {
        double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        double deltaY = player.getLocation().getY() - 1.6D + 2 - 0.4D -
                entity.getLocation().getY();
        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ *
                deltaZ);

        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

        return yawTo180F((float) (-(getYawDifference(from, to) -
                (float) pitchToEntity)));
    }

    public static Vector getVerticalVector(final Vector v) {
        v.setX(0);
        v.setZ(0);
        return v;
    }

    public static double[] cursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);
        double deltaYaw = yawTo180D(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = yawTo180D(playerRotation.getY() - expectedRotation.getY());
        double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);
        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[]{Math.abs(offsetX), Math.abs(offsetY)};
    }

    public static double fixedXAxis(double x) {
        double touchedX = x;
        double rem = touchedX - Math.round(touchedX) + 0.01D;
        if (rem < 0.3D) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }

    public static Vector calculateAngle(Vector player, Vector otherPlayer) {
        Vector delta = new Vector((player.getX() - otherPlayer.getX()), (player.getY() - otherPlayer.getY()), (player.getZ() - otherPlayer.getZ()));
        double hyp = Math.sqrt(delta.getX() * delta.getX() + delta.getY() * delta.getY());

        Vector angle = new Vector(Math.atan((delta.getZ() + 64.06) / hyp) * 57.295779513082, Math.atan((delta.getY() / delta.getX())) * 57.295779513082, 0.0D);
        if (delta.getX() >= 0.0) {
            angle.setY(angle.getY() + 180.0);
        }
        return angle;
    }

    public static double getDistance3D(Location one, Location two) {
        double toReturn = 0.0;
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    public static Vector getRotation(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793);
        return new Vector(yaw, pitch, 0.0f);
    }

    public static float[] getAngles(Player player, Entity e, Location from, Location to) {
        return new float[]{(float) (getYawChangeToEntity(player, e, from, to) + getYawDifference(from, to)), (float) (getPitchChangeToEntity(player, e, from, to) + getYawDifference(from, to))};
    }

    public static double getYawDifference(Location one, Location two) {
        return Math.abs(one.getYaw() - two.getYaw());
    }

    public static double getVerticalDistance(Location to, Location from) {
        double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));

        return Math.sqrt(y * y);
    }

    public static double getYDifference(Location to, Location from) {
        return Math.abs(to.getY() - from.getY());
    }

    public static Vector getHorizontalVector(Location loc) {
        Vector vec = loc.toVector();
        vec.setY(0);
        return vec;
    }

    public static Vector getVerticalVector(Location loc) {
        Vector vec = loc.toVector();
        vec.setX(0);
        vec.setZ(0);
        return vec;
    }

    public static float[] getRotation2(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        dy /= distanceXZ;
        float yaw = (float) (-(Math.atan2(dx, dz) * 57.29577951308232));
        float pitch = (float) (-(Math.asin(dy) * 57.29577951308232));
        return new float[]{yaw, pitch};
    }

    public static float yawTo180F(float flub) {
        flub %= 360.0F;
        if (flub >= 180.0F) {
            flub -= 360.0F;
        }
        if (flub < -180.0F) {
            flub += 360.0F;
        }
        return flub;
    }

    public static double yawTo180D(double dub) {
        dub %= 360.0D;
        if (dub >= 180.0D) {
            dub -= 360.0D;
        }
        if (dub < -180.0D) {
            dub += 360.0D;
        }
        return dub;
    }

    public static double[] getOffsetFromEntity(Player player, LivingEntity entity) {
        double yawOffset = Math.abs(MathUtils.yawTo180F(player.getEyeLocation().getYaw()) - MathUtils.yawTo180F(MathUtils.getRotations(player.getLocation(), entity.getLocation())[0]));
        double pitchOffset = Math.abs(Math.abs(player.getEyeLocation().getPitch()) - Math.abs(MathUtils.getRotations(player.getLocation(), entity.getLocation())[1]));
        return new double[]{yawOffset, pitchOffset};
    }

    public static Vector getHorizontalVector(final Vector v) {
        v.setY(0);
        return v;
    }
}
