package com.phoenix.xml.service;

import com.phoenix.xml.bean.Person;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 采用SAX解析XML内容
 */
public class SAXService {
	public List<Person> getPersons(InputStream inStream) throws Throwable{
		//得到SAX解析器工厂
		SAXParserFactory factory = SAXParserFactory.newInstance();
		//得到SAX解析器
		SAXParser parser = factory.newSAXParser();
		PersonParse personParse = new PersonParse();
		parser.parse(inStream, personParse);
		inStream.close();
		return personParse.getPersons();
	}

	private final class PersonParse extends DefaultHandler{
		private List<Person> persons = null;
		private String tag = null;//记录当前所解析到的元素节点的名称
		private Person person = null;

		public List<Person> getPersons() {
			return persons;
		}

		@Override
		public void startDocument() throws SAXException {
			persons = new ArrayList<Person>();
		}

		/**
		 * String uri		命名空间的URL，我们的XML没有用到，传入的值为空
		 * String localName	标签名，比如<name>标签中的name
		 * String qName	带命名空间前缀（带冒号）的标签名，
		 * 				比如android:orientation="vertical"中的android:orientation
		 * Attributes attributes	属性的值，比如<person id="23">属性id中的23
		 */
		@Override
		public void startElement(String uri, String localName, String qName,
								 Attributes attributes) throws SAXException {
			if("person".equals(localName)){
				person = new Person();
				person.setId(Integer.parseInt(attributes.getValue(0)));
			}
			tag = localName;
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if("person".equals(localName)){
				persons.add(person);
				person = null;
			}
			tag = null;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if(tag!=null){
				String data = new String(ch, start, length);//获取文本节点的数据
				if("name".equals(tag)){
					person.setName(data);
				}else if("age".equals(tag)){
					person.setAge(Short.parseShort(data));
				}
			}
		}
	}
}