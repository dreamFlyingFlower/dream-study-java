package com.wy.enums;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-05-22 17:40:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Configuration
public class CommonEnumJacksonCustomizer {

	@Autowired
	private CommonEnumRegistry enumRegistry;

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer commonEnumBuilderCustomizer() {
		return builder -> {
			Map<Class<?>, List<CommonEnum>> classDict = enumRegistry.getClassDict();
			classDict.forEach((aClass, commonEnums) -> {
				builder.deserializerByType(aClass, new CommonEnumJsonDeserializer(commonEnums));
				builder.serializerByType(aClass, new CommonEnumJsonSerializer());
			});

		};
	}

	static class CommonEnumJsonSerializer extends JsonSerializer<Object> {

		@Override
		public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
				throws IOException {
			CommonEnum commonEnum = (CommonEnum) o;
			CommonEnumVO commonEnumVO = CommonEnumVO.from(commonEnum);
			jsonGenerator.writeObject(commonEnumVO);
		}
	}

	static class CommonEnumJsonDeserializer extends JsonDeserializer<Object> {

		private final List<CommonEnum> commonEnums;

		CommonEnumJsonDeserializer(List<CommonEnum> commonEnums) {
			this.commonEnums = commonEnums;
		}

		@Override
		public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException, JacksonException {
			String value = jsonParser.readValueAs(String.class);
			return commonEnums.stream().filter(commonEnum -> commonEnum.match(value)).findFirst().orElse(null);
		}
	}
}