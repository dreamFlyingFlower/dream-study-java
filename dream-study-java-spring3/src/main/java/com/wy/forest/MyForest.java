package com.wy.forest;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.DataFile;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.annotation.Var;
import com.dtflys.forest.callback.OnError;
import com.dtflys.forest.callback.OnProgress;
import com.dtflys.forest.extensions.DownloadFile;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.ForestInterceptor;
import com.wy.model.User;

/**
 * 使用httpclient或okhttp等组件来进行远程调用,类似于feign,retrofit,resttemplate等
 * 
 * <pre>
 * {@link Request}: 主要配置注解,配置URL,请求头等
 * {@link BaseRequest}: 作用于当前接口,可被接口中方法的{@link Request}同属性覆盖.被标注的接口中所有方法都回以此为基础
 * {@link BaseRequest#baseURL()}: 当前接口的前缀URL,该接口中的方法调用时都会加上该url
 * {@link Header}: 请求头注解
 * {@link Body}: 将属性注入到请求体中.也就是contentType属性为application/x-www-form-urlencoded的格式,即contentType不做配置时的默认值
 * {@link ForestInterceptor}: 异常处理拦截器,实现该接口可处理所有forest产生的异常.该接口不仅可以处理异常,也可以处理其他流程,类似于AOP
 * </pre>
 *
 * @author 飞花梦影
 * @date 2025-12-23 15:09:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@BaseRequest(baseURL = "http://127.0.0.1:8080")
public interface MyForest {

	@Get(value = "/test1", headers = { "token:${token}", "Content-type:application/json" })
	void test1(@Var(value = "token") String token);

	@Request(value = "/test2", type = "POST")
	void test2(@Header("Accept") String accept);

	@Get(value = "/test3", headers = { "token:${token}", "Content-type:application/json" })
	void test3(@Body String accept);

	@Get(value = "/test4", headers = { "token:${token}", "Content-type:application/json" })
	void test4(@Body User user);

	/**
	 * 在请求接口中定义OnError回调函数类型参数进行异常处理
	 */
	@Request(url = "http://localhost:8080/hello/user", headers = { "Accept:text/plain" }, data = "username=${username}")
	String ex1(@Var("username") String username, OnError onError);

	/**
	 * 用`ForestResponse`类作为请求方法的返回值类型, 其泛型参数代表实际返回数据的类型,判断isError()可用来进行异常处理
	 */
	@Request(url = "http://localhost:8080/hello/user", headers = { "Accept:text/plain" }, data = "username=${username}")
	ForestResponse<String> send(@Var("username") String username);

	/**
	 * 文件上传.用@DataFile注解修饰要上传的参数对象,OnProgress参数为监听上传进度的回调函数
	 */
	@Post(url = "/upload")
	Map<String, String> upload(@DataFile("file") String filePath, OnProgress onProgress);

	/**
	 * byte数组 使用byte数组和Inputstream对象时一定要定义fileName属性
	 */
	@Post(url = "/upload")
	Map<String, String> upload(@DataFile(value = "file", fileName = "${1}") byte[] bytes, String filename);

	/**
	 * Inputstream 对象 使用byte数组和Inputstream对象时一定要定义fileName属性
	 */
	@Post(url = "/upload")
	Map<String, String> upload(@DataFile(value = "file", fileName = "${1}") InputStream in, String filename);

	/**
	 * Spring Web MVC 中的 MultipartFile 对象
	 */
	@Post(url = "/upload")
	Map<String, String> upload(@DataFile(value = "file") MultipartFile multipartFile, OnProgress onProgress);

	/**
	 * Spring 的 Resource 对象
	 */
	@Post(url = "/upload")
	Map<String, String> upload(@DataFile(value = "file") Resource resource);

	/**
	 * 文件批量上传.上传Map包装的文件列表 其中 ${_key} 代表Map中每一次迭代中的键值
	 */
	@Post(url = "/upload")
	ForestRequest<Map<String, String>>
			uploadByteArrayMap(@DataFile(value = "file", fileName = "${_key}") Map<String, byte[]> byteArrayMap);

	/**
	 * 文件批量上传.上传List包装的文件列表 其中 ${_index} 代表每次迭代List的循环计数,从零开始计
	 */
	@Post(url = "/upload")
	ForestRequest<Map<String, String>> uploadByteArrayList(
			@DataFile(value = "file", fileName = "test-img-${_index}.jpg") List<byte[]> byteArrayList);

	/**
	 * 文件批量上传.上传数组包装的文件列表 其中 ${_index} 代表每次迭代List的循环计数,从零开始计
	 */
	@Post(url = "/upload")
	ForestRequest<Map<String, String>> uploadByteArrayArray(
			@DataFile(value = "file", fileName = "test-img-${_index}.jpg") byte[][] byteArrayArray);

	/**
	 * 下载.在方法上加上@DownloadFile注解
	 * 
	 * @param dir 文件下载到哪个目录
	 * @param filename 文件下载成功后以什么名字保存,如果不填,默认从URL中取得文件名
	 * @param OnProgress 监听上传进度的回调函数
	 */
	@Get(url = "http://localhost:8080/images/xxx.jpg")
	@DownloadFile(dir = "${0}", filename = "${1}")
	File downloadFile(String dir, String filename, OnProgress onProgress);
}