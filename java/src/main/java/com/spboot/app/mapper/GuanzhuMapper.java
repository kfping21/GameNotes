package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Guanzhu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("关注")
public interface GuanzhuMapper extends BaseMapper<Guanzhu> {}
