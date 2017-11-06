import java.util.Scanner;
import java.util.*;
public class PrimObj{
  int primitiveFieldInt;
  double primitiveFieldDouble;
  public PrimObj(){
    Scanner input = new Scanner(System.in);
    System.out.printf("%n%n *****Creating new Primitive Fields Object***** %n%n");
    System.out.println("Enter integer value for first field");
    try{
      String in = input.nextLine();
      primitiveFieldInt = Integer.parseInt(in);
      System.out.println("Enter decimal value for second field");
      in = input.nextLine();
      primitiveFieldDouble = Double.parseDouble(in);
    } catch (Exception e){
      System.out.println("Error with input, using default values: 1 and 3.14");
      primitiveFieldInt = 1;
      primitiveFieldDouble = 3.14;
    }
  }
}
