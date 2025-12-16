package com.spboot.app.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jntoo.db.DB;
import com.jntoo.db.utils.Convert;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.mapper.YouxiMapper;
import com.spboot.app.pojo.Youxi;
import com.spboot.app.utils.*;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class YouxiService {

    // 获取数据库操作类mapper
    @Resource
    private YouxiMapper mapper;

    /**
     *   根据Youximingcheng字段参数获取一行数据
     */
    public Youxi findByYouximingcheng(String username) {
        // 新建游戏模块实体类Youxi
        Youxi pojo = new Youxi();
        // 设置参数
        pojo.setYouximingcheng(username);
        // 根据实体类新建QueryWrapper查询条件类
        QueryWrapper<Youxi> queryWrapper = Wrappers.query(pojo);
        Youxi row = mapper.selectOne(queryWrapper);
        return row;
    }

    /**
     *   根据Youximingcheng字段参数获取一行数据，并不包含某uid 参数得行
     */
    public Youxi findByYouximingcheng(String username, Integer uid) {
        // 新建游戏模块实体类Youxi
        Youxi pojo = new Youxi();
        // 设置参数
        pojo.setYouximingcheng(username);
        // 根据实体类新建QueryWrapper查询条件类
        QueryWrapper<Youxi> queryWrapper = Wrappers.query(pojo);
        // 设置参数 id != uid变量
        queryWrapper.ne("id", uid);
        // 根据queryWrapper 查询
        Youxi row = mapper.selectOne(queryWrapper);
        return row;
    }

    /**
     *  根据id 获取一行数据（增强：返回游戏详情 + 关联笔记 + 装备列表 + 人物列表）
     */
    public R<Object> findDetailById(Integer id) {
        Youxi youxi = mapper.selectById(id);
        if (youxi == null) return R.error("未找到游戏");
        Map<String, Object> res = new HashMap<>();
        // 将 youxi POJO 转为 Map，并同时保留原有字段名，向前端兼容新增 intro 字段（来自 xiangqing）
        Map<String, Object> youxiMap = cn.hutool.core.bean.BeanUtil.beanToMap(youxi);
        // 将 xiangqing 也以 intro 的键名返回，前端使用 youxi.intro
        youxiMap.put("intro", youxi.getXiangqing());
        res.put("youxi", youxiMap);
        try {
            // 关联笔记（通过 game_note_map）
            String sqlNotes = "SELECT b.* FROM game_note_map m LEFT JOIN biji b ON b.id = m.note_id WHERE m.game_id = " + id + " ORDER BY m.id DESC";
            java.util.List<java.util.Map<String, Object>> notes = DB.select(sqlNotes);
            // 为每条笔记追加 url 字段，便于前端跳转
            if (notes != null) {
                for (java.util.Map<String, Object> r : notes) {
                    Object idObj = r.get("id");
                    r.put("url", idObj == null ? "" : "/note/" + idObj);
                }
            }
            res.put("notes", notes == null ? new java.util.ArrayList<>() : notes);
        } catch (Exception e) {
            res.put("notes", new java.util.ArrayList<>());
        }
        try {
            // 装备
            String sqlEquip = "SELECT * FROM game_equipment WHERE game_id = " + id + " ORDER BY id DESC";
            java.util.List<java.util.Map<String, Object>> equips = DB.select(sqlEquip);
            res.put("equipment", equips == null ? new java.util.ArrayList<>() : equips);
        } catch (Exception e) {
            res.put("equipment", new java.util.ArrayList<>());
        }
        try {
            // 人物
            String sqlChars = "SELECT * FROM game_character WHERE game_id = " + id + " ORDER BY id DESC";
            java.util.List<java.util.Map<String, Object>> chars = DB.select(sqlChars);
            res.put("characters", chars == null ? new java.util.ArrayList<>() : chars);
        } catch (Exception e) {
            res.put("characters", new java.util.ArrayList<>());
        }
        // 周边入口（前端可根据该 URL 跳转到商品列表并带上 gameId）
        res.put("peripheralsUrl", "/mall/products?gameId=" + id);
        return R.success(res);
    }

    /**
     *  根据Wrapper 对象进行数据筛选
     */
    public R<List<Youxi>> selectAll(Wrapper<Youxi> query) {
        return R.success(mapper.selectList(query));
    }

    /**
     *  直接筛选所有数据
     */
    public R<List<Youxi>> selectAll() {
        QueryWrapper<Youxi> wrapper = Wrappers.query();
        wrapper.orderByDesc("id");
        return selectAll(wrapper);
    }

    /**
     *  根据map 条件筛选数据
     *
     */
    public R selectAll(Map<String, Object> map) {
        // 获取筛选数据
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        // 将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象，执行动态查询
        QueryWrapper<Youxi> wrapper = mapToWrapper(map);
        // 设置排序
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        return selectAll(wrapper);
    }

    /**
     *  根据map 条件筛选数据并分页
     *
     */
    public R selectPages(Map<String, Object> map) {
        // 获取筛选数据
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        // 将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象，执行动态查询
        QueryWrapper<Youxi> wrapper = mapToWrapper(map);
        // 设置排序
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        // 设置分页数据
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *   将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象
     */
    public QueryWrapper<Youxi> mapToWrapper(Map<String, Object> map) {
        // 创建 QueryWrapper 对象
        QueryWrapper<Youxi> wrapper = Wrappers.query();

        String where = " 1=1 ";
        // 搜索关键字：同时匹配游戏名称和详情
        if (!StringUtil.isNullOrEmpty(map.get("keyword"))) {
            wrapper.apply(" ( youximingcheng LIKE '%" + map.get("keyword") + "%' OR xiangqing LIKE '%" + map.get("keyword") + "%' ) ");
        }
        // 名称搜索（兼容旧参数）
        if (!StringUtil.isNullOrEmpty(map.get("youximingcheng"))) {
            wrapper.like("youximingcheng", map.get("youximingcheng"));
        }
        // 按分类过滤
        if (map.containsKey("categoryId") && map.get("categoryId") != null) {
            try { wrapper.eq("game_category_id", Integer.parseInt(String.valueOf(map.get("categoryId")))); } catch (Exception e) { }
        }

        if (map.containsKey("session_name")) {
            wrapper.eq(map.get("session_name").toString(), SessionFactory.getUsername());
        }

        wrapper.apply(where);
        return wrapper;
    }

    public R selectPages(QueryWrapper<Youxi> wrapper, IPage page) {
        Map result = new HashMap();
        result.put("lists", mapper.selectPage(page, wrapper));

        return R.success(result);
    }

    /**
     * 获取游戏分类列表（只读）
     * 前端用于展示游戏分类导航/筛选
     */
    public R<List<Map<String, Object>>> getCategories() {
        try {
            java.util.List<java.util.Map<String, Object>> cats = DB.select("SELECT * FROM game_category ORDER BY sort ASC");
            if (cats == null) cats = new java.util.ArrayList<>();
            return R.success(cats);
        } catch (Exception e) {
            return R.success(new java.util.ArrayList<>());
        }
    }

    /**
     * 插入用户数据
     * @param entityData 插入的对象
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R insert(Youxi entityData, Map post) {
        // 判断是否有填写游戏名称。
        if (StringUtil.isNullOrEmpty(post.get("youximingcheng"))) {
            return R.error("请填写游戏名称");
        }
        // 判断是否有填写游戏名称,有则判断是否在数据中已经存在，存在则报错。
        if (findByYouximingcheng(entityData.getYouximingcheng()) != null) {
            return R.error("游戏名称已经存在");
        }
        // 判断是否有填写游戏图片。
        if (StringUtil.isNullOrEmpty(post.get("youxitupian"))) {
            return R.error("请填写游戏图片");
        }
        // 处理 categoryId
        if (post.containsKey("categoryId")) {
            try { entityData.setCategoryId(Integer.parseInt(String.valueOf(post.get("categoryId")))); } catch (Exception e) { entityData.setCategoryId(0); }
        }

        Info.handlerNullEntity(entityData);

        entityData.setId(null);
        mapper.insert(entityData);
        if (entityData.getId() != null) {
            return findDetailById(entityData.getId());
        } else {
            return R.error("插入错误");
        }
    }

    /**
     * 根据id进行更新游戏数据
     * @param entityData 更新的数据
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R<Object> update(Youxi entityData, Map post) {
        // 判断是否有填写游戏名称。
        if (StringUtil.isNullOrEmpty(post.get("youximingcheng"))) {
            return R.error("请填写游戏名称");
        }
        // 判断是否有填写游戏名称,有则判断是否在数据中已经存在，存在则报错。
        if (findByYouximingcheng(entityData.getYouximingcheng(), entityData.getId()) != null) {
            return R.error("游戏名称已经存在");
        }
        // 判断是否有填写游戏图片。
        if (StringUtil.isNullOrEmpty(post.get("youxitupian"))) {
            return R.error("请填写游戏图片");
        }
        // 处理 categoryId
        if (post.containsKey("categoryId")) {
            try { entityData.setCategoryId(Integer.parseInt(String.valueOf(post.get("categoryId")))); } catch (Exception e) { entityData.setCategoryId(0); }
        }

        mapper.updateById(entityData);

        return findDetailById(entityData.getId());
    }

    /**
     * 根据 id列表 删除
     * @param ids  id 列表值
     * @return 是否成功
     */
    public R<Object> delete(List<Integer> ids) {
        try {
            for (Integer id : ids) {
                delete(id);
            }
            return R.success("操作成功");
        } catch (Exception e) {
            return R.error("操作失败");
        }
    }

    /**
     * 根据 id 删除
     * @param id  id 列表值
     * @return 是否成功
     */
    public R<Object> delete(Integer id) {
        try {
            mapper.deleteById(id);

            return R.success("操作成功");
        } catch (Exception e) {
            return R.error("操作失败");
        }
    }
}
