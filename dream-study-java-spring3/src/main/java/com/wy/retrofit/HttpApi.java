package com.wy.retrofit;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;

import dream.flying.flower.result.Result;
import dream.study.common.model.User;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 类似HttpClient的远程调用工具.接口必须使用@RetrofitClient注解标记
 * 
 * <pre>
 * 请求方式: GET,HEAD,POST,PUT,DELETE,OPTIONS
 * 请求头: Header,HeaderMap,Headers
 * Query参数: Query,QueryMap,QueryName
 * path参数: Path
 * form-encoded参数: Field,FieldMap,FormUrlEncoded
 * 文件上传: Multipart,Part,PartMap
 * url参数: Url
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-06-06 09:36:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RetrofitClient(baseUrl = "${test.baseUrl}")
public interface HttpApi {

	@GET("person")
	Result<User> getPerson(@Query("id") Long id);
}