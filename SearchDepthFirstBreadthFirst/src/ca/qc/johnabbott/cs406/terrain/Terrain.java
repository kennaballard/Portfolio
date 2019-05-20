/*
 * Copyright (c) 2018 Ian Clement.  All rights reserved.
 */

package ca.qc.johnabbott.cs406.terrain;

import ca.qc.johnabbott.cs406.generator.Generator;
import ca.qc.johnabbott.cs406.search.Search;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import static ca.qc.johnabbott.cs406.terrain.Token.WALL;

/**
 * <p>This class represents a "terrain" for Search lab in the course
 * 420-406-AB Data Structures. A terrain is a 2D grid containing paths
 * and obstacles for an agent to navigate.</p>
 *
 * <p>Input file format: A terrain is stored in a text file. The first
 * two lines indicate the width and height of the terrain. The
 * remaining lines give the "map" of the terrain using tokens.</p>
 *
 * <p>List of tokens: </p>
 *
 * <table summary="symbols">
 *     <tr><td><tt>#</tt></td><td>cell is a wall.</td></tr>
 *     <tr><td><tt>@</tt></td><td>starting position of agent.</td></tr>
 *     <tr><td><tt>X</tt></td><td>goal position.</td></tr>
 *     <tr><td>(space)</td><td>an empty cell that an agent can move to.</td></tr>
 * </table>

 *
 * <p>Example:</p>
 *
 *    <pre style="border-style: solid; border-width: 2px">
 *    5
 *    5
 *    &#64; #
 *    #   #
 *    # # #
 *    #   #
 *    # X
 *    </pre>
 *
 *
 * <p>See the samples in the 'test' directory.</p>
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class Terrain {

    // if you have problems with the path drawing characters, set this to true
    public static final boolean SIMPLE_PATH = false;

    /**
     * Create a random terrain generator.
     * @param width width of terrain.
     * @param height height of terrain.
     * @param density the
     * @return
     */
    public static Generator<Terrain> generator(int width, int height, double density, int clusters) {
        TerrainGenerator generator = new TerrainGenerator(width, height);
        generator.setClusters(clusters);
        generator.setWallDensity(density);
        return generator;
    }

    /**
     * Genrates a random terrain.
     */
    private static class TerrainGenerator implements Generator<Terrain> {

        private static final int MINIMUM_CLUSTERS = 5;
        private static final double MAX_DENSITY = 0.8;
        private static final int CHANCE_OF_NEW_CLUSTER = 0;
        private final int width;
        private final int height;
        private double wallDensity;
        private int clusters;

        /**
         * Create a random terrain generator
         * @param width the width of the terrain
         * @param height the height of the terrain
         */
        public TerrainGenerator(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Set the density of walls in the terrain.
         * @param wallDensity the percent of walls in the terrain.
         */
        public void setWallDensity(double wallDensity) {
            this.wallDensity = wallDensity;
        }

        /**
         * Set the number of wall clusters in the terrain.
         * @param clusters
         */
        public void setClusters(int clusters) {
            this.clusters = clusters;
        }

        // check for walls in cells surrounding (x,y).
        private boolean wallAdjacent(Terrain terrain, int x, int y) {
            for(int i = -1; i <= 1; i++)
                for(int j = -1; j <= 1; j++) {
                    if(i == j)
                        continue;
                    int k = (y + i) * terrain.width + x + j;
                    if(k >=0 && k < terrain.map.length && terrain.map[k] == WALL)
                        return true;
                }

            return false;
        }

        @Override
        public Terrain generate(Random random) {

            // limit wall density to 50%
            wallDensity = Math.min(wallDensity, MAX_DENSITY);

            Terrain terrain = new Terrain(width, height);

            int totalCells = width * height;
            int totalWalls = (int)(totalCells * wallDensity);

            Generator<Location> generator = Location.generator(terrain);

            // track "clusters" as sets: each set hold possibilities for the
            // next cell in the cluster, called the cluster's "frontier".
            Set<Location>[] clusters = (HashSet<Location>[]) new HashSet[MINIMUM_CLUSTERS];

            // Initialize the clusters

            for(int i = 0; i < MINIMUM_CLUSTERS; i++)
                clusters[i] = new HashSet<>();

            // each cluster starts as a random location.
            for(int i = 0; i < clusters.length; i++) {

                // find a non-blocked location.
                Location location;
                do {
                    location = generator.generate(random);
                } while(terrain.isBlocked(location));

                terrain.setToken(location, WALL);

                // add adjacent cells that are not part of another cluster to the current cluster's "frontier".
                for(Direction direction : Direction.getClockwise()) {
                    Location next = null;
                    boolean firstTime = addAdjacent(next, location, direction, terrain, clusters);
                    if(firstTime)
                        clusters[i].add(next);
                }

            }

            /*
              Add remaining walls.
               - pick a random cluster and a random cell in the cluster "frontier"
               - add a wall
               - add adjacent cells to the "frontier".
             */
            for(int i = MINIMUM_CLUSTERS; i <= totalWalls; i++) {

                // choose a random cluster.
                Set<Location> cluster = clusters[random.nextInt(MINIMUM_CLUSTERS)];
                if(cluster.size() == 0) {
                    i--;
                    continue;
                }

                // choose a random location in the "frontier"
                Location location = null;
                int pos = random.nextInt(cluster.size());
                for(Location tmp : cluster) {
                    if (pos == 0)
                        location = tmp;
                    pos--;
                }

                terrain.setToken(location, WALL);
                cluster.remove(location);

                // add adjacent cells that are not part of another cluster to the current cluster's "frontier".
                for(Direction direction : Direction.getClockwise()) {
                    Location next = null;
                    boolean firstTime = addAdjacent(next, location, direction, terrain, clusters);
                    if(firstTime)
                        cluster.add(next);
                }
            }

            // add start
            while(terrain.start == null) {
                int r = random.nextInt(totalCells);
                if(terrain.map[r] == Token.EMPTY) {
                    terrain.map[r] = Token.START;
                    terrain.start = new Location(r % width, r / width);
                }

            }

            // add goal
            while(terrain.goal == null) {
                int r = random.nextInt(totalCells);
                if(terrain.map[r] == Token.EMPTY) {
                    terrain.map[r] = Token.GOAL;
                    terrain.goal = new Location(r % width, r / width);
                }
            }

            return terrain;
        }
    }

    // add adjacent cells that are not part of another cluster to the current cluster's "frontier".
    private static boolean addAdjacent(Location next, Location location, Direction direction, Terrain terrain, Set<Location>[] clusters){
        next = location.get(direction);
        if(terrain.isBlocked(next))
            return false;

        boolean firstTime = true;
        for(Set<Location> cluster2 : clusters)
            if(cluster2.contains(next))
                firstTime = false;

        return firstTime;
    }

    // the width and height as specified in the terrain file.
    private int width;
    private int height;

    // record the start and goal for the terrain, if they exist.
    private Location start;
    private Location goal;

    // the "map" contains all state information for the terrain.
    private Token[] map;

    /**
     * Load the terrain from file.
     * @param filename
     * @throws FileNotFoundException
     * @precondition The file provided contains valid terrain information.
     */
    public Terrain(String filename) throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileReader(filename));

        width = scanner.nextInt();
        height = scanner.nextInt();

        // initialize terrain's state using dynamic allocation
        map = new Token[width * height];
        for(int i = 0; i < map.length; i++)
            map[i] = Token.EMPTY;

        // read
        String line;
        int currentX, currentY = 0;
        line = scanner.nextLine(); // read rest of line
        boolean hasStart = false, hasGoal = false;

        // read the input stream line by line and create corresponding map row
        while(scanner.hasNext()) {
            line = scanner.nextLine();

            // read the line token by token and initialize the state
            for(currentX = 0; currentX < line.length(); currentX++) {

                Token token = Token.fromChar(line.charAt(currentX));
                map[currentY * width + currentX] = token;

                // record start and goal, and set walls as uncolorable.
                switch(token) {
                    case START:
                        start = new Location(currentX, currentY);
                        hasStart = true;
                        break;
                    case GOAL:
                        goal = new Location(currentX, currentY);
                        hasGoal = true;
                        break;
                }
            }
            currentY++;
        }
        scanner.close();

        // all terrains need a start and a goal(?)
        if(!hasGoal || !hasStart)
            throw new TerrainFileException();
    }

    /**
     * Create an empty terrain.
     * @param width
     * @param height
     */
    private Terrain(int width, int height) {
        this.width = width;
        this.height = height;

        // initialize terrain's state using dynamic allocation
        map = new Token[width * height];
        for(int i = 0; i < map.length; i++)
            map[i] = Token.EMPTY;

    }

    // convert the position to the `map` array position.
    private int positionToIndex(Location location) {
        return location.getY() * width + location.getX();
    }

    /**
     * Retrieve the token at certain position.
     * @param location
     * @return the Token at the position.
     * @precondition The position is in the terrain.
     */
    public Token getToken(Location location) {
        if(!inTerrain(location))
            throw new TerrainBoundsException();
        return map[positionToIndex(location)];
    }



    /**
     * Set the token at a position.
     * @param location
     * @param token
     * @precondition The position is in the terrain.
     */
    public void setToken(Location location, Token token)  {
        if(!inTerrain(location))
            throw new TerrainBoundsException();
        map[positionToIndex(location)] = token;
    }

    /**
     * Check that a terrain position is blocked from movement.
     * A blocked position is either off the terrain or a wall.
     * @param location
     * @return
     */
    public boolean isBlocked(Location location) {
        if(!inTerrain(location))
            return true;
        return getToken(location) == WALL;
    }

    /**
     * Reset the terrain to it's initial values: unless it's a wall, the color of each cell is revert to WHITE and the token is revert to EMPTY.
     * @precondition None.
     */
    public void revert() {
        if(map == null)
            return;
        for(int i = 0; i < height * width; i++)
            if(map[i] != WALL && map[i] != Token.START && map[i] != Token.GOAL)
                map[i] = Token.EMPTY;
    }

    /**
     * Get the terrain's width.
     * @return the terrain's width.
     */
    public int getWidth() { return this.width; }

    /**
     * Get the terrain's height.
     * @return the terrain's height.
     */
    public int getHeight() { return this.height; }

    /**
     * Get the terrain's start position.
     * @return the terrain's start position.
     */
    public Location getStart() { return start; }

    /**
     * Get the terrain's goal position.
     * @return the terrain's goal position.
     */
    public Location getGoal() { return goal; }

    /**
     * Get the string representation of the terrain (without colors).
     * @return the string representation of the terrain (without colors)
     */
    @Override
    public String toString() { return toString(false); }

    /**
     * Get the string representation of the terrain.
     * @param colors display the node colors at empty positions.
     * @return the string representation of the terrain.
     */
    public String toString(boolean colors)  {
        if(map == null)
            return "";


        StringBuilder builder = new StringBuilder();
        builder.append(Token.BORDER_DOWN_AND_RIGHT);
        for(int j = 0; j < width; j++) builder.append(Token.BORDER_HORIZONTAL);
        builder.append(Token.BORDER_DOWN_AND_LEFT + "\n");

        for(int i = 0; i < height; i++) {
            builder.append(Token.BORDER_VERTICAL);
            for(int j = 0; j < width; j++)
                builder.append(map[i * width + j]);
            builder.append(Token.BORDER_VERTICAL + "\n");
        }

        builder.append(Token.BORDER_UP_AND_RIGHT);
        for(int j = 0; j < width; j++) builder.append(Token.BORDER_HORIZONTAL);
        builder.append(Token.BORDER_UP_AND_LEFT + "\n");
        return builder.toString();
    }

    // Determine if a position is within the terrain.
    private boolean inTerrain(Location location) {
        if(map == null)
            throw new TerrainFileException();
        return location.getX() >= 0 && location.getX() < width
                && location.getY() >= 0 && location.getY() < height;
    }

    /**
     * Place tokens on the terrain to indicate the solution path
     * @param solution A search algorithm generated solution.
     * @throws TerrainFileException
     * @throws TerrainBoundsException
     */
    public void applySolution(Search solution) {

        // from the start, follow the to directions and place the tokens appropriately.
        Location current = start;
        Direction to = Direction.NONE;

        // until we reach the goal or we max out
        // TODO: remove limit?
        int limit = height * width;
        solution.reset();
        while(!current.equals(goal) && limit >= 0 && solution.hasNext()) {
            limit--;

            Direction previousTo = to;
            to = solution.next();
            if(to == null || to == Direction.NONE)
                break;

            // for the positions that are not the start or goal, overwrite the token with the
            // appropriate path character.
            if(!current.equals(start) && !current.equals(goal)) {
                if (SIMPLE_PATH)
                    setToken(current, Token.PATH);
                else
                    setToken(current, Token.path[previousTo.opposite().ordinal()][to.ordinal()]);
            }

            // get to the next position
            current = current.get(to);
        }

    }


}
