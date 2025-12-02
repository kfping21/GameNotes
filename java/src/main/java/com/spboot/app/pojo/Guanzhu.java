package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 关注 表 guanzhu.
 */
@TableName("guanzhu")
public class Guanzhu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 笔记id.
    private Integer bijiid;
    // 笔记编号.
    private String bijibianhao;
    // 笔记名称.
    private String bijimingcheng;
    // 标签.
    private String biaoqian;
    // 关联游戏.
    private Integer guanlianyouxi;
    // 添加人.
    private String tianjiaren;
    // 关注人.
    private String guanzhuren;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBijiid() {
        return bijiid;
    }

    public void setBijiid(Integer bijiid) {
        this.bijiid = bijiid == null ? 0 : bijiid;
    }

    public String getBijibianhao() {
        return bijibianhao;
    }

    public void setBijibianhao(String bijibianhao) {
        this.bijibianhao = bijibianhao == null ? "" : bijibianhao.trim();
    }

    public String getBijimingcheng() {
        return bijimingcheng;
    }

    public void setBijimingcheng(String bijimingcheng) {
        this.bijimingcheng = bijimingcheng == null ? "" : bijimingcheng.trim();
    }

    public String getBiaoqian() {
        return biaoqian;
    }

    public void setBiaoqian(String biaoqian) {
        this.biaoqian = biaoqian == null ? "" : biaoqian.trim();
    }

    public Integer getGuanlianyouxi() {
        return guanlianyouxi;
    }

    public void setGuanlianyouxi(Integer guanlianyouxi) {
        this.guanlianyouxi = guanlianyouxi == null ? 0 : guanlianyouxi;
    }

    public String getTianjiaren() {
        return tianjiaren;
    }

    public void setTianjiaren(String tianjiaren) {
        this.tianjiaren = tianjiaren == null ? "" : tianjiaren.trim();
    }

    public String getGuanzhuren() {
        return guanzhuren;
    }

    public void setGuanzhuren(String guanzhuren) {
        this.guanzhuren = guanzhuren == null ? "" : guanzhuren.trim();
    }
}
