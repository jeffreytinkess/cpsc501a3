import java.util.*;
import java.lang.reflect.*;

public class Visualizer{
	private ArrayList<Object> toBeInspected;
	private ArrayList<String> hasBeenInspected;

	public Visualizer(){
		toBeInspected = new ArrayList<Object>();
		hasBeenInspected = new ArrayList<String>();
	}


	public void inspect(Object target){
		Class targetClass = target.getClass();
		String targetName = target.toString();
		for(String s: hasBeenInspected){
			if (targetName.equals(s)){
				//System.out.println("Found duplicate object");
				return;
			}
		}
		hasBeenInspected.add(targetName);
		//print name, superclass name
		System.out.printf("%n%n%n**********Object Inspection Start**********%n%n");
		System.out.println("Object Name: " + targetName);
		System.out.println("Object classname: " + targetClass.getName());
		if (targetClass.getSuperclass() != null){
			System.out.println("Object superclass: " + targetClass.getSuperclass());
		}
		System.out.printf("%n *****Fields*****%n");
		//print all fields and values, if object print objects name and message that detailed printout found elsewhere: add to list
		displayFields(target);
		//print methods method call
		displayMethods(targetClass);

		System.out.printf("%n%n**********Object Inspection End**********%n%n");
		
		for (Object o: toBeInspected){
			inspect(o);
		}
	}

	private void displayFields(Object o){
		Class c = o.getClass();
		//get a list of all fields (declared and inherited)
		Field[] mostFields = c.getFields();
		ArrayList<Field> allFields = new ArrayList<Field>();
		for (int i = 0; i < mostFields.length; i++){
			if (!mostFields[i].getName().equals("this$0")){
				allFields.add(mostFields[i]);
			}
		}
		//Now find all declared fields and add them
		Field[] fields = c.getDeclaredFields();
		for (int i = 0; i < fields.length; i++){
			
			//Add declared fields, but do not add "this" field
			if (!fields[i].getName().equals("this$0")){
				allFields.add(fields[i]);
			}
		}
		//for each, check if its a primitive array or object
		for (Field f: allFields){
			f.setAccessible(true);
			Class fieldClass = f.getType();
			if (fieldClass.isPrimitive()){
				try{
					System.out.println(f.getName() + " = " + f.get(o).toString());
				} catch (IllegalAccessException iae){}
				catch (IllegalArgumentException e){};
			}else if (fieldClass.isArray()){
				displayArrayField(f, o);
			}else {
				displayObjectField(f, o);
			}
			
		}
	}
	private void displayMethods(Class c){}
	private void displayArrayField(Field f, Object target){
		Object targetArray = null;
		try{
			targetArray = f.get(target);
		} catch (Exception e){
			System.out.println("Error parsing array field");
			e.printStackTrace();
			return;
		};
		System.out.println(f.getName()+"[] = ");
		Class elementType = targetArray.getClass().getComponentType();
		int length = Array.getLength(targetArray);
		if (elementType.isPrimitive()){
			//Elements are primitives, print them with name
			for (int i = 0; i < length; i++){
				//for each element, print its element number and value
				Object element = Array.get(targetArray, i);
				System.out.println(f.getName() + "[" + i + "] = " + element.toString());
			}
		} else {
			//Elements are objects, print object names and add each to inspection list
			for (int i = 0; i < length; i++){
				Object element = Array.get(targetArray, i);
				System.out.println(f.getName() + "[" + i + "] = " + element.toString()+ " : See further object inspection for object details");
				toBeInspected.add(element);
			}
		}	
	}
	private void displayObjectField(Field f, Object target){
		//Get reference to object itself
		Object fieldValue;
		try{
			fieldValue = f.get(target);
		} catch (IllegalAccessException iae){return;}
		catch (IllegalArgumentException e){return;};
		//Print object name
		if (fieldValue != null){
			System.out.println(f.getName() + " = " + fieldValue.toString() + " : See further object inspection for object details");
			toBeInspected.add(fieldValue);
		} else {
			System.out.println(f.getName() + " = null");
		}
	}

}
