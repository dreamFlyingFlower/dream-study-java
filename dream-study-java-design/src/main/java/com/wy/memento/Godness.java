package com.wy.memento;

public class Godness implements Cloneable {

	private String name;

	private String sex;

	private int age;

	public Godness() {

	}

	public Godness(String name, String sex, int age) {
		this.name = name;
		this.sex = sex;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void display() {
		System.out.println("name:" + name + ",sex:" + sex + ",age:" + age);
	}

	@Override
	public Godness clone() {
		try {
			return (Godness) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Godness getMemento() {
		return this.clone();
	}

	public void setMemento(Godness memento) {
		this.name = memento.getName();
		this.sex = memento.getSex();
		this.age = memento.getAge();
	}
}