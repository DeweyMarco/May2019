import java.util.ArrayList;
import java.util.Collections;

/**
 * Sudoku Solver 
 * 
 * @author Luan Roos
 * @version 1.0
 */

public class SudokuSolve {

  static final double P_ABANDON = 0.5;
  static final double P_PMX = 0.7;
  static final double P_MUTATE = 0.9;
  static final int NEST_SIZE = 15;
  static final int MAX_GENERATIONS = 50000;
  static final int NUM_NESTS = 40;
  static final int CROSS_MUTATION_GENERATION = 1;
  // EVERY HOW MANY GENERATIONS CROSS MUTATION
  // OCCURS
  static final int ABANDON_GENERATION = 50;
  // JUST USED FOR TESTING PURPOSES. SHOULD BE FALSE FOR
  // ACTUAL PROGRAM.
  static final boolean TEST_MODE = false;
  // prints nest stats if true
  private static ArrayList<SudokuGrid>[] nest;
  private static RandomBoardGenerator randBoard;
  private static SudokuGrid eggBest;
  private static SudokuGrid eggLastBest;
  private static SudokuGrid solveGrid;

  // Currently solveGrid is local variable in 
  // fuction, not kept in class. THough nest is. Wonder
  // whether best use of object orientation

  // MAKE SURE WE DON'T CALL ON AN ALREADY SOLVED GRID.
  // DON'T PREFILTER BOARD BEFORE SENDING TO THIS FUNCTION.
  
  /**
   * Boolean solver
   * 
   * @param grid SudokuGrid
   * @return boolean solved
   */
  
  public static boolean solve(SudokuGrid grid) {
    // int lowestCost = 100000; 
    // big number so first board smaller than it.
    int index = 0;
    boolean growAbandon = false;
    double p;
    SudokuGrid randomGridi, randomGridj;
    solveGrid = grid;
    nest = new ArrayList[NUM_NESTS];

    // initializing
    for (int i = 0; i < NUM_NESTS; i++) {
      nest[i] = new ArrayList<SudokuGrid>();
    }

    randBoard = new RandomBoardGenerator(solveGrid);
    solveGrid.prefilterGrid();

    // SOLVE WITH MULTIPLE NESTS
    for (int generations = 0; generations < MAX_GENERATIONS; generations++) {
      if (TEST_MODE) {
        System.out.println(generations);
      }

      // CODE FOR EVERY NEST:
      for (int l = 0; l < NUM_NESTS; l++) {
        // POPULATE WITH RANDOM BOARDS.
        while (nest[l].size() < NEST_SIZE) {
          nest[l].add(generateRandomBoard(solveGrid));
        }
        //FOR TESTING:
        if (TEST_MODE && generations == 0) {
          int average = 0;
          for (int i = 0; i < nest[l].size(); i++) {
            average += nest[l].get(i).getCost();
          }
          average = average / nest[l].size();
          System.out.println("Nest " + l + " average cost" + average);
        }
        
        // CHECKS IF SOLUTION FOUND
        if (nest[l].get(0).getCost() == 0) {
          grid.setEqual(nest[l].get(0));
          return true;
        }

        // SORTS BOARDS BY COST.
        //INSERTION SORT
        for (int i = 1; i < NEST_SIZE; i++) {
            for (int j = i; j > 0 && nest[l].get(j).getCost() 
                < nest[l].get(j - 1).getCost(); j--) {
                Collections.swap(nest[l], j, j - 1);
            }
        }

        // SETTING BEST AND LASTBEST EGGS.
        // IF FIRST GENERATION, LAST BEST.
        // CHOSEN TO BE SECOND BEST BOARD.
        eggLastBest = eggBest;
        eggBest = nest[l].get(0);
        if (generations == 0) {
          eggLastBest = nest[l].get(1);
        }

        // MUTATES ALL EGGS NOT BEST.
        for (int i = 1; i < NEST_SIZE; i++) { 
          // starts from one to avoid best egg.
          SudokuGrid mutatedEgg = mutate(nest[l].get(i));
          if (mutatedEgg.getCost() <= nest[l].get(i).getCost()) {
            nest[l].set(i, mutatedEgg);
          }
        }
      }
      /*******
       * END OF FOR EVERY NEST
       ********/
      // ONLY CROSS MUTATES EVERY ARBITRARY NUMBER 
      // OF GENERATIONS TO GIVE NESTS TIME TO EVOLVE.
      if (generations % CROSS_MUTATION_GENERATION == 0) {
        // CROSS MUTATING NESTS
        // Accelating cross mutation with multiple eggs...
        for (int i = 0; i < NUM_NESTS / 2; i++) {
          int nestIndexi = (int) (Math.random() * NUM_NESTS);
          double eggIndexi = Math.random();
          eggIndexi *= eggIndexi;
          randomGridi = nest[nestIndexi].get((int) (eggIndexi * NEST_SIZE));

          int nestIndexj = (int) (Math.random() * NUM_NESTS);
          double eggIndexj = Math.random();
          randomGridj = nest[nestIndexj].get((int) (eggIndexj * NEST_SIZE));

          if (randomGridi.getCost() < randomGridj.getCost()) {
            randomGridi.setEqual(randomGridj);
          }
        }
      } else if (generations % ABANDON_GENERATION == 0) { 
        // ABANDONING NESTS.
        ArrayList<Integer> nestCosts = new ArrayList<Integer>();
        int nestCost;
        int lowestCost = 10000;
        int lowestIndex = 0;
        // FOR EVERY NEST, GET NEST COST.
        for (int i = 0; i < NUM_NESTS; i++) {
          // FOR EVERY EGG INSIDE THE NEST
          nestCost = 0;
          for (int j = 0; j < NEST_SIZE; j++) {
            nestCost += nest[i].get(j).getCost();
          }
          nestCosts.add(Integer.valueOf(nestCost));
          // KEEP TRACK OF INDEX OF NEST WITH LOWEST COST
          if (nestCost < lowestCost) {
            lowestIndex = i;
          }
        }

        // ABANDON NESTS THAT AREN'T BEST.
        for (int i = 0; i < NUM_NESTS; i++) {
          if (i != lowestIndex) {
            if (Math.random() < P_ABANDON) {
              nest[i].clear();
            }
          }
        }
      }
    }

    if (TEST_MODE) {
      for (int l = 0; l < NUM_NESTS; l++) {
    
        int average = 0;
        for (int i = 0; i < nest[l].size(); i++) {
          average += nest[l].get(i).getCost();
        }
        average = average / nest[l].size();
        System.out.println("Nest " + l + " average cost" + average);

        for (int i = 0; i < nest[l].size(); i++) {
          System.out.println("Nest " + l + " Egg " + i + " cost: " 
        + nest[l].get(i).getCost());
        }
        
        System.out.println("BEST EGG IN NEST " + l + ":\n" + eggBest);
        System.out.println("\nBEST EGG COST, NEST " + l + ": " 
        + eggBest.getCost());
      }
    }

    return false;
  }

  /**
   * Generates a random board
   * assumes board already prefiltered
   * @param parentGrid SudokuGrid
   * @return SudokuGrid random board
   */
  
  public static SudokuGrid generateRandomBoard(SudokuGrid parentGrid) {
    return randBoard.nextRandomBoard();
  }
  
  /**
   * Does some sick mutations on that egg
   * 
   * @param egg SudokuGrid
   * @return SudokuGrid mutated board
   */

  private static SudokuGrid mutate(SudokuGrid egg) {
    SudokuGrid mutatedEgg;
    if (Math.random() < P_PMX) {
      mutatedEgg = GeometricOperators.pMXCrossover(egg, eggBest);
    } else {
      mutatedEgg = GeometricOperators.multiParentalSortingCrossover(
          egg, eggBest, eggLastBest);
    }
    if (Math.random() < P_MUTATE) {
      GeometricOperators.feasibleGeometricMutation(mutatedEgg, solveGrid);
    }
    return mutatedEgg;
  }
}
