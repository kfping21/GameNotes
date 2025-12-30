package com.spboot.app.service;

import com.spboot.app.mapper.CartMapper;
import com.spboot.app.mapper.OrderItemsMapper;
import com.spboot.app.mapper.OrdersMapper;
import com.spboot.app.mapper.ProductMapper;
import com.spboot.app.pojo.Cart;
import com.spboot.app.pojo.OrderItem;
import com.spboot.app.pojo.Orders;
import com.spboot.app.pojo.Product;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SessionFactory;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class OrderService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    @Transactional
    public R<Object> createOrderFromCart() {
        try {
            String username = SessionFactory.getUsername();
            if (username == null || username.isEmpty()) return R.error("请先登录");

            List<Map<String, Object>> cart = com.jntoo.db.DB.select("SELECT c.product_id, c.quantity, p.price FROM cart c LEFT JOIN product p ON p.id=c.product_id WHERE c.username='" + username + "'");
            if (cart == null || cart.isEmpty()) return R.error("购物车为空");

            BigDecimal total = BigDecimal.ZERO;
            for (Map<String, Object> it : cart) {
                int pid = Integer.parseInt(String.valueOf(it.get("product_id")));
                int qty = Integer.parseInt(String.valueOf(it.get("quantity")));
                BigDecimal price = new BigDecimal(String.valueOf(it.get("price")));
                Product prod = productMapper.selectById(pid);
                int stock = prod == null ? 0 : prod.getStock();
                if (stock < qty) return R.error("商品库存不足: " + pid);
                total = total.add(price.multiply(new BigDecimal(qty)));
            }

            Orders order = new Orders();
            order.setUser(username);
            order.setTotalAmount(total);
            order.setStatus("paid");
            ordersMapper.insert(order);
            Integer orderId = order.getId();

            for (Map<String, Object> it : cart) {
                int pid = Integer.parseInt(String.valueOf(it.get("product_id")));
                int qty = Integer.parseInt(String.valueOf(it.get("quantity")));
                // 不改数据库结构：不写入 order_items 明细表
                // 若需要订单明细持久化，请在数据库增加 order_items 表或改为其它持久化方案

                // 原子扣库存：stock = stock - qty WHERE id = pid AND stock >= qty
                UpdateWrapper<Product> uw = new UpdateWrapper<>();
                uw.setSql("stock = stock - " + qty).eq("id", pid).ge("stock", qty);
                int affected = productMapper.update(null, uw);
                if (affected <= 0) {
                    // 库存不足或并发导致无法扣库存，抛出异常以触发事务回滚
                    throw new RuntimeException("商品库存不足或已被抢购: " + pid);
                }
            }

            // 清空购物车
            cartMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Cart>().eq("username", username));

            Map<String, Object> ret = new HashMap<>();
            ret.put("orderId", orderId);
            return R.success(ret);
        } catch (Exception e) {
            // 标记事务回滚：当前方法捕获异常会导致默认不回滚
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (Exception ignore) {
            }
            return R.error("创建订单失败");
        }
    }

    public R<Object> getOrderDetail(Integer orderId) {
        try {
            if (orderId == null || orderId <= 0) return R.error("缺少 orderId");
            Orders order = ordersMapper.selectById(orderId);
            if (order == null) return R.error("未找到订单");
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            // 不改数据库结构：未启用 order_items 明细表，返回空商品清单
            data.put("items", new java.util.ArrayList<>());
            return R.success(data);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    public R<Object> listOrders() {
        try {
            String username = SessionFactory.getUsername();
            if (username == null || username.isEmpty()) return R.error("请先登录");
            List<Orders> list = ordersMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Orders>().eq("user", username).orderByDesc("id"));
            return R.success(list == null ? new java.util.ArrayList<>() : list);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    // --- Admin functions ---
    public R<Object> adminListOrders(Map<String, Object> req) {
        try {
            // 权限检查（简单）
            String roles = SessionFactory.getRoles();
            if (roles == null || !roles.equals("管理员")) return R.error("权限不足");

            int page = req.get("page") == null ? 1 : Integer.parseInt(String.valueOf(req.get("page")));
            int size = req.get("size") == null ? 20 : Integer.parseInt(String.valueOf(req.get("size")));
            String status = req.get("status") == null ? null : String.valueOf(req.get("status"));
            String orderNo = req.get("orderNo") == null ? null : String.valueOf(req.get("orderNo"));

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Orders> qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            if (status != null && !status.isEmpty()) qw.eq("status", status);
            if (orderNo != null && !orderNo.isEmpty()) qw.like("id", orderNo); // 假设 orderNo 使用 id 或有单号字段
            qw.orderByDesc("id");

            com.baomidou.mybatisplus.extension.plugins.pagination.Page<Orders> p = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<Orders> res = ordersMapper.selectPage(p, qw);

            Map<String, Object> out = new HashMap<>();
            out.put("total", res.getTotal());
            out.put("page", res.getCurrent());
            out.put("size", res.getSize());
            out.put("records", res.getRecords());
            return R.success(out);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    public R<Object> adminUpdateStatus(Map<String, Object> data) {
        try {
            String roles = SessionFactory.getRoles();
            if (roles == null || !roles.equals("管理员")) return R.error("权限不足");
            Integer orderId = data.get("orderId") == null ? null : Integer.parseInt(String.valueOf(data.get("orderId")));
            String status = data.get("status") == null ? null : String.valueOf(data.get("status"));
            String trackingNo = data.get("trackingNo") == null ? null : String.valueOf(data.get("trackingNo"));
            if (orderId == null || status == null) return R.error("缺少参数");
            Orders o = ordersMapper.selectById(orderId);
            if (o == null) return R.error("订单不存在");
            o.setStatus(status);
            // 如果 Orders 有 tracking 字段，可设置，否则忽略
            try { java.lang.reflect.Field f = Orders.class.getDeclaredField("trackingNo"); if (f != null && trackingNo!=null) o.getClass().getMethod("setTrackingNo", String.class).invoke(o, trackingNo); } catch (Exception ex) { }
            ordersMapper.updateById(o);
            return R.ok();
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

}
