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
    private static ParticleHandler instance;
    private final Logger logger;
    private List<ParticleStructureDto> particleStructures;

    private int taskId;

    private ParticleHandler() {
        logger = UltimateParticles.getPluginLogger();
        particleStructures = new ArrayList<>();
    }

    public static ParticleHandler getInstance() {
        if (instance == null) instance = new ParticleHandler();
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
        logger.info("Particle handler enabled.");
    }

    public void disable() {
        try {
            if (Bukkit.getScheduler().isCurrentlyRunning(taskId)) {
                Bukkit.getScheduler().cancelTask(taskId);
                logger.info("Particle handler disabled.");
            }
        } catch (Exception ignore) {
            logger.warning("Error while disabling particle handler.");
        }
    }
}
