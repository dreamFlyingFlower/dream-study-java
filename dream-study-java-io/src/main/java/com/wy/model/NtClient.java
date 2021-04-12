package com.wy.model;

import java.io.Serializable;

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
public class NtClient implements Serializable {

	private static final long serialVersionUID = 7084843947860990140L;

	private Long id;

	private String message;

	private byte[] attachment;
}