package com.dotnomi.ultimateparticles.command;

import com.dotnomi.ultimateparticles.constants.Messages;
import com.dotnomi.ultimateparticles.dto.ParticleDto;
import com.dotnomi.ultimateparticles.dto.ParticleStructureDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import com.dotnomi.ultimateparticles.files.ImageManager;
import com.dotnomi.ultimateparticles.files.MessageManager;
import com.dotnomi.ultimateparticles.files.ParticleStructureCacheManager;
import com.dotnomi.ultimateparticles.util.ParticleHandler;
import com.dotnomi.ultimateparticles.util.MessageUtility;
import com.dotnomi.ultimateparticles.util.ProgressBarHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class UltimateParticlesCommandHandler {
    private UltimateParticlesCommandHandler() {}

    public static void reload(Player player) {
        MessageManager.load();
        ImageManager.getInstance().load();
        player.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_RELOAD, true));
    }

    public static void generateCache(Player player, boolean forceGenerate) {
        if (ProgressBarHandler.getInstance().isRunning()) {
            player.sendMessage(MessageUtility.getColoredMessage(Messages.ERROR_CACHE_GEN_ALREADY_RUNNING, true));
        }

        ImageManager.getInstance().load();
        ParticleStructureCacheManager.getInstance().generateParticleStructureCache(forceGenerate);
        player.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_CACHE_GEN_STARTED, true));
    }

    public static void removeCache(Player player) {
        ParticleStructureCacheManager.getInstance().removeParticleStructureCache();
        player.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_CACHE_DELETED, true));
    }

    public static void render(@Nonnull Player player, @Nonnull String imageName) {
        render(player, imageName, player.getLocation(), new Location(player.getWorld(), 0,0,0), 1);
    }

    public static void render(@Nonnull Player player, @Nonnull String imageName, @Nonnull Location position) {
        render(player, imageName, position, new Location(player.getWorld(), 0,0,0), 1);
    }

    public static void render(@Nonnull Player player, @Nonnull String imageName, @Nonnull Location position, @Nonnull Location rotation) {
        render(player, imageName, position, rotation, 20);
    }

    public static void render(@Nonnull Player player, @Nonnull String imageName, @Nonnull Location position, @Nonnull Location rotation, int duration) {
        List<PixelDto> pixels = ParticleStructureCacheManager.getInstance().loadParticleStructureCache(imageName);
        int imageSize = ImageManager.getInstance().getImageDataByName(imageName).getSize();
        List<ParticleDto> particles = ParticleStructureCacheManager.getInstance().convertPixelsToParticles(pixels, position, rotation, imageSize);
        ParticleStructureDto particleStructure = new ParticleStructureDto(particles, duration * 20);
        ParticleHandler.getInstance().addParticleStructure(particleStructure);
        player.sendMessage(MessageUtility.getColoredMessage("Rendered an image.", true));
    }
}
