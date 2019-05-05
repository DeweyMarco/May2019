import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Returns int[][] of board from a file name input
 * 
 * @author Marco Dewey
 * @version 1.1
 */
public class Input {

  private static int[][] board;

  /**
   * reads a String name of a txt file, finds the file and reads
   * the file into a Sudoku board with empty spaces as 0's
   * 
   * @return int[][] board with 0 as empty spaces
   * @param a This is the file name as a String
   * @throws FileNotFoundException On input error.
   * @throws IOException On input error.
   */
  public static int[][] getBoard(String a) 
      throws FileNotFoundException, IOException {
    board = new int[9][9];
    Scanner input = new Scanner(new File(a));
    String row;
    for (int i = 0; i < 9; i++) {
      row = input.nextLine();
      for (int j = 0; j < 9; j++) {
        if (row.substring(j, j + 1).equals(".")) {
          board[i][j] = 0;
        } else {
          board[i][j] = Integer.parseInt(row.substring(j, j + 1));
        }
      }
    }

    input.close();
    return board;
  }

}
