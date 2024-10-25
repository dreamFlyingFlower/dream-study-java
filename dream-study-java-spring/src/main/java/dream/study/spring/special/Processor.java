package dream.study.spring.special;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * 一些Processor在Spring中的作用
 * 
 * {@link RequestResponseBodyMethodProcessor}:解析{@link RequestBody}和{@link ResponseBody},进行参数校验等
 * 
 * <pre>
 * {@link RequestResponseBodyMethodProcessor#resolveArgument}:解析参数
 * {@link RequestResponseBodyMethodProcessor#readWithMessageConverters}:将请求数据封装到指定对象类型中
 * {@link RequestResponseBodyMethodProcessor#validateIfApplicable}:校验参数,其他参数校验见MethodValidationPostProcessor
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2024-07-09 10:17:50
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Processor {

}