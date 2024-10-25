package dream.study.common.common;

/**
 * 公共配置类
 * 
 * @author wanyang 2018年7月24日
 *
 */
public interface Constant {

	/**
	 * 日志格式化信息
	 */
	String LOG_INFO = "!!!==== {}";

	String LOG_WARN = "@@@===={}";

	String LOG_ERROR = "###===={}";

	/**
	 * 测试mybatis中常量的使用
	 */
	Integer USER_STATE = 1;

	interface Redis {

		String TOKEN_IDEMPOTENT = "token_idempotent";

		String TOKEN_LOGIN = "token_login";

		String TOKEN_PREFIX = "token_";
	}
}