package com.wy.spel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.cache.annotation.CachePut;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import dream.flying.flower.framework.web.helper.MethodHelpers;
import dream.flying.flower.framework.web.helper.SpringContextHelpers;
import lombok.Data;

/**
 * SpringEL表达式,文档:https://docs.spring.io/spring-framework/reference/core/expressions.html
 * 
 * <pre>
 * #this:总是指向当前表达式中计算的对象,即表达式当前所指代的真实值
 * #root:总是指向跟对象
 * #root.methodName:要调用的方法名称
 * #root.method.name:正在调用的方法
 * #root.target:正在调用的目标对象
 * #root.targetClass:正在调用的目标class
 * #root.args[0]:调用目标参数
 * #root.caches[0].name:正在调用的方法使用的缓存列表
 * #参数名:直接引用方法参数名,也可以使用#p0,#p1...
 * #{@serviceName}/#{serviceName}:spring中的组件名,调用spring组件.@表示调用spring上下文中的bean
 * </pre>
 * 
 * SpringEL特殊表达式
 * 
 * <pre>
 * <,>,<=,>=,==,!=,lt,gt,le,ge,eq,ne:各种普通运算
 * +,-,*,/,%,^:普通运算
 * &&,||,!,and,or,not,between,instanceof:逻辑运算
 * matches:正则
 * 
 * expr?.:判断expr表达式不为null才执行后面的表达式.eg:#user.username?.#user.username:如果username存在才解析,否则直接输出null
 * obj.?[expr]:通过从obj的元素中进行选择,将obj转换为另一个集合,即过滤集合.eg:lists是一个集合,#lists.?[#this == 3 || #this == 4]:选取lists中大于3或4的值再组成一个新的集合
 * obj.![expr]:和obj.?类型,只不过用来过滤对象中的属性,而不是基本类型.eg:#obj.![name]:过滤掉对象集合中name为null的对象
 * ^[expr],$[expr]:同上,以expr开头或结尾
 * T(xxx):类型表达式,表示Class的实例,静态方法也可以.T(xxx).method(xxx),在java.lang包下的类可以不指定全限定名,直接指定类名
 * 
 * {@link TemplateParserContext}:模板表达式,以#{}包裹需要解析的表达式的值.eg:#{T(java.lang.Math).random()}->相当于立即输出Math.random()
 * </pre>
 * 
 *
 * @author 飞花梦影
 * @date 2023-06-13 22:31:08
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Data
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

	public static void main(String[] args) {
		// Spel表达式解析器
		ExpressionParser expressionParser = new SpelExpressionParser();
		// 字符串加单引号,直接返回原值
		System.out.println(expressionParser.parseExpression("'直接返回原值'").getValue());
		// 如果只有字符串,会进行解析,找不到会返回null
		// System.out.println(expressionParser.parseExpression("fdsfdsgfdg").getValue());
		// 数字,boolean都可直接返回
		System.out.println(expressionParser.parseExpression("5555").getValue());
		System.out.println(expressionParser.parseExpression("true").getValue());
		System.out.println(expressionParser.parseExpression("true").getValue(Boolean.class));
		// 直接调用方法
		System.out.println(expressionParser.parseExpression("'Hello,SpEL'.substring(0,5)").getValue(String.class));
		// Class
		System.out.println(expressionParser.parseExpression("T(java.util.Date)").getValue(Class.class));
		// 实例化
		MySpEl myspel = expressionParser.parseExpression("new com.wy.spel.MySpEl()").getValue(MySpEl.class);
		System.out.println(myspel);

		System.out.println("simple-----------------------------");
		testArithmetic(expressionParser);
		testObject(expressionParser);
		testArray(expressionParser);
		testMap(expressionParser);
		testVariable(expressionParser);
	}

	public static void testArithmetic(ExpressionParser parser) {
		// 简单算术计算
		boolean result = parser.parseExpression("2 < 5").getValue(Boolean.class);
		System.out.println("2 < 5 : " + result);

		result = parser.parseExpression("2 == 5").getValue(Boolean.class);
		System.out.println("2 == 5 : " + result);

		result = parser.parseExpression("1 instanceof T(int)").getValue(Boolean.class);
		System.out.println("1 instanceof T(int) : " + result);
		result = parser.parseExpression("1 instanceof T(Integer)").getValue(Boolean.class);
		System.out.println("1 instanceof T(Integer) : " + result);

		result = parser.parseExpression("true or false").getValue(Boolean.class);
		System.out.println("true or false : " + result);
		result = parser.parseExpression("true and false").getValue(Boolean.class);
		System.out.println("true and false : " + result);
		result = parser.parseExpression("!true").getValue(Boolean.class);
		System.out.println("!true : " + result);

		int two = parser.parseExpression("1 + 1").getValue(Integer.class); // 2
		System.out.println("1 + 1 : " + two);
		String testString = parser.parseExpression("'test' + ' ' + 'string'").getValue(String.class); // 'test string'
		System.out.println("'test' + ' ' + 'string' : " + testString);
		int four = parser.parseExpression("1 - -3").getValue(Integer.class); // 4
		System.out.println("1 - -3 : " + four);
		double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class); // -9000
		System.out.println("1000.00 - 1e4 : " + d);
		int six = parser.parseExpression("-2 * -3").getValue(Integer.class); // 6
		System.out.println("-2 * -3 : " + six);
		double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class); // 24.0
		System.out.println("2.0 * 3e0 * 4 : " + twentyFour);
		int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class); // -2
		System.out.println("6 / -3 : " + minusTwo);
		double oneD = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class); // 1.0
		System.out.println("8.0 / 4e0 / 2 : " + oneD);
		int three = parser.parseExpression("7 % 4").getValue(Integer.class); // 3
		System.out.println("7 % 4 : " + three);
		int one = parser.parseExpression("8 / 5 % 2").getValue(Integer.class); // 1
		System.out.println("8 / 5 % 2 : " + one);
		int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class); // -21
		System.out.println("1+2-3*8 : " + minusTwentyOne);
	}

	public static void testObject(ExpressionParser expressionParser) {
		// 初始化Spel表达式上下文,并直接和某个对象绑定
		MySpEl mySpEl = new MySpEl();
		mySpEl.setCode("fdsfgfgfdgfd");
		StandardEvaluationContext context2 = new StandardEvaluationContext(mySpEl);
		// 构建一个获取code的解析表达式
		Expression expression2 = expressionParser.parseExpression("code");
		// 从上下文context2中获取code属性,并指定类型
		String code = expression2.getValue(context2, String.class);
		System.out.println(code);
		// 获取code属性值的length,使用点表示级联嵌套属性
		int length = expressionParser.parseExpression("code.length").getValue(context2, int.class);
		System.out.println(length);
	}

	@SuppressWarnings("unchecked")
	public static void testArray(ExpressionParser expressionParser) {
		MySpEl arr1 = new MySpEl();
		arr1.setCode("fdsfgfgfdgfd");
		MySpEl arr2 = new MySpEl();
		arr2.setCode("heiheiehlallalla");
		List<MySpEl> arr = Arrays.asList(arr1, arr2);

		// 初始化Spel表达式上下文,并直接和数组绑定
		StandardEvaluationContext context = new StandardEvaluationContext(arr);
		// 根据索引获取值
		MySpEl spel = expressionParser.parseExpression("[0]").getValue(context, MySpEl.class);
		System.out.println(spel);

		// 索引 + 对象属性值
		System.out.println(expressionParser.parseExpression("[0].code").getValue(context, String.class));

		// 如果内部还有数组
		// System.out.println(expressionParser.parseExpression("[0].booleans[0]").getValue(context,
		// boolean.class));

		// 直接构建一个新的数组
		int[] numbers = expressionParser.parseExpression("new int[]{1,2,3}").getValue(context, int[].class);
		for (int number : numbers) {
			System.out.print(number + " ");
		}
		System.out.println();

		// 构建一个复杂数组
		MySpEl[] complexTypeArrays =
				expressionParser.parseExpression("new com.wy.spel.MySpEl[1]").getValue(context, MySpEl[].class);
		complexTypeArrays[0] = new MySpEl();
		Arrays.stream(complexTypeArrays).forEach(System.out::println);

		// 构建一个集合
		List<MySpEl> mySpEls =
				(List<MySpEl>) expressionParser.parseExpression("{T(com.wy.spel.MySpEl)}").getValue(context);
		System.out.println(mySpEls);
	}

	@SuppressWarnings("unchecked")
	public static void testMap(ExpressionParser expressionParser) {
		System.out.println("testMap----------------------------");
		MySpEl arr1 = new MySpEl();
		arr1.setCode("testmap");
		Map<String, MySpEl> map = new HashMap<>();
		map.put("map", arr1);
		EvaluationContext context = new StandardEvaluationContext(map);
		// Map需要使用[]
		System.out.println(expressionParser.parseExpression("['map']").getValue(context, MySpEl.class));
		// 内部属性也用点
		String code = expressionParser.parseExpression("['map'].code").getValue(context, String.class);
		System.out.println(code);

		Map<String, MySpEl> inventorMap =
				expressionParser.parseExpression("{'testmap1':T(com.wy.spel.MySpEl)}").getValue(context, Map.class);
		System.out.println(inventorMap);
	}

	@SuppressWarnings("unchecked")
	public static void testVariable(ExpressionParser expressionParser) {
		System.out.println("testVariable----------------------------");
		EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
		// 在上下文环境中设置自定义的值,key的命名方式和普通命名一样,但是可以写$
		context.setVariable("newCode", "newCode");
		MySpEl myspel = new MySpEl();
		// 将环境变量中的值赋值给对象的code
		expressionParser.parseExpression("code = #newCode").getValue(context, myspel);
		System.out.println(myspel);

		List<Integer> integers = new ArrayList<>();
		integers.add(1);
		integers.add(2);
		integers.add(3);
		integers.add(4);
		integers.add(5);
		integers.add(6);
		context.setVariable("integers", integers);
		// 找到数组中大于3的值
		String thisExpression = "#integers.?[#this > 3]";
		List<Integer> gt3List = expressionParser.parseExpression(thisExpression).getValue(context, List.class);
		gt3List.forEach(integer -> System.out.print(integer + " "));
		System.out.println();

		// #root
		context = new StandardEvaluationContext(myspel);
		MySpEl value = expressionParser.parseExpression("#root").getValue(context, MySpEl.class);
		System.out.println(value);
	}

	/**
	 * EL表达式解析,参照#CacheOperationExpressionEvaluator
	 * 或{@link CachedExpressionEvaluator}
	 *
	 * @param method 方法
	 * @param args 参数
	 * @return
	 */
	public static Object parse(Method method, Object[] args) {
		// 初始化Spel表达式上下文
		StandardEvaluationContext context = new StandardEvaluationContext();
		// 设置表达式支持spring bean
		ApplicationContext applicationContext = SpringContextHelpers.getApplicationContext();
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));

		String[] parameterNames = MethodHelpers.getParameterNames(method);
		for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
			// 为Spel变量设置方法参数名和值
			context.setVariable(parameterNames[i], args[i]);
		}

		// Spel表达式解析器
		ExpressionParser parser = new SpelExpressionParser();
		// 解析Spel表达式,从spring环境中找到userService
		Expression expression = parser.parseExpression("@userService");
		// 从上下文中根据表达式获取值
		Object value = expression.getValue(context);

		// 后去值,并指定值类型
		expression.getValue(context, Boolean.class);

		return value;
	}
}