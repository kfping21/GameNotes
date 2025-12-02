package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 私信 表 sixin.
 */
@TableName("sixin")
public class Sixin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 笔记id.
    private Integer bijiid;
    // 私信内容.
    private String sixinneirong;
    // 笔记编号.
    private String bijibianhao;
    // 笔记名称.
    private String bijimingcheng;
    // 添加人.
    private String tianjiaren;
    // 图片.
    private String tupian;
    // 私信人.
    private String sixinren;
    // 私信状态.
    // 可选项:未读,已阅。
    private String sixinzhuangtai;
    // 发送时间.
    private String addtime;
    // 私信回复.
    private String sixinhuifu;
    // 回复时间.
    private String huifushijian;

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

    public String getSixinneirong() {
        return sixinneirong;
    }

    public void setSixinneirong(String sixinneirong) {
        this.sixinneirong = sixinneirong == null ? "" : sixinneirong.trim();
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

    public String getTianjiaren() {
        return tianjiaren;
    }

    public void setTianjiaren(String tianjiaren) {
        this.tianjiaren = tianjiaren == null ? "" : tianjiaren.trim();
    }

    public String getTupian() {
        return tupian;
    }

    public void setTupian(String tupian) {
        this.tupian = tupian == null ? "" : tupian.trim();
    }

    public String getSixinren() {
        return sixinren;
    }

    public void setSixinren(String sixinren) {
        this.sixinren = sixinren == null ? "" : sixinren.trim();
    }

    public String getSixinzhuangtai() {
        return sixinzhuangtai;
    }

    public void setSixinzhuangtai(String sixinzhuangtai) {
        this.sixinzhuangtai = sixinzhuangtai == null ? "" : sixinzhuangtai.trim();
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime == null ? "" : addtime.trim();
    }

    public String getSixinhuifu() {
        return sixinhuifu;
    }

    public void setSixinhuifu(String sixinhuifu) {
        this.sixinhuifu = sixinhuifu == null ? "" : sixinhuifu.trim();
    }

    public String getHuifushijian() {
        return huifushijian;
    }

    public void setHuifushijian(String huifushijian) {
        this.huifushijian = huifushijian == null ? "" : huifushijian.trim();
    }
}
