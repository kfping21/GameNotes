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
                BigDecimal price = new BigDecimal(String.valueOf(it.get("price")));
                OrderItem oi = new OrderItem();
                oi.setOrderId(orderId);
                oi.setProductId(pid);
                oi.setQuantity(qty);
                oi.setUnitPrice(price);
                orderItemsMapper.insert(oi);

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
            // 事务回滚
            return R.error("创建订单失败");
        }
    }

    public R<Object> getOrderDetail(Integer orderId) {
        try {
            if (orderId == null || orderId <= 0) return R.error("缺少 orderId");
            Orders order = ordersMapper.selectById(orderId);
            if (order == null) return R.error("未找到订单");
            List<Map<String, Object>> items = com.jntoo.db.DB.select("SELECT oi.*, p.name, p.cover_url FROM order_items oi LEFT JOIN product p ON p.id=oi.product_id WHERE oi.order_id = " + orderId);
            Map<String, Object> data = new HashMap<>();
            data.put("order", order);
            data.put("items", items == null ? new java.util.ArrayList<>() : items);
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
}
