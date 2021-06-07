package com.wy.algorithms.shortest;

import java.util.Scanner;

/**
 * 最短路径
 * 
 * @auther 飞花梦影
 * @date 2021-06-07 22:47:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Djstl {

	public static void main(String[] args) {
		// n,m表示N个点,m条边,x表示要求的最短路径的点
		int n, m, x;
		Scanner cin = new Scanner(System.in);
		n = cin.nextInt();
		m = cin.nextInt();
		x = cin.nextInt();
		// 表示a到b的路径权重
		int value[][] = new int[n + 1][n + 1];
		// 表示存最短路径的
		int dis[] = new int[n + 1];
		// 做路径的初始化,默认全部为INF
		for (int i = 1; i <= n; i++) {
			dis[i] = Integer.MAX_VALUE;
			for (int j = 1; j <= n; j++) {
				if (i == j) {
					// i == j时表示的就是自己
					value[i][j] = 0;
				} else {
					// -1 表示 i 到 j 没有路
					value[i][j] = -1;
				}
			}
		}
		for (int i = 0; i < m; i++) {
			int a = cin.nextInt();
			int b = cin.nextInt();
			int v = cin.nextInt();
			value[a][b] = v;
			if (a == x) {
				dis[b] = v;
			}
		}
		search(x, dis, value, n);
		cin.close();
	}

	private static void search(int x, int dis[], int value[][], int n) {
		boolean mark[] = new boolean[n + 1];
		for (int i = 1; i <= n; i++) {
			mark[i] = false;
		}
		// 表示当前这个点已经被加过了
		mark[x] = true;
		// 自己走自己
		dis[x] = 0;
		// 表示当前加了几个点了
		int count = 1;
		while (count <= n) {
			// 表示贪心策略里面要加的那个点
			int loc = 0;
			int min = Integer.MAX_VALUE;
			for (int i = 1; i <= n; i++) {
				if (!mark[i] && dis[i] < min) {
					min = dis[i];
					loc = i;
				}
			}
			if (loc == 0) {
				// 表示当前已经没有点可以加了
				break;
			}
			mark[loc] = true;
			count++;
			for (int i = 1; i <= n; i++) {
				if (!mark[i] && value[loc][i] != -1 && (dis[loc] + value[loc][i] < dis[i])) {
					dis[i] = dis[loc] + value[loc][i];
				}
			}
		}
		System.out.println("以" + x + "为起点的最短路径");
		for (int i = 1; i <= n; i++) {
			if (dis[i] != Integer.MAX_VALUE) {
				System.out.println(i + "最短为:" + dis[i]);
			} else
				System.out.println(i + "没有路");
		}
	}
}