import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Partally Matched Crossover
 * This function takes 2 parent rows, it keeps part of 
 * the first parent row (between two integers (1-9))
 * and suffles the rest. This creates a mutated Child 
 * that is returned/printed
 * 
 * @author Jaco Du Plessis
 * @version 1.1
 */
public class PartiallyMatchedCrossover {

  /**
   * Runs Partially Matched Crossover from a file 
   * containing 2 integers and 2 row sections of a board
   * 
   * input:
   * 4 9
   * 4 7 3 6 1 8 9 2 5
   * 2 5 9 4 1 7 3 8 6
   * 
   * output (printed):
   * 7 4 3 6 1 8 9 2 5 
   * 
   * @param fileName name of the file 
   * @throws IOException for error.
   */
  public static void runPartiallyMatchedCrossover(String fileName) 
      throws IOException {
    Scanner inputFile = new Scanner(new File(fileName));
    int a = inputFile.nextInt();
    int b = inputFile.nextInt();
    int[] p1 = new int[9];
    int[] p2 = new int[9];
    for (int i = 0; i < 9; i++) {
      p1[i] = inputFile.nextInt();
    }
    for (int i = 0; i < 9; i++) {
      p2[i] = inputFile.nextInt();
    }
    inputFile.close();
    int[] child = pmxCrossoverFixed(a, b, p1, p2);
    String strChild = "";
    for (int i = 0; i < 9; i++) {
      strChild = strChild + child[i] + " ";
    }
    System.out.println(strChild);
  }

  /**
   * Does a Partially Matched Crossover with 2 parent 
   * boards and then randomly gernerates a "a" and "b" index
   * for the saved section of parent 1
   * 
   * @param parent1 int array that represents the frist parent
   * @param parent2 int array that represents the second parent
   * @return int[] that represent the new mutated child
   */

  public static int[] pmxCrossover(int[] parent1, int[] parent2) {
    int a = (int) Math.random() * 9 + 1;
    int b = (int) Math.random() * 9 + 1;
    return pmxCrossoverFixed(a, b, parent1, parent2);
  }
  
  /**
   * Initalizes a PMX crossover
   * 
   * @param a int the begining index of the saved section
   * @param b int the end index of the saved section
   * @param parent1 int array that represents the frist parent
   * @param parent2 int array that represents the second parent
   * @return int[] that represent the new mutated child
   */
  
  private static int[] pmxCrossoverFixed(int a, int b, 
      int[] parent1, int[] parent2) {
    int[] child = new int[9];
    a--;
    b--;
    if (a == b) {
      for (int i = 0; i < 9; i++) {
        child[i] = parent2[i];
      }
      return child;
    }
    if (a > b) {
      int temp = a;
      a = b;
      b = temp;
    }
    for (int i = a; i < b + 1; i++) {
      child[i] = parent1[i];
    }
    int[] listVal = new int[(b - a) + 1];
    int len = 0;
    for (int i = a; i < b + 1; i++) {
      if (indexOf(parent2[i], child) == -1) {
        listVal[len++] = parent2[i];
      }
    }
    int p = 0;
    while (p < len) {
      int lval = listVal[p];
      int v = parent1[indexOf(listVal[p], parent2) - 1];
      while (child[(indexOf(v, parent2)) - 1] > 0) {
        listVal[p] = v;
        v = parent1[indexOf(listVal[p], parent2) - 1];
      }
      child[indexOf(v, parent2) - 1] = lval;
      p++;
    }
    for (int i = 0; i < child.length; i++) {
      if (child[i] == 0) {
        child[i] = parent2[i];
      }
    }
    return child;
  }

  /**
   * A search for the value (val) that is passed 
   * to the function. The index of that value is passed back 
   * but from the notation 1-9, not 0-8. if not found -1 is
   * returned 
   * 
   * @param val value
   * @param list the list to search
   * @return int or -1 if error
   */
  private static int indexOf(int val, int[] list) {
    for (int i = 0; i < list.length; i++) {
      if (list[i] == val) {
        return i + 1;
      }
    }
    return -1;
  }
}
