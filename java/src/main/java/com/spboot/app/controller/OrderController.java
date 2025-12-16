package com.spboot.app.controller;

import com.spboot.app.service.OrderService;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "订单控制器" })
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @ApiOperation(value = "从购物车创建订单（立即写入并扣库存）", httpMethod = "POST")
    @RequestMapping("/createFromCart")
    public R<Object> createFromCart() {
        return orderService.createOrderFromCart();
    }

    @ApiOperation(value = "获取订单详情", httpMethod = "GET")
    @RequestMapping("/detail")
    public R<Object> detail(@RequestParam Integer id) {
        return orderService.getOrderDetail(id);
    }

    @ApiOperation(value = "获取我的订单列表", httpMethod = "GET")
    @RequestMapping("/list")
    public R<Object> list() {
        return orderService.listOrders();
    }
}

