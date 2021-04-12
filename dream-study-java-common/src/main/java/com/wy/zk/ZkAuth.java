package com.wy.zk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * @description zk的节点授权,用的不多.授权只会对一个节点认证,要对所有的都认证,则都需要auth
 *              需要在定义节点的时候添加认证,之后所有对节点的操作都需要认证
 * @author ParadiseWY
 * @date 2019年4月25日 下午4:13:22
 * @git {@link https://github.com/mygodness100}
 */
public class ZkAuth implements Watcher {

	private static ZooKeeper zk = null;

	private final static String CONNECT_ADDR = "192.168.1.146:2181";

	// 测试节点
	private final static String PATH = "/testAuth";

	// 测试删除节点
	private final static String PATH_DEL = "/testAuth/delNode";

	// 认证类型
	private final static String AUTH_TYPE = "digest";

	// 正确的密码
	private final static String AUTH_CORRECT = "123456";

	// 错误的密码
	private final static String AUTH_BAD = "654321";

	// 计时器
	private AtomicInteger seq = new AtomicInteger();

	// 标识
	private static final String LOG_PREFIX_OF_MAIN = "【Main】";

	// 计数
	private CountDownLatch connectedSemaphore = new CountDownLatch(1);

	/**
	 * 测试认证
	 */
	public static void main(String[] args) throws Exception {
		ZkAuth testAuth = new ZkAuth();
		testAuth.createConnection(CONNECT_ADDR, 2000);
		// 新建一个ip模式的所有权限的授权对象
		// new ACL(Perms.ALL, new Id("ip", "192.1681.1.1"));
		// 创建一个digest模式的所有权限的授权对象
		// new ACL(Perms.ALL, new
		// Id("digest",DigestAuthenticationProvider.generateDigest("username:password")));
		// 使用zk预设的权限方式
		List<ACL> acls = new ArrayList<ACL>(1);
		for (ACL ids_acl : Ids.CREATOR_ALL_ACL) {
			acls.add(ids_acl);
		}
		try {
			// zk节点名,节点内容,认证方式,节点持久化方式
			zk.create(PATH, "init content".getBytes(), acls, CreateMode.PERSISTENT);
			System.out.println("创建授权节点成功");
			zk.create(PATH_DEL, "will be deleted! ".getBytes(), acls, CreateMode.PERSISTENT);
			System.out.println("创建授权节点成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 获取数据
		getDataByNoAuthentication();
		getDataByBadAuthentication();
		getDataByCorrectAuthentication();
		// 更新数据
		updateDataByNoAuthentication();
		updateDataByBadAuthentication();
		updateDataByCorrectAuthentication();
		// 删除数据
		deleteNodeByBadAuthentication();
		deleteNodeByNoAuthentication();
		deleteNodeByCorrectAuthentication();
		Thread.sleep(1000);
		deleteParent();
		// 释放连接
		releaseConnection(zk);
	}

	/**
	 * 若需要一直监听某个节点,可以将get事件写到process中,并且监听的参数为true
	 */
	@Override
	public void process(WatchedEvent event) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (event == null) {
			return;
		}
		// 连接状态
		KeeperState keeperState = event.getState();
		// 事件类型
		EventType eventType = event.getType();
		// 受影响的path
		String path = event.getPath();
		System.out.println(path);
		String logPrefix = "【Watcher-" + this.seq.incrementAndGet() + "】";
		System.out.println(logPrefix + "收到Watcher通知");
		System.out.println(logPrefix + "连接状态:\t" + keeperState.toString());
		System.out.println(logPrefix + "事件类型:\t" + eventType.toString());
		if (KeeperState.SyncConnected == keeperState) {
			// 成功连接上ZK服务器
			if (EventType.None == eventType) {
				System.out.println(logPrefix + "成功连接上ZK服务器");
				connectedSemaphore.countDown();
			} else {
				// 当监听到子节点变化时到事件
				if (EventType.NodeChildrenChanged == eventType) {
					System.out.println(logPrefix + "子节点有变化");
				}
			}
		} else if (KeeperState.Disconnected == keeperState) {
			System.out.println(logPrefix + "与ZK服务器断开连接");
		} else if (KeeperState.AuthFailed == keeperState) {
			System.out.println(logPrefix + "权限检查失败");
		} else if (KeeperState.Expired == keeperState) {
			System.out.println(logPrefix + "会话失效");
		}
		System.out.println("--------------------------------------------");
	}

	/**
	 * 创建ZK连接
	 * 
	 * @param connectString ZK服务器地址列表
	 * @param sessionTimeout Session超时时间
	 */
	public void createConnection(String connectString, int sessionTimeout) {
		// 释放连接
		releaseConnection(zk);
		try {
			zk = new ZooKeeper(connectString, sessionTimeout, this);
			// 添加节点授权
			zk.addAuthInfo(AUTH_TYPE, AUTH_CORRECT.getBytes());
			System.out.println(LOG_PREFIX_OF_MAIN + "开始连接ZK服务器");
			// 倒数等待
			connectedSemaphore.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭ZK连接
	 */
	public static void releaseConnection(ZooKeeper zk) {
		if (zk != null) {
			try {
				zk.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,获取数据密码错误
	 */
	static void getDataByBadAuthentication() {
		String prefix = "[使用错误的授权信息]";
		try {
			ZooKeeper badzk = new ZooKeeper(CONNECT_ADDR, 2000, null);
			// 授权
			badzk.addAuthInfo(AUTH_TYPE, AUTH_BAD.getBytes());
			Thread.sleep(2000);
			System.out.println(prefix + "获取数据：" + PATH);
			System.out.println(prefix + "成功获取数据：" + badzk.getData(PATH, false, null));
			releaseConnection(badzk);
		} catch (Exception e) {
			System.err.println(prefix + "获取数据失败，原因：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,获取数据不采用密码
	 */
	static void getDataByNoAuthentication() {
		String prefix = "[不使用任何授权信息]";
		try {
			System.out.println(prefix + "获取数据：" + PATH);
			ZooKeeper nozk = new ZooKeeper(CONNECT_ADDR, 2000, null);
			Thread.sleep(2000);
			System.out.println(prefix + "成功获取数据：" + nozk.getData(PATH, false, null));
			releaseConnection(nozk);
		} catch (Exception e) {
			System.err.println(prefix + "获取数据失败，原因：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,获取数据使用正确的密码
	 */
	static void getDataByCorrectAuthentication() {
		String prefix = "[使用正确的授权信息]";
		try {
			System.out.println(prefix + "获取数据：" + PATH);
			System.out.println(prefix + "成功获取数据：" + zk.getData(PATH, false, null));
		} catch (Exception e) {
			System.out.println(prefix + "获取数据失败，原因：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,更新数据不使用密码
	 */
	static void updateDataByNoAuthentication() {
		String prefix = "[不使用任何授权信息]";
		System.out.println(prefix + "更新数据： " + PATH);
		try {
			ZooKeeper nozk = new ZooKeeper(CONNECT_ADDR, 2000, null);
			Thread.sleep(2000);
			Stat stat = nozk.exists(PATH, false);
			if (stat != null) {
				nozk.setData(PATH, prefix.getBytes(), -1);
				System.out.println(prefix + "更新成功");
			}
			releaseConnection(nozk);
		} catch (Exception e) {
			System.err.println(prefix + "更新失败，原因是：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,更新数据使用错误密码
	 */
	static void updateDataByBadAuthentication() {
		String prefix = "[使用错误的授权信息]";
		System.out.println(prefix + "更新数据：" + PATH);
		try {
			ZooKeeper badzk = new ZooKeeper(CONNECT_ADDR, 2000, null);
			// 授权
			badzk.addAuthInfo(AUTH_TYPE, AUTH_BAD.getBytes());
			Thread.sleep(2000);
			Stat stat = badzk.exists(PATH, false);
			if (stat != null) {
				badzk.setData(PATH, prefix.getBytes(), -1);
				System.out.println(prefix + "更新成功");
			}
			releaseConnection(badzk);
		} catch (Exception e) {
			System.err.println(prefix + "更新失败，原因是：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,更新数据使用正确密码
	 */
	static void updateDataByCorrectAuthentication() {
		String prefix = "[使用正确的授权信息]";
		System.out.println(prefix + "更新数据：" + PATH);
		try {
			Stat stat = zk.exists(PATH, false);
			if (stat != null) {
				zk.setData(PATH, prefix.getBytes(), -1);
				System.out.println(prefix + "更新成功");
			}
		} catch (Exception e) {
			System.err.println(prefix + "更新失败，原因是：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,删除节点不使用密码
	 */
	static void deleteNodeByNoAuthentication() throws Exception {
		String prefix = "[不使用任何授权信息]";
		try {
			System.out.println(prefix + "删除节点：" + PATH_DEL);
			ZooKeeper nozk = new ZooKeeper(CONNECT_ADDR, 2000, null);
			Thread.sleep(2000);
			Stat stat = nozk.exists(PATH_DEL, false);
			if (stat != null) {
				nozk.delete(PATH_DEL, -1);
				System.out.println(prefix + "删除成功");
			}
			releaseConnection(nozk);
		} catch (Exception e) {
			System.err.println(prefix + "删除失败，原因是：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,删除节点使用错误密码
	 */
	static void deleteNodeByBadAuthentication() throws Exception {
		String prefix = "[使用错误的授权信息]";
		try {
			System.out.println(prefix + "删除节点：" + PATH_DEL);
			ZooKeeper badzk = new ZooKeeper(CONNECT_ADDR, 2000, null);
			// 授权
			badzk.addAuthInfo(AUTH_TYPE, AUTH_BAD.getBytes());
			Thread.sleep(2000);
			Stat stat = badzk.exists(PATH_DEL, false);
			if (stat != null) {
				badzk.delete(PATH_DEL, -1);
				System.out.println(prefix + "删除成功");
			}
			releaseConnection(badzk);
		} catch (Exception e) {
			System.err.println(prefix + "删除失败，原因是：" + e.getMessage());
		}
	}

	/**
	 * 当zk开启了权限验证时,获取数据必须有对应的权限,此处开启digest模式,删除节点使用正确密码
	 */
	static void deleteNodeByCorrectAuthentication() throws Exception {
		String prefix = "[使用正确的授权信息]";
		try {
			System.out.println(prefix + "删除节点：" + PATH_DEL);
			Stat stat = zk.exists(PATH_DEL, false);
			if (stat != null) {
				zk.delete(PATH_DEL, -1);
				System.out.println(prefix + "删除成功");
			}
		} catch (Exception e) {
			System.out.println(prefix + "删除失败，原因是：" + e.getMessage());
		}
	}

	/**
	 * 使用正确的密码删除节点
	 */
	private static void deleteParent() throws Exception {
		try {
			Stat stat = zk.exists(PATH_DEL, false);
			if (stat == null) {
				zk.delete(PATH, -1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}