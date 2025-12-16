package com.spboot.app.controller;

import com.spboot.app.service.MallService;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = { "周边商城控制器" })
@RestController
@RequestMapping("/api/mall")
public class MallController {

    @Resource
    private MallService mallService;

    @ApiOperation(value = "获取商品列表（可按分类/游戏/关键词过滤）", httpMethod = "GET")
    @RequestMapping("/products")
    public R<Object> listProducts(@RequestParam(required = false) Integer gameId,
                                  @RequestParam(required = false) Integer categoryId,
                                  @RequestParam(required = false) String keyword,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "20") Integer size) {
        return mallService.listProducts(gameId, categoryId, keyword, page, size);
    }

    @ApiOperation(value = "根据id获取商品详情", httpMethod = "GET")
    @RequestMapping("/products/{id}")
    public R<Object> productDetail(@PathVariable Integer id) {
        return mallService.getProductDetail(id);
    }
}

