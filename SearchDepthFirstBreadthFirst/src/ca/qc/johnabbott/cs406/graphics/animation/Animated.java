/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.graphics.animation;

import ca.qc.johnabbott.cs406.graphics.Drawable;

/**
 * Animation interface for all animated components of the solver.
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public interface Animated extends Drawable {
    /**
     * Start the animation.
     */
    void start();

    /**
     * Determine if the animation is done.
     * @return
     */
    boolean isDone();

    /**
     * Update the animation given the elapsed time.
     * @param time time elapsed.
     */
    void animate(int time);
}
