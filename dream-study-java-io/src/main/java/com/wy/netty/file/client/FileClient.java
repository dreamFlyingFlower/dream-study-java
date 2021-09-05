package com.wy.netty.file.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.netty.file.Constants;
import com.wy.netty.file.Result;
import com.wy.netty.file.client.handler.CreateThumbPictureClientHandler;
import com.wy.netty.file.client.handler.DeleteFileClientHandler;
import com.wy.netty.file.client.handler.ReplaceFileClientHandler;
import com.wy.netty.file.client.handler.UploadFileClientHandler;
import com.wy.netty.file.client.handler.WrapFileClientHandler;
import com.wy.netty.file.client.support.FileClientPipelineFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

/**
 * 客户端对外暴露的API接口
 * 
 * <pre>
 * 1.上传文件
 * 2.替换文件
 * 3.删除文件
 * 4.生成缩略图
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-09-03 11:11:26
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class FileClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileClient.class);

	private static URI getUri(String host, int port) {
		String postUrl = "http://" + host + ":" + port + "/formpost";
		URI uri;
		try {
			uri = new URI(postUrl);
		} catch (URISyntaxException e) {
			LOGGER.error("Error: " + e.getMessage());
			return null;
		}
		return uri;
	}

	private static Bootstrap createClientBootstrap(FileClientPipelineFactory clientPipelineFactory) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.channel(NioSocketChannel.class);
		return bootstrap;
	}

	private static HttpDataFactory getHttpDataFactory() {
		HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
		DiskFileUpload.deleteOnExitTemporaryFile = false;
		DiskFileUpload.baseDirectory = System.getProperty("user.dir");
		DiskAttribute.deleteOnExitTemporaryFile = false;
		DiskAttribute.baseDirectory = System.getProperty("user.dir");
		return factory;
	}

	private static void uploadFile(Bootstrap bootstrap, String host, int port, File file, String fileName,
			String thumbMark, String userName, String pwd) {
		// 构建uri对象
		URI uri = getUri(host, port);
		// 连接netty服务端
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		// 异步获取Channel对象
		Channel channel = future.awaitUninterruptibly().channel();
		if (!future.isSuccess()) {
			future.cause().printStackTrace();
			future.channel().closeFuture();
			return;
		}
		// 初始化文件上传句柄对象
		WrapFileClientHandler handler =
				new UploadFileClientHandler(host, uri, file, fileName, thumbMark, userName, pwd);
		// 获取Request对象
		HttpRequest request = handler.getRequest();
		// 获取Http数据处理工厂
		HttpDataFactory factory = getHttpDataFactory();
		// 进行数据的包装处理,主要是进行上传文件所需要的参数的设置,此时调用的句柄是具体的UploadFileClientHandler对象
		HttpPostRequestEncoder bodyRequestEncoder = handler.wrapRequestData(factory);
		// 把request写到管道中,传输给服务端
		channel.write(request);
		// 做一些关闭资源的动作
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.closeFuture().awaitUninterruptibly();
		future.channel().closeFuture();
		factory.cleanAllHttpData();
	}

	/**
	 * 文件上传
	 * 
	 * @param file 需要上传的文件
	 * @param fileName 文件名称
	 * @param thumbMark 是否需要生成缩略图
	 * @return
	 */
	public static String uploadFile(File file, String fileName, boolean thumbMark) {
		try (FileInputStream fis = new FileInputStream(file);) {
			FileClientPipelineFactory clientPipelineFactory =
					new FileClientPipelineFactory((Channel) (fis.getChannel()));
			// 辅助类,用于帮助创建NETTY服务
			Bootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
			String strThumbMark = Constants.THUMB_MARK_NO;
			if (thumbMark) {
				strThumbMark = Constants.THUMB_MARK_YES;
			}
			// 具体处理上传文件逻辑
			uploadFile(bootstrap, FileClientContainer.getHost(), FileClientContainer.getPort(), file, fileName,
					strThumbMark, FileClientContainer.getUserName(), FileClientContainer.getPassword());
			Result result = clientPipelineFactory.getResult();
			if ((result != null) && (result.isCode())) {
				return result.getFilePath();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void deleteFile(Bootstrap bootstrap, String host, int port, String filePath, String userName,
			String pwd) {
		URI uri = getUri(host, port);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		Channel channel = future.awaitUninterruptibly().channel();
		if (!future.isSuccess()) {
			future.cause().printStackTrace();
			future.channel().closeFuture();
			return;
		}

		WrapFileClientHandler handler = new DeleteFileClientHandler(host, uri, filePath, userName, pwd);
		HttpRequest request = handler.getRequest();
		HttpDataFactory factory = getHttpDataFactory();
		HttpPostRequestEncoder bodyRequestEncoder = handler.wrapRequestData(factory);
		channel.write(request);
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.closeFuture().awaitUninterruptibly();
		channel.closeFuture();
		factory.cleanAllHttpData();
	}

	/**
	 * 文件删除
	 * 
	 * @param filePath 文件服务器存储的文件路径（相对路径）
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public static boolean deleteFile(String filePath, String userName, String pwd) {
		try (FileInputStream fis = new FileInputStream(filePath);) {
			FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory((Channel) fis.getChannel());
			Bootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
			deleteFile(bootstrap, FileClientContainer.getHost(), FileClientContainer.getPort(), filePath, userName,
					pwd);
			Result result = clientPipelineFactory.getResult();
			if ((result != null) && (result.isCode())) {
				return result.isCode();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 文件删除
	 * 
	 * @param filePath 文件服务器存储的文件路径（相对路径）
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		return deleteFile(filePath, FileClientContainer.getUserName(), FileClientContainer.getPassword());
	}

	private static void replaceFile(Bootstrap bootstrap, String host, int port, File file, String filePath,
			String userName, String pwd) {
		URI uri = getUri(host, port);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		Channel channel = future.awaitUninterruptibly().channel();
		if (!future.isSuccess()) {
			future.cause().printStackTrace();
			future.channel().closeFuture();
			return;
		}

		WrapFileClientHandler handler = new ReplaceFileClientHandler(host, uri, filePath, file, userName, pwd);
		HttpRequest request = handler.getRequest();
		HttpDataFactory factory = getHttpDataFactory();
		HttpPostRequestEncoder bodyRequestEncoder = handler.wrapRequestData(factory);
		channel.write(request);
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.closeFuture().awaitUninterruptibly();
		future.channel().closeFuture();
		factory.cleanAllHttpData();
	}

	/**
	 * 替换文件
	 * 
	 * @param file 需要替换的文件
	 * @param filePath 文件服务器存储的文件路径（相对路径）
	 * @return
	 */
	public static boolean replaceFile(File file, String filePath) {
		try (FileInputStream fis = new FileInputStream(file);) {
			FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory((Channel) fis);
			Bootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
			replaceFile(bootstrap, FileClientContainer.getHost(), FileClientContainer.getPort(), file, filePath,
					FileClientContainer.getUserName(), FileClientContainer.getPassword());
			Result result = clientPipelineFactory.getResult();
			if ((result != null) && (result.isCode())) {
				return result.isCode();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void createThumbPicture(Bootstrap bootstrap, String host, int port, String filePath, String userName,
			String pwd) {
		URI uri = getUri(host, port);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
		Channel channel = future.awaitUninterruptibly().channel();
		if (!future.isSuccess()) {
			future.cause().printStackTrace();
			future.channel().closeFuture();
			return;
		}

		WrapFileClientHandler handler = new CreateThumbPictureClientHandler(host, uri, userName, pwd, filePath);
		HttpRequest request = handler.getRequest();
		HttpDataFactory factory = getHttpDataFactory();
		HttpPostRequestEncoder bodyRequestEncoder = handler.wrapRequestData(factory);
		channel.write(request);
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.closeFuture().awaitUninterruptibly();
		future.channel().closeFuture();
		factory.cleanAllHttpData();
	}

	/**
	 * 生成缩略图
	 * 
	 * @param filePath 文件服务器存储的文件路径（相对路径）
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public static boolean createThumbPicture(String filePath, String userName, String pwd) {
		try (FileInputStream fis = new FileInputStream(filePath);) {
			FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory((Channel) fis.getChannel());
			Bootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
			createThumbPicture(bootstrap, FileClientContainer.getHost(), FileClientContainer.getPort(), filePath,
					userName, pwd);
			Result result = clientPipelineFactory.getResult();
			if ((result != null) && (result.isCode())) {
				return result.isCode();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成缩略图
	 * 
	 * @param filePath 文件服务器存储的文件路径（相对路径）
	 * @return
	 */
	public static boolean createThumbPicture(String filePath) {
		return createThumbPicture(filePath, FileClientContainer.getUserName(), FileClientContainer.getPassword());
	}
}