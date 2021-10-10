package com.wy;

/**
 * 中序遍历
 * 
 * @auther 飞花梦影
 * @date 2021-06-07 21:15:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MiddleSearch {

	int data;

	MiddleSearch left;

	MiddleSearch right;

	public MiddleSearch() {
		super();
	}

	public MiddleSearch(int data) {
		this.data = data;
		left = null;
		right = null;
	}

	// 左子树的点永远小于根节点 ,右子树永远大于根节点
	public void insert(MiddleSearch root, int data) {
		if (data > root.data) { // 插入右边
			if (root.right == null) {
				root.right = new MiddleSearch(data);
			} else {
				insert(root.right, data);
			}
		} else if (data <= root.data) {
			if (root.left == null) {
				root.left = new MiddleSearch(data);
			} else {
				insert(root.left, data);
			}
		} else {
			return;
		}
	}

	public static void inOrder(MiddleSearch root) {
		if (root != null) {
			inOrder(root.left);
			System.out.print(root.data + " ");
			inOrder(root.right);
		}
	}

	public void find() {

	}

	public static void main(String[] args) {
		int[] data = { 3, 14, 7, 1, 1, 8 };
		MiddleSearch nodeTreeSearch = new MiddleSearch(data[0]);
		for (int i = 1; i < data.length; i++) {
			nodeTreeSearch.insert(nodeTreeSearch, data[i]);
		}
		System.out.println("中序遍历");
		inOrder(nodeTreeSearch);
	}
}