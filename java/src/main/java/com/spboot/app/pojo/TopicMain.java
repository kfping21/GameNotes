package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("topic_main")
public class TopicMain implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String intro;
    private String tagIds;
    private Integer createdBy;
    private String status;
    private String createdAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title == null ? "" : title.trim(); }
    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro == null ? "" : intro.trim(); }
    public String getTagIds() { return tagIds; }
    public void setTagIds(String tagIds) { this.tagIds = tagIds == null ? "" : tagIds.trim(); }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy == null ? 0 : createdBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status == null ? "" : status.trim(); }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt == null ? "" : createdAt.trim(); }
}

