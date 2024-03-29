package com.wy.debezium;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.dream.collection.MapHelper;

import io.debezium.data.Envelope;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听事件改变
 *
 * @author 飞花梦影
 * @date 2024-03-21 13:13:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Component
@Slf4j
public class ChangeEventHandler {

	public static final String DATA = "data";

	public static final String BEFORE_DATA = "beforeData";

	public static final String EVENT_TYPE = "eventType";

	public static final String SOURCE = "source";

	public static final String TABLE = "table";

	private enum FilterJsonFieldEnum {

		/** 表 */
		table,
		/** 库 */
		db,
		/** 操作时间 */
		ts_ms,;

		public static Boolean filterJsonField(String fieldName) {
			return Stream.of(values()).map(Enum::name).collect(Collectors.toSet()).contains(fieldName);
		}
	}

	public enum EventTypeEnum {

		/** 增 */
		CREATE(1),
		/** 删 */
		UPDATE(2),
		/** 改 */
		DELETE(3),;

		@Getter
		private final int type;

		EventTypeEnum(int type) {
			this.type = type;
		}
	}

	public void handlePayload(List<RecordChangeEvent<SourceRecord>> recordChangeEvents,
			DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordCommitter) {
		for (RecordChangeEvent<SourceRecord> r : recordChangeEvents) {
			SourceRecord sourceRecord = r.record();
			Struct sourceRecordChangeValue = (Struct) sourceRecord.value();
			if (sourceRecordChangeValue == null) {
				continue;
			}
			// 获取变更表数据
			Map<String, Object> changeMap = getChangeTableInfo(sourceRecordChangeValue);
			if (MapHelper.isEmpty(changeMap)) {
				continue;
			}
			ChangeListenerModel changeListenerModel = getChangeDataInfo(sourceRecordChangeValue, changeMap);
			if (changeListenerModel == null) {
				continue;
			}
			String jsonString = JSON.toJSONString(changeListenerModel);
			log.info("发送变更数据：{}", jsonString);
		}
		try {
			recordCommitter.markBatchFinished();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private ChangeListenerModel getChangeDataInfo(Struct sourceRecordChangeValue, Map<String, Object> changeMap) {
		// 操作类型过滤,只处理增删改
		Envelope.Operation operation =
				Envelope.Operation.forCode((String) sourceRecordChangeValue.get(Envelope.FieldName.OPERATION));
		if (operation != Envelope.Operation.READ) {
			Integer eventType = null;
			Map<String, Object> result = new HashMap<>(4);
			if (operation == Envelope.Operation.CREATE) {
				eventType = EventTypeEnum.CREATE.getType();
				result.put(DATA, getChangeData(sourceRecordChangeValue, Envelope.FieldName.AFTER));
				result.put(BEFORE_DATA, null);
			}
			// 修改需要特殊处理,拿到前后的数据
			if (operation == Envelope.Operation.UPDATE) {
				if (!changeMap.containsKey(TABLE)) {
					return null;
				}
				eventType = EventTypeEnum.UPDATE.getType();
				String currentTableName = String.valueOf(changeMap.get(TABLE).toString());
				// 忽略非重要属性变更
				Map<String, String> resultMap = filterChangeData(sourceRecordChangeValue, currentTableName);
				if (MapHelper.isEmpty(resultMap)) {
					return null;
				}
				result.put(DATA, resultMap.get(Envelope.FieldName.AFTER));
				result.put(BEFORE_DATA, resultMap.get(Envelope.FieldName.BEFORE));
			}
			if (operation == Envelope.Operation.DELETE) {
				eventType = EventTypeEnum.DELETE.getType();
				result.put(DATA, getChangeData(sourceRecordChangeValue, Envelope.FieldName.BEFORE));
				result.put(BEFORE_DATA, getChangeData(sourceRecordChangeValue, Envelope.FieldName.BEFORE));
			}
			result.put(EVENT_TYPE, eventType);
			result.putAll(changeMap);
			ChangeListenerModel ChangeListenerModel = new ChangeListenerModel();
			BeanUtils.copyProperties(result, ChangeListenerModel);
			return ChangeListenerModel;
		}
		return null;
	}

	/**
	 * 过滤非重要变更数据
	 *
	 * @param sourceRecordChangeValue
	 * @param currentTableName
	 * @return
	 */
	private Map<String, String> filterChangeData(Struct sourceRecordChangeValue, String currentTableName) {
		Map<String, String> resultMap = new HashMap<>(4);
		Map<String, Object> afterMap = getChangeDataMap(sourceRecordChangeValue, Envelope.FieldName.AFTER);
		Map<String, Object> beforeMap = getChangeDataMap(sourceRecordChangeValue, Envelope.FieldName.BEFORE);
		// todo 根据表过滤字段
		resultMap.put(Envelope.FieldName.AFTER, JSON.toJSONString(afterMap));
		resultMap.put(Envelope.FieldName.BEFORE, JSON.toJSONString(beforeMap));
		return resultMap;
	}

	/**
	 * 校验是否仅仅是非重要字段属性变更
	 * 
	 * @param currentTableName
	 * @param afterMap
	 * @param beforeMap
	 * @param filterColumnList
	 * @return boolean
	 */
	public boolean checkNonEssentialData(String currentTableName, Map<String, Object> afterMap,
			Map<String, Object> beforeMap, List<String> filterColumnList) {
		Map<String, Boolean> filterMap = new HashMap<>(16);
		for (String key : afterMap.keySet()) {
			Object afterValue = afterMap.get(key);
			Object beforeValue = beforeMap.get(key);
			filterMap.put(key, !Objects.equals(beforeValue, afterValue));
		}
		filterColumnList.parallelStream().forEach(filterMap::remove);
		if (filterMap.values().stream().noneMatch(x -> x)) {
			log.info("表:{}无核心资料变更,忽略此次操作!", currentTableName);
			return true;
		}
		return false;
	}

	public String getChangeData(Struct sourceRecordChangeValue, String record) {
		Map<String, Object> changeDataMap = getChangeDataMap(sourceRecordChangeValue, record);
		if (MapHelper.isEmpty(changeDataMap)) {
			return null;
		}
		return JSON.toJSONString(changeDataMap);
	}

	public Map<String, Object> getChangeDataMap(Struct sourceRecordChangeValue, String record) {
		Struct struct = (Struct) sourceRecordChangeValue.get(record);
		// 将变更的行封装为Map
		Map<String,
				Object> changeData = struct.schema().fields().stream().map(Field::name)
						.filter(fieldName -> struct.get(fieldName) != null)
						.map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
						.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
		if (MapHelper.isEmpty(changeData)) {
			return null;
		}
		return changeData;
	}

	private Map<String, Object> getChangeTableInfo(Struct sourceRecordChangeValue) {
		Struct struct = (Struct) sourceRecordChangeValue.get(SOURCE);
		Map<String, Object> map = struct.schema().fields().stream().map(Field::name)
				.filter(fieldName -> struct.get(fieldName) != null && FilterJsonFieldEnum.filterJsonField(fieldName))
				.map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
				.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
		if (map.containsKey(FilterJsonFieldEnum.ts_ms.name())) {
			map.put("changeTime", map.get(FilterJsonFieldEnum.ts_ms.name()));
			map.remove(FilterJsonFieldEnum.ts_ms.name());
		}
		return map;
	}
}