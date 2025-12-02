package com.spboot.app.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Pinglunhuifu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@TableName("评论回复")
public interface PinglunhuifuMapper extends BaseMapper<Pinglunhuifu> {}
