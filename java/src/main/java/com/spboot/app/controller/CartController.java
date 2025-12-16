package com.spboot.app.controller;

import com.spboot.app.service.CartService;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "购物车控制器" })
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @ApiOperation(value = "获取购物车列表", httpMethod = "GET")
    @RequestMapping("/list")
    public R<Object> list() {
        return cartService.listCart();
    }

    @ApiOperation(value = "加入购物车", httpMethod = "POST")
    @RequestMapping("/add")
    public R<Object> add(@RequestBody Map<String, Object> data) {
        Integer productId = data.get("productId") == null ? null : Integer.parseInt(String.valueOf(data.get("productId")));
        Integer quantity = data.get("quantity") == null ? 1 : Integer.parseInt(String.valueOf(data.get("quantity")));
        return cartService.addToCart(productId, quantity);
    }

    @ApiOperation(value = "更新购物车数量", httpMethod = "POST")
    @RequestMapping("/update")
    public R<Object> update(@RequestBody Map<String, Object> data) {
        Integer productId = data.get("productId") == null ? null : Integer.parseInt(String.valueOf(data.get("productId")));
        Integer quantity = data.get("quantity") == null ? null : Integer.parseInt(String.valueOf(data.get("quantity")));
        return cartService.updateCart(productId, quantity);
    }

    @ApiOperation(value = "删除购物车项", httpMethod = "POST")
    @RequestMapping("/remove")
    public R<Object> remove(@RequestBody Map<String, Object> data) {
        Integer productId = data.get("productId") == null ? null : Integer.parseInt(String.valueOf(data.get("productId")));
        return cartService.remove(productId);
    }

    @ApiOperation(value = "清空购物车", httpMethod = "POST")
    @RequestMapping("/clear")
    public R<Object> clear() {
        return cartService.clearCart();
    }
}

