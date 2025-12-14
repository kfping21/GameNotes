package com.spboot.app.service;

import com.spboot.app.mapper.TopicChatRoomMapper;
import com.spboot.app.pojo.TopicChatRoom;
import com.spboot.app.utils.R;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TopicChatRoomService {
    @Resource
    private TopicChatRoomMapper mapper;

    public R<List<TopicChatRoom>> selectAll() {
        return R.success(mapper.selectList(null));
    }

    public R insert(TopicChatRoom post, java.util.Map data) {
        mapper.insert(post);
        return R.success(post);
    }

    public R delete(java.util.List<Integer> ids) {
        for(Integer id: ids) mapper.deleteById(id);
        return R.success("删除成功");
    }
}

