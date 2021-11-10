package com.wy.state;

/**
 * 诛仙
 *
 * @author 飞花梦影
 * @date 2021-11-10 14:00:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class JadeDynasty {

	private String name;

	private String author;

	private Integer age;

	private State state;

	public void doSomething() {
		state.doSomething(this);
		// 复位,都所以方法以后再执行
		state = new MState();
	}

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}