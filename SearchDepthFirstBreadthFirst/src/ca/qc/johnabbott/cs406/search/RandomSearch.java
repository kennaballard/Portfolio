/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;

import java.util.Random;

/**
 * A random "search" for the goal.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 * @since 2017-02-08
 */
public class RandomSearch implements Search {

    private Memory memory;

    private final int maxMoves;

    private Location solution;
    private Location start;
    private Location goal;
    private int moves;

    public RandomSearch(int maxMoves) {
        this.maxMoves = maxMoves;
    }

    public int getMaxMoves() {
        return maxMoves;
    }

    @Override
    public void solve(Terrain terrain) {

        memory = new Memory(terrain.getWidth(), terrain.getHeight());
        start = terrain.getStart();
        goal = terrain.getGoal();

        Location current = terrain.getStart();

        Random random = new Random();

        for (int i = 0; i < maxMoves; i++) {

            // create a list of possible directions
            Direction[] directions = new Direction[4];
            int count = 0;

            // check all directions for possible moves.
            for(Direction direction : Direction.getClockwise()) {
                Location next = current.get(direction);
                if(!terrain.isBlocked(next) && memory.getColor(next) == Color.WHITE)
                        directions[count++] = direction;

            }

            // dead end... abandon search
            if(count == 0)
                break;


            // choose a random direction
            Direction randomDirection = directions[random.nextInt(count)];
            memory.setToDirection(current, randomDirection);
            current = current.get(randomDirection);
            memory.setColor(current, Color.BLACK);
            if (current.equals(goal))
                break;
        }

    }

    @Override
    public boolean hasSolution() {
        // consider all random paths a solution... even if the goal isn't found.
        return true;
    }

    @Override
    public void reset() {
        solution = start;
        moves = 0;
    }

    @Override
    public Direction next() {
        Direction direction = memory.getToDirection(solution);
        solution = solution.get(direction);
        moves++;
        return direction;
    }

    @Override
    public boolean hasNext() {
        return !solution.equals(goal) || moves > maxMoves;
    }
}
