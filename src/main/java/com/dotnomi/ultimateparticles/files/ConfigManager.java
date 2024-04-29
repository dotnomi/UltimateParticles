package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.constants.Config;
import com.dotnomi.ultimateparticles.constants.Constants;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
    private ConfigManager() {}

    public static void load() {
        Logger logger = UltimateParticles.getInstance().getLogger();
        File configFile = new File(UltimateParticles.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            UltimateParticles.getInstance().saveResource("config.yml", false);
        }

        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

            // TEMPORARY SETTINGS
            int[] tempImageSize = sendImageSizeWarnings(
                    config.getInt("image-settings.min-size", 0),
                    config.getInt("image-settings.max-size", 0));

            // GLOBAL SETTINGS
            Config.LANGUAGE_FILE_NAME = config.getString("global-settings.language", "english");
            Config.DEBUG_MODE = config.getBoolean("global-settings.debug-mode", false);

            // IMAGE SETTINGS
            Config.MIN_IMAGE_SIZE = tempImageSize[0];
            Config.MAX_IMAGE_SIZE = tempImageSize[1];
        } catch (Exception exception) {
            logger.log(Level.WARNING, Constants.LOG_CANT_LOAD_FILE.replace("%filename%", "config"));
        }
    }

    private static int[] sendImageSizeWarnings(int minSize, int maxSize) {
        Logger logger = UltimateParticles.getInstance().getLogger();

        if (minSize < 0) {
            minSize = 0;
            logger.warning(Constants.LOG_NEGATIVE_IMAGE_SIZE
                    .replace("%variable%", "min-size")
                    .replace("%size%", "0"));
        }

        if (minSize > maxSize) {
            maxSize = minSize;
            logger.warning(Constants.LOG_MAX_SMALLER_THAN_MIN
                    .replace("%variable%", "max-size")
                    .replace("%size%", "" + maxSize));
        }

        return new int[]{minSize, maxSize};
    }
}
