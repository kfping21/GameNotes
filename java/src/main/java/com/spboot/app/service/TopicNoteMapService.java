package com.spboot.app.service;

import com.spboot.app.mapper.TopicNoteMapMapper;
import com.spboot.app.pojo.TopicNoteMap;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SelectPage;
import com.jntoo.db.DB;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TopicNoteMapService {
    @Resource
    private TopicNoteMapMapper mapper;

    public R<List<TopicNoteMap>> selectAll() {
        return R.success(mapper.selectList(null));
    }

    public R<Object> selectPages(Map<String, Object> map) {
        SelectPage sp = new SelectPage(map, 10, "id", "DESC");
        int page = sp.getPage();
        int pageSize = sp.getPagesize();
        int offset = (page - 1) * pageSize;
        String where = " WHERE 1=1 ";
        if (map.containsKey("topicId")) {
            where += " AND topic_id='" + map.get("topicId") + "' ";
        }
        String sqlCount = "SELECT COUNT(*) AS count FROM topic_note_map t " + where;
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
        String sql = "SELECT b.* FROM topic_note_map t LEFT JOIN biji b ON t.note_id=b.id " + where + " ORDER BY t.id DESC LIMIT " + offset + "," + pageSize;
        List<Map<String, Object>> rows = DB.select(sql);
        // 为每条笔记追加 url 字段，便于前端跳转到笔记详情页
        if (rows != null) {
            for (Map<String, Object> r : rows) {
                Object idObj = r.get("id");
                if (idObj != null) {
                    r.put("url", "/note/" + String.valueOf(idObj));
                } else {
                    r.put("url", "");
                }
            }
        }
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("lists", rows == null ? new java.util.ArrayList<>() : rows);
        result.put("total", total);
        result.put("page", page);
        result.put("pagesize", pageSize);
        return R.success(result);
    }

    public R<Object> insert(TopicNoteMap post, Map data) {
        mapper.insert(post);
        return R.success(post);
    }

    public R<Object> delete(java.util.List<Integer> ids) {
        for(Integer id: ids) mapper.deleteById(id);
        return R.success("删除成功");
    }

    // 新增：将一篇笔记与多个话题关联（先删除原有关联再插入新关联）
    public R<Object> bindTopicsToNote(Integer noteId, java.util.List<Integer> topicIds) {
        if (noteId == null || noteId == 0) return R.error("noteId 为空");
        try {
            // 删除旧的关联
            DB.execute("DELETE FROM topic_note_map WHERE note_id = " + noteId);
            if (topicIds != null) {
                for (Integer tid : topicIds) {
                    if (tid == null || tid == 0) continue;
                    TopicNoteMap m = new TopicNoteMap();
                    m.setTopicId(tid);
                    m.setNoteId(noteId);
                    mapper.insert(m);
                }
            }
            return R.success("绑定成功");
        } catch (Exception e) {
            return R.error("绑定失败: " + e.getMessage());
        }
    }

    // 新增：获取某篇笔记关联的话题列表（返回 topic_main 表中的字段）
    public java.util.List<java.util.Map<String, Object>> getTopicsByNoteId(Integer noteId) {
        if (noteId == null || noteId == 0) return new java.util.ArrayList<>();
        String sql = "SELECT t.* FROM topic_note_map m LEFT JOIN topic_main t ON t.id = m.topic_id WHERE m.note_id = " + noteId + " ORDER BY m.id DESC";
        java.util.List<java.util.Map<String, Object>> rows = DB.select(sql);
        return rows == null ? new java.util.ArrayList<>() : rows;
    }

    public R<Object> getTopicsForNote(Integer noteId) {
        return R.success(getTopicsByNoteId(noteId));
    }
}
