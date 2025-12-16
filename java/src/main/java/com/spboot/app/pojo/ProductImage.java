package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("product_image")
public class ProductImage implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("product_id")
    private Integer productId;
    private String url;
    private Integer sort;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId == null ? 0 : productId; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url == null ? "" : url.trim(); }
    public Integer getSort() { return sort == null ? 0 : sort; }
    public void setSort(Integer sort) { this.sort = sort == null ? 0 : sort; }
}

