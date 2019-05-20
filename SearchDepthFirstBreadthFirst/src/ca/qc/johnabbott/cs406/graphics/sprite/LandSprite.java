package ca.qc.johnabbott.cs406.graphics.sprite;

import javax.swing.*;
import java.awt.*;

/**
 * Sprite singleton class for "land.png"
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-22
 */
public class LandSprite {

    private static final String LAND_FILE = "assets/land.png";
    private static LandSprite instance;
    private final SpriteSheet sheet;

    public static LandSprite getInstance() {
        if (instance == null)
            instance = new LandSprite();
        return instance;
    }

    private LandSprite() {
        Image image = (new ImageIcon(LAND_FILE)).getImage();
        sheet = new SpriteSheet(image, 32, 32);
    }

    public SpriteSheet getSpriteSheet() {
        return sheet;
    }
}