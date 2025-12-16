package com.spboot.app.service;

import com.spboot.app.mapper.CartMapper;
import com.spboot.app.mapper.ProductMapper;
import com.spboot.app.pojo.Cart;
import com.spboot.app.pojo.Product;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SessionFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    public R<Object> listCart() {
        try {
            String username = SessionFactory.getUsername();
            if (username == null || username.isEmpty()) return R.error("请先登录");
            List<Map<String, Object>> list = com.jntoo.db.DB.select("SELECT c.id, c.product_id, c.quantity, p.name, p.cover_url, p.price, p.stock FROM cart c LEFT JOIN product p ON p.id = c.product_id WHERE c.username='" + username + "' ORDER BY c.id DESC");
            Map<String, Object> res = new HashMap<>();
            res.put("records", list == null ? new java.util.ArrayList<>() : list);
            return R.success(res);
        } catch (Exception e) {
            return R.error("查询购物车失败");
        }
    }

    public R<Object> addToCart(Integer productId, Integer quantity) {
        try {
            String username = SessionFactory.getUsername();
            if (username == null || username.isEmpty()) return R.error("请先登录");
            if (productId == null || productId <= 0) return R.error("缺少 productId");
            if (quantity == null || quantity <= 0) quantity = 1;

            Product prod = productMapper.selectById(productId);
            if (prod == null) return R.error("商品不存在");
            if (prod.getStock() < quantity) return R.error("库存不足");

            Cart exist = cartMapper.selectOne(new QueryWrapper<Cart>().eq("username", username).eq("product_id", productId));
            if (exist != null) {
                exist.setQuantity(exist.getQuantity() + quantity);
                cartMapper.updateById(exist);
            } else {
                Cart c = new Cart();
                c.setProductId(productId);
                c.setQuantity(quantity);
                c.setUsername(username);
                cartMapper.insert(c);
            }
            return R.success("已加入购物车");
        } catch (Exception e) {
            return R.error("加入购物车失败");
        }
    }

    public R<Object> updateCart(Integer productId, Integer quantity) {
        try {
            String username = SessionFactory.getUsername();
            if (username == null || username.isEmpty()) return R.error("请先登录");
            if (productId == null || productId <= 0) return R.error("缺少 productId");
            if (quantity == null || quantity < 0) return R.error("数量不正确");
            if (quantity == 0) {
                cartMapper.delete(new QueryWrapper<Cart>().eq("username", username).eq("product_id", productId));
                return R.success("已移除");
            }
            Product prod = productMapper.selectById(productId);
            if (prod == null) return R.error("商品不存在");
            if (prod.getStock() < quantity) return R.error("库存不足");
            Cart exist = cartMapper.selectOne(new QueryWrapper<Cart>().eq("username", username).eq("product_id", productId));
            if (exist == null) return R.error("购物车项不存在");
            exist.setQuantity(quantity);
            cartMapper.updateById(exist);
            return R.success("更新成功");
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    public R<Object> remove(Integer productId) {
        try {
            String username = SessionFactory.getUsername();
            if (username == null || username.isEmpty()) return R.error("请先登录");
            if (productId == null || productId <= 0) return R.error("缺少 productId");
            cartMapper.delete(new QueryWrapper<Cart>().eq("username", username).eq("product_id", productId));
            return R.success("已移除");
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    public R<Object> clearCart() {
        try {
            String username = SessionFactory.getUsername();
            if (username == null || username.isEmpty()) return R.error("请先登录");
            cartMapper.delete(new QueryWrapper<Cart>().eq("username", username));
            return R.success("已清空购物车");
        } catch (Exception e) {
            return R.error("清空失败");
        }
    }
}
