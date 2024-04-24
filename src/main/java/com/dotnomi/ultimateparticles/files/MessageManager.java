package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageManager {
    private static final MessageManager instance = new MessageManager();
    private static Logger logger;
    private static File dataFolder;
    private static YamlConfiguration messageConfig;

    private MessageManager() {
        logger = UltimateParticles.getInstance().getLogger();
        dataFolder = UltimateParticles.getInstance().getDataFolder();
    }

    public static MessageManager getInstance() {
        return instance;
    }

    public void load() {
        File messagesFile = new File(dataFolder, "messages.yml");

        if (!messagesFile.exists()) {
            UltimateParticles.getInstance().saveResource("messages.yml", false);
        }

        try {
            messageConfig = YamlConfiguration.loadConfiguration(messagesFile);
        } catch (Exception exception) {
            logger.log(Level.WARNING, exception.getMessage(), exception);
        }
    }

    public YamlConfiguration getMessages() {
        return messageConfig;
    }
}
