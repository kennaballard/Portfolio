/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.graphics;

import ca.qc.johnabbott.cs406.terrain.Token;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;

import javax.swing.*;
import java.awt.*;

/**
 * A Drawable representation of a terrain
 * @author Ian Clement (ian.clement@johnabbott.qa.ca)
 */
public class TerrainDrawable implements Drawable {

    private static int cellSize = 32;

    public static void setCellSize(int cellSize) {
        TerrainDrawable.cellSize = cellSize;
    }

    public static int getCellSize() {
        return cellSize;
    }


    // the solution terrain
    private Terrain terrain;

    // the image of the start and end cell
    private Drawable start;
    private Drawable goal;

    // textures for terrain
    private Drawable wall;
    private Drawable floor;

    // draw the colors
    private boolean doColors;

    /**
     * Create a drawable terrain
     * @param terrain the terrain
     */
    public TerrainDrawable(Terrain terrain, Drawable start, Drawable goal, Drawable wall, Drawable floor, boolean doColors) {
        this.terrain = terrain;
        this.start = start;
        this.goal = goal;
        this.doColors = doColors;
        this.wall = wall;
        this.floor = floor;
    }

    @Override
    public void setObserver(JComponent observer) {
        start.setObserver(observer);
        goal.setObserver(observer);
    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY) {

        Graphics2D graphics2D = (Graphics2D)g;

        // draw all the walls and colors if asked
        for(int i = 0; i < terrain.getWidth(); i++)
            for(int j = 0; j < terrain.getHeight(); j++) {

                Location current = new Location(i, j);
                // draw wall in black
                if (terrain.getToken(current) == Token.WALL)
                    wall.draw(graphics2D, offsetX + i * cellSize, offsetY + j * cellSize);
                else
                    floor.draw(graphics2D, offsetX + i * cellSize, offsetY + j * cellSize);
                /*else if(doColors) {
                    switch(terrain.getColor(current)) {
                        case WHITE:
                            break;
                        case GREY:
                            graphics2D.setColor(Color.LIGHT_GRAY);
                            graphics2D.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                        case BLACK:
                            graphics2D.setColor(Color.DARK_GRAY);
                            graphics2D.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            break;
                    }
                }*/

            }

        // draw the start
        start.draw(graphics2D, offsetX + terrain.getStart().getX() * cellSize, offsetY + terrain.getStart().getY() * cellSize);

        // draw the goal
        goal.draw(graphics2D, offsetX + terrain.getGoal().getX() * cellSize, offsetY + terrain.getGoal().getY() * cellSize);

    }
}
