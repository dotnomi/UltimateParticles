package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {

    private static final ConfigManager instance = new ConfigManager();
    private static Logger logger;
    private static File dataFolder;
    private static YamlConfiguration config;

    private ConfigManager() {
        logger = UltimateParticles.getInstance().getLogger();
        dataFolder = UltimateParticles.getInstance().getDataFolder();
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public void load() {
        File configFile = new File(dataFolder, "config.yml");

        if (!configFile.exists()) {
            UltimateParticles.getInstance().saveResource("config.yml", false);
        }

        try {
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception exception) {
            logger.log(Level.WARNING, exception.getMessage(), exception);
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
