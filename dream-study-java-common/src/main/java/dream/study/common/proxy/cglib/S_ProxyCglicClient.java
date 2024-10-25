package dream.study.common.proxy.cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * cglib代理类,需要实现特定的方法
 *
 * @author ParadiseWY
 * @date 2020-09-27 20:24:50
 */
public class S_ProxyCglicClient implements MethodInterceptor {

	/**
	 * 被代理对象
	 */
	private Object targetObject;

	public S_ProxyCglicClient(Object targetObject) {
		this.targetObject = targetObject;
	}

	/**
	 * @param obj 代理对象,是底层通过字节码生成的新的对象,不是被代理的对象
	 * @param method 被代理对象被调用的方法
	 * @param params 被调用方法的参数
	 * @param methodProxy 代理类的代理方法
	 */
	@Override
	public Object intercept(Object obj, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
		System.out.println(11);
		return methodProxy.invokeSuper(targetObject, params);
	}
}