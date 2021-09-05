package com.wy.netty.file.server.support;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.netty.file.Constants;
import com.wy.netty.file.enums.EnumFileAction;
import com.wy.netty.file.server.FileServerContainer;
import com.wy.netty.file.server.handler.factory.FileServerHandlerFactory;
import com.wy.netty.file.server.parse.RequestParam;
import com.wy.netty.file.server.parse.RequestParamParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;

/**
 * 文件处理核心句柄类
 * 
 * @author 飞花梦影
 * @date 2021-09-03 12:06:56
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FileServerHandler extends SimpleChannelInboundHandler<DefaultFullHttpRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileServerHandler.class);

	// http请求
	private HttpRequest request;

	// 是否需要断点续传作业
	private boolean readingChunks;

	// 接收到的文件内容
	private final StringBuffer responseContent = new StringBuffer();

	// 解析收到的文件,16384L
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

	// post请求的解码类,它负责把字节解码成Http请求
	private HttpPostRequestDecoder decoder;

	// 请求参数
	private RequestParam requestParams = new RequestParam();

	@Override
	public void channelRead0(ChannelHandlerContext ctx, DefaultFullHttpRequest request) throws Exception {
		if (!this.readingChunks) {
			if (this.decoder != null) {
				this.decoder.cleanFiles();
				this.decoder = null;
			}

			this.request = request;
			URI uri = new URI(request.uri());

			// 初始页面
			if (!uri.getPath().startsWith("/form")) {
				writeForm(ctx, request);
				return;
			}
			try {
				// 初始化decoder
				this.decoder = new HttpPostRequestDecoder(factory, request);
			} catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
				e1.printStackTrace();
				e1.printStackTrace();
				this.responseContent.append(e1.getMessage());
				writeResponse(ctx.channel());
				ctx.channel().close();
				return;
			} catch (Exception e1) {
				e1.printStackTrace();
				this.responseContent.append(e1.getMessage());
				this.responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
				writeResponse(ctx.channel());
				return;
			}
			// 说明还没有请求完成,继续
			if (!request.release()) {
				this.readingChunks = true;
				LOGGER.info("文件分块操作....");
			} else {
				LOGGER.info("文件大小小于1KB,文件接收完成,直接进行相应的文件处理操作....");
				// 请求完成,则接收请求参数,进行初始化请求参数
				RequestParamParser.parseParams(this.decoder, this.requestParams);
				// 根据请求参数进行相应的文件操作
				LOGGER.info("文件处理开始....requestParams参数解析:{}", requestParams);
				String result = FileServerHandlerFactory.process(this.requestParams);
				LOGGER.info("文件处理结束....FileServerHandlerFactory处理结果:{}", result);
				this.responseContent.append(result);
				// 给客户端响应信息
				writeResponse(ctx.channel());
				// request.getFuture().addListener(ChannelFutureListener.CLOSE);
			}
		} else {
			HttpChunkedInput chunk = (HttpChunkedInput) request.copy();
			try {
				LOGGER.info("文件分块操作....文件大小:{} bytes", chunk.readChunk(ctx.alloc()).content().capacity());
				this.decoder.offer(chunk.readChunk(ctx.alloc()));
			} catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
				e1.printStackTrace();
				this.responseContent.append(e1.getMessage());
				writeResponse(ctx.channel());
				ctx.channel().close();
				return;
			}

			if (chunk.isEndOfInput()) {
				// 文件末尾
				this.readingChunks = false;
				LOGGER.info("到达文件内容的末尾,进行相应的文件处理操作....start");
				RequestParamParser.parseParams(this.decoder, this.requestParams);
				LOGGER.info("文件处理开始....requestParams参数解析:{}", requestParams);
				String result = FileServerHandlerFactory.process(this.requestParams);
				LOGGER.info("文件处理结束....FileServerHandlerFactory处理结果:{}", result);
				this.responseContent.append(result);
				// 给客户端响应信息
				writeResponse(ctx.channel());
				// request.getFuture().addListener(ChannelFutureListener.CLOSE);
				LOGGER.info("到达文件内容的末尾,进行相应的文件处理操作....end");
			}
		}
	}

	private void writeResponse(Channel channel) {
		ByteBuf buf = Unpooled.buffer();
		ByteBufUtil.writeUtf8(buf, responseContent);
		this.responseContent.setLength(0);
		boolean close = ("close".equalsIgnoreCase(this.request.headers().get("Connection")))
				|| ((this.request.protocolVersion().equals(HttpVersion.HTTP_1_0))
						&& (!"keep-alive".equalsIgnoreCase(this.request.headers().get("Connection"))));
		DefaultFullHttpResponse response =
				new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().add("Content-Type", "text/plain; charset=UTF-8");
		if (!close) {
			response.headers().add("Content-Length", String.valueOf(buf.readableBytes()));
		}
		ChannelFuture future = channel.write(response);
		if (close)
			future.addListener(ChannelFutureListener.CLOSE);
	}

	private void writeForm(ChannelHandlerContext ctx, HttpRequest e) {
		this.responseContent.setLength(0);
		this.responseContent.append("<html>");
		this.responseContent.append("<head>");
		this.responseContent.append("<title>Netty Test Form</title>\r\n");
		this.responseContent.append("</head>\r\n");
		this.responseContent.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
		this.responseContent.append("<table border=\"0\">");
		this.responseContent.append("<tr>");
		this.responseContent.append("<td>");
		this.responseContent.append("<h1>Netty Test Form</h1>");
		this.responseContent.append("</td>");
		this.responseContent.append("</tr>");
		this.responseContent.append("</table>\r\n");

		this.responseContent.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		this.responseContent
				.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
		this.responseContent.append("<input type=hidden name=getform value=\"POST\">");

		this.responseContent.append("<input type=hidden name=\"" + Constants.ACTION_KEY + "\" value=\""
				+ EnumFileAction.UPLOAD_FILE.getValue() + "\">");
		this.responseContent.append("<tr><td><input type=hidden name=\"" + Constants.USER_NAME_KEY + "\" value=\""
				+ Constants.DEFAULT_SERVER_ACCOUNT.getUserName() + "\" size=10></td></tr>");
		this.responseContent.append("<tr><td><input type=hidden name=\"" + Constants.PWD_KEY + "\" value=\""
				+ Constants.DEFAULT_SERVER_ACCOUNT.getPwd() + "\" size=10></td></tr>");

		this.responseContent.append("<table border=\"0\">");
		this.responseContent.append("<tr><td>产生缩略图: <br> <select type=file name=\"" + Constants.THUMB_MARK_KEY + "\">");
		this.responseContent.append("<option value=\"" + Constants.THUMB_MARK_YES + "\">是</option>");
		this.responseContent.append("<option value=\"" + Constants.THUMB_MARK_NO + "\">否</option>");
		this.responseContent.append("</select></td></tr>");
		this.responseContent
				.append("<tr><td>Fill with file: <br> <input type=file name=\"" + Constants.FILE_NAME_KEY + "\">");
		this.responseContent.append("</td></tr>");
		this.responseContent.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		this.responseContent.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");

		this.responseContent.append("</table></FORM>\r\n");
		this.responseContent.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		this.responseContent.append("</body>");
		this.responseContent.append("</html>");
		ByteBuf buf = Unpooled.buffer();
		ByteBufUtil.writeUtf8(buf, responseContent);
		this.responseContent.delete(0, this.responseContent.length());
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().add("Content-Type", "text/html; charset=UTF-8");
		response.headers().add("Content-Length", String.valueOf(buf.readableBytes()));
		ctx.channel().write(response);
	}

	static {
		DiskFileUpload.deleteOnExitTemporaryFile = false;
		DiskFileUpload.baseDirectory = FileServerContainer.getInstance().getFileBaseDirectory();
		DiskAttribute.deleteOnExitTemporaryFile = false;
		DiskAttribute.baseDirectory = FileServerContainer.getInstance().getFileBaseDirectory();
	}

	// 可能代替writeForm
	// private void writeMenu(MessageEvent e) {
	// this.responseContent.setLength(0);
	// this.responseContent.append("<html>");
	// this.responseContent.append("<head>");
	// this.responseContent.append("<title>Netty Test Form</title>\r\n");
	// this.responseContent.append("</head>\r\n");
	// this.responseContent
	// .append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
	// this.responseContent.append("<table border=\"0\">");
	// this.responseContent.append("<tr>");
	// this.responseContent.append("<td>");
	// this.responseContent.append("<h1>Netty Test Form</h1>");
	// this.responseContent.append("Choose one FORM");
	// this.responseContent.append("</td>");
	// this.responseContent.append("</tr>");
	// this.responseContent.append("</table>\r\n");
	// this.responseContent
	// .append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE
	// color=\"blue\"></CENTER>");
	// this.responseContent
	// .append("<FORM ACTION=\"/formget\" METHOD=\"POST\">");
	// this.responseContent
	// .append("<input type=hidden name=getform value=\"POST\">");
	// this.responseContent.append("<table border=\"0\">");
	// this.responseContent
	// .append("<tr><td>Fill with value: <br> <input type=text name=\"info\"
	// size=10></td></tr>");
	// this.responseContent
	// .append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\"
	// size=20>");
	// this.responseContent
	// .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40
	// rows=10></textarea>");
	// this.responseContent.append("</td></tr>");
	// this.responseContent
	// .append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\"
	// VALUE=\"Send\"></INPUT></td>");
	// this.responseContent
	// .append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\"
	// ></INPUT></td></tr>");
	// this.responseContent.append("</table></FORM>\r\n");
	// this.responseContent
	// .append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
	// this.responseContent
	// .append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE
	// color=\"blue\"></CENTER>");
	// this.responseContent
	// .append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
	// this.responseContent
	// .append("<input type=hidden name=getform value=\"POST\">");
	// this.responseContent.append("<table border=\"0\">");
	// this.responseContent
	// .append("<tr><td>Fill with value: <br> <input type=text name=\"info\"
	// size=10></td></tr>");
	// this.responseContent
	// .append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\"
	// size=20>");
	// this.responseContent
	// .append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40
	// rows=10></textarea>");
	// this.responseContent
	// .append("<tr><td>Fill with file (only file name will be transmitted): <br>
	// <input type=file name=\"myfile\">");
	// this.responseContent.append("</td></tr>");
	// this.responseContent
	// .append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\"
	// VALUE=\"Send\"></INPUT></td>");
	// this.responseContent
	// .append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\"
	// ></INPUT></td></tr>");
	// this.responseContent.append("</table></FORM>\r\n");
	// this.responseContent
	// .append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
	// this.responseContent
	// .append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE
	// color=\"blue\"></CENTER>");
	// this.responseContent
	// .append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\"
	// METHOD=\"POST\">");
	// this.responseContent
	// .append("<input type=hidden name=getform value=\"POST\">");
	//
	// this.responseContent.append("<input type=hidden name=\"" +
	// Constants.ACTION_KEY + "\" value=\""
	// + EnumFileAction.UPLOAD_FILE.getValue() + "\">");
	// this.responseContent.append("<table border=\"0\">");
	// this.responseContent
	// .append("<tr><td>账户: <br> <input type=text name=\"" + Constants.USER_NAME_KEY
	// + "\" value=\""
	// + Constants.DEFAULT_SERVER_ACCOUNT.getUserName()
	// + "\" size=10></td></tr>");
	// this.responseContent
	// .append("<tr><td>密码: <br> <input type=text name=\"" + Constants.PWD_KEY + "\"
	// value=\""
	// + Constants.DEFAULT_SERVER_ACCOUNT.getPwd()
	// + "\" size=10></td></tr>");
	// this.responseContent
	// .append("<tr><td>产生缩略图: <br> <select type=file name=\"" +
	// Constants.THUMB_MARK_KEY + "\">");
	// this.responseContent.append("<option value=\"" + Constants.THUMB_MARK_YES +
	// "\">是</option>");
	// this.responseContent.append("<option value=\"" + Constants.THUMB_MARK_NO +
	// "\">否</option>");
	// this.responseContent.append("</select></td></tr>");
	// this.responseContent
	// .append("<tr><td>Fill with file: <br> <input type=file name=\"" +
	// Constants.FILE_NAME_KEY + "\">");
	// this.responseContent.append("</td></tr>");
	// this.responseContent
	// .append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\"
	// VALUE=\"Send\"></INPUT></td>");
	// this.responseContent
	// .append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\"
	// ></INPUT></td></tr>");
	//
	// this.responseContent.append("</table></FORM>\r\n");
	// this.responseContent
	// .append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
	// this.responseContent.append("</body>");
	// this.responseContent.append("</html>");
	// ChannelBuffer buf = ChannelBuffers.copiedBuffer(
	// this.responseContent.toString(), CharsetUtil.UTF_8);
	// HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
	// HttpResponseStatus.OK);
	// response.setContent(buf);
	// response.setHeader("Content-Type", "text/html; charset=UTF-8");
	// response.setHeader("Content-Length",
	// String.valueOf(buf.readableBytes()));
	// e.getChannel().write(response);
	// }
}