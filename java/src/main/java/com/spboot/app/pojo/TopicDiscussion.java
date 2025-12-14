package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

@TableName("topic_discussion")
public class TopicDiscussion implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer topicId;
    private String content;
    private Integer createdBy;
    private String status;
    private String createdAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getTopicId() { return topicId; }
    public void setTopicId(Integer topicId) { this.topicId = topicId == null ? 0 : topicId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content == null ? "" : content.trim(); }
    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy == null ? 0 : createdBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status == null ? "" : status.trim(); }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt == null ? "" : createdAt.trim(); }
}

