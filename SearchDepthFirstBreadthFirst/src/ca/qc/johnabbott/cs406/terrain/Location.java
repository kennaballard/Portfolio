/*
 * Copyright (c) 2017 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.terrain;

import ca.qc.johnabbott.cs406.generator.Generator;

import java.util.Random;

/**
 * <p>Represents a position in the terrain. A location never changes (notice there is no setter for x and y coordinates), but
 *    you can "update" a location using the move method:
 * </p>
 *  <pre>
 *  Location pos = terrain.getStart();
 *  pos = pos.get(Direction.UP);
 *  </pre>
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class Location {

    /**
     * Generates random locations with the width and height boundaries.
     */
    private static class LocationGenerator implements Generator<Location> {

        private int width;
        private int height;

        public LocationGenerator(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public Location generate(Random random) {
            return new Location(random.nextInt(width), random.nextInt(height));
        }
    }

    /**
     * Get a location generator for a given terrain.
     * @param terrain
     * @return
     */
    public static Generator<Location> generator(Terrain terrain) {
        return new LocationGenerator(terrain.getWidth(), terrain.getHeight());
    }

    private int x;
    private int y;

    public Location() {
        this.x = this.y = 0;
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x-coordinate of the location.
     * @return the x-coordinate of the location.
     * @precondition None.
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y-coordinate of the location.
     * @return the y-coordinate of the location.
     * @precondition None.
     */
    public int getY() {
        return y;
    }



    /**
     * Get the new location when moving in the given direction.
     * @param direction the direction to get.
     * @return the new location.
     * @precondition None.
     */
    public Location get(Direction direction) {
        Location location = this.clone();
        switch(direction) {
            case UP:
                location.setY( location.getY() - 1 ); break;
            case DOWN:
                location.setY( location.getY() + 1 ); break;
            case LEFT:
                location.setX( location.getX() - 1 ); break;
            case RIGHT:
                location.setX( location.getX() + 1 ); break;
        }
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (x != location.x) return false;
        return y == location.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * Calculate the distance from the current location to another position.
     * @param rhs the position to solve the distance to.
     * @return the straight-line distance between the two points as a double.
     * @precondition None.
     */
    public double dist(Location rhs) {
        return Math.sqrt(Math.pow((double) x - (double) rhs.x, 2) + Math.pow((double) y - (double)rhs.y, 2));
    }

    /**
     * Get the String representation of the location.
     * @return the String representation of the location.
     * @precondition None.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // setters are private

    private void setY(int y) {
        this.y = y;
    }

    private void setX(int x) {
        this.x = x;
    }

    @Override
    protected Location clone()  {
        return new Location(this.x, this.y);
    }
}
