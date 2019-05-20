package ca.qc.johnabbott.cs406.search;

import ca.qc.johnabbott.cs406.collections.Queue;
import ca.qc.johnabbott.cs406.terrain.Location;
import ca.qc.johnabbott.cs406.terrain.Terrain;
import ca.qc.johnabbott.cs406.terrain.Direction;

public class BFS implements Search {
    private Memory memory;
    private final int maxMoves;

    private Location solution;
    private Location start;
    private Location goal;
    private int moves;
    private boolean hasSolution;

    // CONSTRUCTOR
    public BFS(int max){
        maxMoves = max;
    }

    // METHODS

    // Solve the terrain --> find path from start to goal
    // Uses Breadth-First Search
    public void solve(Terrain terrain){
        memory = new Memory(terrain.getWidth(), terrain.getHeight());
        start = terrain.getStart();
        goal = terrain.getGoal();
        hasSolution = false;

        Location current = terrain.getStart();
        Queue<Location> visitQ = new Queue<>();

        for(int i = 0; i < maxMoves; i++) {
            Direction[] directions = Direction.getClockwise();

            // determine which directions are free
            checkDirections(directions, current, terrain, visitQ);

            visitQ.reset();
            while(!visitQ.isEmpty()){

                // visit each seen location in queue
                current = visitQ.dequeue();
                // check for solution
                if(current.equals(goal)){
                    hasSolution = true;
                    break;
                }

                // Otherwise check surrounding locations
                checkDirections(directions, current, terrain, visitQ);
            }

            // complete solution if goal was found
            if(hasSolution){
                // retrace solution
                while(!current.equals(start)) {
                    Direction to = memory.getFromDirection(current).opposite();
                    current = current.get(memory.getFromDirection(current));
                    memory.setToDirection(current, to);
                }
                break;
            }
        }
    }

    // Check all directions of specified location
    private void checkDirections(Direction[] directions, Location current, Terrain terrain, Queue<Location> visitQ){
        memory.setColor(current, Color.BLACK);
        for(Direction dir: directions){
            Location next = current.get(dir);

            // "see" first free direction
            if(!terrain.isBlocked(next) && memory.getColor(next) == Color.WHITE) {
                // Add to seen queue, mark it accordingly
                visitQ.enqueue(next);
                memory.setFromDirection(next, dir.opposite());
                memory.setColor(next, Color.GREY);
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
