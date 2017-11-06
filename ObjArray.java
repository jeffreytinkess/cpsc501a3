import java.util.Scanner;
import java.util.*;
public class ObjArray{
  Object[] myObjArray;

  public ObjArray(ObjCreate creator){
    System.out.println("*****Creating new Object Array Object*****");
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
    } catch (Exception e){
      System.out.println("Input error, using default value of 1");
      arraySize = 1;
    }
    myObjArray = new Object[arraySize];
    for(int i = 0; i < arraySize; i++){
      int choice = 0;
      try{
        System.out.println("Please enter 1 to create a new object, 2 to use a new object");
      in = input.nextLine();
      choice = Integer.parseInt(in);
      if (choice == 2){
        //print all current objects and choose one
        myObjArray[i] = ObjCreate.selectExistingObject();
      } else if (choice == 1){
        myObjArray[i] = creator.userCreate();
      } else {
        System.out.println("Please only enter 1 or 2 as input, using default input of 1");
      }
      } catch (Exception e){
        System.out.println("Input error, using default value of 1");
        myObjArray[i] = creator.userCreate();
      }
    }
  }

}
