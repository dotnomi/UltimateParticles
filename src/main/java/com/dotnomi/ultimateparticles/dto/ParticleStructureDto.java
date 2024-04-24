package com.dotnomi.ultimateparticles.dto;

import org.bukkit.Location;

import java.util.List;

public class ParticleStructureDto {
    private List<ParticleDto> particles;
    private int currentDuration;
    private final int duration;

    public ParticleStructureDto(List<ParticleDto> particles, int duration) {
        this.particles = particles;
        this.duration = Math.max(1, duration);
        this.currentDuration = 0;
    }

    public List<ParticleDto> getParticles() {
        return particles;
    }

    public void setParticles(List<ParticleDto> particles) {
        this.particles = particles;
    }

    public int getDuration() {
        return duration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void addCurrentDuration(int add) {
        currentDuration += add;
    }

    public void render() {
        for (ParticleDto particle : particles) {
            particle.render();
        }
    }
}
