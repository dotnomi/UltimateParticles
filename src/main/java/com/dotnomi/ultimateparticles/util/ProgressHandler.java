package com.dotnomi.ultimateparticles.util;

import com.dotnomi.ultimateparticles.UltimateParticles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgressHandler {

    private static final ProgressHandler instance = new ProgressHandler();
    private static Logger logger;

    private boolean isRunning;
    private BossBar progressBar;
    private int taskId;
    private double currentProgress;

    private ProgressHandler() {
        logger = UltimateParticles.getInstance().getLogger();
        currentProgress = 0;
        isRunning = false;
    }

    public static ProgressHandler getInstance() {
        return instance;
    }

    public void enable() {
        String progressbar = UltimateParticles.getPluginMessages().getString("progressbar") + " ";
        String prefix = UltimateParticles.getPluginMessages().getString("prefix") + " ";
        String imageCacheGenerationSuccess = UltimateParticles.getPluginMessages().getString("info.image-cache-generate-success") + " ";

        progressBar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', progressbar.replace("%progress%", "0")), BarColor.BLUE, BarStyle.SOLID);

        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(UltimateParticles.getInstance(), () -> {
            if (currentProgress > 0 && currentProgress < 100) {
                isRunning = true;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!progressBar.getPlayers().contains(player)) progressBar.addPlayer(player);
                }
                progressBar.setProgress(currentProgress / 100);
                progressBar.setTitle(ChatColor.translateAlternateColorCodes('&', progressbar.replace("%progress%", (int) currentProgress + "")));
            } else {
                if (isRunning) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + imageCacheGenerationSuccess));
                    }
                }

                currentProgress = 0;
                isRunning = false;
                progressBar.removeAll();
            }
        },0,20).getTaskId();
    }

    public void disable() {
        Bukkit.getScheduler().cancelTask(taskId);
        progressBar.removeAll();
    }

    public void updateProgress(double taskProgress) {
        currentProgress = taskProgress;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
