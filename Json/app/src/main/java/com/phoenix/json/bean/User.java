package com.phoenix.json.bean;

import com.google.gson.annotations.SerializedName;

public class User {
	private String name;
	private int age;
	@SerializedName("new")
	public boolean isNew;

	public User() {
	}

	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public User(String name, int age, boolean isNew) {
		this.name = name;
		this.age = age;
		this.isNew = isNew;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public String toString() {
		return "User{" +
				"age=" + age +
				", name='" + name + '\'' +
				", isNew=" + isNew +
				'}';
	}
}
