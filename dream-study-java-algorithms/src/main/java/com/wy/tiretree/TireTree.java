package com.wy.tiretree;

/**
 * 字典树
 * 
 * @auther 飞花梦影
 * @date 2021-06-07 20:57:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class TireTree {

	public static void createTireTree(TireNode node, String str) {
		// 单词全部转成小写,ascii A => 65,a=>97 -97 >0, a->0 b->1,c->2
		char d[] = str.toCharArray();
		for (int i = 0; i < d.length; i++) {
			// 转成0~25之间的数字
			int loc = d[i] - 'a';
			if (node.getChilds()[loc] == null) {
				// 把英文字母存到一个数组里面0~25,a['a'] === a[97] => a[0] = 'a'缩小空间,0+97
				node.getChilds()[loc] = new TireNode();
				node.getChilds()[loc].setData(d[i]);
			}
			// 将node指向下一个节点
			node = node.getChilds()[loc];
		}
		node.setLeaf(true);
	}

	public static boolean find(String str, TireNode node) {
		char d[] = str.toCharArray();
		// O(n)
		for (int i = 0; i < d.length; i++) {
			int loc = d[i] - 'a';
			if (node.getChilds()[loc] != null) {
				node = node.getChilds()[loc];
			} else {
				return false;
			}
		}
		return node.isLeaf();
	}

	public static void main(String[] args) {
		String s[] = { "ps", "php", "ui", "css", "java", "js" };
		TireNode root = new TireNode();
		for (String ss : s) {
			createTireTree(root, ss);
		}
		System.out.println("插入完成");
		System.out.println(find("java", root));
		// 找前缀就是自动补全
		System.out.println(find("jav", root));
	}
}