package com.wy.algorithms.huffman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 哈夫曼树
 * 
 * @author 飞花梦影
 * @date 2021-06-07 17:12:20
 */
public class HuffmenTree {

	private Node root;

	private List<Node> leafs;

	Map<Character, Integer> weights;

	public HuffmenTree(Map<Character, Integer> weights) {
		this.weights = weights;
		leafs = new ArrayList<Node>();
	}

	private String deCode(String str) {
		StringBuffer buffer = new StringBuffer();
		char cs[] = str.toCharArray();
		LinkedList<Character> characters = new LinkedList<Character>();
		for (char c : cs) {
			characters.add(c);
		}
		while (characters.size() > 0) {
			Node node = root;
			do {
				// 每次取第一次字符
				Character c = characters.removeFirst();
				if (c.charValue() == '0') {
					node = node.getLeftNode();
				} else {
					node = node.getRightNode();
				}
			} while (node.getChars().length() != 1);
			buffer.append(node.getChars());
		}
		return buffer.toString();
	}

	private String enCode(String str) {
		Map<Character, String> map = codeInfo();
		char[] keys = str.toCharArray();
		StringBuffer buffer = new StringBuffer();
		for (char character : keys) {
			System.out.println(character + ":" + map.get(character));
			buffer.append(map.get(character));
			// System.out.println(character + ":" + map.get(character));
		}
		return buffer.toString();
	}

	private Map<Character, String> codeInfo() {
		Map<Character, String> map = new HashMap<Character, String>();
		for (Node node : leafs) {
			Character c = new Character(node.getChars().charAt(0));
			String code = "";
			Node start = node;
			do {
				// 判断是否为左节点
				if (start.getParent() != null && start == start.getParent().getLeftNode()) {
					// 因为是先从叶子节点开始的,所以这里不要放到code后面去加
					code = "0" + code;
				} else {
					code = "1" + code;
				}
				start = start.getParent();
			} while (start.getParent() != null);
			map.put(c, code);
		}
		return map;
	}

	private void creatTree() {
		Character[] keys = weights.keySet().toArray(new Character[0]);
		PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
		for (Character c : keys) {
			Node node = new Node();
			node.setChars(c.toString());
			node.setWeight(weights.get(c));
			priorityQueue.add(node);
			leafs.add(node);
		}
		int len = priorityQueue.size();
		// 最后一个不用走就是合成
		for (int i = 1; i <= len - 1; i++) {
			// 每次加进来都会排序 队列是一个排好序的 从小到大
			Node n1 = priorityQueue.poll();
			Node n2 = priorityQueue.poll();
			Node newNode = new Node();
			newNode.setChars(n1.getChars() + n2.getChars());
			newNode.setWeight(n1.getWeight() + n2.getWeight());
			newNode.setLeftNode(n1);
			newNode.setRightNode(n2);
			n1.setParent(newNode);
			n2.setParent(newNode);
			priorityQueue.add(newNode);
		}
		root = priorityQueue.poll();
		System.out.println("哈夫曼树构建成功");
	}

	public static void main(String[] args) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		// a:3 b:24 c:6 d:20 e:34 f:4 g:12
		map.put('a', 3);
		map.put('b', 24);
		map.put('c', 6);
		map.put('d', 20);
		map.put('e', 34);
		map.put('f', 4);
		map.put('g', 12);
		HuffmenTree huffmenTree = new HuffmenTree(map);
		huffmenTree.creatTree();
		huffmenTree.printCode();
		String str = "中午小树林见";
		System.out.println(huffmenTree.enCode(str));
		System.out.println(huffmenTree.deCode("1000001111001001001110"));
	}

	private void printCode() {
		Map<Character, String> map = codeInfo();
		Character[] keys = weights.keySet().toArray(new Character[0]);
		for (Character character : keys) {
			System.out.println(character + ":" + map.get(character));
		}
	}
}