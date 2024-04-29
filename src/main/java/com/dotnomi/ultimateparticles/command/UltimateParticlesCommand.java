package com.dotnomi.ultimateparticles.command;

import com.dotnomi.ultimateparticles.files.ImageManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class UltimateParticlesCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    UltimateParticlesCommandHandler.reload(player);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("cache")) {
                    if (args[1].equalsIgnoreCase("generate")) {
                        UltimateParticlesCommandHandler.generateCache(player, false);
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        UltimateParticlesCommandHandler.removeCache(player);
                    }
                } else if (args[0].equalsIgnoreCase("render")) {
                    UltimateParticlesCommandHandler.render(player, args[1]);
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("cache")) {
                    if (args[1].equalsIgnoreCase("generate") && args[2].equalsIgnoreCase("force-generation")) {
                        UltimateParticlesCommandHandler.generateCache(player, true);
                    }
                }
            } else if (args.length == 5) {
                if (args[0].equalsIgnoreCase("render")) {
                    Location position = new Location(player.getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));

                    UltimateParticlesCommandHandler.render(player, args[1], position);
                }
            } else if (args.length == 8) {
                if (args[0].equalsIgnoreCase("render")) {
                    Location position = new Location(player.getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    Location rotation = new Location(player.getWorld(), Double.parseDouble(args[5]), Double.parseDouble(args[6]), Double.parseDouble(args[7]));

                    UltimateParticlesCommandHandler.render(player, args[1], position, rotation);
                }
            } else if (args.length == 9) {
                if (args[0].equalsIgnoreCase("render")) {
                    Location position = new Location(player.getWorld(), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                    Location rotation = new Location(player.getWorld(), Double.parseDouble(args[5]), Double.parseDouble(args[6]), Double.parseDouble(args[7]));

                    UltimateParticlesCommandHandler.render(player, args[1], position, rotation, Integer.parseInt(args[8]));
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                return List.of("cache", "render", "reload");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("render")) return ImageManager.getInstance().getImageNames();
                return List.of("generate", "remove");
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("render")) return List.of("" + (int) player.getLocation().getX());
                if (args[1].equalsIgnoreCase("generate")) return List.of("force-generation");
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("render")) return List.of("" + (int) player.getLocation().getY());
            } else if (args.length == 5) {
                if (args[0].equalsIgnoreCase("render")) return List.of("" + (int) player.getLocation().getZ());
            } else if (args.length == 6) {
                if (args[0].equalsIgnoreCase("render")) return List.of("0", "90", "180", "270");
            } else if (args.length == 7) {
                if (args[0].equalsIgnoreCase("render")) return List.of("0", "90", "180", "270");
            } else if (args.length == 8) {
                if (args[0].equalsIgnoreCase("render")) return List.of("0", "90", "180", "270");
            } else if (args.length == 9) {
                if (args[0].equalsIgnoreCase("render")) return List.of("1");
            }
        }

        return new ArrayList<>();
    }
}
