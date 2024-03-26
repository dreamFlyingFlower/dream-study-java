package com.wy.liteflow;

import org.springframework.scheduling.annotation.Async;

import com.alibaba.fastjson2.JSONObject;
import com.wy.model.User;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * liteflow是一个规则引擎,可以在短时间内完成复杂的规则编排.liteflow支持较多的规则文件格式,比如xml/json/yaml,对于规则文件的存储方式可以有sql/zk/nacos/apollo
 * 
 * liteflow 的组件在规则文件中即对应的节点,组件对应的种类有很多
 * 
 * <pre>
 * 普通组件:继承NodeComponent,可以用在when和then逻辑中
 * 选择组件:通过业务逻辑来判断接下来的动作要执行哪一个节点,类似于Java中的switch.需要继承NodeSwitchComponent
 * 条件组件:if 组件,返回的结果是true或者false.需要继承NodeIfComponent,返回对应的业务节点,这个和选择组件类似
 * 还有次数循环组件,条件循环组件,循环迭代组件,和退出循环组件,其应用场景比较复杂,可以使用简单的普通组件来替代,太复杂可以使用工作流
 * 
 * 文件编排,then代表串行执行,when表示并行执行
 * 串行编排:THEN(a, b, c, d);
 * 并行编排:WHEN(a, b, c);
 * 串行和并行嵌套结合:THEN( a, WHEN(b, c, d), e);
 * 选择编排:SWITCH(a).to(b, c, d);processSwitch()表达式需要返回的b或者c或者d字符串来执行相应的业务逻辑
 * 条件编排:THEN(IF(x, a),b );
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-03-26 12:44:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
@AllArgsConstructor
public class LiteflowConfig {

	final FlowExecutor flowExecutor;

	@Async
	public void handleApp() {
		// 使用的规则文件,传递参数:上下文对象
		LiteflowResponse response = flowExecutor.execute2Resp("test flow", null, User.class);
		// 获取流程执行后的结果
		if (!response.isSuccess()) {
			Exception e = response.getCause();
			log.warn("error is {}", e.getCause(), e);
		}
		User user = response.getContextBean(User.class);
		log.info(" handleApp 执行完成后context {}", JSONObject.toJSONString(user));
	}
}