package com.wy.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.wy.common.Constants;
import com.wy.properties.WeixinProperties;

/**
 * 相关配置参数
 */
public class ConfigUtil {

	/**
	 * 基础参数
	 * 
	 * @param packageParams void
	 */
	public static void commonParams(SortedMap<Object, Object> packageParams) {
		WeixinProperties properties = SpringContextUtils.getBean(WeixinProperties.class);
		// 账号信息
		String appid = properties.getAppId(); // appid
		String mch_id = properties.getMchId(); // 商业号
		// 生成随机字符串
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;
		packageParams.put("appid", appid);// 公众账号ID
		packageParams.put("mch_id", mch_id);// 商户号
		packageParams.put("nonce_str", nonce_str);// 随机字符串
	}

	/**
	 * 扫码原生支付模式一中的二维码链接转成短链接(weixin://wxpay/s/XXXXXX),减小二维码数据量,提升扫描速度和精确度
	 * 
	 * @param urlCode void
	 */
	public static void shorturl(String urlCode) {
		WeixinProperties properties = SpringContextUtils.getBean(WeixinProperties.class);
		try {
			String key = properties.getApiKey(); // key
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams);
			packageParams.put("long_url", urlCode);// URL链接
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign);// 签名
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(Constants.SHORT_URL, requestXML);
			Map<String, String> map = XMLUtil.doXMLParse(resXml);
			String returnCode = (String) map.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) map.get("return_code");
				if ("SUCCESS".equals(resultCode)) {
					urlCode = (String) map.get("short_url");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}