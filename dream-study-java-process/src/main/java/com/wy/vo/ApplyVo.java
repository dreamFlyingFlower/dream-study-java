package com.wy.vo;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVo {

	private String intentionCourse;

	private File file;

	private String fileFileName;

	private String message;
}