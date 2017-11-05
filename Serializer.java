import org.jdom2.*;
import java.io.*;
import org.jdom2.output.*;
import java.util.*;
import java.lang.reflect.*;
public class Serializer{
	private IdentityHashMap<Integer, Object> objectMap;
	private Integer nextID;
	private ArrayList<Object> toBeSerialized;



	public static void main(String[] args){
		Serializer testing = new Serializer();
		testing.serialize(new String("hello world"));
		
	}
	
	public Serializer(){
		objectMap = new IdentityHashMap<Integer, Object>();
		nextID = 0;
		toBeSerialized = new ArrayList<Object>();
	}
	public org.jdom2.Document serialize(Object obj){
		
		/*
		basic creation
		Element root = new Element("serialized");
		Document myDoc = new Document(root);
		Element obj1 = new Element("object");
		root.addContent(obj1);
		obj1.setAttribute("class", obj.getClass().getName());
		obj1.setAttribute("id", "3");
		Element field1 = new Element("fieldName");
		field1.setAttribute("name", "myFieldName");
		field1.setAttribute("declaringclass", "Object");
		obj1.addContent(field1);
		*/

		Element root = new Element("serialized");
		Document doc = new Document(root);
		Element toAdd = serializeSingleObject(obj);
		root.addContent(toAdd);



		sendToFile(doc);

		return doc;
	}

	/*
 	*	This method DOES NOT check if the object has already been serialized, check must occur elsewhere
 	*/
	private Element serializeSingleObject(Object obj){
		//Create objects root element, add its attributes and add to map
		Element objectElement = new Element("object");
		objectElement.setAttribute("class", obj.getClass().getName());
		objectElement.setAttribute("id", nextID.toString());
		objectMap.put(nextID, obj);
		nextID++;
		
		//get list of all fields (same code as visualizer)
		Field[] allFields = obj.getClass().getDeclaredFields();
		
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
			}else if (allFields[i].getType.isPrimitive()){//If the field is an object, add new object to map and list then put its ID here
				Element value = new Element("reference");
				//check if object in map yet
				boolean inMap = objectMap.containsValue(fieldValue);
				//if yes, get its ID and store that here
				if(inMap){
					
				} else {
				//if no, generate new ID (and store it in xml), add to map and tobeinspected
				}
			}



			objectElement.addContent(fieldElement);
		}
		return objectElement;
	}

	private void sendToFile(Document doc){
		XMLOutputter out = new XMLOutputter();
		
		try{
			PrintWriter writer = new PrintWriter("Test.txt", "UTF-8");
			out.output(doc, writer);
			writer.close();
		} catch (Exception e){};
		
	};


}
