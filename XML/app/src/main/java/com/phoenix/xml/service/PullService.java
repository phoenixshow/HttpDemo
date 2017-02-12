package com.phoenix.xml.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.phoenix.xml.bean.Person;

/**
 * 采用Pull解析XML内容
 */
public class PullService {
	public List<Person> getPersons(InputStream inStream) throws Throwable{
		List<Person> persons = null;
		Person person = null;

		//得到Pull解析器
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inStream, "UTF-8");
		//产生的一个事件
		int eventType = parser.getEventType();
		//只要不是文档结束事件就继续循环推进
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT://开始文档事件
					persons = new ArrayList<Person>();//完成初始化
					break;
				case XmlPullParser.START_TAG://开始元素（标签）事件
					//获取解析器当前指向的元素的名称
					String tagName = parser.getName();
					if("person".equals(tagName)){//如果是person节点
						person = new Person();
						//获取元素的属性值（id的值）并设置给person
						person.setId(Integer.parseInt(parser.getAttributeValue(0)));
					}
					if(person != null){
						if("name".equals(tagName)){
							//获取解析器当前指向元素的下一个文本节点的值
							person.setName(parser.nextText());
						}
						if("age".equals(tagName)){
							//获取解析器当前指向元素的下一个文本节点的值
							person.setAge(Short.parseShort(parser.nextText()));
						}
					}
					break;
				case XmlPullParser.END_TAG://结束元素（标签）事件
					if("person".equals(parser.getName())){
						persons.add(person);
						person = null;
					}
					break;
			}
			//进入下一个元素并触发相应事件
			eventType = parser.next();
		}
		return persons;
	}
}