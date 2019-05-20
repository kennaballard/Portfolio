package ca.qc.johnabbott.cs406.graphics.animation;

import ca.qc.johnabbott.cs406.graphics.Drawable;

import javax.swing.*;
import java.awt.*;

/**
 * Create an constant animation out of a drawable.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-20
 */
public class Constant implements Animated {
    private Drawable drawable;

    public Constant(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void start() {

    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public void animate(int time) {

    }

    @Override
    public void setObserver(JComponent observer) {
        drawable.setObserver(observer);
    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY) {
        drawable.draw(g, offsetX, offsetY);
    }
}
