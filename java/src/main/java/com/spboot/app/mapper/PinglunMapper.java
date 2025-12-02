package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Pinglun;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("评论")
public interface PinglunMapper extends BaseMapper<Pinglun> {}
