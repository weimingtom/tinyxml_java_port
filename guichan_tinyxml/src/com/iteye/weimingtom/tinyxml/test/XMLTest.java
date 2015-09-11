package com.iteye.weimingtom.tinyxml.test;

import com.iteye.weimingtom.tinyxml.TiXmlDocument;
import com.iteye.weimingtom.tinyxml.TiXmlElement;
import com.iteye.weimingtom.tinyxml.TiXmlNode;
import com.iteye.weimingtom.tinyxml.TiXmlText;

public class XMLTest {
	public final static void main(String[] args) {
		String demoStart = 
			"<?xml version=\"1.0\"  standalone='no' >\n" +
			"<!-- Our to do list \n data -->" +
			"<ToDo>\n" +
			"<Item priority=\"1\" distance='close'> Go to the <bold>Toy store!</bold></Item>" +
			"<Item priority=\"2\" distance='none'> Do bills   </Item>" +
			"<Item priority=\"2\" distance='far'> Look for Evil Dinosaurs! </Item>" +
			"</ToDo>";

		{
			TiXmlDocument doc = new TiXmlDocument("demotest.xml");
			doc.Parse(demoStart);

			if (doc.Error()) {
				System.out.printf("Error in %s: %s\n", doc.Value(), doc.ErrorDesc());
				System.exit(1);
			}
			doc.SaveFile();
			doc.destroy();
		}

		TiXmlDocument doc = new TiXmlDocument("demotest.xml" );
		doc.LoadFile();

		System.out.printf("** Demo doc read from disk: ** \n\n");
		doc.Print(System.out);

		TiXmlNode node = null;
		TiXmlElement todoElement = null;
		TiXmlElement itemElement = null;

		node = doc.FirstChild("ToDo");
		assert(node != null);
		todoElement = node.ToElement();
		assert(todoElement != null);

		node = todoElement.FirstChild();
		assert(node != null);
		itemElement = node.ToElement();
		assert(itemElement != null);
		itemElement.SetAttribute("priority", 2);

		itemElement = itemElement.NextSiblingElement();
		itemElement.SetAttribute("distance", "here");

		itemElement = itemElement.NextSiblingElement();
		todoElement.RemoveChild(itemElement);

		itemElement = null;

		
		
		
		
		


		TiXmlElement item = new TiXmlElement("Item");
		item.SetAttribute("priority", "1");
		item.SetAttribute("distance", "far");

		TiXmlText text = new TiXmlText();
		text.SetValue("Talk to:");

		TiXmlElement meeting1 = new TiXmlElement("Meeting");
		meeting1.SetAttribute( "where", "School" );

		TiXmlElement meeting2 = new TiXmlElement("Meeting");
		meeting2.SetAttribute("where", "Lunch");

		TiXmlElement attendee1 = new TiXmlElement("Attendee");
		attendee1.SetAttribute("name", "Marple");
		attendee1.SetAttribute("position", "teacher");

		TiXmlElement attendee2 = new TiXmlElement("Attendee");
		attendee2.SetAttribute("name", "Voo");
		attendee2.SetAttribute("position", "counselor");

		meeting1.InsertEndChild(attendee1);
		meeting1.InsertEndChild(attendee2);

		item.InsertEndChild(text);
		item.InsertEndChild(meeting1);
		item.InsertEndChild(meeting2);

		node = todoElement.FirstChild("Item");
		assert(node != null);
		itemElement = node.ToElement();
		assert(itemElement != null);

		todoElement.InsertAfterChild(itemElement, item);

		System.out.printf("\n** Demo doc processed: ** \n\n");
		doc.Print(System.out);


		
		
		
		
		

		int count = 0;
		TiXmlElement element = null;

		count = 0;
		for (node = doc.FirstChild();
			node != null;
			node = node.NextSibling()) {
			count++;
		}
		System.out.printf("The document contains %d top level nodes. (3)\n", count);

		count = 0;
		for (node = doc.IterateChildren(null);
			node != null;
			node = doc.IterateChildren(node)) {
			count++;
		}
		System.out.printf("The document contains %d top level nodes. (3)\n", count);

		count = 0;
		for (element = todoElement.FirstChildElement();
			element != null;
			element = element.NextSiblingElement()) {
			count++;
		}
		System.out.printf("The 'ToDo' element contains %d elements. (3)\n", count);


		count = 0;
		for (node = todoElement.FirstChild("Item");
			 node != null;
			 node = node.NextSibling("Item")) {
			count++;
		}
		System.out.printf("The 'ToDo' element contains %d nodes with the value of 'Item'. (3)\n", count);
		
		
		
		
		
		
		
//		for (int i = 0; i < 1000; i++) {
//			doc.LoadFile("SmallRuleset1.xml");
//		}
//		doc.SaveFile("smalltest.xml");
	 	
		System.exit(0);
	}
}
