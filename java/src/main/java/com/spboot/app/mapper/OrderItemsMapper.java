package com.spboot.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemsMapper extends BaseMapper<OrderItem> {
}

