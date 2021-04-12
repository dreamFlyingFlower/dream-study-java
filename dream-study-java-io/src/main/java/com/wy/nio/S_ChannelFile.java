package com.wy.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 利用通道操作文件,不利用传统的流
 * 
 * @apiNote 通道:Channel,用于源节点与目标节点的连接,负责缓冲区中数据的传输.<br>
 *          Channel本身不存储数据,因此需要配合缓冲区进行传输<br>
 *          Channel的主要接口{@link java.nio.channels.Channel},子类接口:<br>
 *          {@link java.nio.channels.FileChannel}:主要用于文件的读写<br>
 *          {@link java.nio.channels.SocketChannel},{@link java.nio.channels.ServerSocketChannel}:用于tcp网络传输<br>
 *          {@link java.nio.channels.DatagramChannel}:用于udp网络传输
 * 
 * @apiNote 获取通道:<br>
 *          getChannel():本地IO:FileInputStream,RandomAccessFile;网络IO:Socket,ServerSocket,DatagramSocket<br>
 *          open():针对各个通道提供了静态方法<br>
 *          newByteChannel():Files工具类提供
 * 
 * @apiNote 通道之间的数据传输:transferFrom(),transferTo()
 * 
 * @apiNote 分散读取(Scattering Reads):将通道中的数据依次分散到多个缓冲区中,是有序的,填满一个再换一个<br>
 *          聚集写入(GatheringWrites):将多个缓冲区中的数据聚集到通道中
 * 
 * @apiNote 字符集:Charset->StandardCharsets;<br>
 *          编码:字符串 -> 字节数组;解码:字节数组 -> 字符串
 * 
 * @author ParadiseWY
 * @date 2020-11-26 19:54:42
 * @git {@link https://github.com/mygodness100}
 */
public class S_ChannelFile {

	public static void main(String[] args) {

	}

	/**
	 * 利用通道进行文件的复制,非直接缓冲区
	 */
	public static void copyFile() {
		long start = System.currentTimeMillis();
		try (FileInputStream fis = new FileInputStream("d:/1.mkv");
				FileOutputStream fos = new FileOutputStream("d:/2.mkv");
				FileChannel inChannel = fis.getChannel();
				FileChannel outChannel = fos.getChannel();) {
			// 分配指定大小的缓冲区
			ByteBuffer buf = ByteBuffer.allocate(1024);
			// 将通道中的数据存入缓冲区中
			while (inChannel.read(buf) != -1) {
				// 切换读取数据的模式
				buf.flip();
				// 将缓冲区中的数据写入通道中
				outChannel.write(buf);
				// 清空缓冲区
				buf.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("耗费时间为:" + (end - start));
	}

	/**
	 * 使用直接缓冲区完成文件的复制,内存映射文件.性能消耗大,但是速度极快
	 */
	public static void copy1() {
		long start = System.currentTimeMillis();
		// StandardOpenOption有多种模式,其中CREATE表示文件不存在就创建,存在就覆盖;
		// CREATE_NEW:文件不存在就创建,存在就抛异常
		// 若CREATE和CREATE_NEW同时存在,CREATE的级别将覆盖CREATE_NEW
		try (FileChannel inChannel = FileChannel.open(Paths.get("d:/1.mkv"), StandardOpenOption.READ);
				FileChannel outChannel = FileChannel.open(Paths.get("d:/2.mkv"), StandardOpenOption.WRITE,
						StandardOpenOption.READ, StandardOpenOption.CREATE);) {
			// 内存映射文件
			MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
			MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
			// 直接对缓冲区进行数据的读写操作
			byte[] dst = new byte[inMappedBuf.limit()];
			inMappedBuf.get(dst);
			outMappedBuf.put(dst);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("耗费时间为:" + (end - start));
	}

	/**
	 * 通道之间的数据传输,直接缓冲区
	 */
	public static void copy2() {
		try (FileChannel inChannel = FileChannel.open(Paths.get("d:/1.mkv"), StandardOpenOption.READ);
				FileChannel outChannel = FileChannel.open(Paths.get("d:/2.mkv"), StandardOpenOption.WRITE,
						StandardOpenOption.READ, StandardOpenOption.CREATE);) {
			// 上下2句效果相同
			// inChannel.transferTo(0, inChannel.size(), outChannel);
			outChannel.transferFrom(inChannel, 0, inChannel.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分散和聚集
	 */
	public void copy3() {
		try (RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");
				FileChannel channel1 = raf1.getChannel();
				RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
				FileChannel channel2 = raf2.getChannel();) {
			// 分配指定大小的缓冲区
			ByteBuffer buf1 = ByteBuffer.allocate(100);
			ByteBuffer buf2 = ByteBuffer.allocate(1024);
			// 分散读取
			ByteBuffer[] bufs = { buf1, buf2 };
			channel1.read(bufs);
			for (ByteBuffer byteBuffer : bufs) {
				byteBuffer.flip();
			}
			System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
			System.out.println("-----------------");
			System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));
			// 聚集写入
			channel2.write(bufs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 字符集
	 */
	public void copy4() {
		try {
			// 所有支持的字符集
			Charset.availableCharsets();
			// 指定一个字符集
			Charset cs1 = Charset.forName("GBK");
			// Charset ce = Charset.forName(StandardCharsets.UTF_8.displayName());
			// 获取编码器
			CharsetEncoder ce = cs1.newEncoder();
			// 获取解码器
			CharsetDecoder cd = cs1.newDecoder();
			CharBuffer cBuf = CharBuffer.allocate(1024);
			cBuf.put("飞花梦影");
			cBuf.flip();
			// 编码
			ByteBuffer bBuf = ce.encode(cBuf);
			for (int i = 0; i < 12; i++) {
				System.out.println(bBuf.get());
			}
			// 解码
			bBuf.flip();
			CharBuffer cBuf2 = cd.decode(bBuf);
			System.out.println(cBuf2.toString());
			System.out.println("------------------------------------------------------");
			// GBK编码,UTF8解码会乱码
			Charset cs2 = Charset.forName("UTF-8");
			bBuf.flip();
			CharBuffer cBuf3 = cs2.decode(bBuf);
			System.out.println(cBuf3.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}