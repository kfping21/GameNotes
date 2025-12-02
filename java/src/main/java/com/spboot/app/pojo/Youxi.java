package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 游戏 表 youxi.
 */
@TableName("youxi")
public class Youxi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 游戏名称.
    private String youximingcheng;
    // 关联笔记.
    private Integer guanlianbiji;
    // 游戏图片.
    private String youxitupian;
    // 装备库.
    private String zhuangbeiku;
    // 详情.
    private String xiangqing;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYouximingcheng() {
        return youximingcheng;
    }

    public void setYouximingcheng(String youximingcheng) {
        this.youximingcheng = youximingcheng == null ? "" : youximingcheng.trim();
    }

    public Integer getGuanlianbiji() {
        return guanlianbiji;
    }

    public void setGuanlianbiji(Integer guanlianbiji) {
        this.guanlianbiji = guanlianbiji == null ? 0 : guanlianbiji;
    }

    public String getYouxitupian() {
        return youxitupian;
    }

    public void setYouxitupian(String youxitupian) {
        this.youxitupian = youxitupian == null ? "" : youxitupian.trim();
    }

    public String getZhuangbeiku() {
        return zhuangbeiku;
    }

    public void setZhuangbeiku(String zhuangbeiku) {
        this.zhuangbeiku = zhuangbeiku == null ? "" : zhuangbeiku.trim();
    }

    public String getXiangqing() {
        return xiangqing;
    }

    public void setXiangqing(String xiangqing) {
        this.xiangqing = xiangqing == null ? "" : xiangqing.trim();
    }
}
