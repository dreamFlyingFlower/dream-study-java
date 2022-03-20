package com.wy.jvm;

/**
 * 类加载器(ClassLoader):顶层为BootStrap,该加载器并不属于Java类,而是为了加载Java类存在的原生(native)组件
 * BootStrap主要加载jre/lib/rt.jar,所有追溯到最顶层的ClassLoader都是null,即BootStrap
 * ExtClassLoader:BootStrap的下一层,主要加载jre/lib/ext/*.jar,如果将其他jar包放在该目录下,则其加载类则为ExtClassLoader
 * AppClassLoader:ExtClassLoader的子加载类,默认的系统加载类,主要加载classpath下的jar包
 * 
 * ClassLoader的加载顺序:当前线程的类加载器加载第一个类->如果类加载器中引入了其他类,则其他类也由当前ClassLoader加载
 * ->每个ClassLoader加载类时,又先委托给其上级ClassLoader,这样是为了保证字节码文件的唯一性
 * ->所有上级ClassLoader没有加载到类时则会回到发起者ClassLoader,还加载不了则抛异常,不会再去找发起者的子ClassLoader
 * 
 * 自定义的ClassLoader必须继承{@link ClassLoader},重写loadClass,findClass,defineClass方法
 * 
 * 类的使用生命周期:
 * 
 * <pre>
 * 加载:通过类的全限定名来获取定义此类的二进制字节流,将这个字节码流锁代表的静态存储结构转化为方法区的运行时数据结构.
 * 		在内存生成一个代表这个类的java.lang.Class对象,作为方法区这个类的各种数据的访问入口
 * 验证:为确保class文件的字节流中包含的信息符合当前虚拟机规范
 * 		文件格式验证:验证字节码流是否符合class文件格式的规范
 * 		元数据验证:对字节码描述的信息进行语义分析
 * 		字节码验证:通过数据流和控制流分析,确定程序语义是合法的,符合逻辑的
 * 		符号引用验证:发生在虚拟机将符号引用转化为直接引用的时候
 * 准备:正式为类变量分配内存并设置类变量初始化的阶段,主要针对静态变量
 * 解析:虚拟机将常量池内的符号引用替换为直接引用的过程
 * 初始化:类初始化阶段是类加载过程中的最后一步,此时才开始执行类中定义的java程序代码.在准备阶段,变量已经赋值过一次系统初始值,
 * 		而在初始化阶段,则根据程序员通过程序制定的主观计划去初始化类变量和其他资源
 * 使用:在程序中使用
 * 卸载:停止程序
 * </pre>
 * 
 * @auther 飞花梦影
 * @date 2021-05-10 23:28:02
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class S_ClassLoader {

	public static void main(String[] args) {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
		try {
			systemClassLoader.loadClass("使用指定的类加载器加载某个类");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}