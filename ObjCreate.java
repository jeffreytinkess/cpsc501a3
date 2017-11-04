import java.util.Scanner;
import java.util.*;
public class ObjCreate{
	Scanner input;
	public static ArrayList<Object> allObjects;
	public ObjCreate(){
		input = new Scanner(System.in);
		if (allObjects == null){
			allObjects = new ArrayList<Object>();
		}
	};


	public static void main(String[] args){
		//Create an instance of this class
		ObjCreate creator = new ObjCreate();
		//Call create method
		creator.userCreate();



	}




	public Object userCreate(){
		//Display menu information to user
		int userChoice = displayMenu();
		Object toReturn = null;
		switch (userChoice) {
			case 1: toReturn = new PrimObj();
				break;

			case 2: System.out.println("Enter 1 to create default object, Enter 2 to use existing object for reference");
				String in = input.nextLine();
				int choice = 0;
				try{ choice = Integer.parseInt(in);}
				catch(Exception e){System.out.println("Input error, using default input");
					choice = 1; }
				if (choice <= 1){ toReturn = new ObjRef(this);}
				else if (ObjCreate.allObjects.size() == 0){
					System.out.println("No objects created yet, using default");
					toReturn = new ObjRef(this);
				} else {
					//call method to find object from list and pass it in
					toReturn = new ObjRef(ObjCreate.selectExistingObject());
				}
				break;

			case 3: toReturn = new PrimArray();
				break;
			case 4: toReturn = new ObjArray(this);
				break;
			case 5: toReturn = new ObjCollection(this);
				break;


		};

		System.out.println("Finished creating object: " + toReturn.toString());
		allObjects.add(toReturn);
		Visualizer display = new Visualizer();
		display.inspect(toReturn);
		return toReturn;
	}


	/*
 	*  Displays the main menu, handles user input and returns the integer of the users choice
 	*
 	*/
	private int displayMenu(){
		System.out.printf("%n%n**********Main Menu**********%n%n");
		System.out.printf("Enter the number of your choice to begin %n%n");
		System.out.println("1) Simple object, with only primitive fields");
		System.out.println("2) Object with object fields");
		System.out.println("3) Object containing an array of primitives");
		System.out.println("4) Object containing an array of objects");
		System.out.println("5) Object with a collection of objects");
		String in = input.nextLine();
		int toReturn = 0;
		try{
			toReturn = Integer.parseInt(in);
		} catch (Exception e){ System.out.println("Input error, using default input");
			toReturn = 1;
		}
		if (toReturn < 1 || toReturn > 5) {
			System.out.println("Invalid selection, using default selection");
			toReturn = 1;
		}
		return toReturn;
	}
/*
	private Object selectExistingObject(){
		System.out.printf("%n%n*****List of existing objects*****");
		for (int i = 0; i < ObjCreate.allObjects.size(); i++){
			System.out.println(i + ") : " + ObjCreate.allObjects.get(i).toString());
		}
		System.out.println("Enter the number for your selection here");
		Scanner in = new Scanner(System.in);
		int userChoice = 0;
		try{
				String input = in.nextLine();
				userChoice = Integer.parseInt(input);
		} catch (Exception e){
			System.out.println("Error making selection, using default value");
		}
		if (userChoice < 0 || userChoice >= ObjCreate.allObjects.size()){
			System.out.println("Invalid selection, using default choice");
			userChoice = 0;
		}
		return ObjCreate.allObjects.get(userChoice);

	}
*/
	private static Object selectExistingObject(){
		if (ObjCreate.allObjects.size() == 0){
			System.out.println("No objects created to be selected");
			return null;
		}
		System.out.printf("%n *****Choose an object*****%n%n");
		int i = 0;
		for (Object o:ObjCreate.allObjects){
			System.out.println("Object #" + i + " :" + o.toString());
			i++;
		}
		System.out.printf("%n Enter the object number you wish to use: ");
		Scanner input = new Scanner(System.in);
		String in = input.nextLine();
		int choice = 0;
		try{
			choice = Integer.parseInt(in);
		} catch (Exception e){ System.out.println("Input error, using default input"); }
		if (choice < 0 || choice > ObjCreate.allObjects.size())	{
			System.out.println("Invalid selection, using first object");
			choice = 0;
		}
		return ObjCreate.allObjects.get(choice);
	}



	//List of class definitions that can be created/modified


	private class PrimObj{
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
	};


	private class ObjRef{
		Object myObject;
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
	};
	private class PrimArray{
		int[] myPrimArray;

		public PrimArray(){
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
	private class ObjArray{
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

	};


	private class ObjCollection{
		ArrayList<Object> myObjCollection;
		public ObjCollection(ObjCreate creator){
			myObjCollection = new ArrayList<Object>();
			Scanner input = new Scanner(System.in);
			boolean userContinue = false;
			while(true){
				System.out.println("Enter 1 to add a new object, anything else to finish list");
				String in = input.nextLine();
				int choice = 0;
				try{
					choice = Integer.parseInt(in);
					if (choice == 1){
						myObjCollection.add(creator.userCreate());
					} else {
						break;
					}
				} catch (Exception e){System.out.println("Input error, ending list creation");
					break;
				}
			}	
		}

};
}
