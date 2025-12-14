package com.spboot.app.service;

import com.spboot.app.mapper.TopicDiscussionMapper;
import com.spboot.app.pojo.TopicDiscussion;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SelectPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TopicDiscussionService {
    @Resource
    private TopicDiscussionMapper mapper;

    public R<List<TopicDiscussion>> selectAll() {
        return R.success(mapper.selectList(null));
    }

    public R<Object> selectPages(Map<String, Object> map) {
        SelectPage sp = new SelectPage(map, 10, "id", "DESC");
        QueryWrapper<TopicDiscussion> wrapper = Wrappers.query();
        if (map.containsKey("topicId")) {
            wrapper.eq("topic_id", Integer.parseInt(String.valueOf(map.get("topicId"))));
        }
        wrapper.orderBy(true, sp.isAsc(), sp.getOrderby());
        Page<TopicDiscussion> page = new Page<>(sp.getPage(), sp.getPagesize());
        Page<TopicDiscussion> res = mapper.selectPage(page, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("lists", res.getRecords());
        result.put("total", res.getTotal());
        result.put("page", res.getCurrent());
        result.put("pagesize", res.getSize());
        return R.success(result);
    }

    public R<Object> insert(TopicDiscussion post, Map data) {
        mapper.insert(post);
        return R.success(post);
    }

    public R<Object> delete(java.util.List<Integer> ids) {
        for(Integer id: ids) mapper.deleteById(id);
        return R.success("删除成功");
    }
}

