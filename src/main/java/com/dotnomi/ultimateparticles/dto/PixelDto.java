package com.dotnomi.ultimateparticles.dto;

import com.dotnomi.ultimateparticles.UltimateParticles;
import org.bukkit.Color;

public class PixelDto {

    private int x, y;
    private Color color;
    private final int maxImageSize;


    public PixelDto(int x,int y, Color color) {
        maxImageSize = UltimateParticles.getPluginConfig().getInt("image-settings.max-size");

        this.x = Math.min(Math.max(0, x), maxImageSize);
        this.y = Math.min(Math.max(0, y), maxImageSize);
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = Math.min(Math.max(0, x), maxImageSize);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = Math.min(Math.max(0, y), maxImageSize);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String toString() {
        return x + "," + y + "," + color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }
}
