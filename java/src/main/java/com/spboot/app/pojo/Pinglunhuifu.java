package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 评论回复 表 pinglunhuifu.
 */
@TableName("pinglunhuifu")
public class Pinglunhuifu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 评论id.
    private Integer pinglunid;
    // 表.
    private String biao;
    // 表id.
    private Integer biaoid;
    // 标题.
    private String biaoti;
    // 评论内容.
    private String pinglunneirong;
    // 评论人.
    private String pinglunren;
    // 回复内容.
    private String huifuneirong;
    // 回复人.
    private String huifuren;
    // 回复时间.
    private String addtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPinglunid() {
        return pinglunid;
    }

    public void setPinglunid(Integer pinglunid) {
        this.pinglunid = pinglunid == null ? 0 : pinglunid;
    }

    public String getBiao() {
        return biao;
    }

    public void setBiao(String biao) {
        this.biao = biao == null ? "" : biao.trim();
    }

    public Integer getBiaoid() {
        return biaoid;
    }

    public void setBiaoid(Integer biaoid) {
        this.biaoid = biaoid == null ? 0 : biaoid;
    }

    public String getBiaoti() {
        return biaoti;
    }

    public void setBiaoti(String biaoti) {
        this.biaoti = biaoti == null ? "" : biaoti.trim();
    }

    public String getPinglunneirong() {
        return pinglunneirong;
    }

    public void setPinglunneirong(String pinglunneirong) {
        this.pinglunneirong = pinglunneirong == null ? "" : pinglunneirong.trim();
    }

    public String getPinglunren() {
        return pinglunren;
    }

    public void setPinglunren(String pinglunren) {
        this.pinglunren = pinglunren == null ? "" : pinglunren.trim();
    }

    public String getHuifuneirong() {
        return huifuneirong;
    }

    public void setHuifuneirong(String huifuneirong) {
        this.huifuneirong = huifuneirong == null ? "" : huifuneirong.trim();
    }

    public String getHuifuren() {
        return huifuren;
    }

    public void setHuifuren(String huifuren) {
        this.huifuren = huifuren == null ? "" : huifuren.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }
}
