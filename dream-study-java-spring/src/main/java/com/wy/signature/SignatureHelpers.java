package com.wy.signature;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;

import com.wy.ConstLang;
import com.wy.lang.StrHelper;

/**
 * 签名工具类
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:49:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SignatureHelpers {

	/**
	 * 提取所有的请求参数,按照固定规则拼接成一个字符串
	 *
	 * @param body post请求的请求体
	 * @param paramMap 路径参数(QueryString)。形如：name=zhangsan&age=18&label=A&label=B
	 * @param uriTemplateVarNap 路径变量(PathVariable)。形如：/{name}/{age}
	 * @return 所有的请求参数按照固定规则拼接成的一个字符串
	 */
	public static String extractRequestParams(@Nullable String body, @Nullable Map<String, String[]> paramMap,
			@Nullable Map<String, String> uriTemplateVarNap) {
		// 路径参数
		// name=zhangsan&age=18&label=A&label=B
		// => ["name=zhangsan", "age=18", "label=A,B"]
		// => name=zhangsan&age=18&label=A,B
		String paramStr = null;
		if (null != paramMap) {
			paramStr = paramMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> {
				// 拷贝一份按字典序升序排序
				String[] sortedValue = Arrays.stream(entry.getValue()).sorted().toArray(String[]::new);
				return entry.getKey() + "=" + joinStr(",", sortedValue);
			}).collect(Collectors.joining("&"));
		}

		// 路径变量
		// /{name}/{age} => /zhangsan/18 => zhangsan,18
		String uriVarStr = null;
		if (null != uriTemplateVarNap) {
			uriVarStr = joinStr(",", uriTemplateVarNap.values().stream().sorted().toArray(String[]::new));
		}

		// { userID: "xxx" }#name=zhangsan&age=18&label=A,B#zhangsan,18
		return joinStr("#", body, paramStr, uriVarStr);
	}

	/**
	 * 使用指定分隔符,拼接字符串
	 *
	 * @param delimiter 分隔符
	 * @param strs 需要拼接的多个字符串,可以为null
	 * @return 拼接后的新字符串
	 */
	public static String joinStr(String delimiter, @Nullable String... strs) {
		if (StrHelper.isAnyBlank(strs)) {
			return ConstLang.STR_EMPTY;
		}
		StringBuilder sbd = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			if (StrHelper.isBlank(strs[i])) {
				continue;
			}
			sbd.append(strs[i].trim());
			if (Objects.nonNull(sbd) && i < strs.length - 1 && StrHelper.isNotBlank(strs[i + 1])) {
				sbd.append(delimiter);
			}
		}
		return sbd.toString();
	}
}