package dream.study.spring.retrofit;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 注解式拦截器.自定义基于url路径的匹配拦截
 * 
 * 继承BasePathMatchInterceptor编写拦截处理器;接口上使用@Intercept进行标注.如需配置多个拦截器,在接口上标注多个@Intercept注解即可
 *
 * @author 飞花梦影
 * @date 2024-06-06 10:58:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
public class TimeStampInterceptor extends BasePathMatchInterceptor {

	/**
	 * 给指定请求的url后面拼接timestamp时间戳
	 * 
	 * @param chain
	 * @return
	 * @throws IOException
	 */
	@Override
	public Response doIntercept(Chain chain) throws IOException {
		Request request = chain.request();
		HttpUrl url = request.url();
		long timestamp = System.currentTimeMillis();
		HttpUrl newUrl = url.newBuilder().addQueryParameter("timestamp", String.valueOf(timestamp)).build();
		Request newRequest = request.newBuilder().url(newUrl).build();
		return chain.proceed(newRequest);
	}
}