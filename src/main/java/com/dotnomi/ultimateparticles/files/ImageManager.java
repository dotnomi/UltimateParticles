package com.dotnomi.ultimateparticles.files;

import com.dotnomi.ultimateparticles.UltimateParticles;
import com.dotnomi.ultimateparticles.dto.ImageDto;
import com.dotnomi.ultimateparticles.dto.PixelDto;
import org.bukkit.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageManager {

    private static final ImageManager instance = new ImageManager();
    private static Logger logger;
    private static File imageFolder;
    private static List<ImageDto> images;

    private ImageManager() {
        logger = UltimateParticles.getInstance().getLogger();
        imageFolder = new File(UltimateParticles.getInstance().getDataFolder(), "images");
        images = new ArrayList<>();
    }

    public static ImageManager getInstance() {
        return instance;
    }

    public void load() {
        images.clear();
        createImageFolderIfNotExists();

        for (File image : Objects.requireNonNull(imageFolder.listFiles())) {
            if (image.getName().toLowerCase().endsWith(".png")) {
                if (isImageValid(image)) {
                    logger.log(Level.INFO, "Loading image " + image.getName());
                    images.add(new ImageDto(image, getImageSize(image)));
                }
            }
        }
    }

    public List<String> getImageNames() {
        List<String> names = new ArrayList<>();

        for (ImageDto image : images) {
            names.add(image.getImageFile().getName().substring(0, image.getImageFile().getName().lastIndexOf(".")));
        }

        return names;
    }

    public ImageDto getImageDataByName(String imageName) {
        for (ImageDto image : images) {
            if (image.getImageFile().getName().substring(0, image.getImageFile().getName().lastIndexOf(".")).equals(imageName)) {
                return image;
            }
        }
        return null;
    }

    public PixelDto getPixelData(File image, int x, int y) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image);
            int rgba = bufferedImage.getRGB(x,y);
            int alpha = (rgba >> 24) & 255;
            int red = (rgba >> 16) & 255;
            int green = (rgba >> 8) & 255;
            int blue = rgba & 255;

            return new PixelDto(x, y, Color.fromARGB(alpha, red, green, blue));
        } catch (Exception exception) {
            return null;
        }
    }

    public Integer getImageSize(File image) {
        int[] size = new int[2];

        try {
            BufferedImage bufferedImage = ImageIO.read(image);
            size[0] = bufferedImage.getWidth();
            size[1] = bufferedImage.getHeight();

            if (size[0] != size[1]) return null;

            return size[0];
        } catch (Exception exception) {
            return null;
        }
    }

    public boolean isImageValid(File image) {
        if (getImageSize(image) == null || getPixelData(image, 0, 0) == null) {
            logger.log(Level.WARNING, "Invalid image " + image.getName());
            return false;
        };
        return true;
    }

    private void createImageFolderIfNotExists() {
        if (!imageFolder.exists()) {
            if (imageFolder.mkdirs()) {
                logger.log(Level.INFO, "Created image folder.");
            }
        }
    }
}
