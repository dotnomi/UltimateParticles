package com.dotnomi.ultimateparticles.dto;

import com.dotnomi.ultimateparticles.UltimateParticles;

import java.io.File;

public class ImageDto {

    private File image;
    private int size;

    private final int minImageSize, maxImageSize;

    public ImageDto(File image, int size) {
        minImageSize = UltimateParticles.getPluginConfig().getInt("image-settings.min-size");
        maxImageSize = UltimateParticles.getPluginConfig().getInt("image-settings.max-size");

        this.image = image;
        this.size = Math.min(Math.max(minImageSize, size), maxImageSize);
    }

    public File getImageFile() {
        return image;
    }

    public void setImageFile(File image) {
        this.image = image;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = Math.min(Math.max(minImageSize, size), maxImageSize);
    }
}
