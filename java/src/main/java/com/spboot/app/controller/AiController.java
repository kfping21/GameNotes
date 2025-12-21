package com.spboot.app.controller;

import com.spboot.app.service.AiService;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = { "AI推荐" })
@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @ApiOperation(value = "AI推荐笔记/话题/游戏/周边", httpMethod = "POST")
    @PostMapping("/recommend")
    public R recommend(@RequestBody AiRequest req) {
        Map<String, Object> res = aiService.recommend(req.getQuery(), req.getCategory(), req.getLimit());
        return new R((Integer) res.get("code"), (String) res.get("msg"), res.get("data"));
    }

    public static class AiRequest {
        private String query;
        private String category;
        private Integer limit;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }
    }
}
