package dream.study.spring.signature;

/**
 * 验签常量
 * 
 * RSA算法验签:私钥加密,公钥验签;私钥解密,公钥加密
 * 
 * 验签规则:
 * 
 * <pre>
 * 1.规定使用RSA非对称加密算法或其他算法
 * 2.调用方(私钥)按规则对请求参数排序和拼接
 * 3.使用SHA256算法对对拼接的字符串进行摘要生成,之后转成16进制
 * 4.将签名放在请求头中,可自定义请求头名称,如X-Request-Signature
 * </pre>
 *
 * @author 飞花梦影
 * @date 2023-12-26 17:55:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstSignature {

	String REQUEST_PARAM_SIGNATURE_KEY = "appId";

	String HEADER_SIGNATURE_KEY = "X-Signature";
}