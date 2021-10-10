package com.wy.huffman;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HuffmenNode implements Comparable<HuffmenNode> {

	/**
	 * 表示节点存的字符
	 */
	private String chars = "";

	/**
	 * 权重
	 */
	private int weight;

	private HuffmenNode leftNode;

	private HuffmenNode rightNode;

	private HuffmenNode parent;

	public int compareTo(HuffmenNode o) {
		return this.weight - o.weight;
	}
}