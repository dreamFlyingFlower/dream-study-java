package dream.study.common.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * 替换springboot默认的jackjson为fastjson
 * 
 * @author paradiseWy
 */
@Configuration
@ConditionalOnClass(JacksonProperties.class)
@EnableConfigurationProperties(JacksonProperties.class)
public class JsonConfig {

	@Bean
	HttpMessageConverters fastJsonConfig(JacksonProperties properties) {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteMapNullValue,
				SerializerFeature.DisableCircularReferenceDetect);
		/** --- 若数据库字段全为大写,且使用了fastjson代替jackjson,可使用如下配置 --- **/
		// SerializeConfig parser = new SerializeConfig();
		// // 解决swagger2在将字段都变成大写之后无法访问的问题
		// NameFilter nameFilter = (object,name,value)->name;
		// parser.addFilter(UiConfiguration.class, nameFilter);
		// parser.addFilter(SwaggerResource.class, nameFilter);
		// // 若数据库字段全为大写开头,此处配置改为大写
		// // 必须放在filter之后,否则无法访问swagger2
		// parser.propertyNamingStrategy = PropertyNamingStrategy.PascalCase;
		// fastJsonConfig.setSerializeConfig(parser);
		/**
		 * ------------------------------------------------------------------------------
		 **/
		// 处理中文乱码问题
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON);
		fastMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
		fastMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);
		// 日期格式化
		fastJsonConfig.setDateFormat(properties.getDateFormat());
		fastConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters(fastConverter);
	}
}