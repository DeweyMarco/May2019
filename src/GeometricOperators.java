/**
 * Does all them Geometric Operations
 * 
 * @author Luan Roos
 * @version 1.2
 */

public class GeometricOperators {
  /**
   * Mutating one egg with the best egg
   * 
   * @param egg this is the egg from the nest
   * @param eggBest The best of the best
   * @return SudokuGrid
   */
  public static SudokuGrid pMXCrossover(SudokuGrid egg, SudokuGrid eggBest) {
    int[][] childGrid = new int[9][9];
    int[] eggRow = new int[9];
    int[] eggBestRow = new int[9];

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        eggRow[j] = egg.getValAt(i, j);
        eggBestRow[j] = eggBest.getValAt(i, j);
      }
      childGrid[i] = PartiallyMatchedCrossover.pmxCrossover(eggRow, eggBestRow);
    }

    return (new SudokuGrid(childGrid));
  }

  /**
   * Multi-parent sorting crossover takes the best egg, 
   * worst egg and random egg and performs swaps
   * 
   * @param egg random egg from nest
   * @param eggBest the best egg in the nest
   * @param eggLastBest the worst egg
   * @return SudokuGrid with the new egg
   */
  public static SudokuGrid multiParentalSortingCrossover(SudokuGrid egg, 
      SudokuGrid eggBest,
      SudokuGrid eggLastBest) {
    int[][] childGrid = new int[9][9];
    int[] eggRow = new int[9];
    int[] eggBestRow = new int[9];
    int[] eggLastBestRow = new int[9];

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        eggRow[j] = egg.getValAt(i, j);
        eggBestRow[j] = eggBest.getValAt(i, j);
        eggLastBestRow[j] = eggLastBest.getValAt(i, j);
      }
      childGrid[i] = Mask.mPSX(eggRow, eggBestRow, eggLastBestRow);
    }

    return (new SudokuGrid(childGrid));
  }

  /**
   * While other functions return newly 
   * created boards, this one edits that board.
   * 
   * @param egg this is the egg from the nest
   * @param problemBoard this is to cheek the egg agaist the origonal board
   */
  public static void feasibleGeometricMutation(SudokuGrid egg, 
      SudokuGrid problemBoard) {
    SudokuGrid mutatedGrid;
    int[][] eggRows = new int[9][9];
    int[][] problemRows = new int[9][9];

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        eggRows[i][j] = egg.getValAt(i, j);
      }
    }

    FeasibleGeometricMutation.fgm(eggRows, problemBoard);
    // changes vals in eggRows.
    mutatedGrid = new SudokuGrid(eggRows);
    egg.setEqual(mutatedGrid);
  }
}
