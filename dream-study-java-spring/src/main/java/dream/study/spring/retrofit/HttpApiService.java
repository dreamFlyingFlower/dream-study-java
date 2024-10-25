package dream.study.spring.retrofit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 将接口注入到其他组件中即可使用
 *
 * @author 飞花梦影
 * @date 2024-06-06 09:37:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class HttpApiService {

	@Autowired
	private HttpApi httpApi;

	public void test() {
		// 通过httpApi发起http请求
		httpApi.getPerson(1L);
	}
}