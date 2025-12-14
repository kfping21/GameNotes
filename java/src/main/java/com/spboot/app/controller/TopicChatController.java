package com.spboot.app.controller;

import com.spboot.app.pojo.TopicChatMessage;
import com.spboot.app.pojo.TopicChatRoom;
import com.spboot.app.service.TopicChatMessageService;
import com.spboot.app.service.TopicChatRoomService;
import com.spboot.app.utils.R;
import com.spboot.app.websocket.WebSocketSever;
import com.spboot.app.websocket.Message;
import com.spboot.app.websocket.WebSocketUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "话题聊天室控制器" })
@RestController
@RequestMapping("/api/topic/chat")
public class TopicChatController {

    @Autowired
    public TopicChatRoomService roomService;
    @Autowired
    public TopicChatMessageService messageService;

    @ApiOperation(value = "获取房间列表", httpMethod = "GET")
    @RequestMapping("/rooms")
    public R<List<TopicChatRoom>> rooms() {
        return roomService.selectAll();
    }

    @ApiOperation(value = "创建房间", httpMethod = "POST")
    @RequestMapping("/rooms/create")
    public R<Object> createRoom(@RequestBody TopicChatRoom room) {
        return roomService.insert(room, null);
    }

    @ApiOperation(value = "根据roomId分页获取消息", httpMethod = "POST")
    @RequestMapping("/messages/paged")
    @ApiImplicitParam(name = "roomId", value = "房间id", dataType = "Integer")
    public R<Object> messagesPaged(@RequestBody Map<String, Object> req) {
        return messageService.selectPages(req);
    }

    @ApiOperation(value = "根据roomId获取消息（不分页）", httpMethod = "GET")
    @RequestMapping("/messages")
    @ApiImplicitParam(name = "roomId", value = "房间id", dataType = "Integer")
    public R messages(@RequestParam Integer roomId) {
        return messageService.selectByRoom(roomId);
    }

    @ApiOperation(value = "发送消息（保存并广播）", httpMethod = "POST")
    @RequestMapping("/send")
    public R send(@RequestBody Map<String, Object> body) {
        // body should contain roomId, senderId, content
        TopicChatMessage msg = new TopicChatMessage();
        if(body.get("roomId") != null) msg.setRoomId(Integer.parseInt(String.valueOf(body.get("roomId"))));
        if(body.get("senderId") != null) msg.setSenderId(Integer.parseInt(String.valueOf(body.get("senderId"))));
        if(body.get("content") != null) msg.setContent(String.valueOf(body.get("content")));
        messageService.insert(msg, body);

        // Try to broadcast via WebSocketSever using sendMessageByRoom (to room users only)
        try {
            Message m = new Message("topic_chat_message", R.success(msg));
            WebSocketSever.sendMessageByRoom(msg.getRoomId(), m);
        } catch (Exception e) {
            // ignore websocket errors; still return success
        }

        return R.success(msg);
    }
}
