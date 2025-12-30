package dream.study.common.filter;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 防盗链配置
 *
 * @author 飞花梦影
 * @date 2025-12-30 13:27:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@ConfigurationProperties("dream.config.anti-leeching")
public class AntiLeechingProperties {

	/**
	 * 是否启用防盗链
	 */
	private boolean enabled;

	/**
	 * 允许的域名列表
	 */
	private List<String> allowedDomains;

	/**
	 * 需要保护的资源格式
	 */
	private List<String> protectedFormats;

	/**
	 * 是否允许直接访问,即无Referer
	 */
	private boolean allowDirectAccess;

	/**
	 * 拒绝访问时的动作
	 */
	private String denyAction;

	/**
	 * 默认图片路径
	 */
	private String defaultImage;
}