import org.jdom2.*;
import java.util.*;
import java.lang.reflect.*;


public class Deserializer{
	private IdentityHashMap<Integer, Object> objectMap;
	
	
	public Deserializer(){
		
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
				createArray(objectElement, className, myClass);
			} else {
				//Create a new instance of this object, then add it to map with its ID
				try{
				Object o = myClass.newInstance();
				Integer objID = Integer.parseInt(objectElement.getAttributeValue("id"));
				objectMap.put(objID, o);
				
				} catch (InstantiationException ie){System.out.println("Failed to create object " + className);}
				catch (Exception e){e.printStackTrace();};	
			}			
		}

		//Map now contains instantiated objects for each serialized object, need to set all fields properly
		while(true){
			if (allObjects.isEmpty()){break;}
			//Already checked if all encodings are elements, so remove next one and call appropriate setting method
			Element objEncoding = (Element) allObjects.remove(0);
			String className = objEncoding.getAttributeValue("class");
			char firstLetter = className.charAt(0);
			if (firstLetter != '['){
				//Object is an array, call that method
				setFields(objEncoding);
			} else {
				//Object is not an array, call generic method
				setArrayElements(objEncoding);
			}
		}
		
		
		//DEBUG
		Visualizer v = new Visualizer();
		v.inspect(objectMap.get(0));
		toReturn = objectMap.get(0);
		return toReturn;
	}

	private void setFields(Element objectElement){
		
		//Get reference to empty object already created
		int objectID = Integer.parseInt(objectElement.getAttributeValue("id"));
		Object obj = objectMap.get(objectID);
		
		//Now get a list of all references from object element
		List<Content> allFieldEncodings = objectElement.getContent();
		for (Content c: allFieldEncodings){
			
			//Make sure content is an element as it should be per specification
			Element fieldElement;
			try{
				fieldElement = (Element) c;
			} catch (Exception e){
				System.out.println("Error: non-element content used to store field information in setFields()");
				break;
			};
			//Now have the element for field itself, get name of its (only) content and switch off it
			Element referenceElement = fieldElement.getChild("value");
			
			
			if (referenceElement == null){
				//Is an object reference, we know fieldValue is an integer ID value so parse it
				referenceElement = fieldElement.getChild("reference");
				Text textElement = (Text) referenceElement.getContent(0);
				String fieldValue = textElement.getText();
				String fieldName = fieldElement.getAttributeValue("name");
			
				
				Integer referenceID = Integer.parseInt(fieldValue);
				//Then find that object in map
				Object reference = objectMap.get(referenceID);
				//finally, get fields name and set it
				try{
				obj.getClass().getDeclaredField(fieldName).set(obj,reference);
				} catch (Exception e){System.out.println("Error setting field value");
				break;
				};
			} else {
				//Is a primitive reference
				Text textElement = (Text) referenceElement.getContent(0);
				String fieldValue = textElement.getText();
				String fieldName = fieldElement.getAttributeValue("name");
				
				Class type;
				Field f;
				
				//Get the field itself
				try{
				f = obj.getClass().getDeclaredField(fieldName);
				type = f.getType();
				System.out.println("fields type is " + type.toString());
				} catch (Exception e){System.out.println("Error setting field value");
				e.printStackTrace();
				break;};
				//Now have a reference to the class type and field, can get its value in a wrapper and set field
				Object parsedValue = parsePrimitive(fieldValue, type);
				try{
					f.set(obj, parsedValue);
					System.out.println("Successfully set value of obj " + obj.toString() + " to value " + parsedValue.toString());
				}catch (Exception e){};
				
			}
		}
	}

	private Object parsePrimitive(String value, Class type){
		String typeCode = type.toString();
		Object toReturn;
		switch(typeCode){
		
			case "int": toReturn = Integer.parseInt(value);
				break;
			case "double": toReturn = Double.parseDouble(value);
				break;
			case "char": toReturn = value.charAt(0);
				break;
			case "boolean": toReturn = Boolean.parseBoolean(value);
				break;
			case "float":toReturn = Float.parseFloat(value);
				break;
			case "byte": toReturn = Byte.parseByte(value);
				break;
			case "short": toReturn = Short.parseShort(value);
				break;
			case "long": toReturn = Long.parseLong(value);
				break;
			default:
				System.out.println("Unsupported primitive detected of type " + typeCode);
				toReturn = null;
				break;
	
		}
	
		return toReturn;
	}
	private void setArrayElements(Element arrayElement){
		System.out.println("DEBUG: entering arrayElements setting method");
	}


	private void createArray(Element arrayElement, String arrayName, Class arrayClass){
		System.out.println("Trying to instantiate array, name is " + arrayName);
		Class componentType = arrayClass.getComponentType();
		int length = -1;
		String lengthAttribute = arrayElement.getAttributeValue("length");
		length = Integer.parseInt(lengthAttribute);
		Object newArray = Array.newInstance(componentType, length);
		Integer arrayID = Integer.parseInt(arrayElement.getAttributeValue("id"));
		objectMap.put(arrayID, newArray);
		
		
	}
	

	


}
