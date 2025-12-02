package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Dianzan;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("点赞")
public interface DianzanMapper extends BaseMapper<Dianzan> {}
