package dream.study.spring.debezium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-03-21 13:50:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeListenerModel {

	/**
	 * 当前DB
	 */
	private String db;

	/**
	 * 当前表
	 */
	private String table;

	/**
	 * 操作类型 1add 2update 3 delete
	 */
	private Integer eventType;

	/**
	 * 操作时间
	 */
	private Long changeTime;

	/**
	 * 现数据
	 */
	private String data;

	/**
	 * 之前数据
	 */
	private String beforeData;
}