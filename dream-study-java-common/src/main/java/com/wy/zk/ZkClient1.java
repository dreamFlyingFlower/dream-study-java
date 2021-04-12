package com.wy.zk;

/**
 * LeaderSelector是选举的核心类
 * 
 * @author ParadiseWY
 * @date 2019年4月25日 下午5:10:06
 * @git {@link https://github.com/mygodness100}
 */
public class ZkClient1 {

	public static void main(String[] args) throws Exception {
		new ZkWatcher();
		Thread.sleep(100000000);
	}
}