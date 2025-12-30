package com.spboot.app.controller;

import com.spboot.app.service.TopicMainService;
import com.spboot.app.service.TopicNoteMapService;
import com.spboot.app.service.TopicDiscussionService;
import com.spboot.app.pojo.TopicDiscussion;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "话题控制器" })
@RestController
@RequestMapping("/api/topic")
public class TopicController {

    @Autowired
    public TopicMainService topicMainService;
    @Autowired
    public TopicNoteMapService topicNoteMapService;
    @Autowired
    public TopicDiscussionService topicDiscussionService;

    @ApiOperation(value = "获取话题分类（大话题列表，可分页）", httpMethod = "POST")
    @RequestMapping("/categories")
    public R<Object> categories(@RequestBody(required = false) Map<String, Object> req) {
        if (req == null) req = new java.util.HashMap<String, Object>();
        return topicMainService.selectPages(req);
    }

    @ApiOperation(value = "根据id获取话题详情", httpMethod = "GET")
    @RequestMapping("/findById")
    @ApiImplicitParam(name = "id", value = "话题id", dataType = "Integer")
    public R<Object> findById(@RequestParam Integer id) {
        return topicMainService.findById(id);
    }

    @ApiOperation(value = "获取话题详情（包含小话题和笔记，支持分页）", httpMethod = "POST")
    @RequestMapping("/detail")
    public R<Object> detail(@RequestBody Map<String, Object> req) {
        if (req == null) req = new java.util.HashMap<String, Object>();
        Object idObj = req.get("id");
        if (idObj == null) {
            return R.error("缺少 id 参数");
        }
        Integer id = Integer.parseInt(String.valueOf(idObj));
        Map<String, Object> result = new java.util.HashMap<String, Object>();
        // main
        Object mainData = topicMainService.findById(id).get("data");
        result.put("main", mainData);
        // discussions 替代原来的子话题
        java.util.Map<String, Object> discReq = new java.util.HashMap<String, Object>();
        discReq.put("topicId", id);
        if (req.containsKey("page")) discReq.put("page", req.get("page"));
        if (req.containsKey("pagesize")) discReq.put("pagesize", req.get("pagesize"));
        Object discsData = topicDiscussionService.selectPages(discReq).get("data");
        result.put("discussions", discsData);
        // notes (pass through pagination params page/pagesize if provided)
        java.util.Map<String, Object> noteReq = new java.util.HashMap<String, Object>();
        noteReq.put("topicId", id);
        if (req.containsKey("page")) noteReq.put("page", req.get("page"));
        if (req.containsKey("pagesize")) noteReq.put("pagesize", req.get("pagesize"));
        Object notesData = topicNoteMapService.selectPages(noteReq).get("data");
        result.put("notes", notesData);
        return R.success(result);
    }

    @ApiOperation(value = "获取话题下的笔记列表（包含笔记详情，支持分页）", httpMethod = "POST")
    @RequestMapping("/notes")
    public R<Object> notes(@RequestBody Map<String, Object> req) {
        return topicNoteMapService.selectPages(req);
    }

    @ApiOperation(value = "获取话题讨论列表（根据 topicId，可分页）", httpMethod = "POST")
    @RequestMapping("/discussion/list")
    public R<Object> discussionList(@RequestBody(required = false) Map<String, Object> req) {
        if (req == null) req = new java.util.HashMap<String, Object>();
        return topicDiscussionService.selectPages(req);
    }

    @ApiOperation(value = "在话题下创建讨论（不需要认证：body 包含 topicId, content, createdBy 可选）", httpMethod = "POST")
    @RequestMapping("/discussion/create")
    public R<Object> discussionCreate(@RequestBody Map<String, Object> body) {
        TopicDiscussion td = new TopicDiscussion();
        if (body.get("topicId") != null) td.setTopicId(Integer.parseInt(String.valueOf(body.get("topicId"))));
        if (body.get("content") != null) td.setContent(String.valueOf(body.get("content")));
        if (body.get("createdBy") != null) td.setCreatedBy(Integer.parseInt(String.valueOf(body.get("createdBy"))));
        return topicDiscussionService.insert(td, body);
    }
}

