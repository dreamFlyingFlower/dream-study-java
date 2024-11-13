package dream.study.spring.spel;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.cache.annotation.CachePut;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.core.MethodParameter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import dream.flying.flower.framework.web.helper.MethodHelpers;
import dream.flying.flower.framework.web.helper.SpringContextHelpers;
import dream.flying.flower.lang.StrHelper;

/**
 * SpringEL表达式,文档:https://docs.spring.io/spring-framework/reference/core/expressions.html
 * 
 * <pre>
 * #root.methodName:要调用的方法名称
 * #root.method.name:正在调用的方法
 * #root.target:正在调用的目标对象
 * #root.targetClass:正在调用的目标class
 * #root.args[0]:调用目标参数
 * #root.caches[0].name:正在调用的方法使用的缓存列表
 * #参数名:直接引用方法参数名,也可以使用#p0,#p1...
 * #{@serviceName}/#{serviceName}:spring中的组件名,调用spring组件.@可加可不加
 * </pre>
 * 
 * SpringEL运算符
 * 
 * <pre>
 * <,>,<=,>=,==,!=,lt,gt,le,ge,eq,ne
 * +,-,*,/,%,^
 * &&,||,!,and,or,not,between,instanceof
 * ?:
 * matches
 * ?.,?[...],![...],^[...],$[...]
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-06-13 22:31:08
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class MySpEl {

	/** 直接赋值基本类型或字符串 */
	@Value("#{'fdsfds'}")
	private String code;

	/** 调用类中的属性赋值,该属性必须是public或含有get方法,否则失败 */
	@Value("#{sysLogService.age}")
	private int age;

	@Value("#{@sysLogService.age}")
	private int age1;

	/** 方法调用,必须是public */
	@Value("#{sysLogService.test()}")
	private int age2;

	/** 方法调用并传参,必须是public */
	@Value("#{sysLogService.test(1)}")
	private int age3;

	/** 直接引用类的属性,需要在类的全限定名外面使用 T () 包围 */
	@Value("#{T(java.lang.Integer).MAX_VALUE}")
	private Integer priority;

	@CachePut(cacheName = "#name")
	public void test(String name) {

	}

	public static void main(Method method, Object[] args) {
		// Spel表达式解析器
		ExpressionParser expressionParser = new SpelExpressionParser();
		// 解析Spel表达式
		Expression expression = expressionParser.parseExpression("spel表达式");
		// 初始化Spel表达式上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 初始化Spel表达式上下文,并直接和某个对象绑定
		new StandardEvaluationContext(new MySpEl());
		// 设置表达式支持spring bean
		ApplicationContext applicationContext = SpringContextHelpers.getApplicationContext();
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		for (int i = 0; i < args.length; i++) {
			// 读取方法参数
			MethodParameter methodParam = MethodHelpers.getMethodParameter(method, i);
			// 为Spel变量设置方法参数名和值
			context.setVariable(methodParam.getParameterName(), args[i]);
		}
		// 获取解析后的结果
		expression.getValue(context, Boolean.class);
	}

	/**
	 * EL表达式解析,参照#CacheOperationExpressionEvaluator
	 * 或{@link CachedExpressionEvaluator}
	 *
	 * @param expression EL表达式
	 * @param method 方法
	 * @param args 参数
	 * @return
	 */
	public static Object parse(String expression, Method method, Object[] args) {
		if (StrHelper.isBlank(expression)) {
			return null;
		}
		String[] parameterNames = MethodHelpers.getParameterNames(method);
		// SPEL解析
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}
		return parser.parseExpression(expression).getValue(context);
	}
}