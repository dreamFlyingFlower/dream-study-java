package com.wy.sse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SSE:server sent events,服务器发送事件,是围绕只读comet交互推出的API
 * 
 * SSE支持短轮询,长轮询和HTTP 流,而且能在断开连接时自动确定何时重新连接
 * SSE向Web推送实时信息,使用的是HTTP流,让链接认为一直都有信息传输,形成类似长链接的链接
 * SSE-API允许网页(Html5)获得来自服务器的更新,用于创建到服务器的单向连接,服务器通过该连接可以发送任意数据.
 * 
 * 服务器响应的MIME必须是text/evet-stream,即只支持文本格式,而且浏览器只能解析指定格式的数据:
 * 
 * <pre>
 * 格式:[data:.......\n.........\n...\n\n]或者是[retry:10\n]
 * data:固定字符串,表示数据
 * \n:表示一行的数据结束
 * \n\n:表示此次数据传输结束
 * </pre>
 * 
 * SSE和WebSocket的区别:
 * 
 * <pre>
 * SSE是单向链接,一旦建立链接后,只能是服务器向Web推送,且只支持文本消息,但是实现比WebSocket要简单
 * WebSocket是双向推送,服务器和Web都可以互相发送消息,支持文本和二进制消息,但是实现比较复杂
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2020-11-13 17:03:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@RestController
@RequestMapping("sse")
public class SseWeb {

	// 前端代码
	// 客户端只需要直接使用window.EventSource对象,然后调用该对象的相应方法即可
	// //判断是否支持SSE
	// if('EventSource' in window){
	// //初始化SSE
	// var url="http:localhost:8080/sse/pushRight";
	// var source=new EventSource(url);
	// // 当成功与服务器建立链接时调用
	// source.onopen=(event)=>{
	// console.log("开启SSE");
	// }
	// // 监听message事件,接收到服务器发送的消息时调用
	// source.onmessage=(event)=>{
	// var data=event.data;
	// $("body").append($("<p>").text(data));
	// }
	// //监听like事件
	// source.addEventListener('like',function(event){
	// var data=event.data;
	// $("body").append($("<p>").text(data));
	// },false);
	// // 发生异常时调用
	// source.onerror=(event)=>{
	// console.log(event);
	// }

	/**
	 * 该方式在Web端仍然会显示的多次调用http请求,要只调用单次请求,数据不能交给Spring处理
	 * 
	 * @param response 响应
	 * @return 结果
	 */
	@GetMapping(value = "push", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String push(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String nowDate = sdf.format(date);
		return "data: 我是一个data 现在时间是" + nowDate + " \nevent:like\n retry:5000\n\n";
	}

	/**
	 * 该方式直接将数据传输到response,写入流中,前端只会有有一次请求
	 * 
	 * @param response
	 */
	@GetMapping(value = "pushRight", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public void pushRight(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		try {
			String data = "data: 我是一个data 现在时间是" + LocalDate.now().toString() + " \nevent:like\n retry:5000\n\n";
			PrintWriter writer = response.getWriter();
			writer.write(data);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}