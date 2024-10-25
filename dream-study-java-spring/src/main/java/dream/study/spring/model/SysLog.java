package dream.study.spring.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日志
 *
 * @author 飞花梦影
 * @date 2021-12-09 16:55:24
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLog {

	@ApiModelProperty("日志id")
	private Integer logid;

	@ApiModelProperty("管理员姓名")
	private String UserName;

	@ApiModelProperty("管理员角色")
	private String UserRole;

	@ApiModelProperty("日志描述")
	private String Content;

	@ApiModelProperty("参数集合")
	private String Remarks;

	@ApiModelProperty("表格名称")
	private String TableName;

	@ApiModelProperty("操作时间")
	private String DateTime;

	@ApiModelProperty("返回值")
	private String resultValue;

	@ApiModelProperty("请求ip地址")
	private String ip;

	@ApiModelProperty("请求地址URL")
	private String requestUrl;

	@ApiModelProperty("操作结果")
	private String result;

	@ApiModelProperty("错误信息")
	private String ExString;
}