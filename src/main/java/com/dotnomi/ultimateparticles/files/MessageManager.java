package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.constants.Config;
import com.dotnomi.ultimateparticles.constants.Constants;
import com.dotnomi.ultimateparticles.constants.Messages;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageManager {
    private MessageManager() {}

    public static void load() {
        Logger logger = UltimateParticles.getPluginLogger();
        File messagesFile = new File(UltimateParticles.getInstance().getDataFolder(), "languages/" + Config.LANGUAGE_FILE_NAME + ".yml");

        initalize();

        if (!messagesFile.exists()) {
            try {
                FileOutputStream messagesOutputStream = new FileOutputStream(messagesFile);
                InputStream messagesStream = UltimateParticles.getInstance().getResource("languages/english.yml");
                if (messagesStream == null) throw new StreamCorruptedException("");
                messagesOutputStream.write(messagesStream.readAllBytes());
                messagesOutputStream.close();
                messagesStream.close();
                logger.info("Successfully created messages file " + Config.LANGUAGE_FILE_NAME + ".yml");
            } catch (Exception exception) {
                logger.warning(Constants.LOG_CANT_INSTANTIATE_FILE.replace("%filename%", Config.LANGUAGE_FILE_NAME));
            }
        }

        try {
            YamlConfiguration messageConfig = YamlConfiguration.loadConfiguration(messagesFile);

            // GENERAL MESSAGES
            Messages.GENERAL_PREFIX = messageConfig.getString("general.prefix", "global");
            Messages.GENERAL_PROGRESS_BAR = messageConfig.getString("general.progress-bar", "global");

            // INFO MESSAGES
            Messages.INFO_RELOAD = messageConfig.getString("info.reload", "value");
            Messages.INFO_CACHE_GEN_STARTED = messageConfig.getString("info.image-cache-generate-started", "info");
            Messages.INFO_CACHE_GEN_FINISHED = messageConfig.getString("info.image-cache-generate-finished", "info");
            Messages.INFO_CACHE_DELETED = messageConfig.getString("info.image-cache-delete", "info");

            // ERROR MESSAGES
            Messages.ERROR_CACHE_GEN_ALREADY_RUNNING = messageConfig.getString("error.process-is-already-running", "error");
        } catch (Exception ignore) {
            logger.log(Level.WARNING, Constants.LOG_CANT_LOAD_FILE.replace("%filename%", Config.LANGUAGE_FILE_NAME));
        }
    }

    private static void initalize() {
        File languagesFolder = new File(UltimateParticles.getInstance().getDataFolder(),"languages");
        List<String> defaultLanguageFiles = List.of("english.yml", "german.yml");

        createLanguagesFolderIfNotExists();

        for (String fileName : defaultLanguageFiles) {
            if (!new File(languagesFolder, fileName).exists()) {
                UltimateParticles.getInstance()
                        .saveResource("languages/" + fileName, false);
            }
        }
    }

    private static  void createLanguagesFolderIfNotExists() {
        File languagesFolder = new File(UltimateParticles.getInstance().getDataFolder(),"languages");
        Logger logger = UltimateParticles.getPluginLogger();

        if (!languagesFolder.exists() && languagesFolder.mkdirs()) {
            logger.log(Level.INFO, "Created languages folder.");
        }
    }
}
