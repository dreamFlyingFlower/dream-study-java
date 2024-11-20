package com.wy.privacy;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * 序列化,写值,输出
 *
 * @author 飞花梦影
 * @date 2023-12-07 14:48:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class PrivacySerializer extends JsonSerializer<String> implements ContextualSerializer {

	private PrivacyType type;

	private Integer prefixNoMaskLen;

	private Integer suffixNoMaskLen;

	private String maskStr;

	public PrivacySerializer(PrivacyType type, Integer prefixNoMaskLen, Integer suffixNoMaskLen, String maskStr) {
		this.type = type;
		this.prefixNoMaskLen = prefixNoMaskLen;
		this.suffixNoMaskLen = suffixNoMaskLen;
		this.maskStr = maskStr;
	}

	public PrivacySerializer() {
	}

	@Override
	public void serialize(String origin, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (StringUtils.isNotBlank(origin) && null != type) {
			switch (type) {
			case CHINESE_NAME:
				jsonGenerator.writeString(DesensitizedHelper.chineseName(origin));
				break;
			case ID_CARD:
				jsonGenerator.writeString(DesensitizedHelper.idCardNum(origin));
				break;
			case FIXED_PHONE:
				jsonGenerator.writeString(DesensitizedHelper.fixedPhone(origin));
				break;
			case MOBILE_PHONE:
				jsonGenerator.writeString(DesensitizedHelper.mobilePhone(origin));
				break;
			case ADDRESS:
				jsonGenerator.writeString(DesensitizedHelper.address(origin));
				break;
			case EMAIL:
				jsonGenerator.writeString(DesensitizedHelper.email(origin));
				break;
			case BANK_CARD:
				jsonGenerator.writeString(DesensitizedHelper.bankCard(origin));
				break;
			case PASSWORD:
				jsonGenerator.writeString(DesensitizedHelper.password(origin));
				break;
			case KEY:
				jsonGenerator.writeString(DesensitizedHelper.key(origin));
				break;
			case CUSTOMER:
				jsonGenerator
						.writeString(DesensitizedHelper.desValue(origin, prefixNoMaskLen, suffixNoMaskLen, maskStr));
				break;
			default:
				throw new IllegalArgumentException("Unknow sensitive type enum " + type);
			}
		} else {
			jsonGenerator.writeString("");
		}

	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		if (beanProperty != null) {
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
				PrivacyEncrypt encrypt = beanProperty.getAnnotation(PrivacyEncrypt.class);
				if (encrypt == null) {
					encrypt = beanProperty.getContextAnnotation(PrivacyEncrypt.class);
				}
				if (encrypt != null) {
					return new PrivacySerializer(encrypt.type(), encrypt.prefixNoMaskLen(), encrypt.suffixNoMaskLen(),
							encrypt.maskStr());
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}
}