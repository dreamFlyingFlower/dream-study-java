package com.wy.qq.student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import com.wy.qq.BytesUtil;
import com.wy.qq.FrameUnit;

/**
 * 学生端接受广播线程
 */
public class ReceiverThread extends Thread {

	// 存放所有帧单元的集合
	private Map<Integer, FrameUnit> map = new HashMap<Integer, FrameUnit>();

	//
	private StudentUI ui;

	//
	private DatagramSocket sock;

	public ReceiverThread(StudentUI ui) {
		try {
			this.ui = ui;
			sock = new DatagramSocket(9999);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			// 数据缓冲区
			byte[] buf = new byte[60 * 1024 + 14];
			DatagramPacket pack = new DatagramPacket(buf, buf.length);
			while (true) {
				sock.receive(pack);

				// 解析数据报包成FrameUnit
				FrameUnit unit = parsePack(pack);

				// 处理帧单元
				processUnit(unit);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理帧单元
	 */
	private void processUnit(FrameUnit unit) {
		// 如果map集合为空，没有帧单元数据
		if (map.isEmpty()) {
			map.put(unit.getIndex(), unit);
		} else {
			// 提取map中存放的帧单元的时间戳
			long oldTime = map.values().iterator().next().getTimestamp();
			long currTime = unit.getTimestamp();
			// 同一帧
			if (oldTime == currTime) {
				map.put(unit.getIndex(), unit);
			}
			// 新帧单元
			else if (currTime > oldTime) {
				map.clear();
				map.put(unit.getIndex(), unit);
			} else {
			}
		}
		// 处理frame
		processFrame(true);

	}

	/**
	 * 判断是否集齐所有帧单元，处理一帧
	 */
	private void processFrame(boolean zip) {
		try {
			int count = map.values().iterator().next().getCount();
			int size = map.size();
			// 集齐了所有帧单元
			if (count == size) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				for (int i = 0; i < count; i++) {
					FrameUnit unit = map.get(i);
					baos.write(unit.getUnitData());
				}
				// 得到一屏画面的帧数据
				byte[] frameData = baos.toByteArray();

				if (zip) {
					ByteArrayOutputStream baos0 = new ByteArrayOutputStream();
					ByteArrayInputStream bais = new ByteArrayInputStream(frameData);
					ZipInputStream zis = new ZipInputStream(bais);
					zis.getNextEntry();
					byte[] buf0 = new byte[1024];
					int len = -1;
					while ((len = zis.read(buf0)) != -1) {
						baos0.write(buf0, 0, len);
					}
					zis.close();
					// 解压数据
					frameData = baos0.toByteArray();
				}
				ui.updateUI(frameData);
				map.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析包,解析帧单元
	 */
	private FrameUnit parsePack(DatagramPacket pack) {
		// 缓冲区数据,含header
		byte[] bufData = pack.getData();

		FrameUnit unit = new FrameUnit();
		// int length = pack.getLength();

		// 1.处理时间戳
		long timestamp = BytesUtil.byteArr2Long(bufData);
		unit.setTimestamp(timestamp);

		// 2.frameUnit个数
		int count = bufData[8];
		unit.setCount(count);

		// 3.frame索引
		int index = bufData[9];
		unit.setIndex(index);

		// 4.数据长度
		byte[] bytes4 = new byte[4];
		System.arraycopy(bufData, 10, bytes4, 0, 4);
		int dataLen = BytesUtil.byteArr2Int(bytes4);
		unit.setLength(dataLen);

		// 5.图像数据
		byte[] unitData = new byte[dataLen];
		System.arraycopy(bufData, 14, unitData, 0, dataLen);
		unit.setUnitData(unitData);

		return unit;
	}
}