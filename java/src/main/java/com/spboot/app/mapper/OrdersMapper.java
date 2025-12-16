package com.spboot.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}

