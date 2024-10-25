package dream.study.common.config;

import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;

/**
 * 国际化初始化{@link MessageSourceAutoConfiguration}
 * 
 * 在spring的环境中使用,需要注入MessageSource,使用messgagesource.getmessage方法获取配置文件中的值
 * 
 * 在application配置文件中注入spring.messages.beanname:i18n/messages/messages
 * 值是国际化文件的地址,前2个可自定义,是文件夹路径,最后一个表示国际化文件的前缀,
 * messages可自定义,不带任何下划线后缀的是默认文件,即找不到本地化配置文件时使用,
 * 带后缀的第一个指语言的缩写,第二个是国家的缩写,可查看{@link java.util.Locale}
 * 
 * @author 飞花梦影
 * @date 2022-05-18 10:10:39
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class I18NConfig {

}