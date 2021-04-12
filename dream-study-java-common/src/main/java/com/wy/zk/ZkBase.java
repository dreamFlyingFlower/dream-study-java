package com.wy.zk;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @description zk的watch事件,是一次性触发的.当watch数据发生变化时,会通知监听了该watch的client
 * @instruction
 * @question 相同zkid如何选择leader?为何集群数必须是奇数?根据cap原则,和eureka的区别;脑列是什么?
 * @author ParadiseWY
 * @date 2019年4月25日 下午5:14:46
 * @git {@link https://github.com/mygodness100}
 */
public class ZkBase {

	private static final String CONNECT_ADDR = "192.168.1.146:2181,192.168.1.146:2182,192.168.1.146:2183";

	// session超时时间
	private static final int SESSION_OUTTIME = 2000;

	// 信号量,阻塞程序执行,用于等待zookeeper连接成功,发送成功信号,此处只需要1次即可
	static final CountDownLatch COUNT_DOWN_AWAIT = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {

		ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// 获取事件的状态
				KeeperState keeperState = event.getState();
				// 获取事件的类型
				EventType eventType = event.getType();
				// 如果是建立连接
				if (KeeperState.SyncConnected == keeperState) {
					// 如果是连接成功
					if (EventType.None == eventType) {
						// 如果建立连接成功,则发送信号量,让后续阻塞程序向下执行
						COUNT_DOWN_AWAIT.countDown();
						System.out.println("zk 建立连接");
					}
				}
			}
		});
		// 进行阻塞,等待连接
		COUNT_DOWN_AWAIT.await();
		System.out.println("..");
		// 创建父节点
		zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		// 创建子节点,无需认证
		zk.create("/testRoot/children", "childrendata".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		// 获取节点洗信息
		byte[] data = zk.getData("/testRoot", false, null);
		System.out.println(new String(data));
		// getChildren:同步获得子节点列表,后一个参数表示是否对该节点对子节点进行监听
		System.out.println(zk.getChildren("/testRoot", false));
		// 异步获得子节点列表,不能立即得到返回值,需要写在回调函数中对结果进行处理
		// 参数列表分别是节点路径,是否监听,回调函数,上下文对象
		zk.getChildren("/testRoot", true, new AsyncCallback.Children2Callback() {

			/**
			 * @param rc 返回码
			 * @param path 当前节点
			 * @param ctx 当前上下文,即当前getChildren的最后一个值,可以是任意值
			 * @param children 子节点列表
			 * @param stat 当前节点状态
			 */
			@Override
			public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {

			}
		}, null);

		// 修改节点的值
		zk.setData("/testRoot", "modify data root".getBytes(), -1);
		// 异步修改节点的值
		zk.setData("testRoot", "modify async data root".getBytes(), -1, new StatCallback() {

			@Override
			public void processResult(int rc, String path, Object ctx, Stat stat) {

			}
		}, null);
		byte[] data1 = zk.getData("/testRoot", false, null);
		System.out.println(new String(data1));

		// 判断节点是否存在
		System.out.println(zk.exists("/testRoot/children", false));
		// 删除节点,节点,版本号(-1表示删除所有历史数据,不校验版本信息)
		zk.delete("/testRoot/children", -1);
		// 异步删除,节点,版本号,回调函数,上下文
		zk.delete("/testRoot", -1, new VoidCallback() {

			/**
			 * rc:服务器响应码,0调用成功-4端口连接,-110直接节点存在,-112回话过期 path:节点路径
			 * ctx:调用接口传入的上下文环境,此处就是a,可以传入其他参数
			 */
			@Override
			public void processResult(int rc, String path, Object ctx) {

			}
		}, "a");
		// 返回当前节点的状态,是否进行监听
		System.out.println(zk.exists("/testRoot/children", false));
		// 异步调用返回当前节点状态
		zk.exists("/testRoot/children", false, new StatCallback() {

			@Override
			public void processResult(int rc, String path, Object ctx, Stat stat) {

			}
		}, null);
		zk.close();
	}
}