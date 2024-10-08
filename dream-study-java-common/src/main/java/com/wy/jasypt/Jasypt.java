package com.wy.jasypt;

/**
 * 配置文件中的密码等敏感数据加密
 * 
 * <pre>
 * 引入jasypt-spring-boot-starter,进入maven仓库中org/jasypt/1.9.3/目录中,使用如下命令对要加解密的数据进行操作:
 * 
 * 加密:java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="9ZvUNNxDsZmjNLrn" password="9ZvUNNxDsZmjNLrn" algorithm=PBEWithMD5AndDES
 * 解密:java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI input="e9KIvF4dxNJ0BWxoVSiLhZcV8DwopQ1k" password="9ZvUNNxDsZmjNLrn" algorithm=PBEWithMD5AndDES
 * 
 * 其中input是需要加密/解密的明文/密文,password是密钥,解密时也是用这个,algorithm为加密算法,固定写这个就可以了
 * 
 * 需要在配置文件中配置jasypt.encryptor.password,该参数为加密时的password密钥.为防止泄露,可以在启动JAR包时输入密码,而不是直接配置在配置文件
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-10-08 16:33:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Jasypt {

}