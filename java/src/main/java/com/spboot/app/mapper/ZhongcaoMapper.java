package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Zhongcao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("种草")
public interface ZhongcaoMapper extends BaseMapper<Zhongcao> {}
