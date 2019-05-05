import java.util.ArrayList;

/**
 * Randon Board Generation
 * 
 * @author Luan Roos
 * @version 1.1
 */

class RandomBoardGenerator {
  private SudokuGrid baseGrid;
  private Tree[] solutionTrees;
  int boardCount = 0;

  /**
   * Creates the Tree
   * 
   */

  public class Tree {
    ArrayList<Tree> children;
    Tree parent;
    int val;
    boolean childrenSpawned;

    /**
     * Tree diagraph
     * 
     * The root of the tree has no parent
     * as per definition, however if no 
     * solution exists this will throw a 
     * null pointer exception
     * 
     * @param value int
     */

    Tree(int value) {
      val = value;
      children = new ArrayList<Tree>();
      childrenSpawned = false;
      parent = null;
    }

    /**
     * Creates the child node
     * 
     * @param value int 
     */

    public void spawnChild(int value) {
      Tree childTree = new Tree(value);
      childTree.parent = this;
      this.children.add(childTree);
      this.childrenSpawned = true;
    }

    /**
     * Removes the child node
     * 
     * @param value int 
     */

    public void removeChild(int value) {
      for (int i = 0; i < children.size(); i++) {
        if (children.get(i).val == value) {
          children.remove(i);
          return;
        }
      }
    }
  }
  // END OF Tree CLASS

  /**
   * Generation of the Board
   * 
   * @param initBoard SudokuGrid
   */

  RandomBoardGenerator(SudokuGrid initBoard) {
    baseGrid = new SudokuGrid();
    baseGrid.setEqual(initBoard);
    solutionTrees = new Tree[9];
    for (int i = 0; i < 9; i++) {
      solutionTrees[i] = new Tree(-1);
      // set to val = -1 for debugging purposes
    }
  }

  /**
   * get Board
   * 
   * @return SudokuGrid 
   */

  public SudokuGrid nextRandomBoard() {
    int[][] randomGrid = new int[9][9];
    for (int i = 0; i < 9; i++) {
      randomGrid[i] = getRandomRow(i);
    }
    SudokuGrid randomSudokuGrid = new SudokuGrid(randomGrid);
    return randomSudokuGrid;
  }

  /**
   * returns a row from board
   * 
   * @param rowNumber int
   * @return int[]
   */

  public int[] getRandomRow(int rowNumber) {
    Tree root = solutionTrees[rowNumber];
    Tree currentTree;
    int[] returnRow = new int[9];
    ArrayList<Integer> possibleEntries = new ArrayList<Integer>();
    int rand;
    int temp;

    currentTree = root;
    for (int i = 0; i < 9; i++) {
      // list of all possible values in row
      possibleEntries.add(Integer.valueOf(i + 1));
    }
    for (int i = 0; i < 9; i++) {
      // removes values already assigned in row (essentially
      // prefiltering again), only redundant on first iteration
      possibleEntries.remove(Integer.valueOf(baseGrid.getCellAt(rowNumber, 
          i).val));
    }
    for (int i = 0; i < 9; i++) {
      // assigning values to open cells.
      if (baseGrid.getCellAt(rowNumber, i).val == 0) {
        // assigns if cell does not have fixed value.
        if (!currentTree.childrenSpawned) {
          // if I haven't spawned children for Tree yet, do so
          for (int j = 0; j < baseGrid.getCellAt(rowNumber, 
              i).possibleVals.size(); j++) {
            if (possibleEntries.contains(baseGrid.getCellAt(rowNumber, 
                i).possibleVals.get(j))) {
              currentTree.spawnChild(baseGrid.getCellAt(rowNumber, 
                  i).possibleVals.get(j));
            }
          }
        }
        // if zero children, we need to trim the tree:
        if (currentTree.children.size() == 0) {
          // //System.out.println("TRIMMING!");
          while (currentTree.children.size() == 0) {
            possibleEntries.add(Integer.valueOf(currentTree.val));
            temp = currentTree.val;
            currentTree = currentTree.parent;
            currentTree.removeChild(temp);
            i--; // goes back 1.
            while (baseGrid.getCellAt(rowNumber, i).val != 0) {
              // goes back to previous point where
              // open cell.
              i--;
              // //System.out.println("Back to element: " + i);
            }
          }
        } // if we trimmed, our currentNode will now be higher.

        // iterate through random child
        rand = (int) (Math.random() * currentTree.children.size());
        currentTree = currentTree.children.get(rand);
        possibleEntries.remove(Integer.valueOf(currentTree.val));
        // System.out.println("Selected Child " + i + ": " + currentTree.val);
      }
    }
    // assigns values to return cells
    for (int i = 0; i < 9; i++) {
      // iterate in reverse, because tree climbing upward.
      if (baseGrid.getCellAt(rowNumber, 8 - i).val != 0) {
        returnRow[8 - i] = baseGrid.getCellAt(rowNumber, 8 - i).val;
        // //System.out.println("non tree vals:\n" + returnRow[8 - i]);
      } else {
        if (currentTree != root) { // means we've hit the null pointer
          returnRow[8 - i] = currentTree.val;
          // //System.out.println("Tree vals:\n" + currentTree.val);
          currentTree = currentTree.parent;
        }
      }
    }
    boardCount++;
    return returnRow;
  }

}
