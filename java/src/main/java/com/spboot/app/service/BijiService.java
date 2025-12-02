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
import com.spboot.app.mapper.BijiMapper;
import com.spboot.app.pojo.Biji;
import com.spboot.app.utils.*;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class BijiService {

    // 获取数据库操作类mapper
    @Resource
    private BijiMapper mapper;

    /**
     *   根据Bijimingcheng字段参数获取一行数据
     */
    public Biji findByBijimingcheng(String username) {
        // 新建笔记模块实体类Biji
        Biji pojo = new Biji();
        // 设置参数
        pojo.setBijimingcheng(username);
        // 根据实体类新建QueryWrapper查询条件类
        QueryWrapper<Biji> queryWrapper = Wrappers.query(pojo);
        Biji row = mapper.selectOne(queryWrapper);
        return row;
    }

    /**
     *   根据Bijimingcheng字段参数获取一行数据，并不包含某uid 参数得行
     */
    public Biji findByBijimingcheng(String username, Integer uid) {
        // 新建笔记模块实体类Biji
        Biji pojo = new Biji();
        // 设置参数
        pojo.setBijimingcheng(username);
        // 根据实体类新建QueryWrapper查询条件类
        QueryWrapper<Biji> queryWrapper = Wrappers.query(pojo);
        // 设置参数 id != uid变量
        queryWrapper.ne("id", uid);
        // 根据queryWrapper 查询
        Biji row = mapper.selectOne(queryWrapper);
        return row;
    }

    /**
     *  根据id 获取一行数据
     */
    public R<Biji> findById(Integer id) {
        return R.success(mapper.selectById(id));
    }

    /**
     *  根据Wrapper 对象进行数据筛选
     */
    public R<List<Biji>> selectAll(Wrapper<Biji> query) {
        return R.success(mapper.selectList(query));
    }

    /**
     *  直接筛选所有数据
     */
    public R<List<Biji>> selectAll() {
        QueryWrapper<Biji> wrapper = Wrappers.query();
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
        QueryWrapper<Biji> wrapper = mapToWrapper(map);
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
        QueryWrapper<Biji> wrapper = mapToWrapper(map);
        // 设置排序
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        // 设置分页数据
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *  根据map 条件筛选tianjiaren字段等于session.username的数据并分页
     *
     */
    public R selectPagesTianjiaren(Map<String, Object> map) {
        // 获取前端给到列表基础参数
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        // 将map参数转换为mybatis-plus的QueryWrapper类
        QueryWrapper<Biji> wrapper = mapToWrapper(map);
        // 设置为当前角色
        wrapper.eq("tianjiaren", SessionFactory.getUsername());
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     *   将提交的参数转换成 mybatisplus 的QueryWrapper 筛选数据对象
     */
    public QueryWrapper<Biji> mapToWrapper(Map<String, Object> map) {
        // 创建 QueryWrapper 对象
        QueryWrapper<Biji> wrapper = Wrappers.query();

        String where = " 1=1 ";
        // 以下是判断搜索框中是否有输入内容，判断是否前台是否有填写相关条件，符合则写入sql搜索语句

        if (!StringUtil.isNullOrEmpty(map.get("keyword"))) {
            wrapper.apply(" ( bijimingcheng LIKE '%" + map.get("keyword") + "%' OR xiangqing LIKE '%" + map.get("keyword") + "%' ) ");
        }

        if (!StringUtil.isNullOrEmpty(map.get("bijimingcheng"))) {
            wrapper.like("bijimingcheng", map.get("bijimingcheng"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("biaoqian"))) {
            wrapper.like("biaoqian", map.get("biaoqian"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("guanlianyouxi"))) {
            wrapper.eq("guanlianyouxi", map.get("guanlianyouxi"));
        }
        if (!StringUtil.isNullOrEmpty(map.get("xiangqing"))) {
            wrapper.like("xiangqing", map.get("xiangqing"));
        }

        if (!StringUtil.isNullOrEmpty(map.get("issh"))) {
            wrapper.eq("issh", map.get("issh"));
        }

        if (map.containsKey("session_name")) {
            wrapper.eq(map.get("session_name").toString(), SessionFactory.getUsername());
        }

        wrapper.apply(where);
        return wrapper;
    }

    public R selectPages(QueryWrapper<Biji> wrapper, IPage page) {
        Map result = new HashMap();
        result.put("lists", mapper.selectPage(page, wrapper));

        return R.success(result);
    }

    /**
     * 插入用户数据
     * @param entityData 插入的对象
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R insert(Biji entityData, Map post) {
        // 判断是否有填写笔记编号。
        if (StringUtil.isNullOrEmpty(post.get("bijibianhao"))) {
            return R.error("请填写笔记编号");
        }
        // 判断是否有填写笔记名称。
        if (StringUtil.isNullOrEmpty(post.get("bijimingcheng"))) {
            return R.error("请填写笔记名称");
        }
        // 判断是否有填写笔记名称,有则判断是否在数据中已经存在，存在则报错。
        if (findByBijimingcheng(entityData.getBijimingcheng()) != null) {
            return R.error("笔记名称已经存在");
        }
        // 判断是否有填写笔记封面。
        if (StringUtil.isNullOrEmpty(post.get("bijifengmian"))) {
            return R.error("请填写笔记封面");
        }
        // 判断是否有填写标签。
        if (StringUtil.isNullOrEmpty(post.get("biaoqian"))) {
            return R.error("请填写标签");
        }
        // 判断是否有填写关联游戏。
        if (StringUtil.isNullOrEmpty(post.get("guanlianyouxi"))) {
            return R.error("请填写关联游戏");
        }

        Info.handlerNullEntity(entityData);

        entityData.setId(null);
        mapper.insert(entityData);
        if (entityData.getId() != null) {
            return findById(entityData.getId());
        } else {
            return R.error("插入错误");
        }
    }

    /**
     * 根据id进行更新笔记数据
     * @param entityData 更新的数据
     * @param post 提交的数据
     * @return 是否处理成功
     */
    public R<Object> update(Biji entityData, Map post) {
        // 判断是否有填写笔记编号。
        if (StringUtil.isNullOrEmpty(post.get("bijibianhao"))) {
            return R.error("请填写笔记编号");
        }
        // 判断是否有填写笔记名称。
        if (StringUtil.isNullOrEmpty(post.get("bijimingcheng"))) {
            return R.error("请填写笔记名称");
        }
        // 判断是否有填写笔记名称,有则判断是否在数据中已经存在，存在则报错。
        if (findByBijimingcheng(entityData.getBijimingcheng(), entityData.getId()) != null) {
            return R.error("笔记名称已经存在");
        }
        // 判断是否有填写笔记封面。
        if (StringUtil.isNullOrEmpty(post.get("bijifengmian"))) {
            return R.error("请填写笔记封面");
        }
        // 判断是否有填写标签。
        if (StringUtil.isNullOrEmpty(post.get("biaoqian"))) {
            return R.error("请填写标签");
        }
        // 判断是否有填写关联游戏。
        if (StringUtil.isNullOrEmpty(post.get("guanlianyouxi"))) {
            return R.error("请填写关联游戏");
        }

        mapper.updateById(entityData);

        return R.success(mapper.selectById(entityData.getId()));
    }

    /**
     * 更新审核数据
     * @param id      笔记id
     * @param value   更新的值
     * @return 是否成功
     */
    public R<Object> checkIssh(Integer id, String value) {
        Biji row = mapper.selectById(id);
        row.setIssh(value);
        mapper.updateById(row);
        return R.ok();
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
