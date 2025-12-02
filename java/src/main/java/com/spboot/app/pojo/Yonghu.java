package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 用户 表 yonghu.
 */
@TableName("yonghu")
public class Yonghu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 账号.
    private String zhanghao;

    // 密码.
    @JsonIgnore // 这个字段不返回前端
    private String mima;

    // 名称.
    private String mingcheng;
    // 性别.
    // 可选项:男,女。
    private String xingbie;
    // 出生日期.
    private String chushengriqi;
    // 手机.
    private String shouji;
    // 邮箱.
    private String youxiang;
    // 个性签名.
    private String gexingqianming;
    // 标签.
    private String biaoqian;
    // 我在玩.
    private String wozaiwan;
    // 头像.
    private String touxiang;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZhanghao() {
        return zhanghao;
    }

    public void setZhanghao(String zhanghao) {
        this.zhanghao = zhanghao == null ? "" : zhanghao.trim();
    }

    public String getMima() {
        return mima;
    }

    public void setMima(String mima) {
        this.mima = mima == null ? "" : mima.trim();
    }

    public String getMingcheng() {
        return mingcheng;
    }

    public void setMingcheng(String mingcheng) {
        this.mingcheng = mingcheng == null ? "" : mingcheng.trim();
    }

    public String getXingbie() {
        return xingbie;
    }

    public void setXingbie(String xingbie) {
        this.xingbie = xingbie == null ? "" : xingbie.trim();
    }

    public String getChushengriqi() {
        return chushengriqi;
    }

    public void setChushengriqi(String chushengriqi) {
        this.chushengriqi = chushengriqi == null ? "" : chushengriqi.trim();
    }

    public String getShouji() {
        return shouji;
    }

    public void setShouji(String shouji) {
        this.shouji = shouji == null ? "" : shouji.trim();
    }

    public String getYouxiang() {
        return youxiang;
    }

    public void setYouxiang(String youxiang) {
        this.youxiang = youxiang == null ? "" : youxiang.trim();
    }

    public String getGexingqianming() {
        return gexingqianming;
    }

    public void setGexingqianming(String gexingqianming) {
        this.gexingqianming = gexingqianming == null ? "" : gexingqianming.trim();
    }

    public String getBiaoqian() {
        return biaoqian;
    }

    public void setBiaoqian(String biaoqian) {
        this.biaoqian = biaoqian == null ? "" : biaoqian.trim();
    }

    public String getWozaiwan() {
        return wozaiwan;
    }

    public void setWozaiwan(String wozaiwan) {
        this.wozaiwan = wozaiwan == null ? "" : wozaiwan.trim();
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang == null ? "" : touxiang.trim();
    }
}
