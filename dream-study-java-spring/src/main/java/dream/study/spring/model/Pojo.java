package dream.study.spring.model;

import java.util.Date;

import lombok.Data;

/**
 * 专门来测试的实体类
 * 
 * @author ParadiseWY
 * @date 2020-09-27 20:20:56
 */
@Data
public class Pojo {

	private Integer id;

	private String username;

	private char sex;

	private boolean flag;

	private double salary;

	private Date birthday;
}