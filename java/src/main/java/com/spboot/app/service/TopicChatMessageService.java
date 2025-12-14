package com.spboot.app.service;

import com.spboot.app.mapper.TopicChatMessageMapper;
import com.spboot.app.pojo.TopicChatMessage;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SelectPage;
import com.jntoo.db.DB;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TopicChatMessageService {
    @Resource
    private TopicChatMessageMapper mapper;

    public R<List<TopicChatMessage>> selectByRoom(Integer roomId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TopicChatMessage> qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        qw.eq("room_id", roomId);
        qw.orderByAsc("id");
        return R.success(mapper.selectList(qw));
    }

    public R<Object> selectPages(Map<String, Object> map) {
        SelectPage sp = new SelectPage(map, 20, "id", "DESC");
        int page = sp.getPage();
        int pageSize = sp.getPagesize();
        int offset = (page - 1) * pageSize;
        String where = " WHERE 1=1 ";
        if (map.containsKey("roomId")) {
            where += " AND room_id='" + map.get("roomId") + "' ";
        }
        String sqlCount = "SELECT COUNT(*) AS count FROM topic_chat_message t " + where;
        List<Map<String, Object>> cntRows = DB.select(sqlCount);
        long total = 0L;
        if (cntRows != null && !cntRows.isEmpty()) {
            Object cntObj = cntRows.get(0).get("count");
            if (cntObj instanceof Number) {
                total = ((Number) cntObj).longValue();
            } else {
                try { total = Long.parseLong(String.valueOf(cntObj)); } catch (Exception e) { total = 0L; }
            }
        }
        String sql = "SELECT * FROM topic_chat_message " + where + " ORDER BY id DESC LIMIT " + offset + "," + pageSize;
        List<Map<String, Object>> rows = DB.select(sql);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("lists", rows == null ? new java.util.ArrayList<>() : rows);
        result.put("total", total);
        result.put("page", page);
        result.put("pagesize", pageSize);
        return R.success(result);
    }

    public R insert(TopicChatMessage post, java.util.Map data) {
        mapper.insert(post);
        return R.success(post);
    }

    public R delete(java.util.List<Integer> ids) {
        for(Integer id: ids) mapper.deleteById(id);
        return R.success("删除成功");
    }
}
