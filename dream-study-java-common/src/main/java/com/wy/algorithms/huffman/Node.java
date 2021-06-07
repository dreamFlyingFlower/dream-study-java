package com.wy.algorithms.huffman;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node implements Comparable<Node> {

	/**
	 * 表示节点存的字符
	 */
	private String chars = "";

	/**
	 * 权重
	 */
	private int weight;

	private Node leftNode;

	private Node rightNode;

	private Node parent;

	public int compareTo(Node o) {
		return this.weight - o.weight;
	}
}