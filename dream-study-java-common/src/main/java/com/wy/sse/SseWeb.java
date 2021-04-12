package com.wy.sse;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过sse向web端推送信息
 * 
 * SSE:server sent events,服务器发送事件,是围绕只读comet交互推出的API.<br>
 * SSE API允许网页获得来自服务器的更新(html5),用于创建到服务器的单向连接,服务器通过该连接可以发送任意数据.
 * 服务器响应的MIME必须是text/evet-stream,而且浏览器必须能解析返回的数据格式
 * 
 * SSE支持短轮询,长轮询和HTTP 流,而且能在断开连接时自动确定何时重新连接
 * 
 * @author ParadiseWY
 * @date 2020-11-13 17:03:20
 * @git {@link https://github.com/mygodness100}
 */
@RestController
@RequestMapping("sseWeb")
public class SseWeb {

	// 前端代码
	// 客户端只需要直接使用**window.EventSource**对象，然后调用该对象的相应方法即可。
	// //判断是否支持SSE
	// if('EventSource' in window){
	// //初始化SSE
	// var url="http:localhost:8080/test/push";
	// var source=new EventSource(url);
	// //开启时调用
	// source.onopen=(event)=>{
	// console.log("开启SSE");
	// }
	// //监听message事件
	// source.onmessage=(event)=>{
	// var data=event.data;
	// $("body").append($("<p>").text(data));
	// }
	// //监听like事件
	// source.addEventListener('like',function(event){
	// var data=event.data;
	// $("body").append($("<p>").text(data));
	// },false);
	// //发生异常时调用
	// source.onerror=(event)=>{
	// console.log(event);
	// }

	@RequestMapping(value = "/push", produces = "text/event-stream;charset=UTF-8")
	public String push(HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "*");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String nowDate = sdf.format(date);
		return "data: 我是一个data 现在时间是" + nowDate + " \nevent:like\n retry:5000\n\n";
	}
}