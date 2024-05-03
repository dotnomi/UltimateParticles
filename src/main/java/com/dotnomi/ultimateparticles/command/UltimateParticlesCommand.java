package com.dotnomi.ultimateparticles.command;

import com.dotnomi.ultimateparticles.constants.Config;
import com.dotnomi.ultimateparticles.files.ImageManager;
import com.dotnomi.ultimateparticles.util.CommandUtility;
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
        UltimateParticlesCommandHandler.handleReloadCommand(sender, args.length, args[0]);
        UltimateParticlesCommandHandler.handleCacheCommand(sender, args.length, args);
        UltimateParticlesCommandHandler.handleRenderCommand(sender, args.length, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                return List.of("cache", "render", "reload");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("render")) return ImageManager.getInstance().getImageNames();
                if (args[0].equalsIgnoreCase("cache")) return List.of("generate", "remove");
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("render")) return List.of("1");
                if (args[1].equalsIgnoreCase("generate")) return List.of("true", "false");
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("render")) return List.of("~", "~ ~", "~ ~ ~");
            } else if (args.length == 5) {
                if (args[0].equalsIgnoreCase("render")) return List.of("~", "~ ~");
            } else if (args.length == 6) {
                if (args[0].equalsIgnoreCase("render")) return List.of("~");
            } else if (args.length == 7) {
                if (args[0].equalsIgnoreCase("render")) return List.of("0", "90", "180", "270");
            } else if (args.length == 8) {
                if (args[0].equalsIgnoreCase("render")) return List.of("0", "90", "180", "270");
            } else if (args.length == 9) {
                if (args[0].equalsIgnoreCase("render")) return List.of("0", "90", "180", "270");
            }
        }

        return new ArrayList<>();
    }
}
