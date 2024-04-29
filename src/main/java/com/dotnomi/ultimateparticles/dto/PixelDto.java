package com.dotnomi.ultimateparticles.dto;

import com.dotnomi.ultimateparticles.constants.Config;
import org.bukkit.Color;

public class PixelDto {
    private int x, y;
    private Color color;

    public PixelDto(int x,int y, Color color) {
        this.x = Math.min(Math.max(0, x), Config.MAX_IMAGE_SIZE);
        this.y = Math.min(Math.max(0, y), Config.MAX_IMAGE_SIZE);
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = Math.min(Math.max(0, x), Config.MAX_IMAGE_SIZE);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = Math.min(Math.max(0, y), Config.MAX_IMAGE_SIZE);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String toString(int colorIndex) {
        return x + "," + y + "," + colorIndex;
    }
}
