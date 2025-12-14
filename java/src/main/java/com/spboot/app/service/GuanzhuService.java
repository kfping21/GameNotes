package com.spboot.app.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jntoo.db.DB;
import com.jntoo.db.utils.Convert;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.mapper.GuanzhuMapper;
import com.spboot.app.pojo.Guanzhu;
import com.spboot.app.utils.*;
import java.util.*;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class GuanzhuService {

    @Resource
    private GuanzhuMapper mapper;

    /**
     * 根据id获取一行用户关注记录
     */
    public R<Guanzhu> findById(Integer id) {
        return R.success(mapper.selectById(id));
    }

    /**
     * 根据 Wrapper 获取所有匹配的关注记录
     */
    public R<List<Guanzhu>> selectAll(Wrapper<Guanzhu> query) {
        return R.success(mapper.selectList(query));
    }

    /**
     * 获取所有关注记录（慎用）
     */
    public R<List<Guanzhu>> selectAll() {
        QueryWrapper<Guanzhu> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return selectAll(wrapper);
    }

    /**
     * 根据 map 条件筛选（兼容旧前端）
     */
    public R selectAll(Map<String, Object> map) {
        QueryWrapper<Guanzhu> wrapper = mapToWrapper(map);
        wrapper.orderByDesc("id");
        return selectAll(wrapper);
    }

    /**
     * 分页查询关注记录（通用）
     */
    public R selectPages(Map<String, Object> map) {
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        QueryWrapper<Guanzhu> wrapper = mapToWrapper(map);
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     * 查询作为被关注者（即我的粉丝），对应旧的 selectPagesTianjiaren
     */
    public R selectPagesTianjiaren(Map<String, Object> map) {
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        QueryWrapper<Guanzhu> wrapper = mapToWrapper(map);
        // 如果前端提供 username，则使用提供的，否则使用当前登录用户
        if (map.containsKey("username") && map.get("username") != null) {
            wrapper.eq("followee", map.get("username"));
        } else {
            wrapper.eq("followee", SessionFactory.getUsername());
        }
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     * 查询作为关注者（我关注的人），对应旧的 selectPagesGuanzhuren
     */
    public R selectPagesGuanzhuren(Map<String, Object> map) {
        SelectPage selectPage = new SelectPage(map, 10, "id", "DESC");
        QueryWrapper<Guanzhu> wrapper = mapToWrapper(map);
        if (map.containsKey("username") && map.get("username") != null) {
            wrapper.eq("follower", map.get("username"));
        } else {
            wrapper.eq("follower", SessionFactory.getUsername());
        }
        wrapper.orderBy(true, selectPage.isAsc(), selectPage.getOrderby());
        Page page = new Page(selectPage.getPage(), selectPage.getPagesize());
        return selectPages(wrapper, page);
    }

    /**
     * 将提交的Map参数转换为QueryWrapper<Guanzhu>
     */
    public QueryWrapper<Guanzhu> mapToWrapper(Map<String, Object> map) {
        QueryWrapper<Guanzhu> wrapper = new QueryWrapper<>();

        if (map == null) return wrapper;

        if (!StringUtil.isNullOrEmpty(map.get("follower"))) {
            wrapper.like("follower", String.valueOf(map.get("follower")));
        }
        if (!StringUtil.isNullOrEmpty(map.get("followee"))) {
            wrapper.like("followee", String.valueOf(map.get("followee")));
        }
        if (map.containsKey("status") && map.get("status") != null) {
            wrapper.eq("status", map.get("status"));
        }
        return wrapper;
    }

    public R selectPages(QueryWrapper<Guanzhu> wrapper, IPage page) {
        Map result = new HashMap();
        result.put("lists", mapper.selectPage((Page) page, wrapper));
        return R.success(result);
    }

    /**
     * 插入关注（兼容原方法签名 insert(Guanzhu, Map)）：现在以 post.get("followee") 为准
     */
    public R insert(Object entityData, Map post) {
        // entityData 可能是旧的 Guanzhu 对象，忽略
        String followee = post.get("followee") == null ? "" : post.get("followee").toString();
        String follower = SessionFactory.getUsername();
        if (follower == null || follower.isEmpty()) return R.error("请先登录");
        if (followee == null || followee.isEmpty()) return R.error("被关注用户不能为空");
        if (follower.equals(followee)) return R.error("不能关注自己");

        // 检查是否已存在
        QueryWrapper<Guanzhu> q = new QueryWrapper<>();
        q.eq("follower", follower).eq("followee", followee);
        Guanzhu exist = mapper.selectOne(q);
        if (exist != null) {
            if (exist.getStatus() != null && exist.getStatus() == 1) {
                return R.error("已关注");
            } else {
                exist.setStatus(1);
                exist.setUpdatedAt(new Date());
                mapper.updateById(exist);
                return R.success(exist);
            }
        }

        Guanzhu uf = new Guanzhu();
        uf.setFollower(follower);
        uf.setFollowee(followee);
        uf.setStatus(1);
        uf.setCreatedAt(new Date());
        uf.setUpdatedAt(new Date());
        mapper.insert(uf);

        // 发送通知给被关注人（保留原有通知逻辑风格）
        try {
            DB.execute("INSERT INTO tongzhi(tongzhineirong, yonghu, issh) VALUES ('恭喜你有新的粉丝', '" + followee + "', '否')");
        } catch (Exception e) {
            // 忽略通知失败
        }

        return R.success(uf);
    }

    /**
     * 更新关注记录（兼容原签名）
     */
    public R<Object> update(Object entityData, Map post) {
        // 期望传入 Guanzhu 格式的 map 或对象
        try {
            Integer id = null;
            if (post.get("id") != null) id = Convert.toInt(post.get("id"));
            if (id == null) return R.error("缺少 id");
            Guanzhu uf = mapper.selectById(id);
            if (uf == null) return R.error("记录不存在");
            if (post.get("status") != null) uf.setStatus(Convert.toInt(post.get("status")));
            if (post.get("followee") != null) uf.setFollowee(String.valueOf(post.get("followee")));
            if (post.get("follower") != null) uf.setFollower(String.valueOf(post.get("follower")));
            uf.setUpdatedAt(new Date());
            mapper.updateById(uf);
            return R.success(mapper.selectById(uf.getId()));
        } catch (Exception e) {
            return R.error("更新失败");
        }
    }

    /**
     * 根据 id 列表删除（兼容旧方法）
     */
    public R<Object> delete(List<Integer> ids) {
        try {
            for (Integer id : ids) {
                mapper.deleteById(id);
            }
            return R.success("操作成功");
        } catch (Exception e) {
            return R.error("操作失败");
        }
    }

    /**
     * 根据单个 id 删除
     */
    public R<Object> delete(Integer id) {
        try {
            mapper.deleteById(id);
            return R.success("操作成功");
        } catch (Exception e) {
            return R.error("操作失败");
        }
    }

    /**
     * 关注用户（前端友好封装）
     */
    public R follow(String followee) {
        String follower = SessionFactory.getUsername();
        if (follower == null || follower.isEmpty()) return R.error("请先登录");
        if (followee == null || followee.isEmpty()) return R.error("被关注用户不能为空");
        if (follower.equals(followee)) return R.error("不能关注自己");

        QueryWrapper<Guanzhu> q = new QueryWrapper<>();
        q.eq("follower", follower).eq("followee", followee);
        Guanzhu exist = mapper.selectOne(q);
        if (exist != null) {
            if (exist.getStatus() != null && exist.getStatus() == 1) {
                return R.error("已关注");
            } else {
                exist.setStatus(1);
                exist.setUpdatedAt(new Date());
                mapper.updateById(exist);
                return R.success(exist);
            }
        }

        Guanzhu g = new Guanzhu();
        g.setFollower(follower);
        g.setFollowee(followee);
        g.setStatus(1);
        g.setCreatedAt(new Date());
        g.setUpdatedAt(new Date());
        mapper.insert(g);
        try {
            DB.execute("INSERT INTO tongzhi(tongzhineirong, yonghu, issh) VALUES ('恭喜你有新的粉丝', '" + followee + "', '否')");
        } catch (Exception e) {
            // 忽略
        }
        return R.success(g);
    }

    /**
     * 取消关注（以 follower=currentSession, followee 为条件）
     */
    public R unfollow(String followee) {
        String follower = SessionFactory.getUsername();
        if (follower == null || follower.isEmpty()) return R.error("请先登录");
        if (followee == null || followee.isEmpty()) return R.error("被关注用户不能为空");

        QueryWrapper<Guanzhu> q = new QueryWrapper<>();
        q.eq("follower", follower).eq("followee", followee);
        int deleted = mapper.delete(q);
        if (deleted > 0) return R.success("取消关注成功");
        return R.error("未关注");
    }

    /**
     * 检查是否已关注
     */
    public R isFollow(String followee, String follower) {
        if (follower == null || follower.isEmpty()) follower = SessionFactory.getUsername();
        if (follower == null || follower.isEmpty()) return R.error("请先登录");
        if (followee == null || followee.isEmpty()) return R.error("被关注用户不能为空");

        QueryWrapper<Guanzhu> q = new QueryWrapper<>();
        q.eq("follower", follower).eq("followee", followee).eq("status", 1);
        Integer count = mapper.selectCount(q);
        Map ret = new HashMap();
        ret.put("isFollow", count != null && count > 0);
        return R.success(ret);
    }

    /**
     * 分页获取我关注的人（封装，兼容 controller）
     */
    public R listFollowing(Map<String, Object> map) {
        return this.selectPagesGuanzhuren(map);
    }

    /**
     * 分页获取关注我的人（粉丝）
     */
    public R listFollowers(Map<String, Object> map) {
        return this.selectPagesTianjiaren(map);
    }

}
