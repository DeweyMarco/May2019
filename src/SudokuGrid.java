import java.util.ArrayList;

/**
 * Class containing the sudoku grid, as well as 
 * the elements in the grid and the operations on them,
 * including prefiltering.
 * 
 * @author Luan Roos
 * @version 1.0
 */
public class SudokuGrid {

  /**
   * Cell class contains the cells themselves. 
   * Arraylist contains the possible values. Has an int
   * val, that is zero if there are more than one 
   * possibilities, and some value between 1-9 if it is
   * fixed.
   */
  public class Cell { 
    // MADE CLASS PUBLIC SO OTHERS CAN USE. DUMB IDEA? 
    // WELL THEY NEED IT SO NO I
    // GUESS.
    public ArrayList<Integer> possibleVals;
    public int val; // NOW PUBLIC, MAKE SURE NOT TO CHANGE VAL IN CLASS.

    /**
     * Cell constructor. val is a parameter with value 
     * between 0-9. 0 means an empty square. 1-9
     * means it has a fixed value.
     * 
     * @param initVal integer
     */
    Cell(int initVal) {
      possibleVals = new ArrayList<Integer>();
      if (initVal > 9 || initVal < 0) {
        System.out.println("Cell: Cell assigned illegal value " + initVal);
        System.exit(0);
      }
      val = initVal;
      Integer temp;
      if (val == 0) {
        for (int i = 1; i < 10; i++) {
          temp = i;
          possibleVals.add(temp);
        }
      } else {
        temp = val;
        possibleVals.add(temp);
      }
    }

    /**
     * Attempts to remove the given removeVal. 
     * Maybe dangerous to not make sure that we're not
     * trying to delete the final value. 
     * Currently nothing will happen if someone tries to do so.
     * 
     * @param removeVal remove int value
     */
    public void removePossibility(int removeVal) {
      if (val != 0) {
        return;
      }
      Integer temp = removeVal;
      possibleVals.remove(temp);
      if (possibleVals.size() == 1) {
        val = possibleVals.get(0);
      }
    }

    /**
     * Get Possible values for cell
     * 
     * @return int array
     */
    public int[] getPossibleValues() {
      return possibleVals.stream().mapToInt(i -> i).toArray();
      // returns arraylist of Integer as array of ints.
    }

    /**
     * Get Possible values for cell as a string
     * 
     * @return String of Values
     */
    public String possibleValsToString() {
      String s = "";
      for (int i = 0; i < possibleVals.size(); i++) {
        s += Integer.toString(possibleVals.get(i)) + " ";
      }
      return s;
    }

    /**
     * Overide toString
     * 
     * @return String with "."
     */
    public String toString() {
      if (val == 0) {
        return ".";
      }
      return Integer.toString(val);
    }

    /**
     * get the Cell
     * 
     * @return Cell copy on the Cell
     */
    
    public Cell copy() {
      Cell copyCell = new Cell(0);
      for (int i = 0; i < this.possibleVals.size(); i++) {
        copyCell.removePossibility(this.possibleVals.get(i));
      }
      return copyCell;
    }
  }

  /**
   * END OF CELL, SUDOKUGRID CLASS:
   */
  private Cell[][] grid;
  private int cost;

  /**
   * The SudokuGrid constructor takes an 9x9 2d array of integers 
   * and initialises 81 Cells in a 2d
   * grid. First bracket represents row measure from 
   * top to bottom, and second bracket represents
   * colomns from left to right.
   */
  public SudokuGrid() {
    int[][] temp = new int[9][9];
    initialiseGrid(temp);
  }

  /**
   * The Values in the Sudoku Grid
   * 
   * @param gridVals 2D array values (Sudoku board)
   */
  public SudokuGrid(int[][] gridVals) {
    initialiseGrid(gridVals);
  }

  /**
   * Intizalizer for board
   * 
   * @param gridVals 2D array values (Sudoku board)
   */
  private void initialiseGrid(int[][] gridVals) {
    cost = -1;
    if (gridVals.length != 9) {
      System.out.println("SudokuGrid: Invalid number of " 
    + "rows for SudokuGrid intisialising.");
      System.exit(0);
    }
    grid = new Cell[9][9];
    for (int i = 0; i < 9; i++) {
      if (gridVals[i].length != 9) {
        System.out.println(
            "SudokuGrid: Invalid number of " 
        + "colomns for SudokuGrid intisialising in row " + i);
        System.exit(0);
      }
      for (int j = 0; j < 9; j++) {
        grid[i][j] = new Cell(gridVals[i][j]);
      }
    }
  }

  /**
   * Overide toString
   * 
   * @return String values list
   */
  public String toString() {
    String s = "";
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        s += grid[i][j].toString();
      }
      s += "\n";
    }
    return s;
  }

  /**
   * Get all possiblilities for a cell
   * 
   * @return String of values
   */
  public String getAllPossibleCellValues() {
    String s = "";
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        s += grid[i][j].possibleValsToString() + "\n";
      }
    }
    return s;
  }

  /**
   * Prefiltering method. Keeps track of which cells 
   * have been filtered using boolean arrays.
   */
  public void prefilterGrid() {
    Boolean[][] rowFiltered = new Boolean[9][9];
    Boolean[][] colFiltered = new Boolean[9][9];
    Boolean[][] subgridFiltered = new Boolean[9][9];
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        rowFiltered[i][j] = false;
        colFiltered[i][j] = false;
        subgridFiltered[i][j] = false;
      }
    }
    /*
     * Continually filter every element until no elements unfiltered left.
     */
    Boolean filtered = false;
    while (!filtered) {
      // ** BEGINNING OF Iterate over every element
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          // ** CODE FOR INSIDE EVERY CELL
          if (grid[i][j].val != 0) {
            rowFiltered[i][j] = true;
            colFiltered[i][j] = true;
            subgridFiltered[i][j] = true;
            continue;
            // Goes to next loop iteration if cell has value,
            // and is therefore already filtered.
          }
          // ROW FILTERING:
          if (!rowFiltered[i][j]) {
            rowFilter(i, j);
            rowFiltered[i][j] = true;
            if (grid[i][j].val != 0) {
              for (int a = 0; a < 9; a++) {
                rowFiltered[i][a] = false;
                // every value in current row
                colFiltered[a][j] = false;
                // every value in current col
              }
              // Iterates over subgrid
              for (int a = i - (i % 3); a < i - (i % 3) + 3; a++) {
                for (int b = j - (j % 3); b < j - (j % 3) + 3; b++) {
                  subgridFiltered[a][b] = false;
                }
              }
            }
          }
          // COL FILTERING:
          if (!colFiltered[i][j]) {
            colFilter(i, j);
            colFiltered[i][j] = true;
            if (grid[i][j].val != 0) {
              for (int a = 0; a < 9; a++) {
                rowFiltered[i][a] = false;
                // every value in current row
                colFiltered[a][j] = false;
                // every value in current col
              }
              // Iterates over subgrid
              for (int a = i - (i % 3); a < i - (i % 3) + 3; a++) {
                for (int b = j - (j % 3); b < j - (j % 3) + 3; b++) {
                  subgridFiltered[a][b] = false;
                }
              }
            }
          }
          // IMPLEMENT: SUBGRID FILTERING!
          if (!subgridFiltered[i][j]) {
            subgridFilter(i, j);
            subgridFiltered[i][j] = true;
            if (grid[i][j].val != 0) {
              for (int a = 0; a < 9; a++) {
                rowFiltered[i][a] = false;
                // every value in current row
                colFiltered[a][j] = false;
                // every value in current col
              }
              // Iterates over subgrid
              for (int a = i - (i % 3); a < i - (i % 3) + 3; a++) {
                for (int b = j - (j % 3); b < j - (j % 3) + 3; b++) {
                  subgridFiltered[a][b] = false;
                }
              }
            }
          }
          // END OF CODE INSIDE EVERY ELEMENT.
        }
      }
      // END OF ITERATING OVER EVERY ELEMENT
      // Go over element and see if
      // it is filtered in every way.
      filtered = true;
      testFilter: for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          if (!rowFiltered[i][j]) {
            filtered = false;
            break testFilter;
          }
          if (!colFiltered[i][j]) {
            filtered = false;
            break testFilter;
          }
          if (!subgridFiltered[i][j]) {
            filtered = false;
            break testFilter;
          }
        }
      }
    }
    // END OF CONTINUALLY FILTERING LIST UNTIL IT IS COMPLETELY FILTERED
    return;
  }

  // Counts number of errors in board. 
  // Not sure how to count if more than 2 of same digit in row 
  // col / subg
  // OUT OF BOUNDS EXCEPTION FOR NON FILLED BOARDS WILL BE THROWN.
  
  /**
   * Generates a random board
   * 
   * @return int the number of errors in the board
   */
  
  public int getCost() {
    boolean[] usedValues = new boolean[9];
    boolean[] usedValues2 = new boolean[9]; 
    // used so that one array for both rows and colomns can
    // be used.
    if (cost < 0) { // ONLY COMPUTES IF HAVEN'T BEEN COMPUTED YET.
      cost = 0;
      for (int i = 0; i < 9; i++) {
        // ROW COSTS
        for (int k = 0; k < 9; k++) {
          usedValues[k] = false;
        }

        for (int j = 0; j < 9; j++) {
          if (usedValues[grid[i][j].val - 1]) {
            cost++;
          } else {
            usedValues[grid[i][j].val - 1] = true;
          }
        }

        // COL COSTS
        for (int k = 0; k < 9; k++) {
          usedValues2[k] = false;
        }

        for (int j = 0; j < 9; j++) {
          if (usedValues2[grid[j][i].val - 1]) {
            cost++;
          } else {
            usedValues2[grid[j][i].val - 1] = true;
          }
        }
      } // END OF ROW AND COL COSTS

      // SUBGRID COST
      // for every subgrid
      for (int a = 0; a < 3; a++) {
        for (int b = 0; b < 3; b++) {
          for (int k = 0; k < 9; k++) {
            usedValues[k] = false;
          }
          // Do inside subg:
          for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
              if (usedValues[grid[3 * a + i][3 * b + j].val - 1]) {
                cost++;
              } else {
                usedValues[grid[3 * a + i][3 * b + j].val - 1] = true;
              }
            }
          }
        }
      }

    }
    return cost;
  }

  /**
   * Generates a random board
   * 
   * @param copyGrid SudokuGrid
   */
  
  public void setEqual(SudokuGrid copyGrid) {
    this.grid = copyGrid.grid;
    this.cost = copyGrid.cost;
  }

  // REMEMBER, ASSUMES USER KNOWS THAT (0, 0) IS FIRST ELEMENT.
  /**
   * Generates a random board
   * 
   * @param row int
   * @param col int
   * @return int  the integer int the cell from row and col
   */
  
  public int getValAt(int row, int col) {
    return grid[row][col].val;
  }

  /**
   * Generates a random board
   * 
   * @param row int
   * @param col int
   * @return Cell the cell from row and col
   */
  
  public Cell getCellAt(int row, int col) {
    return grid[row][col];
  }

  /**
   * Removes possibilities row wise for a given cell.
   * 
   * @param row int row number
   * @param col int col number
   */
  
  private void rowFilter(int row, int col) {
    for (int j = 0; j < 9; j++) {
      if (j == col) {
        continue;
        // don't compare cell to its own value.
      }
      if (grid[row][j].val != 0) {
        // If current element we tracing over has a fixed value,
        // remove its value from our desired cell.
        grid[row][col].removePossibility(grid[row][j].val);
      }
    }
  }

  /**
   * Removes possibilities col wise for a given cell.
   * 
   * @param row int row number
   * @param col int col number
   */
  private void colFilter(int row, int col) {
    for (int i = 0; i < 9; i++) {
      if (i == row) {
        continue;
        // don't compare cell to its own value.
      }
      if (grid[i][col].val != 0) {
        // If current element we tracing over has a fixed value,
        // remove its value from our desired cell.
        grid[row][col].removePossibility(grid[i][col].val);
      }
    }
  }

  /**
   * IMPLEMENT: Removes possibilities subgridwise for a given cell.
   * 
   * @param row int row number
   * @param col int col number
   */
  
  private void subgridFilter(int row, int col) {
    // Iterates over subgrid
    for (int a = row - (row % 3); a < row - (row % 3) + 3; a++) {
      for (int b = col - (col % 3); b < col - (col % 3) + 3; b++) {
        if (a == row && b == col) {
          continue;
          // don't compare cell to its own value.
        }
        if (grid[a][b].val != 0) {
          // If current element we tracing over has a fixed value,
          // remove its value from our desired cell.
          grid[row][col].removePossibility(grid[a][b].val);
        }
      }
    }
  }
}
