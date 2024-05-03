package com.dotnomi.ultimateparticles.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class CommandArgsUtility {
    private CommandArgsUtility() {}

    public static Boolean getArgumentBoolean(@Nonnull String argument, Boolean def) {
        try {
            if (List.of("true","false").contains(argument)) {
                return Boolean.parseBoolean(argument);
            } else {
                return def;
            }
        } catch (Exception ignore) {
            return def;
        }
    }

    public static Integer getArgumentInteger(@Nonnull String argument, Integer def) {
        try {
            return Integer.parseInt(argument);
        } catch (Exception ignore) {
            return def;
        }
    }

    public static Location getArgumentLocation(@Nonnull String argX, @Nonnull String argY, @Nonnull String argZ, Location def) {
        try {
            Double x = getArgumentDouble(argX, null);
            Double y = getArgumentDouble(argY, null);
            Double z = getArgumentDouble(argZ, null);

            if (x != null && y != null && z != null) {
                return new Location(Bukkit.getWorld("world"), x, y, z);
            } else {
                return def;
            }
        } catch (Exception ignore) {
            return def;
        }
    }

    public static Location getArgumentLocation(@Nonnull Player player, @Nonnull String argX, @Nonnull String argY, @Nonnull String argZ, Location def) {
        Location playerLocation = player.getLocation();
        double x = playerLocation.getX();
        double y = playerLocation.getY();
        double z = playerLocation.getZ();

        try {
            if (argX.startsWith("~")) {
                if (argX.length() > 1) {
                    Double tempX = getArgumentDouble(argX.substring(1), null);
                    if (tempX == null) return def;
                    x += tempX;
                }
            } else {
                Double tempX = getArgumentDouble(argX, null);
                if (tempX == null) return def;
                x = tempX;
            }

            if (argY.startsWith("~")) {
                if (argY.length() > 1) {
                    Double tempY = getArgumentDouble(argY.substring(1), null);
                    if (tempY == null) return def;
                    y += tempY;
                }
            } else {
                Double tempY = getArgumentDouble(argY, null);
                if (tempY == null) return def;
                y = tempY;
            }

            if (argZ.startsWith("~")) {
                if (argZ.length() > 1) {
                    Double tempZ = getArgumentDouble(argZ.substring(1), null);
                    if (tempZ == null) return def;
                    z += tempZ;
                }
            } else {
                Double tempZ = getArgumentDouble(argZ, null);
                if (tempZ == null) return def;
                z = tempZ;
            }

            return new Location(playerLocation.getWorld(), x, y, z);
        } catch (Exception ignore) {
            return def;
        }
    }

    public static Double getArgumentDouble(@Nonnull String argument, Double def) {
        try {
            return Double.parseDouble(argument);
        } catch (Exception ignore) {
            return def;
        }
    }
}
