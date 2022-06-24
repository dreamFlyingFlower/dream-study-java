package com.wy.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wy.shiro.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

	int batchInsert(List<User> list);
}