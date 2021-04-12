package com.wy.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NtServer implements Serializable {

	private static final long serialVersionUID = -8134313953478922076L;

	private Long id;

	private String message;
}