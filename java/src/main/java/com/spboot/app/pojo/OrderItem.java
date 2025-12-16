package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("order_items")
public class OrderItem implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("order_id")
    private Integer orderId;
    @TableField("product_id")
    private Integer productId;
    private Integer quantity;
    @TableField("unit_price")
    private java.math.BigDecimal unitPrice;
    @TableField("created_at")
    private String createdAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId == null ? 0 : orderId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId == null ? 0 : productId; }
    public Integer getQuantity() { return quantity == null ? 0 : quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity == null ? 0 : quantity; }
    public java.math.BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(java.math.BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

