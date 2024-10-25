package dream.study.spring.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;

import dream.flying.flower.result.Result;
import dream.study.common.model.User;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 使用拦截器
 * 
 * @author 飞花梦影
 * @date 2024-06-06 09:36:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RetrofitClient(baseUrl = "${test.baseUrl}")
@Intercept(handler = TimeStampInterceptor.class, include = { "/api/**" }, exclude = "/api/test/savePerson")
public interface HttpApiInterceptor {

	@GET("person")
	Result<User> getPerson(@Query("id") Long id);
}