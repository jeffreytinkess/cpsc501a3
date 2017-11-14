import java.util.Scanner;
import java.util.*;
public class ObjRef{
  Object myObject;
  public ObjRef(){myObject = new PrimObj();}

  public ObjRef(ObjCreate creator){
    Scanner input = new Scanner(System.in);
    System.out.printf("%n%n *****Creating new Object Field Object***** %n%n");
    System.out.println("Creating new default object for reference");
    myObject = creator.userCreate();
  }

  public ObjRef(Object reference){
    System.out.printf("%n%n *****Creating new Object Field Object with pre-created reference%n%n");
    myObject = reference;
  }
  public Object getRef(){
	return myObject;
  }
}
