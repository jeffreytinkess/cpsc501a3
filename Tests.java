import org.junit.Test;
import org.junit.Before;
import org.jdom2.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
public class Tests{

private Serializer serial;
private Deserializer deserial;
private Visualizer display;





@Before
public void setup(){
serial = new Serializer();
deserial = new Deserializer();
display = new Visualizer();
}


@Test
public void testSerializePrimitive(){
	PrimObj test = new PrimObj();
	Document d = serial.serialize(test);
	assertEquals(d.getContentSize(), 1);//assert only one root
	Element object = (Element)d.getContent(0);
	assertEquals(object.getContentSize(), 1);//assert only one object serialized
	Element referenceList = (Element) object.getContent(0);
	assertEquals(referenceList.getContentSize(), 2); //assert two fields serialized
}

@Test
public void testSerializeObject(){
	ObjRef test = new ObjRef();
	Document d = serial.serialize(test);
	assertEquals(d.getContentSize(), 1);
	Element object = (Element) d.getContent(0);
	assertEquals(object.getContentSize(), 2);//assert both object itself and primitive were created
	Element refList = (Element) object.getContent(0);
	assertEquals(refList.getContentSize(), 1);//assert objects reference was only field here
}


@Test
public void testSerializePrimArray(){
	PrimArray test = new PrimArray();
	Document d = serial.serialize(test);
	assertEquals(d.getContentSize(), 1);
	Element object = (Element) d.getContent(0);
	assertEquals(object.getContentSize(), 2);
	Element refList = (Element) object.getContent(0);
	assertEquals(refList.getContentSize(), 1);//assert only a single object reference to the array was created


}

@Test
public void testDeserializePrimitive(){
	//create an object, serialize it, deserialize it, then ensure references equal defaults
	PrimObj test = new PrimObj();
	Document d = serial.serialize(test);
	try{
		PrimObj recreated = (PrimObj) deserial.deserialize(d);
		assertEquals(recreated.getInt(), 1);
		assertEquals(recreated.getDouble(), 3.14, 0.5); 
	} catch (Exception e){
		fail();
	};	
}

@Test
public void testDeserializeObject(){
	//Create an object, serialize it (and its internal reference), then deserialize and check the returned object
	ObjRef test = new ObjRef();
	Document d = serial.serialize(test);
	try{
		ObjRef recreated = (ObjRef) deserial.deserialize(d);
		PrimObj internalRef = (PrimObj) recreated.getRef();
		assertEquals(internalRef.getInt(), 1);
		assertEquals(internalRef.getDouble(), 3.14, 0.5);
	} catch (Exception e){
		fail();
	};


}
@Test
public void testDeserializePrimArray(){
	//Create and serialize array object
	PrimArray test = new PrimArray();
	Document d = serial.serialize(test);
	//deserialize array
	Object o = deserial.deserialize(d);
	try{
		//cast object, check that array length was set properly and each element equals the set value
		PrimArray recreated = (PrimArray) o;
		int[] theArray = recreated.getArray();
		assertEquals(theArray.length, 5);
		for(int i = 0; i < 5; i++){
			assertEquals(theArray[i], i);
		}
	} catch (Exception e){fail();};
}



}

