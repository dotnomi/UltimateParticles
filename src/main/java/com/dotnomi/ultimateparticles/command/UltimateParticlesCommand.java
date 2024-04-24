package com.dotnomi.ultimateparticles.command;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.dto.ParticleDto;
import com.dotnomi.ultimateparticles.dto.ParticleStructureDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import com.dotnomi.ultimateparticles.files.ImageManager;
import com.dotnomi.ultimateparticles.files.ParticleStructureCacheManager;
import com.dotnomi.ultimateparticles.util.ParticleHandler;
import com.dotnomi.ultimateparticles.util.ProgressHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class UltimateParticlesCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = UltimateParticles.getPluginMessages().getString("prefix");
        String reload = UltimateParticles.getPluginMessages().getString("info.reload");
        String imageCacheGenerationStarted = UltimateParticles.getPluginMessages().getString("info.image-cache-generate-started");
        String imageCacheDeleted = UltimateParticles.getPluginMessages().getString("info.image-cache-delete-success");
        String processAlreadyRunning = UltimateParticles.getPluginMessages().getString("error.process-is-already-running");

        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    ImageManager.getInstance().load();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reload));
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("cache")) {


                    if (args[1].equalsIgnoreCase("generate")) {
                        if (ProgressHandler.getInstance().isRunning()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + processAlreadyRunning));
                            return true;
                        }

                        ImageManager.getInstance().load();
                        ParticleStructureCacheManager.getInstance().generateParticleStructureCache(false);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + imageCacheGenerationStarted));
                    } else if (args[1].equalsIgnoreCase("remove")) {
                        ParticleStructureCacheManager.getInstance().removeParticleStructureCache();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + imageCacheDeleted));
                    }
                } else if (args[0].equalsIgnoreCase("render")) {
                    List<PixelDto> pixels = ParticleStructureCacheManager.getInstance().loadParticleStructureCache(args[1]);
                    int imageSize = ImageManager.getInstance().getImageDataByName(args[1]).getSize();
                    List<ParticleDto> particles = ParticleStructureCacheManager.getInstance().convertPixelsToParticles(pixels, player.getLocation(), imageSize);
                    ParticleStructureDto particleStructure = new ParticleStructureDto(particles, 40);
                    ParticleHandler.getInstance().addParticleStructure(particleStructure);
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("cache")) {
                    if (args[1].equalsIgnoreCase("generate") && args[2].equalsIgnoreCase("force-generation")) {
                        if (ProgressHandler.getInstance().isRunning()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + processAlreadyRunning));
                            return true;
                        }
                        ParticleStructureCacheManager.getInstance().generateParticleStructureCache(true);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + imageCacheGenerationStarted));
                    }
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("cache", "render", "reload");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("render")) return ImageManager.getInstance().getImageNames();
            return List.of("generate", "remove");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("generate")) return List.of("force-generation");
        }

        return new ArrayList<>();
    }
}
