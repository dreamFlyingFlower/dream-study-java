package com.wy.qq.teacher;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import com.wy.qq.BytesUtil;
import com.wy.qq.FrameUnit;

/**
 * 发送服务器
 */
public class SenderServer {

	/** UDP套接字 */
	private DatagramSocket socket;

	/** 机器人,抓图 */
	private Robot robot;

	private Rectangle rect;

	public SenderServer() {
		try {
			socket = new DatagramSocket(8888);
			robot = new Robot();
			rect = new Rectangle(0, 0, 1366, 768);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		int i = 0;
		while (true) {
			sendOneScreen();
			System.out.println("发送 : " + i + " 屏");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
	}

	/**
	 * 发送一屏画面
	 */
	private void sendOneScreen() {
		// 1.抓图.
		byte[] frameData = catchOneScreen(true);
		// 2.切屏
		List<FrameUnit> units = splitScreen(frameData);
		// 3.发送所有帧单元
		sendAllUnits(units);
	}

	/**
	 * 发送所有帧单元
	 */
	private void sendAllUnits(List<FrameUnit> units) {
		try {
			for (FrameUnit unit : units) {
				// 处理帧单元
				DatagramPacket pack = processUnit(unit);
				socket.send(pack);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理帧单元
	 */
	private DatagramPacket processUnit(FrameUnit unit) {
		byte[] buf = new byte[unit.getLength() + 14];
		// timestamp
		byte[] timeBytes = BytesUtil.long2ByteArr(unit.getTimestamp());
		System.arraycopy(timeBytes, 0, buf, 0, 8);
		// count
		buf[8] = (byte) unit.getCount();
		// index
		buf[9] = (byte) unit.getIndex();
		// data长度
		byte[] unitLenBytes = BytesUtil.int2ByteArr(unit.getLength());
		System.arraycopy(unitLenBytes, 0, buf, 10, 4);
		// 数据
		byte[] unitDataBytes = unit.getUnitData();
		System.arraycopy(unitDataBytes, 0, buf, 14, unitDataBytes.length);
		DatagramPacket pack = new DatagramPacket(buf, buf.length);
		// 设置数据报包的广播地址
		InetSocketAddress bcAddr = new InetSocketAddress("192.168.12.255", 9999);
		pack.setSocketAddress(bcAddr);
		return pack;
	}

	/**
	 * 切屏
	 */
	private List<FrameUnit> splitScreen(byte[] frameData) {
		// frameUnit的长度
		int unitLength = 60 * 1024;
		// 帧单元集合
		List<FrameUnit> list = new ArrayList<FrameUnit>();
		// 帧单元个数
		int count = 0;
		if (frameData.length % unitLength == 0) {
			count = frameData.length / unitLength;
		} else {
			count = frameData.length / unitLength + 1;
		}
		// 取出时间戳
		long timestamp = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			FrameUnit unit = new FrameUnit();
			unit.setTimestamp(timestamp);
			unit.setCount(count);
			unit.setIndex(i);
			// 不是最后一帧单元,大小为60K
			if (i != (count - 1)) {
				unit.setLength(60 * 1024);
				byte[] uniData = new byte[60 * 1024];
				System.arraycopy(frameData, i * 60 * 1024, uniData, 0, 60 * 1024);
				unit.setUnitData(uniData);
			}
			// 最后一帧单元
			else {
				// 取得最后一帧的长度
				int remain = frameData.length % unitLength == 0 ? 60 * 1024 : frameData.length % unitLength;
				unit.setLength(remain);
				byte[] uniData = new byte[remain];
				System.arraycopy(frameData, i * 60 * 1024, uniData, 0, remain);
				unit.setUnitData(uniData);
			}
			list.add(unit);
		}
		return list;
	}

	/**
	 * 抓一屏画面(未压缩)
	 */
	private byte[] catchOneScreen(boolean zip) {
		try {
			BufferedImage img = robot.createScreenCapture(rect);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
			// 原生数据
			byte[] rawData = baos.toByteArray();
			// 需要压缩
			if (zip) {
				ByteArrayOutputStream baos0 = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos0);
				zos.putNextEntry(new ZipEntry("0001"));
				zos.write(rawData);
				zos.close();
				baos0.close();
				return baos0.toByteArray();
			}
			return rawData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}