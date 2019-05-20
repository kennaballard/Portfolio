/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.graphics;

import ca.qc.johnabbott.cs406.graphics.animation.Animated;
import ca.qc.johnabbott.cs406.terrain.Location;

import javax.swing.*;
import java.awt.*;

/**
 * A step in the solution path graphics
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public class Step implements Animated {

    // the start and end position
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    // the current position in the graphics
    private float currentX;
    private float currentY;

    // the speed of the graphics
    private float speed;

    // the progress of the graphics, in percent between 0.0 and 1.0
    private float animationProgress;

    // the "observer" for drawing
    private JComponent observer;

    // the image to display
    private Drawable drawable;
    private Animated animation;

    /**
     * Create an graphics "step"
     * @param start
     * @param end
     * @param speed
     */
    public Step(Location start, Location end, float speed, Animated animation) {

        // scale the start position from units to pixels
        startX = (start.getX() + 1) * TerrainDrawable.getCellSize();
        startY = (start.getY() + 1) * TerrainDrawable.getCellSize();

        // same as above for end
        endX = (end.getX() + 1) * TerrainDrawable.getCellSize();
        endY = (end.getY() + 1) * TerrainDrawable.getCellSize();

        this.speed = speed;
        this.animation = animation;
    }

    @Override
    public void start() {
        animationProgress = 0.0f;
        animation.start();
    }

    @Override
    public boolean isDone() {
        return animationProgress >= 1.0f;
    }

    @Override
    public void setObserver(JComponent observer) {
        this.observer = observer;
        animation.setObserver(observer);
    }

    @Override
    public void animate(int time) {
        animationProgress += (float)time / speed;

        /*
		 * How this works:
		 *   when graphics starts ->
		 *      animationProgress == 0.0f and therefore positionX == (current.getCenterX() - offsetX)
		 *   when graphics ends ->
		 *      animationProgress == 1.0f and therefore positionX == (next.getCenterX() - offsetX)
		 */
        currentX = (float) startX * (1.0f - animationProgress)
                     +
                   (float) endX * animationProgress;

        currentY = (float) startY * (1.0f - animationProgress)
                     +
                   (float) endY * animationProgress;

        animation.animate(time);
    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY) {
        animation.draw(g, (int) currentX, (int) currentY);
    }
}
