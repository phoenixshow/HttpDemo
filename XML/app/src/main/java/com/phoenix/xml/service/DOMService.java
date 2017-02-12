package com.phoenix.xml.service;

import com.phoenix.xml.bean.Person;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 采用DOM解析XML内容
 */
public class DOMService {
	public List<Person> getPersons(InputStream inStream) throws Throwable{
		List<Person> persons = new ArrayList<Person>();

		//获取DOM解析器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//获取DOM解析器
		DocumentBuilder builder = factory.newDocumentBuilder();
		//将解析树放入内存，通过返回值Document来描述解析结果这棵树
		Document document = builder.parse(inStream);
		//取得根元素，就是<persons>
		Element root = document.getDocumentElement();
		//得到所有person节点集合
		NodeList personNodes = root.getElementsByTagName("person");
		for(int i=0; i<personNodes.getLength(); i++){
			Person person = new Person();
			//取得了person元素节点
			Element personElement = (Element)personNodes.item(i);
			//获取属性id的值并设置给person
			person.setId(Integer.parseInt(personElement.getAttribute("id")));
			//获取person的子节点集合，一共有5个子节点，包括三个空
			NodeList personChilds = personElement.getChildNodes();
			for(int j=0; j<personChilds.getLength(); j++){
				//判断当前节点是否是元素类型节点，因为我们对三个空（文本类型节点）不感兴趣
				if(personChilds.item(j).getNodeType() == Node.ELEMENT_NODE){
					//获取person的子节点（<name>、<age>）
					Element childElement = (Element)personChilds.item(j);
					if("name".equals(childElement.getNodeName())){
						//获取孙节点的值，就是李雷，放入person
//						person.setName("设置Name："+childElement.getNodeValue());
						person.setName(childElement.getFirstChild().getNodeValue());
					}else if("age".equals(childElement.getNodeName())){
						//获取孙节点的值，就是30，放入person
						person.setAge(Short.parseShort(childElement.getFirstChild().getNodeValue()));
					}
				}
			}
			persons.add(person);
		}
		return persons;
	}
}