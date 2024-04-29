package com.dotnomi.ultimateparticles.dto;

import com.dotnomi.ultimateparticles.constants.Config;

import java.io.File;

public class ImageDto {

    private File image;
    private int size;

    public ImageDto(File image, int size) {
        this.image = image;
        this.size = Math.min(Math.max(Config.MIN_IMAGE_SIZE, size), Config.MAX_IMAGE_SIZE);
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
        this.size = Math.min(Math.max(Config.MIN_IMAGE_SIZE, size), Config.MAX_IMAGE_SIZE);
    }
}
