package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 私聊 表 siliao.
 */
@TableName("siliao")
public class Siliao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 编号.
    private String bianhao;
    // 收信人.
    private String shouxinren;
    // 发信人.
    private String faxinren;
    // 消息最后时间.
    private String addtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getXiaoxiCount() {
        return DB.name("xiaoxi").where("siliaoid", id).count();
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao == null ? "" : bianhao.trim();
    }

    public String getShouxinren() {
        return shouxinren;
    }

    public void setShouxinren(String shouxinren) {
        this.shouxinren = shouxinren == null ? "" : shouxinren.trim();
    }

    public String getFaxinren() {
        return faxinren;
    }

    public void setFaxinren(String faxinren) {
        this.faxinren = faxinren == null ? "" : faxinren.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }
}
