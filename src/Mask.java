import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Mask does the Multiparental Sorting Crossover
 * runs both from a text file and from array inputs
 * can both use any mask passed to it and can also generate 
 * a mask with any weights
 * 
 * 
 * @author Marco Dewey
 * @version 1.0.69
 */
public class Mask {

  /**
   * Mask implements the Multiparental Sorting 
   * Crossover with equal weights on each of the parents
   * This implementation uses three parents and a
   * mask to dictated which parent is selected from
   * the i value of the parent chosen, the other 
   * rows Swap the values of there i’th element with
   * that of the one with the value of the chosen Parents value
   * 
   * @param mask a int[9] with only values 1,2,3
   * @param p1 Parent 1
   * @param p2 Parent 2
   * @param p3 Parent 3
   * @return int[] mutated child made with the mask
   */
  private static int[] mPSXFixed(int[] mask, int[] p1, int[] p2, int[] p3) {
    int[] mk = new int[9];
    for (int i = 0; i < 9; i++) {
      int pick = mask[i];
      if (pick == 1) {
        mk[i] = p1[i];
        swap(p2, i, p1[i]);
        swap(p3, i, p1[i]);
      } else if (pick == 2) {
        mk[i] = p2[i];
        swap(p1, i, p2[i]);
        swap(p3, i, p2[i]);
      } else {
        mk[i] = p3[i];
        swap(p1, i, p3[i]);
        swap(p2, i, p3[i]);
      }
    }
    return mk;
  }

  /**
   * Swaps the values in a int array, it takes both a value 
   * to be searched for and index location, the int in the
   * index location and the int value index neex to swap
   * 
   * example:
   * a[] = 1 2 3 4 5 6 7 8 9
   * index = 3
   * val = 7
   * 
   * return:
   * 1 2 7 4 5 6 3 8 9
   * 
   * @param a Array to make the swaps
   * @param index index of the swap
   * @param val value to be swaped
   */
  private static void swap(int[] a, int index, int val) {
    int pivot = 0;
    for (int i = 0; i < 9; i++) {
      if (a[i] == val) {
        pivot = i;
        break;
      }
    }
    a[pivot] = a[index];
    a[index] = val;
  }

  /**
   * Runs Multiparental Sorting Crossover from a file
   * the first line is the mask telling which board takes
   * precedence in the swap. The next three lines are the 
   * three input parent boards
   * 
   * input:
   * 2 2 2 1 2 3 3 3 3
   * 7 5 6 8 1 2 4 9 3
   * 2 1 9 4 8 5 6 7 3
   * 7 6 9 8 4 2 3 1 5
   *
   * output (print): 
   * 2 1 9 8 4 7 3 6 5 
   * 
   * @param fileName String of a file
   * @throws IOException On input error.
   */
  public static void runMPSX(String fileName) throws IOException {
    Scanner inputFile = new Scanner(new File(fileName));
    int[] mask = new int[9];
    int[] p1 = new int[9];
    int[] p2 = new int[9];
    int[] p3 = new int[9];
    for (int i = 0; i < 9; i++) {
      mask[i] = inputFile.nextInt();
    }
    for (int i = 0; i < 9; i++) {
      p1[i] = inputFile.nextInt();
    }
    for (int i = 0; i < 9; i++) {
      p2[i] = inputFile.nextInt();
    }
    for (int i = 0; i < 9; i++) {
      p3[i] = inputFile.nextInt();
    }
    inputFile.close();
    int[] child = mPSXFixed(mask, p1, p2, p3);
    String strChild = "";
    for (int i = 0; i < 9; i++) {
      strChild = strChild + child[i] + " ";
    }
    System.out.println(strChild);
  }

  /**
   * Initalizes a Multiparental Sorting Crossover
   * and uses Fisher-Yates suffle to make the mask.
   * The mask uses 5 1's, 3 2's and 1 3.
   *  
   * @param p1 int
   * @param p2 int
   * @param p3 int
   * @return int[]
   */

  public static int[] mPSX(int[] p1, int[] p2, int[] p3) {
    // GENERATES RANDOM MASK: (Fisher-Yates shuffle)
    int[] mask = {1, 1, 1, 1, 1, 2, 2, 2, 3};
    int index;
    int temp;
    for (int i = 0; i < 9; i++) {
      index = (int) (Math.random() * (9 - i));
      temp = mask[index];
      mask[index] = mask[8 - i];
      mask[8 - i] = temp;
    }

    return mPSXFixed(mask, p1, p2, p3);
  }
}
