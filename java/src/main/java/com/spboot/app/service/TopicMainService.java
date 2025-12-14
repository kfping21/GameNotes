package com.spboot.app.service;

import com.spboot.app.mapper.TopicMainMapper;
import com.spboot.app.pojo.TopicMain;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SelectPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TopicMainService {
    @Resource
    private TopicMainMapper mapper;

    public R<List<TopicMain>> selectAll() {
        return R.success(mapper.selectList(null));
    }

    public R<Object> selectPages(Map<String, Object> map) {
        SelectPage sp = new SelectPage(map, 10, "id", "DESC");
        QueryWrapper<TopicMain> wrapper = Wrappers.query();
        wrapper.orderBy(true, sp.isAsc(), sp.getOrderby());
        Page<TopicMain> page = new Page<>(sp.getPage(), sp.getPagesize());
        Page<TopicMain> res = mapper.selectPage(page, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("lists", res.getRecords());
        result.put("total", res.getTotal());
        result.put("page", res.getCurrent());
        result.put("pagesize", res.getSize());
        return R.success(result);
    }

    public R<Object> findById(Integer id) {
        return R.success(mapper.selectById(id));
    }

    public R<Object> insert(TopicMain post, java.util.Map data) {
        mapper.insert(post);
        return R.success(post);
    }

    public R<Object> update(TopicMain post, java.util.Map data) {
        mapper.updateById(post);
        return R.success(post);
    }

    public R<Object> delete(java.util.List<Integer> ids) {
        for(Integer id: ids) mapper.deleteById(id);
        return R.success("删除成功");
    }
}
