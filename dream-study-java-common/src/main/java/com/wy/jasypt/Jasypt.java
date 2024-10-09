package com.wy.jasypt;

/**
 * 配置文件中的密码等敏感数据加密
 * 
 * <pre>
 * 引入jasypt-spring-boot-starter,进入maven仓库中org/jasypt/1.9.3/目录中,使用如下命令对要加解密的数据进行操作:
 * 
 * JDK8加密:
 * 		java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI 
 * 		password=盐 algorithm=PBEWithMD5AndDES input="原密码"
 * JDK8解密:
 * 		java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI 
 * 		password=盐 algorithm=PBEWithMD5AndDES input="加密后密码"
 * JDK8以上加密:
 * 		java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI 
 * 		password=盐 algorithm=PBEWITHHMACSHA512ANDAES_256 ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator input="原密码"
 * JDK8以上解密:
 * 		java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI
 * 		password=盐 algorithm=PBEWITHHMACSHA512ANDAES_256 ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator input="加密后的密码"
 * 
 * 其中input是需要加密/解密的明文/密文,password是密钥/盐,解密时也是用这个,algorithm为加密算法
 * 
 * 需要在配置文件中配置jasypt.encryptor.password,jasypt.encryptor.algorithm,jasypt.encryptor.iv-generator-classname
 * 
 * 如果使用默认的PBEWITHHMACSHA512ANDAES_256算法加密,JDK8的AES最大支持128bit的密钥,不支持256bit的密钥,会抛出一个异常,
 * 需要下载“Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files for JDK/JRE 8”,替换JDK/JRE里的2个jar包
 * 下载地址: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html,
 * 下载zip包,将里面的local_policy.jar和US_export_policy.jar解压到\jre\lib\security下覆盖原文件即可,参照 https://www.cnblogs.com/merray/p/9437797.html
 * 
 * JDK8的配置:algorithm=PBEWithMD5AndDES,iv-generator-classname=org.jasypt.iv.NoIvGenerator
 * JDK8替换了jar包或以上:algorithm=PBEWITHHMACSHA512ANDAES_256,iv-generator-classname=org.jasypt.iv.RandomIvGenerator
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-10-08 16:33:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Jasypt {

}