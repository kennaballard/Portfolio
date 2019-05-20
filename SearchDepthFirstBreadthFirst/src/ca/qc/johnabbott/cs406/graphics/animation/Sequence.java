/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.graphics.animation;

import ca.qc.johnabbott.cs406.collections.Queue;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Perform a sequence of animations.
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public class Sequence implements Animated {

    // store the permanent graphics sequence in a list
    private List<Animated> animation;

    // store the active graphics sequence in a queue with a current graphics.
    private Queue<Animated> animationQueue;
    private Animated current;

    // the observer is needed to render images.
    private JComponent observer;

    // the graphics capacity
    private int capacity;

    /**
     * Create a sequence with a max capacity.
     * @param capacity
     */
    public Sequence(int capacity) {
        this.capacity = capacity;
        animation = new LinkedList<>();
        animationQueue = new Queue<>(capacity);
    }

    /**
     * Add an graphics to the sequence.
     * @param animated
     */
    public void add(Animated animated) {
        animation.add(animated);
    }

    @Override
    public void start() {
        // empty the graphics queue
        while(!animationQueue.isEmpty())
            animationQueue.dequeue();

        // enqueue all the animations in the sequence
        for(Animated animated : animation)
            animationQueue.enqueue(animated);

        // start the first graphics, if there is one.
        if(!animationQueue.isEmpty()) {
            current = animationQueue.dequeue();
            current.start();
        }
    }

    @Override
    public boolean isDone() {
        return current.isDone() && animationQueue.isEmpty();
    }

    @Override
    public void animate(int time) {
        if(current.isDone()) {
            if(animationQueue.isEmpty())
                return;
            current = animationQueue.dequeue();
            current.start();
        }
        current.animate(time);
    }

    @Override
    public void setObserver(JComponent observer) {
        this.observer = observer;

        // set the observer
        Queue<Animated> tmp = new Queue<>(capacity);
        while(!animationQueue.isEmpty()) {
            Animated a = animationQueue.dequeue();
            a.setObserver(observer);
            tmp.enqueue(a);
        }

        while(!tmp.isEmpty())
            animationQueue.enqueue(tmp.dequeue());

    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY) {
        current.draw(g, offsetX, offsetY);
    }
}
