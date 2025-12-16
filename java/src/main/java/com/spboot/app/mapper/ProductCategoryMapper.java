package com.spboot.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spboot.app.pojo.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}

