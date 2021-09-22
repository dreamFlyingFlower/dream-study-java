package com.wy.netty.chat.server.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 玩家实体对象
 */
@Entity
@Table(name = "player")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Player {

	/**
	 * 玩家id
	 */
	@Id
	@GeneratedValue
	private long playerId;

	/**
	 * 玩家名
	 */
	private String playerName;

	/**
	 * 密码
	 */
	private String passward;

	/**
	 * 等级
	 */
	private int level;

	/**
	 * 经验
	 */
	private int exp;
}