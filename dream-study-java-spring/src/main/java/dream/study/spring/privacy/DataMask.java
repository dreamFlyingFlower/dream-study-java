package dream.study.spring.privacy;

import java.io.Serializable;

/**
 * 设置类是否需要进行数据脱敏
 *
 * @author 飞花梦影
 * @date 2023-12-07 15:10:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DataMask implements Serializable {

	private static final long serialVersionUID = 6874458859299074718L;

	// 不进行序列化,设置key 来进行过滤的把控,默认不开启
	private transient Boolean isPrivacyKey = false;

	public Boolean getPrivacyKey() {
		return isPrivacyKey;
	}

	public void setPrivacyKey(Boolean privacyKey) {
		isPrivacyKey = privacyKey;
	}
}
