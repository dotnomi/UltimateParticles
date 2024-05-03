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
            Messages.GENERAL_PREFIX = messageConfig.getString("general.prefix", "&8[&3UltimateParticles&8] &7");
            Messages.GENERAL_PROGRESS_BAR = messageConfig.getString("general.progress-bar", Constants.ERROR_INVALID_MESSAGE_FILE);

            // INFO MESSAGES
            Messages.INFO_RELOAD = messageConfig.getString("info.reload", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.INFO_CACHE_GEN_STARTED = messageConfig.getString("info.image-cache-generate-started", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.INFO_CACHE_GEN_FINISHED = messageConfig.getString("info.image-cache-generate-finished", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.INFO_CACHE_DELETED = messageConfig.getString("info.image-cache-delete", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.INFO_RENDER_IMG_FINISHED = messageConfig.getString("info.image-render-finished", Constants.ERROR_INVALID_MESSAGE_FILE);

            // ERROR MESSAGES
            Messages.ERROR_CACHE_GEN_ALREADY_RUNNING = messageConfig.getString("error.process-is-already-running", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.ERROR_NO_PERMISSION = messageConfig.getString("error.no-permission", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.ERROR_NOT_A_PLAYER = messageConfig.getString("error.not-a-player", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.ERROR_IMAGE_NOT_FOUND = messageConfig.getString("error.image-not-found", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.ERROR_WRONG_RELOAD_CMD_USEAGE = messageConfig.getString("error.wrong-reload-cmd-usage", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.ERROR_WRONG_CACHE_CMD_USEAGE = messageConfig.getString("error.wrong-cache-cmd-usage", Constants.ERROR_INVALID_MESSAGE_FILE);
            Messages.ERROR_WRONG_RENDER_CMD_USEAGE = messageConfig.getString("error.wrong-render-cmd-usage", Constants.ERROR_INVALID_MESSAGE_FILE);
        } catch (Exception ignore) {
            logger.log(Level.WARNING, Constants.LOG_CANT_LOAD_FILE.replace("%filename%", Config.LANGUAGE_FILE_NAME));
        }
    }

    private static void initalize() {
        File languagesFolder = new File(UltimateParticles.getInstance().getDataFolder(),"languages");
        List<String> defaultLanguageFiles = List.of("english.yml", "german.yml", "french.yml", "spanish.yml");
        Logger logger = UltimateParticles.getPluginLogger();

        createLanguagesFolderIfNotExists();

        for (String fileName : defaultLanguageFiles) {
            try {
                if (!new File(languagesFolder, fileName).exists()) {
                    UltimateParticles.getInstance()
                            .saveResource("languages/" + fileName, false);
                }
            } catch (Exception ignore) {
                logger.warning("Failed to instantiate language file " + fileName);
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
