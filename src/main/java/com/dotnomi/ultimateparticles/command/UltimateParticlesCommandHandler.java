package com.dotnomi.ultimateparticles.command;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.dto.ParticleDto;
import com.dotnomi.ultimateparticles.dto.ParticleStructureDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import com.dotnomi.ultimateparticles.files.ImageManager;
import com.dotnomi.ultimateparticles.files.MessageManager;
import com.dotnomi.ultimateparticles.files.ParticleStructureCacheManager;
import com.dotnomi.ultimateparticles.util.ParticleHandler;
import com.dotnomi.ultimateparticles.util.ProgressHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.logging.Logger;

public class UltimateParticlesCommandHandler {

    private static final UltimateParticlesCommandHandler instance = new UltimateParticlesCommandHandler();
    private static Logger logger;

    private static String prefix;
    private static String reload;
    private static String imageCacheGenerationStarted;
    private static String imageCacheDeleted;
    private static String processAlreadyRunning;

    private UltimateParticlesCommandHandler() {
        logger = UltimateParticles.getInstance().getLogger();
        prefix = UltimateParticles.getPluginMessages().getString("prefix");
        reload = UltimateParticles.getPluginMessages().getString("info.reload");
        imageCacheGenerationStarted = UltimateParticles.getPluginMessages().getString("info.image-cache-generate-started");
        imageCacheDeleted = UltimateParticles.getPluginMessages().getString("info.image-cache-delete-success");
        processAlreadyRunning = UltimateParticles.getPluginMessages().getString("error.process-is-already-running");
    }

    public static UltimateParticlesCommandHandler getInstance() {
        return instance;
    }

    public void reload(Player player) {
        MessageManager.getInstance().load();
        ImageManager.getInstance().load();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + reload));
    }

    public void generateCache(Player player, boolean forceGenerate) {
        if (ProgressHandler.getInstance().isRunning()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + processAlreadyRunning));
        }

        ImageManager.getInstance().load();
        ParticleStructureCacheManager.getInstance().generateParticleStructureCache(forceGenerate);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + imageCacheGenerationStarted));
    }

    public void removeCache(Player player) {
        ParticleStructureCacheManager.getInstance().removeParticleStructureCache();
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + imageCacheDeleted));
    }

    public void render(@Nonnull Player player, @Nonnull String imageName) {
        render(player, imageName, player.getLocation(), new Location(player.getWorld(), 0,0,0), 1);
    }

    public void render(@Nonnull Player player, @Nonnull String imageName, @Nonnull Location position) {
        render(player, imageName, position, new Location(player.getWorld(), 0,0,0), 1);
    }

    public void render(@Nonnull Player player, @Nonnull String imageName, @Nonnull Location position, @Nonnull Location rotation) {
        render(player, imageName, position, rotation, 20);
    }

    public void render(@Nonnull Player player, @Nonnull String imageName, @Nonnull Location position, @Nonnull Location rotation, int duration) {
        List<PixelDto> pixels = ParticleStructureCacheManager.getInstance().loadParticleStructureCache(imageName);
        int imageSize = ImageManager.getInstance().getImageDataByName(imageName).getSize();
        List<ParticleDto> particles = ParticleStructureCacheManager.getInstance().convertPixelsToParticles(pixels, position, rotation, imageSize);
        ParticleStructureDto particleStructure = new ParticleStructureDto(particles, duration * 20);
        ParticleHandler.getInstance().addParticleStructure(particleStructure);
    }
}
