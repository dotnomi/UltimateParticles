package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {

    private static final FileManager instance = new FileManager();
    private final Logger logger;
    private final File dataFolder;

    private FileManager() {
        logger = UltimateParticles.getPluginLogger();
        dataFolder = UltimateParticles.getInstance().getDataFolder();
    }

    public static FileManager getInstance() {
        return instance;
    }

    public void createFolderIfNotExist(String path) {
        try {
            File folder = new File(dataFolder, path);

            if (!folder.exists() && folder.mkdirs()) {

            }
        } catch (Exception exception) {
            logger.log(Level.WARNING, exception.getMessage(), exception);
        }
    }

    public void createYamlFileIfNotExist(String filename) {
        try {
            File yamlFile = new File(dataFolder, filename);
            YamlConfiguration yamlConfig = new YamlConfiguration();
            yamlConfig.options().parseComments(true);

            if (!yamlFile.exists()) {
                yamlConfig.save(yamlFile);
            }
        } catch (Exception exception) {
            logger.log(Level.WARNING, exception.getMessage(), exception);
        }
    }

    public YamlConfiguration loadYamlFile(String filename) {
        try {
            File yamlFile = new File(dataFolder, filename);
            YamlConfiguration yamlConfig = new YamlConfiguration();
            yamlConfig.options().parseComments(true);

            if (yamlFile.exists()) {
                yamlConfig.load(yamlFile);
                return yamlConfig;
            }
        } catch (Exception exception) {
            logger.log(Level.WARNING, exception.getMessage(), exception);
            return null;
        }
        return null;
    }
}
