package com.wy.sqlsession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.wy.mapper.UserMapper;
import com.wy.model.User;

/**
 * 批量插入,使用SqlSession的批量模式更快,更节省时间
 *
 * @author 飞花梦影
 * @date 2023-11-04 21:13:28
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MyBatchInsert {

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	public void insertBatch() {
		// 开启批量处理模式 BATCH 、关闭自动提交事务 false
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		// 反射获取,获取Mapper
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		ArrayList<User> arrayList = new ArrayList<>();
		// 模拟数据
		for (int i = 0; i < 50000; i++) {
			User student = new User();
			arrayList.add(student);
		}
		int count = arrayList.size();
		// 每批次插入的数据量
		int pageSize = 1000;
		// 线程数
		int threadNum = count / pageSize + 1;
		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		for (int i = 0; i < threadNum; i++) {
			int startIndex = i * pageSize;
			int endIndex = Math.min(count, (i + 1) * pageSize);
			List<User> subList = arrayList.subList(startIndex, endIndex);
			threadPoolTaskExecutor.execute(() -> {
				DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
				TransactionStatus status = transactionManager.getTransaction(transactionDefinition);
				try {
					userMapper.inserts(subList);
					transactionManager.commit(status);
				} catch (Exception e) {
					transactionManager.rollback(status);
					throw e;
				} finally {
					countDownLatch.countDown();
				}
			});
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}