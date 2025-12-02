package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Biaoqian;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("标签")
public interface BiaoqianMapper extends BaseMapper<Biaoqian> {}
