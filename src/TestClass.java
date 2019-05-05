import java.io.IOException;

/**
 * The test mode of the Sudoku problem
 * for the developers use
 * 
 * @author Marco Dewey
 * @version 1.0.69
 */

class TestClass {
  
  /**
   * Prints parts of mutated boards as well as the 
   * cost of the boards and the number of generations
   * 
   * Running test allows for faster debuging and 
   * early error detection
   * 
   * @param args String 
   * @throws IOException On input error.
   */

  public static void main(String[] args) throws IOException {
    
    /*
     * SudokuGrid testGrid = new SudokuGrid
     *   (Input.getBoard(args[0])); 
     *   testGrid.prefilterGrid();
     *   System.out.println(testGrid); 
     *   RandomBoardGenerator randGen = new 
     *   RandomBoardGenerator(testGrid);
     * 
     * SudokuGrid testrandom = randGen.nextRandomBoard();
     * 
     * System.out.println("");
     * 
     * 
     * randGen.getRandomRow(0);
     * 
     * System.out.println(testrandom);
     */

    /*
     * long startTime = System.nanoTime(); 
     * int avg = 0; 
     * SudokuGrid testGrid = new SudokuGrid(Input.getBoard(args[0])); 
     * testGrid.prefilterGrid(); 
     * SudokuGrid randomGrid;
     * RandomBoardGenerator randBoard = new RandomBoardGenerator(testGrid); 
     * for (int i = 0; i < 1000000; i++) { 
     * //randomGrid = randBoard.nextRandomBoard(); 
     * randomGrid = SudokuSolve.badRandomBoard(testGrid); 
     * avg += randomGrid.getCost(); 
     * } 
     * avg = avg / 1000000;
     * System.out.println(avg); 
     * long endTime = System.nanoTime();
     * 
     * long duration = (endTime - startTime); 
     * System.out.println("TIME: " + duration);
     */
    
    SudokuGrid testGrid = new SudokuGrid(Input.getBoard(args[0]));
    testGrid.prefilterGrid();
    RandomBoardGenerator rand = new RandomBoardGenerator(testGrid);
    SudokuGrid originalGrid = rand.nextRandomBoard();
    SudokuGrid mutatedGrid = new SudokuGrid();
    mutatedGrid.setEqual(originalGrid);
    GeometricOperators.feasibleGeometricMutation(mutatedGrid, testGrid);
    System.out.println("Original board: \n" + originalGrid);
    System.out.println("Mutated board: \n" + mutatedGrid);
    
    boolean equals = true;
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (originalGrid.getValAt(i, j) != mutatedGrid.getValAt(i, j)) {
          System.out.println("Row: " + i + ", Col: " + j);
          equals = false;
        }
      }
    }
    if (equals) {
      System.out.println("equals");
    } else {
      System.out.println("Does not equal");
    }
  }
}
