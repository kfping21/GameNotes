package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("cart")
public class Cart implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("product_id")
    private Integer productId;
    private Integer quantity;
    private String username;
    @TableField("added_at")
    private String addedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId == null ? 0 : productId; }
    public Integer getQuantity() { return quantity == null ? 0 : quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity == null ? 0 : quantity; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username == null ? "" : username; }
    public String getAddedAt() { return addedAt; }
    public void setAddedAt(String addedAt) { this.addedAt = addedAt; }
}

