package com.dotnomi.ultimateparticles.util;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.dto.ParticleDto;
import com.dotnomi.ultimateparticles.dto.ParticleStructureDto;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticleHandler {
    private static final ParticleHandler instance = new ParticleHandler();
    private final Logger logger;
    private List<ParticleStructureDto> particleStructures;

    private int taskId;

    private ParticleHandler() {
        logger = UltimateParticles.getPluginLogger();
        particleStructures = new ArrayList<>();
    }

    public static ParticleHandler getInstance() {
        return instance;
    }

    public void addParticleStructure(ParticleStructureDto particleStructure) {
        particleStructures.add(particleStructure);
    }

    public void removeFinishedParticleStructures() {
        List<ParticleStructureDto> newList = new ArrayList<>();

        for (ParticleStructureDto particleStructure : particleStructures) {
            if (particleStructure.getCurrentDuration() < particleStructure.getDuration()) {
                newList.add(particleStructure);
            }
        }

        particleStructures = newList;
    }

    public void enable() {
        taskId = Bukkit.getScheduler().runTaskTimer(UltimateParticles.getInstance(), () -> {
            for (ParticleStructureDto particleStructure : particleStructures) {
                if (particleStructure.getCurrentDuration() < particleStructure.getDuration()) {
                    particleStructure.render();
                    particleStructure.addCurrentDuration(1);
                }
            }

            removeFinishedParticleStructures();
        }, 0,1).getTaskId();
        logger.log(Level.INFO, "Particle handler enabled.");
    }

    public void disable() {
        if (Bukkit.getScheduler().isCurrentlyRunning(taskId)) {
            Bukkit.getScheduler().cancelTask(taskId);
            logger.log(Level.INFO, "Particle handler disabled.");
        }
    }
}
