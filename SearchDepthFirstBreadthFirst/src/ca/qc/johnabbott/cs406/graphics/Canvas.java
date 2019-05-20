/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.graphics;

import ca.qc.johnabbott.cs406.graphics.animation.Animated;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A canvas for animating the solver.
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public class Canvas extends JComponent {

    // the speed of the graphics
    private static int ANIM_SPEED = 30;

    // store the drawales and the animations for the solution
    private List<Drawable> drawables;
    private List<Animated> animations;

    // for timing the animations
    private Timer animationTimer;

    /**
     * Initialize the solution canvas with drawables and animations
     * @param drawables
     * @param animations
     */
    public Canvas(List<Drawable> drawables, final List<Animated> animations) {

        // the canvas drawables also include animations
        this.drawables = drawables;

        // set the drawable observers to be the canvas.
        for(Drawable d : drawables)
            d.setObserver(this);

        // set the graphics observers to be the canvas
        this.animations = animations;
        for(Animated a : animations)
            a.setObserver(this);

        // create an graphics timer to call the animate() method on each graphics
        this.animationTimer = new Timer(ANIM_SPEED, e -> {
            for(Animated a : animations)
                a.animate(ANIM_SPEED);
            repaint();  // repaint the entire canvas.
        });

        // start the animations and start the timer
        for(Animated a : animations)
            a.start();
        this.animationTimer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw all drawables
        for(Drawable d : drawables)
            d.draw(g, TerrainDrawable.getCellSize(), TerrainDrawable.getCellSize());

    }

}

