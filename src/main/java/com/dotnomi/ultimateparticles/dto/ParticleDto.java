package com.dotnomi.ultimateparticles.dto;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import javax.annotation.Nonnull;

public class ParticleDto {

    private final Particle type;
    private final Location position;
    private final Location offset;
    private final Color color;
    private final int count;
    private final float size;
    private final int speed;

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
