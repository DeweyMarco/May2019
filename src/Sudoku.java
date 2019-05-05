import java.io.IOException;

/**
 * The Main Sudoku Interface
 * 
 * @author The Sexy three
 * @version 69.0
 */
public class Sudoku {
  
  /**
   * Main program takes a integer as a mode 
   * and a file name as second argument.
   * 
   * Case 0: test Partially Matched Crossover
   * 		
   * Case 1: test MPSX
   * 
   * Case 2: test prefiltering
   * 			prints all posible values for each Cell
   * 
   * Case 3: test the full Sudoku Solver
   * 			prints solved boards or Error message for overflow iterations
   * 
   * @param args input from user
   * @throws IOException for file errors
   */
  public static void main(String[] args) throws IOException {
    int mode = Integer.parseInt(args[0]);
    String fileName = args[1];
    switch (mode) {
      case 0:
        PartiallyMatchedCrossover.runPartiallyMatchedCrossover(fileName);
        break;
      case 1:
        Mask.runMPSX(fileName);
        break;
      case 2:
        SudokuGrid prefilterTestGrid = new SudokuGrid(Input.getBoard(args[1]));
        prefilterTestGrid.prefilterGrid();
        System.out.println(prefilterTestGrid.getAllPossibleCellValues());
        break;
      case 3:
        SudokuGrid solveGrid = new SudokuGrid(Input.getBoard(args[1]));
        if (SudokuSolve.solve(solveGrid)) {
          System.out.println(solveGrid);
        } else {
          System.out.println("MAX ITER EXCEEDED");
        }
        break;
      default:
        System.out.println("Invalid mode");
        System.exit(0);
    }
  }
}
