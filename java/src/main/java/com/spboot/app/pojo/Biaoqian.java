package com.spboot.app.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jntoo.db.DB;
import java.io.Serializable;
import java.util.*;

/**
 * 标签 表 biaoqian.
 */
@TableName("biaoqian")
public class Biaoqian implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    // 标签名称.
    private String biaoqianmingcheng;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBiaoqianmingcheng() {
        return biaoqianmingcheng;
    }

    public void setBiaoqianmingcheng(String biaoqianmingcheng) {
        this.biaoqianmingcheng = biaoqianmingcheng == null ? "" : biaoqianmingcheng.trim();
    }
}
