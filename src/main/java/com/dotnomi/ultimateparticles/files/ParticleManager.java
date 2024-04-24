package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.dto.ImageDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public class ParticleManager {
    private static final ParticleManager instance = new ParticleManager();
    private static Logger logger;

    private ParticleManager() {
        logger = UltimateParticles.getInstance().getLogger();
    }

    public static ParticleManager getInstance() {
        return instance;
    }

    public void render(@Nonnull Location position, @Nonnull Location offset, @Nonnull Particle particleType,
                       @Nonnull PixelDto pixelData, int imageSize, int count, int speed, float size) {
        if (position.getWorld() == null) return;

        double relativeX = (pixelData.getX() - Math.ceil((double) imageSize / 2)) * 0.125;
        double relativeZ = (pixelData.getY() - Math.ceil((double) imageSize / 2)) * 0.125;

        Location calculatedPosition = position.add(relativeX,0, relativeZ);

        position.getWorld().spawnParticle(particleType, calculatedPosition, count, offset.getX(), offset.getY(), offset.getZ(),
                speed, new Particle.DustOptions(pixelData.getColor(), size));
    }
}
