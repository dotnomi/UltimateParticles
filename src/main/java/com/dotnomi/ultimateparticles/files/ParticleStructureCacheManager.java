package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.dto.ImageDto;
import com.dotnomi.ultimateparticles.dto.ParticleDto;
import com.dotnomi.ultimateparticles.dto.ParticleStructureDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import com.dotnomi.ultimateparticles.util.ProgressHandler;
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

        ProgressHandler.getInstance().updateProgress(0);
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

            List<String> pixelList = new ArrayList<>();

            for (int x = 0; x < imageData.getSize(); x++) {
                for (int y = 0; y < imageData.getSize(); y++) {
                    if (closeTask) {
                        return;
                    }
                    count++;
                    ProgressHandler.getInstance().updateProgress((double) count / maxProgress * 100);
                    PixelDto pixelData = ImageManager.getInstance().getPixelData(imageData.getImageFile(), x, y);
                    if (pixelData != null && pixelData.getColor().getAlpha() == 255) pixelList.add(pixelData.toString());
                }
            }

            try {
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

        List<PixelDto> pixelDataResult = new ArrayList<>();
        List<String> pixelDataList = cacheConfig.getStringList("data.pixels");
        logger.log(Level.INFO, "Loaded cache from " + imageName + " " + pixelDataList.size());

        for (String pixelDataString : pixelDataList) {
            Pattern pattern = Pattern.compile("(\\d+),(\\d+),(\\d+),(\\d+),(\\d+)");
            List<MatchResult> matchResultList = pattern.matcher(pixelDataString).results().toList();

            int x = Integer.parseInt(matchResultList.get(0).group(1));
            int y = Integer.parseInt(matchResultList.get(0).group(2));
            int red = Integer.parseInt(matchResultList.get(0).group(3));
            int green = Integer.parseInt(matchResultList.get(0).group(4));
            int blue = Integer.parseInt(matchResultList.get(0).group(5));

            pixelDataResult.add(new PixelDto(x, y, Color.fromRGB(red, green, blue)));
        }

        return pixelDataResult;
    }

    public List<ParticleDto> convertPixelsToParticles(@Nonnull List<PixelDto> pixels, @Nonnull Location location, int imageSize) {
        List<ParticleDto> particles = new ArrayList<>();

        for (PixelDto pixel : pixels) {
            double relativeX = (pixel.getX() - Math.ceil((double) imageSize / 2)) * 0.125;
            double relativeZ = (pixel.getY() - Math.ceil((double) imageSize / 2)) * 0.125;
            Location particleLocation = location.clone().add(relativeX, 0, relativeZ);
            Location offset = new Location(location.getWorld(), 0,0,0);
            particles.add(new ParticleDto(Particle.REDSTONE, particleLocation, offset, pixel.getColor(),
                    1, 0.6f, 0));
        }

        return particles;
    }
}
