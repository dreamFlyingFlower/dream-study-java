package dream.study.spring.aop;

import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;

/**
 * {@link EnableLoadTimeWeaving}:用于切换不同场景下实现增强,该注解一般是在编译期执行,但是使用有点麻烦,不经常使用
 * 
 * {@link AspectJWeaving}:开启LTW/关闭LTW/自动(如果在类路径下能读取到META-INF/aop.xml,则开启LTW,否则关闭)
 *
 * @author 飞花梦影
 * @date 2022-09-20 00:16:42
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@EnableLoadTimeWeaving
public class MyEnableLoadTimeWeaving {

}