import org.jdom2.*;
import java.util.*;
import java.lang.reflect.*;


public class Deserializer{
	private IdentityHashMap<Integer, Object> objectMap;
	private ArrayList<Object> toBeHandled;
	
	public Deserializer(){
		toBeHandled = new ArrayList<Object>();
		objectMap = new IdentityHashMap<Integer, Object>();
	}

	public Object deserialize(org.jdom2.Document document){
		Object toReturn = null;
		List<Content> allObjects = document.getRootElement().getContent();
		for (Content c: allObjects){
			//If the XML is properly formatted, all of these will be Elements. Cast and use as such (with check)
			Element objectElement;
			switch(c.getCType()){
				case Element :
					
					break;
				default :
					System.out.println("Error with XML formatting");
					return null;
			}
			objectElement = (Element) c;
			String className = objectElement.getAttributeValue("class");
			if (className == null){
				System.out.println("Error: object in XML file has no class attribute");
				return null;
			}
			//Now know what the class name of this object is, get a reference to its class
			Class myClass;
			try{
				myClass = Class.forName(className);
			} catch (ClassNotFoundException cnfe){System.out.println("Cant locate class file" + className);
				return null;}
			catch (LinkageError le){le.printStackTrace();
				return null;};
			//Use class to create a new instance of this object
			//check if it is an array, handle these seperatly
			char firstLetterOfName = className.charAt(0);
			if (firstLetterOfName == '['){
				//Object is an array, call array handling method
			} else {
				//Create a new instance of this object, then add it to map with its ID
				try{
				Object o = myClass.newInstance();
				Integer objID = Integer.parseInt(objectElement.getAttributeValue("id"));
				objectMap.put(objID, o);
				toBeHandled.add(o);
				} catch (InstantiationException ie){System.out.println("Failed to create object " + className);}
				catch (Exception e){e.printStackTrace();};	
			}			
		}

		//Map now contains instantiated objects for each serialized object, need to set all fields properly




		return toReturn;
	}

	


}
