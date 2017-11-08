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
		Object created = creator.createStart();
		Serializer s = new Serializer();
		s.serialize(created);


	}


	public Object createStart(){
		Object created = userCreate();
		Visualizer display = new Visualizer();
		//display.inspect(created);
		return created;
	}

	public Object userCreate(){
		//Display menu information to user
		int userChoice = displayMenu();
		Object toReturn = null;
		switch (userChoice) {
			case 1: toReturn = new PrimObj(true);
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

			case 3: toReturn = new PrimArray(true);
				break;
			case 4: toReturn = new ObjArray(this);
				break;
			case 5: toReturn = new ObjCollection(this);
				break;


		};

		System.out.println("Finished creating object: " + toReturn.toString());
		allObjects.add(toReturn);
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

	public static Object selectExistingObject(){
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








}
