package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Biji;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("笔记")
public interface BijiMapper extends BaseMapper<Biji> {}
