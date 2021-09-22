package com.wy.netty.chat.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wy.netty.chat.server.entity.Player;

/**
 * 玩家数据访问层
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}