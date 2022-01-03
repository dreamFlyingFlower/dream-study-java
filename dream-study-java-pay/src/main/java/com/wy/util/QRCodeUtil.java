package com.wy.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wy.properties.WeixinProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 二维码生成器(扫码支付模式一)
 */
@Slf4j
public class QRCodeUtil {

	// 商户支付回调URL设置指引：进入公众平台-->微信支付-->开发配置-->扫码支付-->修改 加入回调URL
	public static void main(String[] args) {
		WeixinProperties weixinProperties = SpringContextUtils.getBean(WeixinProperties.class);
		// 注意参数初始化 这只是个Demo
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		// 封装通用参数
		WeixinUtils.commonParams(packageParams);
		packageParams.put("product_id", "20170731");// 真实商品ID
		packageParams.put("time_stamp", WeixinUtils.getCurrTime());
		// 生成签名
		String sign = WeixinUtils.createSign("UTF-8", packageParams, weixinProperties.getApiKey());
		// 组装二维码信息(注意全角和半角：的区别 狗日的腾讯)
		StringBuffer qrCode = new StringBuffer();
		qrCode.append("weixin://wxpay/bizpayurl?");
		qrCode.append("appid=" + weixinProperties.getAppId());
		qrCode.append("&mch_id=" + weixinProperties.getMchId());
		qrCode.append("&nonce_str=" + packageParams.get("nonce_str"));
		qrCode.append("&product_id=20170731");
		qrCode.append("&time_stamp=" + packageParams.get("time_stamp"));
		qrCode.append("&sign=" + sign);
		// 生成二维码
		getQRCodeImge(qrCode.toString(), 256, "D:\\weixn.png");
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, 1);
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				image.setRGB(x, y, matrix.get(x, y) ? -16777216 : -1);
			}
		}
		return image;
	}

	private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public static File getQRCodeImge(String contents, int width, String imgPath) {
		return getQRCodeImge(contents, width, width, imgPath);
	}

	public static File getQRCodeImge(String contents, int width, int height, String imgPath) {
		try {
			Map<EncodeHintType, Object> hints = new ConcurrentHashMap<>();
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
			BitMatrix bitMatrix =
					(new MultiFormatWriter()).encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
			File imageFile = new File(imgPath);
			writeToFile(bitMatrix, "png", imageFile);
			return imageFile;
		} catch (Exception var7) {
			log.error("create QR code error!", var7);
			return null;
		}
	}
}