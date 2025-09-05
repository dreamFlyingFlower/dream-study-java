package dream.study.common.json;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Jackson学习
 * 
 * <pre>
 * {@link ObjectMapper}: Jackson核心类,用于序列化和反序列化操作:
 * {@link ObjectMapper#writeValueAsString(Object)}: 将Java对象序列化为JSON字符串
 * {@link ObjectMapper#readValue(String, Class)}: 将JSON字符串反序列化为Java对象
 * 
 * {@link JsonParser}: 从JSON数据源(如文件、输入流或字符串)解析JSON数据
 * {@link JsonParser#nextToken()}: 获取下一个JSON令牌(如START_OBJECT、FIELD_NAME等)
 * {@link JsonParser#getValueAsString()}: 将当前令牌作为字符串返回
 * 
 * {@link JsonGenerator}: 将JSON数据写入数据源(如文件、输出流或字符串缓冲区)
 * {@link JsonGenerator#writeStartObject()}: 写入开始对象标记{
 * {@link JsonGenerator#writeFieldName(String)}: 写入字段名称
 * {@link JsonGenerator#writeString(String)}: 写入字符串值
 * {@link JsonGenerator#writeEndObject()}: 写入结束对象标记}
 * 
 * {@link JsonNode}: 表示JSON树模型中的节点,可以是对象、数组、字符串、数字等
 * {@link JsonNode#get(String)}: 获取指定字段的子节点
 * {@link JsonNode#path(String)}: 获取指定字段的子节点,如果不存在则返回一个“missing”节点
 * {@link JsonNode#isObject()}: 检查当前节点是否是一个对象
 * {@link JsonNode#isArray()}:检查当前节点是否是一个数组
 *	</pre>
 * 
 * 常用注解
 * 
 * <pre>
 * {@link JsonProperty}: 指定字段在JSON数据中的名称
 * ->{@link JsonProperty#value()}: 指定在JSON中的名称
 * ->{@link JsonProperty#access()}: 指定是否可读可写
 * {@link JsonIgnore}: 指定字段在序列化和反序列化过程中被忽略
 * {@link JsonFormat}: 指定Java属性的日期合时间格式
 * {@link JsonInclude}: 指定序列化 Java 对象时包含哪些属性,常用的参数有
 * ->{@link JsonInclude#value()}: 指定包含哪些属性:
 * -->JsonInclude.Include.ALWAYS: 始终包含
 * -->JsonInclude.Include.NON_NULL: 值不为 null 时包含
 * -->JsonInclude.Include.NON_DEFAULT: 值不为默认值时包含
 * -->JsonInclude.Include.NON_EMPTY: 值不为空时包含
 * -->JsonInclude.Include.CUSTOM: 自定义条件
 * ->{@link JsonInclude#content()}: 指定自定义条件的实现类
 * {@link JsonCreator}: 指定用于反序列化的构造函数或工厂方法
 * {@link JsonSetter}: 指定反序列化时使用的方法
 * {@link JsonGetter}: 指定序列化时使用的方法
 * {@link JsonAnySetter}: 指定反序列化时使用的方法,用于处理 JSON 中未知的属性
 * {@link JsonAnyGetter}: 指定序列化时使用的方法,用于处理 Java 对象中未知的属性
 * {@link JsonSerialize}: 指定用于序列化特定字段或类的自定义序列化器,需实现{@link JsonSerializer}
 * {@link JsonDeserialize}: 指定用于反序列化特定字段或类的自定义反序列化器,需实现{@link JsonDeserializer}
 * {@link JsonTypeInfo}: 指定 Java 对象在序列化和反序列化时的类型信息
 * ->{@link JsonTypeInfo#use()}: 指定类型信息的使用方式.
 * -->JsonTypeInfo.Id.CLASS: 使用 Java 类的全限定名
 * -->JsonTypeInfo.Id.NAME: 使用名称
 * -->JsonTypeInfo.Id.NONE: 不使用类型信息
 * ->{@link JsonTypeInfo#include()}: 指定类型信息的包含方式
 * -->JsonTypeInfo.As.PROPERTY: 作为 JSON 属性
 * -->JsonTypeInfo.As.EXTERNAL_PROPERTY: 作为外部属性
 * ->{@link JsonTypeInfo#property()}: 指定包含类型信息的属性名,当 include 的值为 JsonTypeInfo.As.PROPERTY 时使用
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-09-05 09:49:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Jackson {

}