package com.wy.enums;

public enum UserType {
	
	BOSS("boss"),MANAGER("manager"),STORE("store"),EMPLOYEE("employee");
	
	private String type;
	
	private UserType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
