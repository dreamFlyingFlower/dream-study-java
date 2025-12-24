package com.wy.password;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.AllowedCharacterRule;
import org.passay.AllowedRegexRule;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterOccurrencesRule;
import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.DictionarySubstringRule;
import org.passay.DigestHistoryRule;
import org.passay.DigestSourceRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.HistoryRule;
import org.passay.IllegalCharacterRule;
import org.passay.IllegalRegexRule;
import org.passay.IllegalSequenceRule;
import org.passay.LengthComplexityRule;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.NumberRangeRule;
import org.passay.PasswordData;
import org.passay.PasswordData.HistoricalReference;
import org.passay.PasswordData.Reference;
import org.passay.PasswordData.SourceReference;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RepeatCharacterRegexRule;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SourceRule;
import org.passay.UsernameRule;
import org.passay.WhitespaceRule;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;

/**
 * Passy密码策略校验,官网 https://www.passay.org/reference/
 * 
 * <pre>
 * {@link PasswordData}:核心类.容纳待检查信息的容器,包括username,password和一些密码引用列表
 * {@link Reference}:密码引用.{@link HistoricalReference}和{@link SourceReference}类,可以增加它们至密码引用列表
 * {@link PasswordData.Origin}:表示密码是生成的或由用户定义的
 * {@link PasswordValidator}:检查密码,需要定义一系列规则
 * {@link RuleResult}:包括检测的结果信息
 * {@link RuleResultDetail}:校验结果详情,包括失败原因
 * {@link MessageResolver}:自定义密码错误的提示信息
 * </pre>
 * 
 * 正匹配规则:接受包含提供字符、正则表达式或符合某些限制的密码
 * 
 * <pre>
 * {@link AllowedCharacterRule}:定义密码必须包括的所有字符
 * {@link AllowedRegexRule}:定义密码必须匹配的正则表达式
 * {@link CharacterRule}:定义字符集和密码中应该包含的最小字符数
 * ->{@link EnglishCharacterData#LowerCase}:小写字符串序列
 * ->{@link EnglishCharacterData#UpperCase}:大写字符串序列
 * ->{@link EnglishCharacterData#Alphabetical}:大写小写混合序列
 * ->{@link EnglishCharacterData#Digit}:数字序列
 * ->{@link EnglishCharacterData#Special}:特殊字符序列
 * {@link LengthRule}:定义密码的最小长度和最大长度
 * {@link CharacterCharacteristicsRule}:检查密码是否满足定义的N个规则,需要提供一组CharacterRule,而且还要设置密码需要满足这些规则中的几个
 * {@link LengthComplexityRule}:允许为不同的密码长度定义不同的规则,它支持使用各种规则,不仅仅是CharacterRule
 * </pre>
 * 
 * 反向匹配规则:拒绝包含所提供的字符、正则表达式、字典等的密码
 * 
 * <pre>
 * {@link IllegalCharacterRule}:定义密码中不能包含的所有字符
 * {@link IllegalRegexRule}:定义一个不能匹配的正则表达式
 * {@link IllegalSequenceRule}:检查密码是否有非法字符序列,检查是否有连续的字符串
 * ->{@link IllegalSequenceRule#wrapSequence}:序列是否能首尾相连:true->检查首尾相连;false->不检查首尾相连,如xyzabc,true检查为非法,返回false
 * ->{@link EnglishSequenceData#Numerical}:非法数字序列
 * ->{@link EnglishSequenceData#Alphabetical}:非法字符串序列,不区分大小写
 * ->{@link EnglishSequenceData#USQwerty}:非法键盘序列,如qwerty,asdfg等,包括标点符号
 * {@link NumberRangeRule}:定义密码不能包含的数字范围
 * {@link WhitespaceRule}:检查密码是否包含空格.包含则为false
 * {@link DictionaryRule}:检查一个密码是否等于任何字典记录,用于检查弱密码或容易破解的密码,相当于禁用
 * {@link DictionarySubstringRule}:检查密码是否包含任何历史密码引用,和上述相似,只不过是包含
 * {@link HistoryRule}:检查密码是否包含任何摘录的历史密码引用,检查以前是否使用过需检查的密码
 * {@link SourceRule}:检查密码是否包含任何源密码引用,检查密码是否与SourceReference中提供的不同
 * {@link DigestHistoryRule}:检查密码是否包含任何摘录的历史密码引用.处理存储为摘要或Hash的密码,需要提供EncodingHashBean
 * {@link DigestSourceRule}:检查密码是否包含任何摘要源密码引用
 * {@link UsernameRule}:检查密码是否包含用户名
 * {@link RepeatCharacterRegexRule}:检查密码是否包含重复的ASCII字符
 * {@link CharacterOccurrencesRule}:检查密码是否包含太多相同的字符
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-02-09 12:50:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Passy {

	public static void main(String[] args) {
		simple();
		// 生成密码
		genereate();
		// 正匹配规则
		regex();
		// 反匹配规则
		unregex();
		dictionary();
		historySource();
		repeat();
		// 非法序列
		illegalSequence();
	}

	public static void simple() {
		PasswordData passwordData = new PasswordData("1234");
		passwordData.setPassword("1234");
		// 校验
		PasswordValidator passwordValidator = new PasswordValidator(new LengthRule(5));
		// 校验结果
		RuleResult ruleResult = passwordValidator.validate(passwordData);
		System.out.println("检查是否通过:" + ruleResult.isValid());
		// 校验详情
		ruleResult.getDetails().forEach(System.out::println);
		// 查看密码有效性的元数据
		ruleResult.getMetadata().getCounts().get(RuleResultMetadata.CountCategory.Length);
	}

	public static void genereate() {
		// 创建规则,也可以使用自定义的,实现CharacterData即可
		CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);
		PasswordGenerator passwordGenerator = new PasswordGenerator();
		// 生成10位数的密码
		String password = passwordGenerator.generatePassword(10,
				// 至少有1个数字
				digits,
				// 至少有1个大写
				new CharacterRule(EnglishCharacterData.UpperCase),
				// 至少有2个小写
				new CharacterRule(EnglishCharacterData.LowerCase, 2));
		System.out.println(password);
	}

	public static void regex() {
		PasswordValidator passwordValidator = new PasswordValidator(
				// 只允许某些字符
				new AllowedCharacterRule(new char[] { 'a', 'b', 'c' }),
				// 最少有5个小写字符
				new CharacterRule(EnglishCharacterData.LowerCase, 5),
				// 密码长度在8到10之间,闭区间
				new LengthRule(8, 10));

		RuleResult validate = passwordValidator.validate(new PasswordData("12abc"));

		System.out.println(validate.isValid());
		validate.getDetails().forEach(System.out::println);

		System.out.println("+++++++++++++++++");

		// 需要满足4个条件中的3个
		CharacterCharacteristicsRule characterCharacteristicsRule =
				new CharacterCharacteristicsRule(3, new CharacterRule(EnglishCharacterData.LowerCase, 5),
						new CharacterRule(EnglishCharacterData.UpperCase, 5),
						// 最少一个数字
						new CharacterRule(EnglishCharacterData.Digit),
						// 最少一个特殊字符
						new CharacterRule(EnglishCharacterData.Special));
		PasswordValidator passwordValidator2 = new PasswordValidator(characterCharacteristicsRule);
		RuleResult validate2 = passwordValidator2.validate(new PasswordData("12abc"));
		System.out.println(validate2.isValid());
		validate2.getDetails().forEach(System.out::println);

		System.out.println("+++++++++++++++++");

		LengthComplexityRule lengthComplexityRule = new LengthComplexityRule();
		lengthComplexityRule.addRules("[1,5]", new CharacterRule(EnglishCharacterData.LowerCase, 5));
		lengthComplexityRule.addRules("[6,10]", new AllowedCharacterRule(new char[] { 'a', 'b', 'c', 'd' }));
		PasswordValidator passwordValidator3 = new PasswordValidator(characterCharacteristicsRule);
		RuleResult validate3 = passwordValidator3.validate(new PasswordData("12abc"));
		System.out.println(validate3.isValid());
		validate3.getDetails().forEach(System.out::println);
	}

	public static void unregex() {
		PasswordValidator passwordValidator = new PasswordValidator(
				// 不能包含a
				new IllegalCharacterRule(new char[] { 'a' }),
				// 不能包含1到10的数
				new NumberRangeRule(1, 10),
				// 不能包含空白,包括制表符,换行符等
				new WhitespaceRule());

		RuleResult validate = passwordValidator.validate(new PasswordData("abcd22 "));

		System.out.println(validate.isValid());
		validate.getDetails().forEach(System.out::println);
	}

	public static void dictionary() {
		WordListDictionary wordListDictionary =
				new WordListDictionary(new ArrayWordList(new String[] { "111111", "222222" }));
		DictionaryRule dictionaryRule = new DictionaryRule(wordListDictionary);
		PasswordValidator passwordValidator = new PasswordValidator(dictionaryRule);
		RuleResult validate = passwordValidator.validate(new PasswordData("111111"));

		// false
		System.out.println(validate.isValid());
		validate.getDetails().forEach(System.out::println);
		System.out.println("+++++++++++++++++");

		DictionarySubstringRule dictionarySubstringRule = new DictionarySubstringRule(wordListDictionary);
		PasswordValidator passwordValidator1 = new PasswordValidator(dictionarySubstringRule);
		RuleResult validate1 = passwordValidator1.validate(new PasswordData("aaaa111111"));
		// false
		System.out.println(validate1.isValid());
		validate1.getDetails().forEach(System.out::println);
	}

	public static void historySource() {
		SourceRule sourceRule = new SourceRule();
		HistoryRule historyRule = new HistoryRule();

		PasswordData passwordData = new PasswordData("123");
		passwordData.setPasswordReferences(new PasswordData.SourceReference("source", "password"),
				new PasswordData.HistoricalReference("12345"));

		PasswordValidator passwordValidator = new PasswordValidator(sourceRule, historyRule);
		RuleResult validate = passwordValidator.validate(new PasswordData("111111"));

		System.out.println(validate.isValid());
		validate.getDetails().forEach(System.out::println);
	}

	public static void digest() {
		List<PasswordData.Reference> historicalReferences = Arrays.asList(new PasswordData.HistoricalReference("SHA256",
				"2e4551de804e27aacf20f9df5be3e8cd384ed64488b21ab079fb58e8c90068ab"));

		EncodingHashBean encodingHashBean =
				new EncodingHashBean(new CodecSpec("Base64"), new DigestSpec("SHA256"), 1, false);

		PasswordData passwordData = new PasswordData("111111");
		passwordData.setPasswordReferences(historicalReferences);

		PasswordValidator passwordValidator = new PasswordValidator(new DigestHistoryRule(encodingHashBean));
		RuleResult validate = passwordValidator.validate(passwordData);

		System.out.println(validate.isValid());
		validate.getDetails().forEach(System.out::println);
	}

	public static void repeat() {
		System.out.println("repeat++++++++");
		PasswordValidator passwordValidator = new PasswordValidator(
				// 不能有连续重复的3个字符
				new RepeatCharacterRegexRule(3));
		RuleResult validate = passwordValidator.validate(new PasswordData("aacabnbb"));

		System.out.println(validate.isValid());
		validate.getDetails().forEach(System.out::println);
		System.out.println("repeat++++++++");

		System.out.println("occurances++++++++");
		PasswordValidator passwordValidator1 = new PasswordValidator(
				// 不能有连续或不连续重复的3个字符
				new CharacterOccurrencesRule(3));
		RuleResult validate1 = passwordValidator1.validate(new PasswordData("aacaabnbb"));

		System.out.println(validate1.isValid());
		validate1.getDetails().forEach(System.out::println);

		System.out.println("occurances++++++++");
	}

	public static void messageResolver() throws IOException {
		// 加载自己的提示信息
		URL resource = Passy.class.getClassLoader().getResource("messages.properties");
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(resource.getPath())) {
			props.load(fis);
			MessageResolver resolver = new PropertiesMessageResolver(props);
			PasswordValidator validate = new PasswordValidator(resolver, new LengthRule(8, 16), new WhitespaceRule());

			RuleResult tooShort = validate.validate(new PasswordData("XXXX"));
			RuleResult tooLong = validate.validate(new PasswordData("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"));

			System.out.println(tooShort.isValid());
			tooShort.getDetails().forEach(System.out::println);

			System.out.println(tooLong.isValid());
			tooLong.getDetails().forEach(System.out::println);
		}
	}

	public static void illegalSequence() {
		// 非法的连续字符串,不能超过5个,false首尾相连的字符串不非法,true首尾相连的字符串非法,如xyzabcd非法
		new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false);
		// 非法的连续数字,不能超过5个
		new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false);
		// 非法的连续键盘字符,如qwert,asdfg,zxcvb
		new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false);

		PasswordValidator validate =
				new PasswordValidator(new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, true));
		// 非法
		RuleResult validate2 = validate.validate(new PasswordData("zabcd"));
		System.out.println(validate2.isValid());
		validate2.getDetails().forEach(System.out::println);

		PasswordValidator validate3 =
				new PasswordValidator(new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false));
		// 不非法
		RuleResult validate4 = validate3.validate(new PasswordData("zabcd"));
		System.out.println(validate4.isValid());
		validate4.getDetails().forEach(System.out::println);
	}
}