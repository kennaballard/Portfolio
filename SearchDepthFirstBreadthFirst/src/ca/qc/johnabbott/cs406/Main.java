package ca.qc.johnabbott.cs406;

import ca.qc.johnabbott.cs406.generator.Generator;
import ca.qc.johnabbott.cs406.graphics.GraphicalTerrain;
import ca.qc.johnabbott.cs406.search.Search;
import ca.qc.johnabbott.cs406.search.UnknownAlgorithm;
import ca.qc.johnabbott.cs406.terrain.Terrain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * DESCRIBE THIS PROGRAM
 *
 * @author YOU
 */
public class Main {

    //public static final String CURRENT_SEARCH = "Random";
    public static final String CURRENT_SEARCH = "BFS";
    //public static final String CURRENT_SEARCH = "DFS";
    public static final boolean ANIMATION = true;

    public static void main(String[] args) throws IOException, UnknownAlgorithm {

        /*
         TODO: uncomment one of these static methods.
            - Test your algorithms using runFromFile(..). To change the terrain
              edit "run configuration" and specify a different file in "Program Arguments".

            - Try interesting terrains using runRandom(..).
         */
        runFromFile(args[0]);
        //runRandom();
    }

    /**
     * Run a random
     * @throws UnknownAlgorithm
     */
    public static void runRandom() throws UnknownAlgorithm {
        Scanner consoleInput = new Scanner(System.in);

        Generator<Terrain> generator = Terrain.generator(50, 25, 0.4, 6);
        Random random = new Random();

        boolean again;
        do {

            Terrain terrain = generator.generate(random);
            System.out.println(terrain);

            Search search = Search.getAlgorithm(CURRENT_SEARCH);
            search.solve(terrain);

            if (search.hasSolution()) {
                terrain.applySolution(search);
                System.out.println(terrain.toString(false));
            } else
                System.out.println("No solution.");

            if(ANIMATION)
                GraphicalTerrain.run(terrain, search, 1200, 800, false);

            // ask the user if they want to continue.
            boolean input;
            do {
                System.out.print("Run again [y/N]? ");
                String line = consoleInput.nextLine().trim().toLowerCase();

                // default is "N"
                if(line.isEmpty())
                    line = "n";

                input = line.matches("^[yn]$"); // valid input is "y" or "n"
                again = line.equals("y");

            } while(!input);

        } while(again);

    }

    /**
     * Run the terrain search from a file.
     * @param terrainFile
     * @throws FileNotFoundException
     * @throws UnknownAlgorithm
     */
    public static void runFromFile(String terrainFile) throws FileNotFoundException, UnknownAlgorithm {

        Terrain terrain = new Terrain(terrainFile);
        System.out.println(terrain);

        Search search = Search.getAlgorithm(CURRENT_SEARCH);
        search.solve(terrain);

        if (search.hasSolution()) {
            terrain.applySolution(search);
            System.out.println(terrain.toString(false));
        }
        else
            System.out.println("No solution.");

        if(ANIMATION)
            GraphicalTerrain.run(terrain, search, 1200, 800, false);

    }


}
