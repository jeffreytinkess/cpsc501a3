import org.jdom2.*;
import java.io.*;
import org.jdom2.output.*;
import java.util.*;
import java.lang.reflect.*;
import java.net.Socket;


public class Serializer{
	private IdentityHashMap<Object, Integer> objectMap;
	private Integer nextID;
	private ArrayList<Object> toBeSerialized;

	public Serializer(){
		objectMap = new IdentityHashMap<Object, Integer>();
		nextID = 0;
		toBeSerialized = new ArrayList<Object>();
	}
	public org.jdom2.Document serialize(Object obj){


		Element root = new Element("serialized");
		Document doc = new Document(root);
		objectMap.put(obj, nextID);
		nextID++;
		Element toAdd = serializeSingleObject(obj);
		root.addContent(toAdd);

		while(true){
			if (toBeSerialized.isEmpty()){
				break;
			} else {

				Object o = toBeSerialized.remove(0);
				Element extraObject = serializeSingleObject(o);
				root.addContent(extraObject);
			}
		}

		//sendToSocket(doc);
		//Deserializer de = new Deserializer();
		//de.deserialize(doc);
		return doc;
	}

	/*
 	*	This method DOES NOT check if the object has already been serialized, check must occur elsewhere
 	*/
	private Element serializeSingleObject(Object obj){
		//Create objects root element, add its attributes and add to map
		Element objectElement = new Element("object");
		objectElement.setAttribute("class", obj.getClass().getName());
		objectElement.setAttribute("id", objectMap.get(obj).toString());

		if (obj.getClass().isArray()){
			//call and return value from different method
			return serializeArray(obj, objectElement);
		}

		//get list of all fields (same code as visualizer)
		Field[] someFields = obj.getClass().getDeclaredFields();
		Field[] someMoreFields = obj.getClass().getFields();

		Field[] allFields =  new Field[someFields.length + someMoreFields.length];

		for (int i = 0; i < someFields.length; i++){
				allFields[i] = someFields[i];
		}
		for (int i = 0; i < someMoreFields.length; i++){
			allFields[i+someFields.length] = someMoreFields[i];
		}
		//for each field, create a new element
		for (int i = 0; i < allFields.length; i++){
			//Ignore the "this" field
			if (allFields[i].getName().equals("this$0")){continue;};
			Element fieldElement = new Element("field");
			fieldElement.setAttribute("name", allFields[i].getName());
			fieldElement.setAttribute("declaringclass", obj.getClass().getName());
			//handle primitive/object fields seperatley
			Object fieldValue = null;
			try{
				fieldValue = allFields[i].get(obj);
			} catch (Exception e){e.printStackTrace();};
			if (fieldValue == null){ continue;}



			if (allFields[i].getType().isPrimitive()){//If the field is primitive, add it as raw text
				Element value = new Element("value");
				Text valueText = new Text(fieldValue.toString());
				value.addContent(valueText);
				fieldElement.addContent(value);
			} else {
				//Field is an object
				Integer objectID = -1;
				//check if object is already in map
				if (objectMap.containsKey(fieldValue)){
					//Object has been serialized, get its ID and use that
					objectID = objectMap.get(fieldValue);
				} else {
					//add object to map and list
					objectID = nextID;
					objectMap.put(fieldValue, nextID);
					toBeSerialized.add(fieldValue);
					nextID++;
				}
				Element reference = new Element("reference");
				Text valueText = new Text(objectID.toString());
				reference.addContent(valueText);
				fieldElement.addContent(reference);
			}
			objectElement.addContent(fieldElement);
		}
		return objectElement;
	}

	private Element serializeArray(Object obj, Element e){
		//Base element of array has already been created, add unique attributes and add values then return e
		int length = Array.getLength(obj);
		e.setAttribute("length", "" + length);
		if (length == 0){
			return e;
		}
		Class arrayType = obj.getClass().getComponentType();
		
		if (arrayType.isPrimitive()){
			//Array of primitives, wrap each into a value and add to element
			for (int i = 0; i < length; i++){
				Element value = new Element("value");
				Text valueText = new Text(Array.get(obj, i).toString());
				value.addContent(valueText);
				e.addContent(value);

			}
		} else {
			//Array is of object references, for each check if it has been serialized and handle
			for (int i = 0; i < length; i++){
				Element reference = new Element("reference");
				Object arrayElement = Array.get(obj, i);
				Integer objectID = -1;
				if (objectMap.containsKey(arrayElement)){
					objectID = objectMap.get(arrayElement);
				} else {
					objectID = nextID;
					objectMap.put(arrayElement, objectID);
					toBeSerialized.add(arrayElement);
					nextID++;
				}
				Text referenceText = new Text(objectID.toString());
				reference.addContent(referenceText);
				e.addContent(reference);
			}
		}


		return e;
	}

	private void sendToFile(Document doc){
		XMLOutputter out = new XMLOutputter();

		try{
			PrintWriter writer = new PrintWriter("Test.txt", "UTF-8");
			out.output(doc, writer);
			writer.close();
		} catch (Exception e){};

	};

	public void sendToSocket(Document doc){
		XMLOutputter out = new XMLOutputter();
		
		try{
			Socket socketOut = new Socket("localhost", 6789);
			PrintWriter output = new PrintWriter(new DataOutputStream(socketOut.getOutputStream()));
			out.output(doc, output);
			output.close();
			System.out.println("it worked");
		} catch (Exception e){
			e.printStackTrace();
		};
		
	}


}
