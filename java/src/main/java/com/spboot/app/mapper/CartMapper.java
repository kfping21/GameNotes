package com.spboot.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}

