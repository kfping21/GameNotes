package com.spboot.app.controller;

import com.spboot.app.service.MallService;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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

    // --- Compatibility aliases for frontend expected paths ---

    @ApiOperation(value = "兼容：插入商品（旧路径）", httpMethod = "POST")
    @RequestMapping("/product/add")
    public R<Object> aliasInsertProduct(@RequestBody Map<String, Object> data) {
        com.spboot.app.pojo.Product p = com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSONObject.toJSONString(data), com.spboot.app.pojo.Product.class);
        return mallService.insertProduct(p, data);
    }

    @ApiOperation(value = "兼容：更新商品（旧路径）", httpMethod = "POST")
    @RequestMapping("/product/update")
    public R<Object> aliasUpdateProduct(@RequestBody Map<String, Object> data) {
        com.spboot.app.pojo.Product p = com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSONObject.toJSONString(data), com.spboot.app.pojo.Product.class);
        return mallService.updateProduct(p, data);
    }

    @ApiOperation(value = "兼容：删除商品（旧路径）", httpMethod = "POST")
    @RequestMapping("/product/delete")
    public R<Object> aliasDeleteProduct(@RequestBody java.util.List<Integer> ids) {
        return mallService.deleteProducts(ids);
    }

    @ApiOperation(value = "兼容：分类列表（旧路径）", httpMethod = "GET")
    @RequestMapping("/category/list")
    public R<java.util.List<com.spboot.app.pojo.ProductCategory>> aliasListCategories() {
        return mallService.listCategories();
    }

    @ApiOperation(value = "兼容：新增分类（旧路径）", httpMethod = "POST")
    @RequestMapping("/category/add")
    public R<Object> aliasInsertCategory(@RequestBody Map<String, Object> data) {
        com.spboot.app.pojo.ProductCategory c = com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSONObject.toJSONString(data), com.spboot.app.pojo.ProductCategory.class);
        return mallService.insertCategory(c, data);
    }

    @ApiOperation(value = "兼容：更新分类（旧路径）", httpMethod = "POST")
    @RequestMapping("/category/update")
    public R<Object> aliasUpdateCategory(@RequestBody Map<String, Object> data) {
        com.spboot.app.pojo.ProductCategory c = com.alibaba.fastjson.JSONObject.parseObject(com.alibaba.fastjson.JSONObject.toJSONString(data), com.spboot.app.pojo.ProductCategory.class);
        return mallService.updateCategory(c, data);
    }

    @ApiOperation(value = "兼容：删除分类（旧路径）", httpMethod = "POST")
    @RequestMapping("/category/delete")
    public R<Object> aliasDeleteCategories(@RequestBody java.util.List<Integer> ids) {
        return mallService.deleteCategories(ids);
    }

}
