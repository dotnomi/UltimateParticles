package com.dotnomi.ultimateparticles;

import com.dotnomi.ultimateparticles.command.UltimateParticlesCommand;
import com.dotnomi.ultimateparticles.files.ConfigManager;
import com.dotnomi.ultimateparticles.files.ImageManager;
import com.dotnomi.ultimateparticles.files.MessageManager;
import com.dotnomi.ultimateparticles.util.ParticleHandler;
import com.dotnomi.ultimateparticles.util.ProgressHandler;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class UltimateParticles extends JavaPlugin {

    private ParticleHandler particleHandler;
    private ProgressHandler progressHandler;

    @Override
    public void onEnable() {
        initializeVariables();
        initializeCommands();

        particleHandler.enable();
        progressHandler.enable();
    }

    @Override
    public void onDisable() {
        particleHandler.disable();
        progressHandler.disable();
    }

    private void initializeVariables() {
        ConfigManager.getInstance().load();
        MessageManager.getInstance().load();
        particleHandler = ParticleHandler.getInstance();
        progressHandler = ProgressHandler.getInstance();

        ImageManager.getInstance().load();
    }

    private void initializeCommands() {
        PluginCommand ultimateparticlesCommand = getCommand("ultimateparticles");
        if (ultimateparticlesCommand != null) ultimateparticlesCommand.setExecutor(new UltimateParticlesCommand());
    }

    public static UltimateParticles getInstance() {
        return getPlugin(UltimateParticles.class);
    }

    public static Logger getPluginLogger() {
        return UltimateParticles.getInstance().getLogger();
    }

    public static YamlConfiguration getPluginConfig() {
        return ConfigManager.getInstance().getConfig();
    }

    public static YamlConfiguration getPluginMessages() {
        return MessageManager.getInstance().getMessages();
    }
}
