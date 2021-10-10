package com.wy.tiretree;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典树节点
 * 
 * @auther 飞花梦影
 * @date 2021-06-07 21:17:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
public class TireNode {

	/**
	 * 英文字母个数
	 */
	final static int MAX_SIZE = 26;

	/**
	 * 当前节点存的字母
	 */
	private char data;

	/**
	 * 是否为叶子节点:true->是,false->否
	 */
	private boolean leaf = false;

	/**
	 * 子节点
	 */
	private TireNode[] childs;

	public TireNode() {
		childs = new TireNode[MAX_SIZE];
		leaf = false;
	}
}