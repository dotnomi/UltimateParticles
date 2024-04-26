package com.dotnomi.ultimateparticles.dto;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import javax.annotation.Nonnull;

public class ParticleDto {

    private Particle type;
    private Location position;
    private Location offset;
    private Color color;
    private int count;
    private float size;
    private int speed;

    public ParticleDto(@Nonnull Particle particleType, @Nonnull Location position,
                       @Nonnull Location offset, @Nonnull Color color, int count, float size, int speed) {
        this.type = particleType;
        this.position = position;
        this.offset = offset;
        this.color = color;
        this.count = count;
        this.size = size;
        this.speed = speed;
    }

    public void render() {
        if (position.getWorld() == null) return;
        position.getWorld().spawnParticle(type, position, count, offset.getX(), offset.getY(), offset.getZ(),
                speed, new Particle.DustOptions(color, size));
    }
}
