package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("product_category")
public class ProductCategory implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    @TableField("parent_id")
    private Integer parentId;
    private Integer sort;
    @TableField("created_at")
    private String createdAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name == null ? "" : name.trim(); }
    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId == null ? 0 : parentId; }
    public Integer getSort() { return sort == null ? 0 : sort; }
    public void setSort(Integer sort) { this.sort = sort == null ? 0 : sort; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

