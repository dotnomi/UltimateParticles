package com.dotnomi.ultimateparticles.util;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.constants.Messages;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class ProgressBarHandler {
    private static ProgressBarHandler instance;
    private int taskId;
    private boolean isRunning;
    private double currentProgress;
    private BossBar bossBar;

    private ProgressBarHandler() {
        currentProgress = 0;
        isRunning = false;
    }

    public static ProgressBarHandler getInstance() {
        if (instance == null) instance = new ProgressBarHandler();
        return instance;
    }

    public void enable() {
        bossBar = Bukkit.createBossBar(getCurrentProgressBarTitle(), BarColor.BLUE, BarStyle.SOLID);
        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(UltimateParticles.getInstance(), () -> {
            if (currentProgress > 0 && currentProgress < 100) {
                isRunning = true;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!bossBar.getPlayers().contains(player)) bossBar.addPlayer(player);
                }
                bossBar.setProgress(currentProgress / 100);
                bossBar.setTitle(getCurrentProgressBarTitle());
            } else {
                endProgressBar();
            }
        },0,20).getTaskId();
    }

    public void disable() {
        Bukkit.getScheduler().cancelTask(taskId);
        bossBar.removeAll();
    }

    public void updateProgress(double taskProgress) {
        currentProgress = taskProgress;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private String getCurrentProgressBarTitle() {
        return MessageUtility.getColoredMessage(
                Messages.GENERAL_PROGRESS_BAR.replace("%progress%",
                        (int) currentProgress + ""));
    }

    private void endProgressBar() {
        if (isRunning) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(MessageUtility.getColoredMessage(Messages.INFO_CACHE_GEN_FINISHED, true));
            }
        }

        currentProgress = 0;
        isRunning = false;
        bossBar.removeAll();
    }
}
