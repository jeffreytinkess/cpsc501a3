import java.util.Scanner;
import java.util.*;
	public class ObjCollection{
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
