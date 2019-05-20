/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.graphics;

import javax.swing.*;
import java.awt.*;

/**
 * Drawable interface
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public interface Drawable {
    /**
     * Set the observer for the drawable.
     * @param observer
     */
    void setObserver(JComponent observer);

    /**
     * Draws the drawable.
     * @param g the graphics used to draw the drawable.
     */
    void draw(Graphics g, int offsetX, int offsetY);
}
