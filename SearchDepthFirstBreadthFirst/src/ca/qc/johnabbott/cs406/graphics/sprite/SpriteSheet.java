package ca.qc.johnabbott.cs406.graphics.sprite;

import ca.qc.johnabbott.cs406.graphics.Drawable;
import ca.qc.johnabbott.cs406.graphics.TerrainDrawable;
import ca.qc.johnabbott.cs406.graphics.animation.Animated;
import ca.qc.johnabbott.cs406.graphics.animation.Sequence;

import javax.swing.*;
import java.awt.*;

/**
 * Creates a sprite-sheet out of an image, capable of defining drawables and animation.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-17
 */
public class SpriteSheet {

    private Image sheet;

    // the image is divided into cells
    private int cellWidth;
    private int cellHeight;

    public SpriteSheet(Image sheet, int cellWidth, int cellHeight) {
        this.sheet = sheet;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
    }

    /**
     * Get the cell at (x,y)
     * @param x
     * @param y
     * @return
     */
    public Drawable getSheet(int x, int y) {
        return new CellDrawable(x, y);
    }

    /**
     * Get the cell at (x,y) and drawing at (offsetX, offsetY)  with the width and height defined.
     * @param x
     * @param y
     * @param offsetX
     * @param offsetY
     * @param width
     * @param height
     * @return
     */
    public Drawable getSheet(int x, int y, int offsetX, int offsetY, int width, int height) {
        return new CellDrawable(x, y, offsetX, offsetY, width, height);
    }

    /**
     * A drawable class for a subsection of the sprite sheet.
     */
    private class CellDrawable implements Drawable {

        private JComponent observer;
        private int x;
        private int y;
        private int offsetX;
        private int offsetY;
        private int width;
        private int height;

        public CellDrawable(int x, int y) {
            this(x, y, 0, 0, TerrainDrawable.getCellSize(), TerrainDrawable.getCellSize());
        }

        public CellDrawable(int x, int y, int offsetX, int offsetY, int width, int height) {
            this.x = x * cellWidth;
            this.y = y * cellHeight;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.width = width;
            this.height = height;
        }


        @Override
        public void setObserver(JComponent observer) {
            this.observer = observer;
        }

        @Override
        public void draw(Graphics g, int offsetX, int offsetY) {
            g.drawImage(sheet, offsetX + this.offsetX, offsetY + this.offsetY, offsetX + this.offsetX + width, offsetY + this.offsetY + height,
                               x, y, x + cellWidth, y + cellHeight, observer);
        }
    }

    /**
     * Get an animation builder for the sprite sheet.
     * @param size the number of frames in the animation.
     * @param offsetX the x-coordinate offset for drawing the animation
     * @param offsetY the y-coordinate offset for drawing the animation
     * @param width the width used when drawing the animation
     * @param height the height used when drawing the animation
     * @return
     */
    public Builder getBuilder(int size, int offsetX, int offsetY, int width, int height) {
        return new Builder(size, offsetX, offsetY, width, height);
    }

    /**
     * A animation sequence builder for the current sprite sheet
     */
    public class Builder {

        private final int offsetX;
        private final int offsetY;
        private final int width;
        private final int height;
        private Sequence sequence;

        /**
         * Create an animation builder for the sprite sheet.
         * @param size the number of frames in the animation.
         * @param offsetX the x-coordinate offset for drawing the animation
         * @param offsetY the y-coordinate offset for drawing the animation
         * @param width the width used when drawing the animation
         * @param height the height used when drawing the animation
         */
        public Builder(int size, int offsetX, int offsetY, int width, int height) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.width = width;
            this.height = height;
            sequence = new Sequence(size);
        }

        /**
         * Add a cell as a frame to the animation.
         * @param x
         * @param y
         * @param duration
         * @return
         */
        public Builder add(int x, int y, float duration) {
            final Drawable drawable = getSheet(x, y, offsetX, offsetY, width, height);
            sequence.add(new Animated() {

                private float animationProgress;

                @Override
                public void start() {
                    animationProgress = 0.0f;
                }

                @Override
                public boolean isDone() {
                    return animationProgress >= duration;
                }

                @Override
                public void animate(int time) {
                    animationProgress += time;
                }

                @Override
                public void setObserver(JComponent observer) {
                    drawable.setObserver(observer);
                }

                @Override
                public void draw(Graphics g, int offsetX, int offsetY) {
                    drawable.draw(g, offsetX, offsetY);
                }
            });
            return this;
        }

        /**
         * Generate the animation sequence.
         * @return
         */
        public Animated build()  {
            return sequence;
        }
    }
}
