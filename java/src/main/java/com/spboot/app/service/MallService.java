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

    // --- Admin functions (minimal implementations) ---

    public R<Object> insertProduct(Product product, Map<String, Object> data) {
        try {
            product.setId(null);
            productMapper.insert(product);
            // handle images if provided
            if (data.get("images") instanceof java.util.List) {
                java.util.List imgs = (java.util.List) data.get("images");
                int sort = 0;
                for (Object o : imgs) {
                    String url = String.valueOf(o);
                    com.spboot.app.pojo.ProductImage pi = new com.spboot.app.pojo.ProductImage();
                    pi.setProductId(product.getId());
                    pi.setUrl(url);
                    pi.setSort(sort++);
                    productImageMapper.insert(pi);
                }
            }
            return R.success(product);
        } catch (Exception e) {
            return R.error("插入商品失败");
        }
    }

    public R<Object> updateProduct(Product product, Map<String, Object> data) {
        try {
            if (product.getId() == null) return R.error("缺少 id");
            productMapper.updateById(product);
            // update images if provided: delete old and insert new
            if (data.get("images") instanceof java.util.List) {
                productImageMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.spboot.app.pojo.ProductImage>().eq("product_id", product.getId()));
                java.util.List imgs = (java.util.List) data.get("images");
                int sort = 0;
                for (Object o : imgs) {
                    String url = String.valueOf(o);
                    com.spboot.app.pojo.ProductImage pi = new com.spboot.app.pojo.ProductImage();
                    pi.setProductId(product.getId());
                    pi.setUrl(url);
                    pi.setSort(sort++);
                    productImageMapper.insert(pi);
                }
            }
            return R.success(product);
        } catch (Exception e) {
            return R.error("更新商品失败");
        }
    }

    public R<Object> deleteProducts(java.util.List<Integer> ids) {
        try {
            for (Integer id : ids) {
                productMapper.deleteById(id);
                productImageMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.spboot.app.pojo.ProductImage>().eq("product_id", id));
            }
            return R.success("删除成功");
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    public R<Object> updateStock(Integer productId, Integer stock) {
        if (productId == null) return R.error("缺少 productId");
        Product p = productMapper.selectById(productId);
        if (p == null) return R.error("未找到商品");
        p.setStock(stock == null ? 0 : stock);
        productMapper.updateById(p);
        return R.success(p);
    }

    public R<java.util.List<ProductCategory>> listCategories() {
        return R.success(productCategoryMapper.selectList(null));
    }

    public R<Object> insertCategory(ProductCategory category, Map<String, Object> data) {
        try {
            category.setId(null);
            productCategoryMapper.insert(category);
            return R.success(category);
        } catch (Exception e) {
            return R.error("插入分类失败");
        }
    }

    public R<Object> updateCategory(ProductCategory category, Map<String, Object> data) {
        try {
            if (category.getId() == null) return R.error("缺少 id");
            productCategoryMapper.updateById(category);
            return R.success(category);
        } catch (Exception e) {
            return R.error("更新分类失败");
        }
    }

    public R<Object> deleteCategories(java.util.List<Integer> ids) {
        try {
            for (Integer id : ids) productCategoryMapper.deleteById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }
}
