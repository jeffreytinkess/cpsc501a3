import java.util.Scanner;
import java.util.*;
public class PrimArray{
  int[] myPrimArray;
  public PrimArray(){}
  public PrimArray(boolean isCreator){
    System.out.printf("%n%n*****Creating new Primitive Array Object*****%n%n");
    System.out.println("Enter the number of elements you want the array to have");
    Scanner input = new Scanner(System.in);
    String in = input.nextLine();
    int arraySize = 1;
    try{
      arraySize = Integer.parseInt(in);
      if (arraySize < 0){
        System.out.println("Size cannot be negative, using default value of 1");
        arraySize = 1;
      }
    }catch (Exception e){
      System.out.println("Input error, using default value of 1");
      arraySize = 1;
    }

    myPrimArray = new int[arraySize];
    for (int i = 0; i < arraySize; i++){
      try{
        System.out.println("Enter the value for array entry #" + i);
        in = input.nextLine();
        myPrimArray[i] = Integer.parseInt(in);
      } catch (Exception e){
        System.out.println("Error with input, using default value");
        myPrimArray[i] = -1;
      }
    }
  }
};
