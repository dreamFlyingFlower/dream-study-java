package com.wy.util;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.wy.common.Constants;
import com.wy.digest.DigestTool;
import com.wy.http.HttpTool;
import com.wy.model.OpenIdClass;
import com.wy.properties.WeixinProperties;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-30 15:01:27
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class WeixinUtils {

	/**
	 * 微信H5支付,获取用户openID
	 * 
	 * @param code
	 * @return String
	 */
	public static String getOpenId(String code) {
		WeixinProperties weixinProperties = SpringContextUtils.getBean(WeixinProperties.class);
		if (code != null) {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + weixinProperties.getAppId()
					+ "&secret=" + weixinProperties.getAppSecret() + "&code=" + code + "&grant_type=authorization_code";
			String returnData = getReturnData(url);
			OpenIdClass openIdClass = JSON.parseObject(returnData, OpenIdClass.class);
			if (openIdClass.getOpenid() != null) {
				return openIdClass.getOpenid();
			}
		}
		return "**************";
	}

	/**
	 * 微信H5支付,获得URL后面的数据
	 * 
	 * @param urlString
	 * @return
	 */
	public static String getReturnData(String urlString) {
		String res = "";
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.connect();
			java.io.BufferedReader in =
					new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 微信H5支付,,回调request,参数解析为map格式
	 * 
	 * @param request
	 * @return
	 * @throws Exception Map<String,String>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
		// 解析结果存储在HashMap
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());
		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}

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
		String currTime = getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = buildRandom(4) + "";
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
			commonParams(packageParams);
			packageParams.put("long_url", urlCode);// URL链接
			String sign = createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign);// 签名
			String requestXML = getRequestXml(packageParams);
			String resXml = HttpTool.sendPost(Constants.SHORT_URL, requestXML);
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
	


	/**
	 * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * 
	 * @param characterEncoding
	 * @param packageParams
	 * @param API_KEY
	 * @return boolean
	 */
	@SuppressWarnings({ "rawtypes" })
	public static boolean isTenpaySign(String characterEncoding, SortedMap<String, String> packageParams,
			String API_KEY) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + API_KEY);
		// 算出摘要
		String mysign = DigestTool.md5Hex(sb.toString(), characterEncoding).toLowerCase();
		String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();
		return tenpaySign.equals(mysign);
	}

	/**
	 * sign签名
	 * 
	 * @param characterEncoding
	 * @param packageParams
	 * @param API_KEY
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + API_KEY);
		String sign = DigestTool.md5Hex(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}

	/**
	 * 将请求参数转换为xml格式的string
	 * 
	 * @param parameters
	 * @return String
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @Author 科帮网
	 * @param length
	 * @return int
	 * @Date 2017年7月31日 更新日志 2017年7月31日 科帮网 首次创建
	 *
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
}