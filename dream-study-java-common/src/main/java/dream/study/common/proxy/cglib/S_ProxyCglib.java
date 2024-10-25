package dream.study.common.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

import dream.study.common.model.Pojo;

/**
 * 利用cglib实现代理,不需要接口,而是会生成被代理类的子类,并重写父类非final的方法
 * ASM:直接修改底层的字节码文件,功能比jdk和cglib代理更加强大,只是没有他们方便,cglib底层是用ASM实现
 * ASM主要的类为{@link org.springframework.asm.ClassReader}{@link org.springframework.asm.ClassWriter}
 * 
 * @author ParadiseWY
 * @date 2020-9-1 11:03:57
 */
public class S_ProxyCglib {

	public static void main(String[] args) {
		Enhancer e1 = new Enhancer();
		e1.setSuperclass(Pojo.class); // 被代理的对象类
		S_ProxyCglicClient t1 = new S_ProxyCglicClient(new Pojo()); // 设置代理类的代理对象
		e1.setCallback(t1); // 设置回调对象
		Pojo p = (Pojo) e1.create(); // 生成实际调用的类
		p.setUsername("test1111"); // 调用一次代理
		System.out.println("33333333" + p.getUsername()); // 调用一次代理
	}
}