package com.dotnomi.ultimateparticles.util;

import com.dotnomi.ultimateparticles.constants.Messages;
import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

public final class PlayerUtility {
    public static String getColoredMessage(@Nonnull String message, boolean withPrefix) {
        if (withPrefix) {
            return getColoredMessage(Messages.GENERAL_PREFIX + message);
        } else {
            return getColoredMessage(message);
        }
    }

    public static String getColoredMessage(@Nonnull String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
