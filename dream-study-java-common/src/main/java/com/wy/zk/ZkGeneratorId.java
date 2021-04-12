package com.wy.zk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;

/**
 * 在分布式环境下生成一个唯一的id
 * 
 * @author ParadiseWY
 * @date 2020-09-27 16:27:28
 */
public class ZkGeneratorId {

	private CuratorFramework client = null;

	private final String server;

	private final String root;

	private final String nodeName;

	private volatile boolean running = false;

	private ExecutorService cleanExector = null;

	public enum RemoveMethod {
		NONE, IMMEDIATELY, DELAY
	}

	public ZkGeneratorId(String zkServer, String root, String nodeName) {
		this.root = root;
		this.server = zkServer;
		this.nodeName = nodeName;
	}

	public void start() throws Exception {
		if (running) {
			throw new Exception("server has stated...");
		}
		running = true;
		init();
	}

	public void stop() throws Exception {
		if (!running) {
			throw new Exception("server has stopped...");
		}
		running = false;
		freeResource();
	}

	private void init() {
		client = CuratorFrameworkFactory.newClient(server, 5000, 5000, new RetryUntilElapsed(5000, 1000));
		client.start();
		cleanExector = Executors.newFixedThreadPool(10);
		try {
			client.create().withMode(CreateMode.PERSISTENT).forPath(root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void freeResource() {
		cleanExector.shutdown();
		try {
			cleanExector.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			cleanExector = null;
		}
		if (client != null) {
			client.close();
			client = null;
		}
	}

	private void checkRunning() throws Exception {
		if (!running)
			throw new Exception("请先调用start");
	}

	private String ExtractId(String str) {
		int index = str.lastIndexOf(nodeName);
		if (index >= 0) {
			index += nodeName.length();
			return index <= str.length() ? str.substring(index) : "";
		}
		return str;
	}

	public String generateId(RemoveMethod removeMethod) throws Exception {
		checkRunning();
		final String fullNodePath = root.concat("/").concat(nodeName);
		final String ourPath = client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(fullNodePath, null);
		if (removeMethod.equals(RemoveMethod.IMMEDIATELY)) {
			client.delete().forPath(ourPath);
		} else if (removeMethod.equals(RemoveMethod.DELAY)) {
			cleanExector.execute(new Runnable() {

				public void run() {
					try {
						client.delete().forPath(ourPath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		// node-0000000000, node-0000000001
		return ExtractId(ourPath);
	}
}