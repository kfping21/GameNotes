package com.spboot.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}

