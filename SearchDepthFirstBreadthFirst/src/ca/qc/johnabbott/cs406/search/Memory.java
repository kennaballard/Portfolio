/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Token;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Stores the solution to the search problem. At each position, you can set:
 *
 * <ul>
 *     <li>A color (WHITE, GREY, BLACK).</li>
 *     <li>A direction "from" used to indicate how a node is discovered.</li>
 *     <li>A direction "to" to store the solution path.</li>
 * </ul>
 *
 * </p>
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-10
 */
public class Memory {

    /**
     * Store individual information at a specific position.
     */
    private static class Node {
        public Color color;
        public Direction to;
        public Direction from;

        public Node() {
            color = Color.NONE;
            to = Direction.NONE;
            from = Direction.NONE;
        }

        public Node(Color color) {
            this();
            this.color = color;
        }

    }

    // fields
    private Map<Location, Node> nodes;
    private int width;
    private int height;

    /**
     * Create a solution of a specific size.
     * @param width
     * @param height
     */
    public Memory(int width, int height) {
        this.width = width;
        this.height = height;
        nodes = new HashMap<>();
    }

    // checks to see if a node isn't already at `position`, if not,
    // create one.
    private void makeNodeIfNecessary(Location location) {
        if(!nodes.containsKey(location))
            nodes.put(location, new Node(Color.WHITE));
    }

    /**
     * Retrieve the color at certain position.
     * @param location
     * @return the Color at the position.
     * @precondition The position is in the terrain.
     */
    public Color getColor(Location location) {
        makeNodeIfNecessary(location);
        return nodes.get(location).color;
    }

    /**
     * Retrieve the fromDirection at certain position.
     * @param location
     * @return the Direction at the position.
     * @precondition The position is in the terrain.
     */
    public Direction getFromDirection(Location location) {
        makeNodeIfNecessary(location);
        return nodes.get(location).from;
    }

    /**
     * Retrieve the "to" fromDirection at certain position.
     * @param location
     * @return the Direction at the position.
     * @precondition The position is in the terrain.
     */
    public Direction getToDirection(Location location) {
        makeNodeIfNecessary(location);
        return nodes.get(location).to;
    }

    /**
     * Set the color at a position.
     * @param location
     * @param color
     * @precondition The position is in the terrain.
     */
    public void setColor(Location location, Color color) {
        makeNodeIfNecessary(location);
        nodes.get(location).color = color;
    }

    /**
     * Set the fromDirection at a position.
     * @param location
     * @param fromDirection
     * @precondition The position is in the terrain.
     */
    public void setFromDirection(Location location, Direction fromDirection) {
        makeNodeIfNecessary(location);
        nodes.get(location).from = fromDirection;
    }

    /**
     * Set the "to" fromDirection at a position.
     * @param location
     * @param toDirection
     * @precondition The position is in the terrain.
     */
    public void setToDirection(Location location, Direction toDirection) {
        makeNodeIfNecessary(location);
        nodes.get(location).to = toDirection;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(Token.BORDER_DOWN_AND_RIGHT);
        for(int j = 0; j < width; j++) builder.append(Token.BORDER_HORIZONTAL);
        builder.append(Token.BORDER_DOWN_AND_LEFT + "\n");

        for(int i = 0; i < height; i++) {
            builder.append(Token.BORDER_VERTICAL);
            for(int j = 0; j < width; j++) {

                Location pos = new Location(j, i);
                if(nodes.containsKey(pos)) {
                    switch (nodes.get(pos).color) {
                        case GREY:
                            builder.append(Token.GREY_TOKEN);
                            break;
                        case BLACK:
                            builder.append(Token.BLACK_TOKEN);
                            break;
                        default:
                            builder.append(Token.EMPTY);
                    }
                }
                else
                    builder.append(Token.UNKNOWN);

            }
            builder.append(Token.BORDER_VERTICAL + "\n");
        }

        builder.append(Token.BORDER_UP_AND_RIGHT);
        for(int j = 0; j < width; j++) builder.append(Token.BORDER_HORIZONTAL);
        builder.append(Token.BORDER_UP_AND_LEFT + "\n");
        return builder.toString();
    }
}
