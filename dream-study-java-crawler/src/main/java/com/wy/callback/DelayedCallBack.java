package com.wy.callback;

/**
 * 延时回调接口
 * 
 * 为了判断下载页面是否成功,因为csdn的cookie登录验证是通过js实现的,需要通过Selenium下载页面后等待一会检测cookie是否注入成功
 * 
 * @author 飞花梦影
 * @date 2021-01-07 17:31:08
 * @git {@link https://github.com/mygodness100}
 */
public interface DelayedCallBack<T> {

	/**
	 * 延时调用方法
	 *
	 * @param time
	 * @return
	 */
	T callBack(long time);

	/**
	 * 判断是否存在
	 *
	 * @return
	 */
	boolean isExist();

	/**
	 * 获取每次睡眠时间
	 *
	 * @return
	 */
	long sleepTime();

	/**
	 * 超时时间
	 *
	 * @return
	 */
	long timeOut();
}