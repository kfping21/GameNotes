package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Tongzhi;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("通知")
public interface TongzhiMapper extends BaseMapper<Tongzhi> {}
