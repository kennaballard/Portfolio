/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.graphics;

import ca.qc.johnabbott.cs406.graphics.animation.Animated;
import ca.qc.johnabbott.cs406.graphics.animation.Constant;
import ca.qc.johnabbott.cs406.graphics.animation.Loop;
import ca.qc.johnabbott.cs406.graphics.animation.Sequence;
import ca.qc.johnabbott.cs406.graphics.sprite.CoffeeSprite;
import ca.qc.johnabbott.cs406.graphics.sprite.IanSprite;
import ca.qc.johnabbott.cs406.graphics.sprite.LandSprite;
import ca.qc.johnabbott.cs406.search.Search;
import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Create an animated solution to the search problem
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public class GraphicalTerrain {

    // the canvas displays the solution
    private Canvas canvas;

    // the terrain with the solution set
    private Terrain terrain;

    /**
     * Construct and run the animated solution
     * @param terrain
     */
    private GraphicalTerrain(Terrain terrain, Search search, int width, int height, boolean doColors) {
        this.terrain = terrain;

        TerrainDrawable.setCellSize(Math.min(width / (terrain.getWidth() + 2), height / (terrain.getHeight() + 2)));

        IanSprite ianAnimation = IanSprite.getInstance();
        CoffeeSprite coffeeAnimation = CoffeeSprite.getInstance();
        LandSprite landAnimation = LandSprite.getInstance();

        Animated startAnimation = new Constant(coffeeAnimation.getSpriteSheet().getSheet(3, 0));
        Animated goalAnimation = new Loop(coffeeAnimation.getFullCoffeeAnimation());

        Drawable floor = landAnimation.getSpriteSheet().getSheet(0, 0);
        Drawable wall = landAnimation.getSpriteSheet().getSheet(0, 1);

        // initalize all drawables: the terrain with familiar graphics!
        List<Drawable> drawables = new LinkedList<>();
        drawables.add(new TerrainDrawable(terrain, startAnimation, goalAnimation, wall, floor, doColors));

        List<Animated> animations = new LinkedList<>();

        animations.add(startAnimation);
        animations.add(goalAnimation);

        Sequence seq = new Sequence(terrain.getWidth() * terrain.getHeight());

        Location current = terrain.getStart();

        if(search.hasSolution()) {

            Location goal = terrain.getGoal();

            // Follow the solution path from the start to the goal

            int limit = terrain.getHeight() * terrain.getWidth();

            // TODO: remove limit?
            search.reset();
            while (!current.equals(goal) && limit >= 0 && search.hasNext()) {
                limit--;

                // get the fromDirection and the position to get to
                Direction to = search.next();

                if (to == null || to == Direction.NONE)
                    break;

                Location next = current.get(to);

                // add a new Step graphics from the current position to the next position
                Animated animation = null;
                switch (to) {
                    case UP:
                        animation = ianAnimation.getMoveUpAnimation();
                        break;
                    case DOWN:
                        animation = ianAnimation.getMoveDownAnimation();
                        break;
                    case RIGHT:
                        animation = ianAnimation.getMoveRightAnimation();
                        break;
                    case LEFT:
                        animation = ianAnimation.getMoveLeftAnimation();
                        break;
                }
                seq.add(new Step(current, next, 250, animation));

                // step to the next position
                current = next;
            }

        }


        if(current.equals(terrain.getGoal()))
            seq.add(new Loop(ianAnimation.getVictoryAnimation(current.getX() * TerrainDrawable.getCellSize(), current.getY() * TerrainDrawable.getCellSize())));
        else
            seq.add(ianAnimation.getDefeatAnimation(current.getX() * TerrainDrawable .getCellSize(), current.getY() * TerrainDrawable.getCellSize()));


        // add the movement sequence to the animations and the drawables
        animations.add(seq);
        drawables.add(seq);

        // create a Swing frame to contain the solution canvas
        JFrame frame = new JFrame();
        frame.setTitle("Explorer!");
        frame.setBounds(0, 0, width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // initialize the canvas with the drawables and animations.
        canvas = new Canvas(drawables, animations);

        // include the canvas in the frame and show to the user
        canvas.setFocusable(true);
        frame.add(canvas);
        frame.setVisible(true);
    }

    /**
     * Run the animated solution using the solution contained in the provided terrain.
     * @param terrain contains the solution.
     */
    public static void run(Terrain terrain, int width, int height, boolean doColors) {
        new GraphicalTerrain(terrain, null, width, height, doColors);
    }

    /**
     * Run the animated solution using the solution contained in the provided terrain.
     * @param terrain contains the solution.
     */
    public static void run(Terrain terrain, Search search, int width, int height, boolean doColors) {
        new GraphicalTerrain(terrain, search, width, height, doColors);
    }

}
