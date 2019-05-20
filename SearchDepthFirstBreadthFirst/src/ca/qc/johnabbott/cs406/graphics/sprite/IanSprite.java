package ca.qc.johnabbott.cs406.graphics.sprite;

import ca.qc.johnabbott.cs406.graphics.TerrainDrawable;
import ca.qc.johnabbott.cs406.graphics.animation.Animated;

import javax.swing.*;
import java.awt.*;

/**
 * Sprite singleton class for "ian.png"
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-20
 */
public class IanSprite {

    private static final String IAN_FILE = "assets/ian.png";
    private static IanSprite instance;

    public static IanSprite getInstance() {
        if (instance == null)
            instance = new IanSprite();
        return instance;
    }

    private final Animated moveUpAnimation;
    private final Animated moveDownAnimation;
    private final Animated moveLeftAnimation;
    private final Animated moveRightAnimation;
    private Animated victoryAnimation;
    private Animated defeatAnimation;
    private final SpriteSheet sheet;

    private IanSprite() {
        Image image = (new ImageIcon(IAN_FILE)).getImage();
        sheet = new SpriteSheet(image, 32, 32);

        moveDownAnimation = sheet.getBuilder(4, - TerrainDrawable.getCellSize()/2, -TerrainDrawable.getCellSize(),
                                            TerrainDrawable.getCellSize() * 2, TerrainDrawable.getCellSize() * 2)
                .add(0, 0, 250f / 4)
                .add(1, 0, 250f / 4)
                .add(0, 0, 250f / 4)
                .add(2, 0, 250f / 4)
                .build();

        moveUpAnimation = sheet.getBuilder(4, - TerrainDrawable.getCellSize()/2, -TerrainDrawable.getCellSize(),
                TerrainDrawable.getCellSize() * 2, TerrainDrawable.getCellSize() * 2)
                .add(0, 1, 250f / 4)
                .add(1, 1, 250f / 4)
                .add(0, 1, 250f / 4)
                .add(2, 1, 250f / 4)
                .build();

        moveRightAnimation = sheet.getBuilder(4, - TerrainDrawable.getCellSize()/2, -TerrainDrawable.getCellSize(),
                TerrainDrawable.getCellSize() * 2, TerrainDrawable.getCellSize() * 2)
                .add(0, 2, 250f / 4)
                .add(1, 2, 250f / 4)
                .add(0, 2, 250f / 4)
                .add(2, 2, 250f / 4)
                .build();

        moveLeftAnimation = sheet.getBuilder(4, - TerrainDrawable.getCellSize()/2, -TerrainDrawable.getCellSize(),
                TerrainDrawable.getCellSize() * 2, TerrainDrawable.getCellSize() * 2)
                .add(0, 3, 250f / 4)
                .add(1, 3, 250f / 4)
                .add(0, 3, 250f / 4)
                .add(2, 3, 250f / 4)
                .build();

    }

    public Animated getMoveUpAnimation() {
        return moveUpAnimation;
    }

    public Animated getMoveDownAnimation() {
        return moveDownAnimation;
    }

    public Animated getMoveLeftAnimation() {
        return moveLeftAnimation;
    }

    public Animated getMoveRightAnimation() {
        return moveRightAnimation;
    }

    public Animated getVictoryAnimation(int x, int y) {
        victoryAnimation = sheet.getBuilder(4, x - TerrainDrawable.getCellSize()/2, y -TerrainDrawable.getCellSize(),
                TerrainDrawable.getCellSize() * 2, TerrainDrawable.getCellSize() * 2)
                .add(0, 4, 250f)
                .add(1, 4, 250f)
                .add(0, 4, 250f)
                .add(2, 4, 250f)
                .build();
        return victoryAnimation;
    }

    public Animated getDefeatAnimation(int x, int y) {

        defeatAnimation = sheet.getBuilder(8, x - TerrainDrawable.getCellSize()/2, y -TerrainDrawable.getCellSize(),
                TerrainDrawable.getCellSize() * 2, TerrainDrawable.getCellSize() * 2)
                .add(0, 5, 1000f)
                .add(1, 5, 250f)
                .add(2, 5, 250f)
                .add(0, 6, 250f)
                .add(1, 6, 250f)
                .add(2, 6, 250f)
                .add(0, 7, 250f)
                .add(1, 7, 10000f)
                .build();
        return defeatAnimation;
    }

    public SpriteSheet getSpriteSheet() {
        return sheet;
    }
}
