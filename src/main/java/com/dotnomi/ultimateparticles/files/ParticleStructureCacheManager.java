package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.dto.ImageDto;
import com.dotnomi.ultimateparticles.dto.ParticleDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import com.dotnomi.ultimateparticles.util.ProgressBarHandler;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class ParticleStructureCacheManager {

    private static final ParticleStructureCacheManager instance = new ParticleStructureCacheManager();
    private static Logger logger;

    private final List<Integer> asyncTasks;
    private boolean closeTask = false;

    private ParticleStructureCacheManager() {
        logger = UltimateParticles.getInstance().getLogger();
        asyncTasks = new ArrayList<>();
    }

    public static ParticleStructureCacheManager getInstance() {
        return instance;
    }

    public void generateParticleStructureCache(boolean forceGenerate) {
        ImageManager.getInstance().getImageNames().forEach(imageName -> {
            asyncTasks.add(generateParticleStructureCacheAsync(imageName, forceGenerate));
        });
    }

    public void removeParticleStructureCache() {
        closeTask = true;
        asyncTasks.forEach(taskId -> {
            Bukkit.getScheduler().cancelTask(taskId);
        });

        ImageManager.getInstance().getImageNames().forEach(imageName -> {
            ImageDto imageData = ImageManager.getInstance().getImageDataByName(imageName);
            File imageFolder = new File(UltimateParticles.getInstance().getDataFolder(), "images");

            if (imageData == null) {
                logger.log(Level.WARNING, "Could not find image " + imageName);
                return;
            }

            File cacheFile = new File(imageFolder, imageName + "_cache.yml");

            cacheFile.delete();
        });

        ProgressBarHandler.getInstance().updateProgress(0);
        closeTask = false;
    }

    private int generateParticleStructureCacheAsync(String imageName, boolean forceGenerate) {
        return Bukkit.getScheduler().runTaskAsynchronously(UltimateParticles.getInstance(), () -> {
            ImageDto imageData = ImageManager.getInstance().getImageDataByName(imageName);
            File imageFolder = new File(UltimateParticles.getInstance().getDataFolder(), "images");

            if (imageData == null) {
                logger.log(Level.WARNING, "Could not find image " + imageName);
                return;
            }

            File cacheFile = new File(imageFolder, imageName + "_cache.yml");
            YamlConfiguration cacheConfig = new YamlConfiguration();

            if (cacheFile.exists() && !forceGenerate) {
                return;
            }

            int maxProgress = imageData.getSize() * imageData.getSize();
            int count = 0;

            List<String> colorList = new ArrayList<>();
            List<String> pixelList = new ArrayList<>();

            for (int x = 0; x < imageData.getSize(); x++) {
                for (int y = 0; y < imageData.getSize(); y++) {
                    if (closeTask) {
                        return;
                    }
                    count++;
                    ProgressBarHandler.getInstance().updateProgress((double) count / maxProgress * 100);
                    PixelDto pixelData = ImageManager.getInstance().getPixelData(imageData.getImageFile(), x, y);
                    if (pixelData != null && pixelData.getColor().getAlpha() == 255) {
                        String colorString = getColorString(pixelData.getColor());
                        if (!colorList.contains(colorString)) colorList.add(colorString);
                        pixelList.add(pixelData.toString(colorList.indexOf(colorString)));
                    }
                }
            }

            try {
                cacheConfig.set("data.colors", colorList);
                cacheConfig.set("data.pixels", pixelList);
                cacheConfig.save(cacheFile);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Unable to save cache file from " + imageName);
            }
        }).getTaskId();
    }

    public List<PixelDto> loadParticleStructureCache(String imageName) {
        ImageDto imageData = ImageManager.getInstance().getImageDataByName(imageName);
        File imageFolder = new File(UltimateParticles.getInstance().getDataFolder(), "images");
        File cacheFile = new File(imageFolder, imageName + "_cache.yml");

        if (imageData == null || !cacheFile.exists())  {
            logger.log(Level.WARNING, "Failed to load cache from " + imageName);
            return null;
        }

        YamlConfiguration cacheConfig = YamlConfiguration.loadConfiguration(cacheFile);

        List<PixelDto> pixels = new ArrayList<>();
        List<Color> colors = new ArrayList<>();

        List<String> colorStrings = cacheConfig.getStringList("data.colors");
        List<String> pixelStrings = cacheConfig.getStringList("data.pixels");
        logger.log(Level.INFO, "Loaded cache from " + imageName + " " + pixelStrings.size());

        for (String colorString : colorStrings) {
            Pattern pattern = Pattern.compile("(\\d+),(\\d+),(\\d+)");
            List<MatchResult> matchResultList = pattern.matcher(colorString).results().toList();

            int red = Integer.parseInt(matchResultList.get(0).group(1));
            int green = Integer.parseInt(matchResultList.get(0).group(2));
            int blue = Integer.parseInt(matchResultList.get(0).group(3));

            colors.add(Color.fromRGB(red, green, blue));
        }

        for (String pixelString : pixelStrings) {
            Pattern pattern = Pattern.compile("(\\d+),(\\d+),(\\d+)");
            List<MatchResult> matchResultList = pattern.matcher(pixelString).results().toList();

            int x = Integer.parseInt(matchResultList.get(0).group(1));
            int y = Integer.parseInt(matchResultList.get(0).group(2));
            int colorIndex = Integer.parseInt(matchResultList.get(0).group(3));

            pixels.add(new PixelDto(x, y, colors.get(colorIndex)));
        }

        return pixels;
    }

    public List<ParticleDto> convertPixelsToParticles(@Nonnull List<PixelDto> pixels, @Nonnull Location location, @Nonnull Location rotation, int imageSize) {
        List<ParticleDto> particles = new ArrayList<>();
        double xRadians = Math.toRadians(rotation.getX());
        double yRadians = Math.toRadians(rotation.getY());
        double zRadians = Math.toRadians(rotation.getZ());

        double centerX = Math.ceil((double) imageSize / 2);
        double centerZ = centerX;

        for (PixelDto pixel : pixels) {
            double originalX = (pixel.getX() - centerX) * 0.125;
            double originalY = 0;
            double originalZ = (pixel.getY() - centerZ) * 0.125;

            // Y-Rotation
            double tempX = originalX * Math.cos(yRadians) - originalZ * Math.sin(yRadians);
            double tempZ = originalX * Math.sin(yRadians) + originalZ * Math.cos(yRadians);

            // X-Rotation
            double tempY = originalY * Math.cos(xRadians) - tempZ * Math.sin(xRadians);
            tempZ = originalY * Math.sin(xRadians) + tempZ * Math.cos(xRadians);

            // Z-Rotation
            double rotatedX = tempX * Math.cos(zRadians) - tempY * Math.sin(zRadians);
            double rotatedY = tempX * Math.sin(zRadians) + tempY * Math.cos(zRadians);

            Location particleLocation = location.clone().add(rotatedX, rotatedY, tempZ);
            Location offset = new Location(location.getWorld(), 0, 0, 0);
            particles.add(new ParticleDto(Particle.REDSTONE, particleLocation, offset, pixel.getColor(),
                    1, 0.65f, 0));
        }

        return particles;
    }

    private String getColorString(@Nonnull Color color) {
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }
}
