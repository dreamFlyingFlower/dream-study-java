package dream.study.jdk.jdk25;

import java.util.concurrent.StructuredTaskScope;

import dream.study.jdk.model.User;

/**
 * JDK25(LTS)新特性
 * 
 * <pre>
 * 1.基本类型模式匹配(JEP 507,预览)
 * ->switch 或 instanceof 中只能对对象类型做判断->允许直接匹配基本类型,不再需要手动拆箱或强制转换
 * 2.模块导入声明(JEP 511): 支持一次导入整个模块,如import module java.base;
 * 3.更轻量的 Main 方法(JEP 512)
 * 4.更自然的构造函数(JEP 513):支持在调用 super() 前做输入校验
 * 5.Record 类的增强(JEP 395/最新增强):可以在构造函数里加校验逻辑;可以定义方法,让数据类带业务行为
 * 6.结构化并发(JEP 505,预览)
 * 7.作用域值(Scoped Values)(JEP 506):替代 ThreadLocal 的新方案,跨线程共享上下文更安全
 * 8.稳定值(Stable Values)(JEP 502,预览): 线程安全的懒加载配置更简单,不需要双重检查锁定
 * 9.向量 API(JEP 508,孵化): 高性能向量运算,适合 AI、数据分析场景
 * 10.紧凑对象头(JEP 519): 对象头缩小到 64 位,减少内存占用,提高缓存效率
 * 11.分代 Shenandoah GC(JEP 521): Shenandoah GC 支持分代,降低延迟,提高吞吐,适合高并发场景
 * 12.提前编译(AOT)优化(JEP 514 & 515): 启动更快,预热更快,云原生应用、微服务受益明显
 * 13.JFR 增强(JEP 509, 518, 520): Java Flight Recorder 支持: CPU 时间分析;方法执行跟踪;更低开销采样.生产环境可观测性更强
 * 14.安全性更新(JEP 470, 510): 内置 PEM 编解码,标准化 KDF（PBKDF2、Argon2 等）
 * 15.移除 32 位 x86(JEP 503)
 * </pre>
 *
 * @author 飞花梦影
 * @date 2026-03-14 17:13:13
 */
public class JDK25 {

	public static void main(String[] args) {

	}

	// feature3
	void main() {
		System.out.println("Hello, World!");
	}

	private static void feature1() {
		Object obj = 42;

		switch (obj) {
		case int i -> System.out.println("整数：" + i);
		case double d -> System.out.println("小数：" + d);
		default -> System.out.println("其他类型");
		}
	}

	private static void feature6() {
		// 异常自动取消任务
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			var user = scope.fork(() -> fetchUser());
			var orders = scope.fork(() -> fetchOrders());

			scope.join().throwIfFailed();
			System.out.println(user.resultNow() + " " + orders.resultNow());
		}
	}

	ScopedValue<String> USER_ID = ScopedValue.newInstance();

	void feature7(String userId) {
		// 将USER_ID传递到doFeature7中
		ScopedValue.where(USER_ID, userId).run(() -> {
			doFeature7();
		});
	}

	void doFeature7() {
		System.out.println("当前用户：" + USER_ID.get());
	}
	
	// feature8
	StableValue<User> user = StableValue.of();

	User getUser() {
	    return user.orElseSet(this::loadConfig);
	}
	
	// feature9
	var species = FloatVector.SPECIES_256;
	var a = FloatVector.fromArray(species, arr1, 0);
	var b = FloatVector.fromArray(species, arr2, 0);
	var c = a.add(b);
	c.intoArray(result, 0);
}