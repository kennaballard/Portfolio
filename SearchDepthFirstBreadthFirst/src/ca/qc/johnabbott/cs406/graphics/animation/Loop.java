package ca.qc.johnabbott.cs406.graphics.animation;

import javax.swing.*;
import java.awt.*;

/**
 * Loop and animation.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-20
 */
public class Loop implements Animated {

    private Animated animation;

    public Loop(Animated animation) {
        this.animation = animation;
    }

    @Override
    public void start() {
        animation.start();
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public void animate(int time) {
        if(animation.isDone())
            animation.start();
        animation.animate(time);
    }

    @Override
    public void setObserver(JComponent observer) {
        animation.setObserver(observer);
    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY) {
        animation.draw(g, offsetX, offsetY);
    }
}
