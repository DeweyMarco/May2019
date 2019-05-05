import java.util.ArrayList;

/**
 * Returns FGM board
 * 
 * @author Marco Dewey
 * @version 1.1
 */

public class FeasibleGeometricMutation {

  /**
   * FMG changes values of potential solution (given in int array format).
   * 
   * @param possibleSolution mutated egg
   * @param problemBoard origional problem board
   */
  public static void fgm(int[][] possibleSolution, SudokuGrid problemBoard) {
    // selects 2 randoms from list
    int[] randomRows = chooseRows();
    ArrayList<Integer> emptyPositions = new ArrayList<Integer>();
    int pos1, pos2, index, temp, colIterator;
    Integer valAtPos1, valAtPos2;
    // VALUES WRAPPER TYPE, SO ARRAY LIST SEARCHING DOESN"T USE INDEX.

    // DOES FOR EVERY CHOSEN ROW.
    // REMEMBER TO USE randomRow[i] instead of just i.
    for (int i = 0; i < randomRows.length; i++) {
      colIterator = randomRows[i];
      // POPULATE ARRAYLIST WITH POSITIONS OF ZEROES IN PROBLEMBOARD.
      for (int j = 0; j < 9; j++) {
        if (problemBoard.getValAt(colIterator, j) == 0) {
          emptyPositions.add(Integer.valueOf(j));
        }
      }

      if (emptyPositions.size() < 2) {
        continue;
      }

      while (emptyPositions.size() > 1) {
        // GETS RANDOM POSITION FROM ARRAYLIST, REMOVES IT, STORES IT IN POS1.
        index = (int) (Math.random() * emptyPositions.size());
        pos1 = emptyPositions.get(index);
        emptyPositions.remove(index);
        index = (int) (Math.random() * emptyPositions.size());
        pos2 = emptyPositions.get(index);

        valAtPos1 = possibleSolution[colIterator][pos1];
        valAtPos2 = possibleSolution[colIterator][pos2];

        if (problemBoard.getCellAt(colIterator,
            pos1).possibleVals.contains(valAtPos2)
            && problemBoard.getCellAt(colIterator, pos2).possibleVals.contains(
                valAtPos1)) {
          temp = possibleSolution[colIterator][pos1];
          possibleSolution[colIterator][pos1] = 
              possibleSolution[colIterator][pos2];
          possibleSolution[colIterator][pos2] = 
              temp;
          break;
        }
      }
    }
  }

  /**
   * this returns the index of the rows for mutation
   * 
   * @return rows to mutate
   */
  private static int[] chooseRows() {
    int numRows = (int) (Math.random() * 9 + 1);
    int[] rowShuffle = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    int index;
    int temp;
    for (int i = 0; i < 9; i++) {
      index = (int) (9 - (Math.random() * (9 - i)));
      temp = rowShuffle[index];
      rowShuffle[index] = rowShuffle[i];
      rowShuffle[i] = temp;
    }
    int[] returnRows = new int[numRows];
    for (int i = 0; i < numRows; i++) {
      returnRows[i] = rowShuffle[i];
    }
    return returnRows;
  }
}
