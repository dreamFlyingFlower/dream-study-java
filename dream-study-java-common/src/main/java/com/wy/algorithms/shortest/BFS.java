package com.wy.algorithms.shortest;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 广度优先搜索
 * 
 * @auther 飞花梦影
 * @date 2021-06-07 22:45:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class BFS {

	public static void main(String[] args) {
		// n个点,m条边
		int n, m;
		Scanner cin = new Scanner(System.in);
		while (cin.hasNext()) {
			n = cin.nextInt();
			m = cin.nextInt();
			int data[][] = new int[n + 1][n + 1];
			// a到b有路可以走
			int a, b;
			for (int i = 0; i < m; i++) {
				a = cin.nextInt();
				b = cin.nextInt();
				data[a][b] = 1;
			}
			Queue<Integer> queue = new LinkedBlockingQueue<Integer>();
			int start = cin.nextInt();
			int end = cin.nextInt();
			queue.add(start);
			// 有多少条路径可以到
			int tot = 0;
			// 该点是不是已经走过
			boolean mark[] = new boolean[n + 1];
			mark[start] = true;
			while (!queue.isEmpty()) {
				int current = queue.poll();
				for (int i = 1; i <= n; i++) {
					if (data[current][i] == 1 && !mark[i]) {
						if (i == end)
							tot++;
						else {
							queue.add(i);
							mark[i] = true;
						}
					}
				}
			}
			System.out.println("从" + start + "到" + end + "总共有" + tot + "条路径");
		}
		cin.close();
	}
}