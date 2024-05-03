package com.dotnomi.ultimateparticles.util;

import com.dotnomi.ultimateparticles.constants.Messages;
import com.dotnomi.ultimateparticles.dto.ParticleDto;
import com.dotnomi.ultimateparticles.dto.ParticleStructureDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import com.dotnomi.ultimateparticles.files.ImageManager;
import com.dotnomi.ultimateparticles.files.MessageManager;
import com.dotnomi.ultimateparticles.files.ParticleStructureCacheManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public final class CommandUtility {
    private CommandUtility() {}

    public static void reload() {
        MessageManager.load();
        ImageManager.getInstance().load();
    }

    public static void generateCache(CommandSender sender, boolean forceGenerate) {
        if (ProgressBarHandler.getInstance().isRunning()) {
            sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_CACHE_GEN_ALREADY_RUNNING, true));
        }

        ImageManager.getInstance().load();
        ParticleStructureCacheManager.getInstance().generateParticleStructureCache(forceGenerate);
        sender.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_CACHE_GEN_STARTED, true));
    }

    public static void removeCache(CommandSender sender) {
        ParticleStructureCacheManager.getInstance().removeParticleStructureCache();
        sender.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_CACHE_DELETED, true));
    }

    public static void render(@Nonnull Player player, @Nonnull String imageName) {
        render(player, imageName, 1, player.getLocation(), new Location(player.getWorld(), 0,0,0));
    }

    public static void render(@Nonnull Player player, @Nonnull String imageName, int duration) {
        render(player, imageName, duration, player.getLocation(), new Location(player.getWorld(), 0,0,0));
    }

    public static void render(@Nonnull Player player, @Nonnull String imageName, int duration, @Nonnull Location position) {
        render(player, imageName, duration, position, new Location(player.getWorld(), 0,0,0));
    }

    public static void render(@Nonnull CommandSender sender, @Nonnull String imageName, int duration, @Nonnull Location position) {
        render(sender, imageName, duration, position, new Location(Bukkit.getWorld("world"), 0,0,0));
    }


    public static void render(@Nonnull Player player, @Nonnull String imageName, int duration, @Nonnull Location position, @Nonnull Location rotation) {
        List<PixelDto> pixels = ParticleStructureCacheManager.getInstance().loadParticleStructureCache(imageName);
        if (pixels == null || pixels.isEmpty()) {
            player.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_IMAGE_NOT_FOUND.replace("%imagename%", imageName), true));
            return;
        }
        int imageSize = ImageManager.getInstance().getImageDataByName(imageName).getSize();
        List<ParticleDto> particles = ParticleStructureCacheManager.getInstance().convertPixelsToParticles(pixels, position, rotation, imageSize);
        ParticleStructureDto particleStructure = new ParticleStructureDto(particles, duration * 20);
        ParticleHandler.getInstance().addParticleStructure(particleStructure);
        player.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_RENDER_IMG_FINISHED.replace("%imagename%", imageName), true));
    }

    public static void render(@Nonnull CommandSender sender, @Nonnull String imageName, int duration, @Nonnull Location position, @Nonnull Location rotation) {
        List<PixelDto> pixels = ParticleStructureCacheManager.getInstance().loadParticleStructureCache(imageName);
        if (pixels == null || pixels.isEmpty()) {
            sender.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_IMAGE_NOT_FOUND.replace("%imagename%", imageName), true));
            return;
        }
        int imageSize = ImageManager.getInstance().getImageDataByName(imageName).getSize();
        List<ParticleDto> particles = ParticleStructureCacheManager.getInstance().convertPixelsToParticles(pixels, position, rotation, imageSize);
        ParticleStructureDto particleStructure = new ParticleStructureDto(particles, duration * 20);
        ParticleHandler.getInstance().addParticleStructure(particleStructure);
        sender.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_RENDER_IMG_FINISHED.replace("%imagename%", imageName), true));
    }
}
