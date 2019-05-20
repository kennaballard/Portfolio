package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.terrain.Direction;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;

public class DFS implements Search {
    private final int maxMoves;

    private Memory memory;
    private Location solution;
    private Location start;
    private Location goal;
    private int moves;
    private boolean hasSolution = false;

    // CONSTRUCTOR
    public DFS(int max){
        maxMoves = max;
    }

    // METHODS

    // Solve the terrain --> find path from start to goal
    //Uses Depth-First Search
    public void solve(Terrain terrain){
        memory = new Memory(terrain.getWidth(), terrain.getHeight());
        start = terrain.getStart();
        goal = terrain.getGoal();

        Location current = terrain.getStart();

        for(int i = 0; i < maxMoves; i++){
            Direction[] directions = Direction.getClockwise();
            boolean backTrack = true;


            for(Direction dir: directions){
                Location next = current.get(dir);

                // go in first free direction
                if(!terrain.isBlocked(next) && memory.getColor(next) == Color.WHITE){
                    // go there
                    backTrack = false;
                    memory.setToDirection(current, dir);
                    memory.setFromDirection(next, dir.opposite());
                    current = next;
                    memory.setColor(current, Color.BLACK);
                    break;
                }
                // if not check next direction
            }

            if(backTrack){
                // must backtrack --> path is dead end
                current = current.get(memory.getFromDirection(current));
            }

            if(current.equals(goal)) {
                hasSolution = true;
                break;
            }
        }
    }

    // does the terrain have a path to the goal
    public boolean hasSolution(){
        return hasSolution;
    }

    // returns next direction in solution
    public Direction next(){
        Direction direction = memory.getToDirection(solution);
        solution = solution.get(direction);
        moves++;
        return direction;
    }

    // returns whether the solution has a next step
    public boolean hasNext(){
        return !solution.equals(goal) || moves > maxMoves;
    }

    // start solution over from beginning
    public void reset(){
        solution = start;
        moves = 0;
    }

}
