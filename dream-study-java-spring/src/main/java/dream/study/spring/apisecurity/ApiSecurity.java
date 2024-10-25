package dream.study.spring.apisecurity;

/**
 * API安全
 * 
 * Token授权认证,防止未授权用户获取数据
 * 
 * <pre>
 * 应用内一定要唯一,否则会出现授权混乱,A用户看到了B用户的数据
 * 每次生成的Token一定要不一样,防止被记录,授权永久有效
 * 一般Token对应的是Redis的key,value存放的是这个用户相关缓存信息
 * 要设置Token的过期时间,过期后需要重新登录
 * </pre>
 * 
 * 时间戳超时机制:每次请求接口都带上当前时间的时间戳,服务端将时间戳跟当前时间进行比对,如果时间差大于一定时间,则认为该请求失效.
 * 时间戳超时机制是防御DOS攻击的有效手段
 * 
 * URL签名:对URL中的明文参数进行签名,参考JWT
 * 
 * 防重放,防止接口被第二次请求,防采集
 *
 * <pre>
 * 客户端第一次访问时,将签名sign存放到服务器的Redis中,超时时间设定为跟时间戳的一致,
 * 二者时间一致可以保证无论在timestamp限定时间内还是外,URL都只能访问一次,
 * 如果被非法者截获,使用同一个URL再次访问,如果发现缓存服务器中已经存在了本次签名,则拒绝服务.
 * 
 * 客户端通过用户名密码登录服务器并获取Token
 * 客户端生成时间戳timestamp,并将timestamp作为其中一个参数
 * 客户端将所有的参数,包括Token和timestamp按照自己的签名算法进行排序加密得到签名sign
 * 将token、timestamp和sign作为请求时必须携带的参数加在每个请求的URL后边,如:http://url/request?token=&timetamp=&sign=
 * 服务端对token、timestamp和sign进行验证,只有在token有效、timestamp未超时、缓存服务器中不存在sign三种情况同时满足,本次请求才有效
 * </pre>
 * 
 * 采用HTTPS通信协议,防止数据明文传输
 *
 * @author 飞花梦影
 * @date 2023-10-12 17:27:11
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class ApiSecurity {

}