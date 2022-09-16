package com.wy.composite.exp;

/**
 * 菜单项类:属于叶子节点
 * 
 * @author 飞花梦影
 * @date 2022-09-16 11:29:26
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class MenuItem extends MenuComponent {

	public MenuItem(String name, int level) {
		this.name = name;
		this.level = level;
	}

	@Override
	public void print() {
		// 打印菜单项的名称
		for (int i = 0; i < level; i++) {
			System.out.print("--");
		}
		System.out.println(name);
	}
}