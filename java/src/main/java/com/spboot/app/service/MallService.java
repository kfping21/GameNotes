package com.spboot.app.service;

import com.spboot.app.mapper.ProductCategoryMapper;
import com.spboot.app.mapper.ProductImageMapper;
import com.spboot.app.mapper.ProductMapper;
import com.spboot.app.pojo.Product;
import com.spboot.app.pojo.ProductCategory;
import com.spboot.app.pojo.ProductImage;
import com.spboot.app.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MallService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductImageMapper productImageMapper;

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 列表查询：支持按 gameId, categoryId, keyword 分页
     */
    public R<Object> listProducts(Integer gameId, Integer categoryId, String keyword, Integer page, Integer size) {
        try {
            QueryWrapper<Product> wrapper = new QueryWrapper<>();
            if (gameId != null && gameId > 0) wrapper.eq("game_id", gameId);
            if (categoryId != null && categoryId > 0) wrapper.eq("category_id", categoryId);
            if (keyword != null && !keyword.trim().isEmpty()) {
                wrapper.and(w -> w.like("name", keyword).or().like("tags", keyword));
            }
            wrapper.orderByDesc("id");

            Page<Product> p = new Page<>(page == null ? 1 : page, size == null ? 20 : size);
            Page<Product> ret = productMapper.selectPage(p, wrapper);

            Map<String, Object> res = new HashMap<>();
            res.put("total", ret.getTotal());
            res.put("page", ret.getCurrent());
            res.put("size", ret.getSize());
            res.put("records", ret.getRecords());
            return R.success(res);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    /**
     * 商品详情：包含图片列表与分类信息与关联游戏简单信息
     */
    public R<Object> getProductDetail(Integer id) {
        try {
            if (id == null || id <= 0) return R.error("缺少 id");
            Product prod = productMapper.selectById(id);
            if (prod == null) return R.error("未找到商品");

            Map<String, Object> data = new HashMap<>();
            data.put("id", prod.getId());
            data.put("game_id", prod.getGameId());
            data.put("category_id", prod.getCategoryId());
            data.put("name", prod.getName());
            data.put("cover_url", prod.getCoverUrl());
            data.put("price", prod.getPrice());
            data.put("stock", prod.getStock());
            data.put("tags", prod.getTags());
            data.put("intro", prod.getIntro());
            data.put("created_at", prod.getCreatedAt());

            List<ProductImage> images = productImageMapper.selectList(new QueryWrapper<ProductImage>().eq("product_id", id).orderByAsc("sort"));
            data.put("images", images);

            if (prod.getCategoryId() != null && prod.getCategoryId() > 0) {
                ProductCategory cat = productCategoryMapper.selectById(prod.getCategoryId());
                data.put("category", cat == null ? new HashMap<>() : cat);
            }

            if (prod.getGameId() != null && prod.getGameId() > 0) {
                Map<String, Object> game = com.jntoo.db.DB.find("SELECT id, youximingcheng, youxitupian FROM youxi WHERE id = " + prod.getGameId());
                data.put("game", game == null ? new HashMap<>() : game);
            }

            return R.success(data);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }
}
