package com.scs.multiplayerplatformer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class XMLHelper {

	private HashMap<String, String> map = new HashMap<>();

	public XMLHelper() {

	}


	public String getString(String id) {
		if (map.containsKey(id)) {
			return map.get(id);
		} else {
			String s;
			try {
				s = loadXML("assets/strings/strings.xml", id);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
				s = id;//"[Not Found]"
			}
			if (s == null) {
				s = id;
			}
			map.put(id, s);
			return s;
		}
	}


	public String loadXML(String filename, String lookFor) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = null;//dBuilder.parse(fXmlFile);

		File fXmlFile = new File(filename);
		if (fXmlFile.canRead()) {
			doc = dBuilder.parse(fXmlFile);
		} else {
			InputStream fntStr = this.getClass().getClassLoader().getResourceAsStream(filename);
			doc = dBuilder.parse(fntStr);

		}

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("resources");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				NodeList nList2 = eElement.getChildNodes();
				for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
					Node nNode2 = nList2.item(temp2);
					if (nNode2.getNodeType() == 1) {
						Element eElement2 = (Element) nNode2;
						String id = eElement2.getAttribute("name");
						String value = eElement2.getTextContent();
						//System.out.println("Name=" + value);
						if (lookFor.equalsIgnoreCase(id)) {
							value = value.replace("\\n", "\n"); 
							value = value.replace("\\'", "'"); 
							return value; 
						}
					}
				}
			}
		}
		return null;
	}


	public static void main(String args[]) {
		XMLHelper xml = new XMLHelper();
		String s = xml.getString("app_name");
		System.out.println(s);
	}

}
