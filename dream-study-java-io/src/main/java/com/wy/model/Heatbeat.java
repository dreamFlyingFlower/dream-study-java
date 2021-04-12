package com.wy.model;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Heatbeat implements Serializable {

	private static final long serialVersionUID = 2827219147304706826L;

	private String ip;

	private Map<String, Object> cpuMsgMap;

	private Map<String, Object> memMsgMap;

	private Map<String, Object> fileSysMsgMap;
}