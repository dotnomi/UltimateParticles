package com.dotnomi.ultimateparticles;

import com.dotnomi.ultimateparticles.command.UltimateParticlesCommand;
import com.dotnomi.ultimateparticles.constants.Config;
import com.dotnomi.ultimateparticles.files.ConfigManager;
import com.dotnomi.ultimateparticles.files.ImageManager;
import com.dotnomi.ultimateparticles.files.MessageManager;
import com.dotnomi.ultimateparticles.util.ParticleHandler;
import com.dotnomi.ultimateparticles.util.ProgressBarHandler;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public final class UltimateParticles extends JavaPlugin {

    private ParticleHandler particleHandler;
    private ProgressBarHandler progressBarHandler;

    @Override
    public void onEnable() {
        initializeVariables();
        initializeCommands();

        particleHandler.enable();
        progressBarHandler.enable();
    }

    @Override
    public void onDisable() {
        particleHandler.disable();
        progressBarHandler.disable();
    }

    private void initializeVariables() {
        ConfigManager.load();
        MessageManager.load();

        particleHandler = ParticleHandler.getInstance();
        progressBarHandler = ProgressBarHandler.getInstance();

        ImageManager.getInstance().load();
    }

    private void initializeCommands() {
        PluginCommand mainCommand = getCommand("ultimateparticles");
        if (mainCommand != null) {
            mainCommand.setExecutor(new UltimateParticlesCommand());
        }
    }

    public static UltimateParticles getInstance() {
        return getPlugin(UltimateParticles.class);
    }

    public static Logger getPluginLogger() {
        return UltimateParticles.getInstance().getLogger();
    }
}
