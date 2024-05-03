package com.dotnomi.ultimateparticles.command;

import com.dotnomi.ultimateparticles.constants.Config;
import com.dotnomi.ultimateparticles.constants.Messages;
import com.dotnomi.ultimateparticles.util.CommandArgsUtility;
import com.dotnomi.ultimateparticles.util.CommandUtility;
import com.dotnomi.ultimateparticles.util.MessageUtility;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class UltimateParticlesCommandHandler {
    private UltimateParticlesCommandHandler() {}

    public static void handleReloadCommand(
            @Nonnull CommandSender sender, @Nonnull Integer argLength, @Nonnull String mainArg) {
        if (argLength >= 1 && !mainArg.equalsIgnoreCase("reload")) return;
        if (argLength == 1) {
            if (sender instanceof Player player) {
                if (player.hasPermission(Config.PERMISSION_RELOAD)) {
                    CommandUtility.reload();
                    player.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_RELOAD, true));
                } else {
                    player.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_NO_PERMISSION, true));
                }
            } else {
                CommandUtility.reload();
                sender.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_RELOAD, true));
            }
        } else {
            sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RELOAD_CMD_USEAGE, true));
        }
    }

    public static void handleCacheCommand(@Nonnull CommandSender sender, @Nonnull Integer argLength, @Nonnull String[] args) {
        if (argLength >= 1 && !args[0].equalsIgnoreCase("cache")) return;
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_NOT_A_PLAYER, true));
            return;
        }
        if (!sender.hasPermission(Config.PERMISSION_CACHE)) {
            sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_NO_PERMISSION, true));
            return;
        }
        if (argLength == 2) {
            String function = args[1];
            if (function.equalsIgnoreCase("generate")) {
                CommandUtility.generateCache(sender, false);
            } else if (function.equalsIgnoreCase("remove")) {
                CommandUtility.removeCache(sender);
            } else {
                sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_CACHE_CMD_USEAGE, true));
            }
        } else if (argLength == 3) {
            String function = args[1];
            Boolean force = CommandArgsUtility.getArgumentBoolean(args[2], null);
            if (force == null) {
                sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_CACHE_CMD_USEAGE, true));
            } else if (function.equalsIgnoreCase("generate")) {
                CommandUtility.generateCache(sender, force);
            } else {
                sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_CACHE_CMD_USEAGE, true));
            }
        } else {
            sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_CACHE_CMD_USEAGE, true));
        }
    }

    public static void handleRenderCommand(@Nonnull CommandSender sender, @Nonnull Integer argLength, @Nonnull String[] args) {
        if (argLength >= 1 && !args[0].equalsIgnoreCase("render")) return;
        if (sender instanceof Player player) {
            if (!sender.hasPermission(Config.PERMISSION_RENDER)) {
                sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_NO_PERMISSION, true));
                return;
            }

            if (argLength == 2) {
                CommandUtility.render(player, args[1]);
            } else if (argLength == 3) {
                Integer duration = CommandArgsUtility.getArgumentInteger(args[2], null);
                if (duration == null) {
                    sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RENDER_CMD_USEAGE, true));
                } else {
                    CommandUtility.render(player, args[1], duration);
                }
            } else if (argLength == 6) {
                Integer duration = CommandArgsUtility.getArgumentInteger(args[2], null);
                Location location = CommandArgsUtility.getArgumentLocation(player, args[3], args[4], args[5], null);

                if (duration == null || location == null) {
                    sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RENDER_CMD_USEAGE, true));
                } else {
                    CommandUtility.render(player, args[1], duration, location);
                }
            } else if (argLength == 9) {
                Integer duration = CommandArgsUtility.getArgumentInteger(args[2], null);
                Location location = CommandArgsUtility.getArgumentLocation(player, args[3], args[4], args[5], null);
                Location rotation = CommandArgsUtility.getArgumentLocation(player, args[6], args[7], args[8], null);

                if (duration == null || location == null || rotation == null) {
                    sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RENDER_CMD_USEAGE, true));
                } else {
                    CommandUtility.render(player, args[1], duration, location, rotation);
                }
            } else {
                sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RENDER_CMD_USEAGE, true));
            }
        } else {
            sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_NOT_A_PLAYER, true));

            /*if (argLength == 6) {
                Integer duration = CommandArgsUtility.getArgumentInteger(args[2], null);
                Location location = CommandArgsUtility.getArgumentLocation(args[3], args[4], args[5], null);

                if (duration == null || location == null) {
                    sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RENDER_CMD_USEAGE, true));
                } else {
                    CommandUtility.render(sender, args[1], duration, location);
                }
            } else if (argLength == 9) {
                Integer duration = CommandArgsUtility.getArgumentInteger(args[2], null);
                Location location = CommandArgsUtility.getArgumentLocation(args[3], args[4], args[5], null);
                Location rotation = CommandArgsUtility.getArgumentLocation(args[6], args[7], args[8], null);

                if (duration == null || location == null || rotation == null) {
                    sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RENDER_CMD_USEAGE, true));
                } else {
                    CommandUtility.render(sender, args[1], duration, location, rotation);
                }
            } else {
                sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_WRONG_RENDER_CMD_USEAGE, true));
            }*/
        }
    }
}
