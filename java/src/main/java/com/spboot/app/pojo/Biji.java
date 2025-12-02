package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 笔记 表 biji.
 */
@TableName("biji")
public class Biji implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 笔记编号.
    private String bijibianhao;
    // 笔记名称.
    private String bijimingcheng;
    // 笔记封面.
    private String bijifengmian;
    // 标签.
    private String biaoqian;
    // 关联游戏.
    private Integer guanlianyouxi;
    // 视频.
    private String shipin;
    // 关注量.
    private Integer guanzhuliang;
    // 种草度.
    private Integer zhongcaodu;
    // 详情.
    private String xiangqing;
    // 添加人.
    private String tianjiaren;
    // 是否审核
    private String issh;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getZhongcaoCount() {
        return DB.name("zhongcao").where("bijiid", id).count();
    }

    public Long getSixinCount() {
        return DB.name("sixin").where("bijiid", id).count();
    }

    public Long getGuanzhuCount() {
        return DB.name("guanzhu").where("bijiid", id).count();
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

    public String getBijifengmian() {
        return bijifengmian;
    }

    public void setBijifengmian(String bijifengmian) {
        this.bijifengmian = bijifengmian == null ? "" : bijifengmian.trim();
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

    public String getShipin() {
        return shipin;
    }

    public void setShipin(String shipin) {
        this.shipin = shipin == null ? "" : shipin.trim();
    }

    public Integer getGuanzhuliang() {
        return guanzhuliang;
    }

    public void setGuanzhuliang(Integer guanzhuliang) {
        this.guanzhuliang = guanzhuliang == null ? 0 : guanzhuliang;
    }

    public Integer getZhongcaodu() {
        return zhongcaodu;
    }

    public void setZhongcaodu(Integer zhongcaodu) {
        this.zhongcaodu = zhongcaodu == null ? 0 : zhongcaodu;
    }

    public String getXiangqing() {
        return xiangqing;
    }

    public void setXiangqing(String xiangqing) {
        this.xiangqing = xiangqing == null ? "" : xiangqing.trim();
    }

    public String getTianjiaren() {
        return tianjiaren;
    }

    public void setTianjiaren(String tianjiaren) {
        this.tianjiaren = tianjiaren == null ? "" : tianjiaren.trim();
    }

    public String getIssh() {
        return issh;
    }

    public void setIssh(String issh) {
        this.issh = issh == null ? "" : issh.trim();
    }
}
