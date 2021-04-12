package com.wy.zk.curator;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * zk的另外一个封装框架Curator,弥补了一些zk的缺点,比如断线重连,级联创建等
 * 
 * @author ParadiseWY
 * @date 2019-03-09 23:31:27
 * @git {@link https://github.com/mygodness100}
 */
public class ZkCurator {

	private static String path = "/testRoot";

	private static byte[] data = "testData".getBytes();

	public static void main(String[] args) {
		// 建立zk连接:连接地址;session超时时间,单位毫秒;连接超时时间,单位毫秒;重试策略
		// 重试策略
		// 指定最大重试次数,间隔时间相同:最大重试次数;2次重试之间的间隔
		new RetryNTimes(5, 1000);
		// 指定重试次数,每次重试的时间递增:基本重试时间,单位毫秒;最大重试次数;最大重试时间,单位毫秒
		new ExponentialBackoffRetry(1000, 5, 10000);
		// 指定最大重试时间,每次重试时间间隔
		new RetryUntilElapsed(5000, 1000);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.1.106:2181", 5000, 5000,
				new RetryForever(5000));
		client.start();

		try {
			// 创建节点
			client.create()
					// 若创建的path的父节点不存在,自动创建
					.creatingParentsIfNeeded()
					// 节点类型
					.withMode(CreateMode.EPHEMERAL)
					// 路径以及数据
					.forPath(path, data);
			// 删除节点
			client.delete()
					// 保证在zk连接的过程中能删除节点,若第一次失败,只要连接不断开就可继续删除
					.guaranteed()
					// 若被删除节点下还有子节点,将子节点也删除
					.deletingChildrenIfNeeded()
					// 删除指定版本,-1所有版本
					.withVersion(-1)
					// 需要删除的节点路径
					.forPath(path);
			// 获得子节点列表
			client.getChildren().forPath(path);
			// 获得节点数据
			Stat stat = new Stat();
			client.getData()
					// 将节点状态存入到状态对象中
					.storingStatIn(stat)
					// 节点路径
					.forPath(path);
			client.setData()
					// 指定更新的版本号
					.withVersion(stat.getVersion()).forPath("", data);
			// 判断节点是否存在
			client.checkExists().forPath(path);
			// 异步调用方法:context是上下文,最好是放入一个线程池
			client.checkExists().inBackground(new BackgroundCallback() {

				@Override
				public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
					// 当前调用的事件类型
					System.out.println(event.getType());
					// 获得调用方法的返回码:0表示成功,非0表示失败
					System.out.println(event.getResultCode());
					// 获得上下文对象
					event.getContext();
				}
			}, "context", Executors.newFixedThreadPool(5)).forPath(path);

			// 节点监听
			NodeCache nodeCache = new NodeCache(client, path);
			nodeCache.start();
			nodeCache.getListenable().addListener(new NodeCacheListener() {

				@Override
				public void nodeChanged() throws Exception {
					// 获得当前监听节点的数据
					nodeCache.getCurrentData().getData();
					nodeCache.close();
				}
			});

			// 子节点监听
			PathChildrenCache cache = new PathChildrenCache(client, path, true);
			cache.start();
			cache.getListenable().addListener(new PathChildrenCacheListener() {

				@Override
				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
					cache.close();
				}
			});

			// 初始化zk的watcher
			TreeCache tc = new TreeCache(client, "需要监听的节点路径,如/test");
			// 启动cache
			tc.start();
			// 添加监听事件
			tc.getListenable().addListener(new TreeCacheListener() {

				@Override
				public void childEvent(CuratorFramework cf, TreeCacheEvent event) throws Exception {
					// 事件处理
					// 初始化事件
					if (event.getType() == Type.INITIALIZED) {

					}
					// 添加节点事件
					if (event.getType() == Type.NODE_ADDED) {

					}
					// 删除节点
					if (event.getType() == Type.NODE_REMOVED) {

					}
					// 更新节点
					if (event.getType() == Type.NODE_UPDATED) {

					}
				}
			});
			tc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AtomicLong atomicLong = new AtomicLong(System.currentTimeMillis());

	/**
	 * 利用zk生成分布式主键
	 * 
	 * @return 主键
	 */
	public Long generateDistributeKey() {
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.1.106:2181", 5000, 5000,
				new RetryForever(5000));
		// 原子性操作
		DistributedAtomicLong distributedAtomicLong = new DistributedAtomicLong(client, "/distributeKey",
				new RetryNTimes(5, 500));
		try {
			AtomicValue<Long> increment = distributedAtomicLong.increment();
			if (increment.succeeded()) {
				return increment.postValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 不安全,分布式下仍有多线程问题,可加分布式锁保证安全,但没必要
		return atomicLong.getAndIncrement();
	}
}