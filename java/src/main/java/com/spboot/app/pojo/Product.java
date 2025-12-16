package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("product")
public class Product implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("game_id")
    private Integer gameId;
    @TableField("category_id")
    private Integer categoryId;
    private String name;
    @TableField("cover_url")
    private String coverUrl;
    private java.math.BigDecimal price;
    private Integer stock;
    private String tags;
    private String intro;
    @TableField("created_at")
    private String createdAt;

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getGameId() { return gameId; }
    public void setGameId(Integer gameId) { this.gameId = gameId; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name == null ? "" : name.trim(); }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl == null ? "" : coverUrl.trim(); }
    public java.math.BigDecimal getPrice() { return price; }
    public void setPrice(java.math.BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock == null ? 0 : stock; }
    public void setStock(Integer stock) { this.stock = stock == null ? 0 : stock; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags == null ? "" : tags.trim(); }
    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro == null ? "" : intro; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

