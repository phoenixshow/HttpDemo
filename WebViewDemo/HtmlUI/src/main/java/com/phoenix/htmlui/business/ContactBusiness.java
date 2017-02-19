package com.phoenix.htmlui.business;

import com.phoenix.htmlui.bean.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactBusiness {
	public List<Contact> getContacts(){
		List<Contact> list = new ArrayList<Contact>();
		list.add(new Contact(45, "张飞", "1381333322"));
		list.add(new Contact(47, "王飞", "1384762555"));
		list.add(new Contact(89, "老毕", "1381555552"));
		return list;
	}
}
