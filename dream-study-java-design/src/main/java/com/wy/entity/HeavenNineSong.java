package com.wy.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 天行九歌
 *
 * @author 飞花梦影
 * @date 2021-11-04 09:54:11
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class HeavenNineSong implements Cloneable {

	private String name;

	private String author;

	private List<String> people;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getPeople() {
		return people;
	}

	public void setPeople(List<String> people) {
		this.people = people;
	}

	@Override
	public HeavenNineSong clone() {
		try {
			// 深克隆需要重写引用类型,不包括String
			HeavenNineSong result = (HeavenNineSong) super.clone();
			result.setPeople(new ArrayList<>());
			return result;
			// 直接返回就是浅克隆,浅克隆无法复制引用类型,引用类型仍然是同一个地址
			// return (HeavenNineSong) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, name, people);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeavenNineSong other = (HeavenNineSong) obj;
		return Objects.equals(author, other.author) && Objects.equals(name, other.name)
				&& Objects.equals(people, other.people);
	}
}