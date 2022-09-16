package com.wy.entity;

/**
 * 动漫
 *
 * @author 飞花梦影
 * @date 2022-09-16 15:29:24
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class Comic {

	private String name;

	private String country;

	private String label;

	public Comic(String name, String country, String label) {
		super();
		this.name = name;
		this.country = country;
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}