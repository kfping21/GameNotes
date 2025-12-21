package com.spboot.app.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.jntoo.db.DB;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AiService {

    @Value("${ai.api-key:}")
    private String aiApiKey;

    @Value("${ai.base-url:https://ark.cn-beijing.volces.com/api/v3}")
    private String aiBaseUrl;

    @Value("${ai.model:deepseek-v3-250324}")
    private String aiModel;

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final Pattern TAG_PATTERN = Pattern.compile("<[^>]+>");
    private static final MediaType JSON_MEDIA = MediaType.parse("application/json; charset=utf-8");

    public Map<String, Object> recommend(String query, String category, Integer limit) {
        String q = StrUtil.blankToDefault(query, "").trim();
        if (q.isEmpty()) {
            return R("请输入需求", Collections.emptyList());
        }
        int size = Math.max(1, Math.min(limit == null || limit <= 0 ? 5 : limit, 20));

        List<String> scopes = parseCategory(category);
        List<Map<String, Object>> items = new ArrayList<>();
        if (scopes.contains("note")) {
            items.addAll(searchNotes());
        }
        if (scopes.contains("topic")) {
            items.addAll(searchTopics());
        }
        if (scopes.contains("game")) {
            items.addAll(searchGames());
        }
        if (scopes.contains("product")) {
            items.addAll(searchProducts());
        }

        List<Map<String, Object>> finalList = rerankByAiIfConfigured(q, items);
        List<Map<String, Object>> resultItems = finalList.stream().limit(size).collect(Collectors.toList());
        log.info("[AI] query='{}', searched={}, final_return={}", q, items.size(), resultItems.size());

        try {
            List<Map<String, Object>> ids = finalList.stream()
                    .map(i -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("type", i.get("type"));
                        m.put("id", i.get("id"));
                        return m;
                    })
                    .collect(Collectors.toList());
            DB.name("ai_query_log").insert(new HashMap<String, Object>() {{
                put("user_id", 0);
                put("query_text", q);
                put("limit_count", size);
                put("result_type", "mixed");
                put("result_ids", JSON.toJSONString(ids));
            }});
        } catch (Exception ignore) {
            // ignore log error
        }

        Map<String, Object> data = new HashMap<>();
        data.put("query", q);
        data.put("items", resultItems);
        if (resultItems.isEmpty()) {
            if (!StrUtil.isNotBlank(aiApiKey)) {
                data.put("emptyReason", "AI 未配置，无法提供更合适的推荐");
            } else {
                data.put("emptyReason", "AI 暂时没有找到更合适的内容，请尝试换个描述或换个关键词");
            }
        }
        return R(null, data);
    }

    private List<Map<String, Object>> searchNotes() {
        String sql = "select id, bijimingcheng as title, bijifengmian as cover, xiangqing as summary from biji order by id desc";
        return cast(DB.select(sql)).stream()
                .map(r -> item("note", intVal(r.get("id")),
                        (String) r.get("title"),
                        clean((String) r.get("summary")),
                        "/biji/detail?id=" + r.get("id"),
                        (String) r.get("cover"),
                        8))
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> searchTopics() {
        List<Map<String, Object>> all = new ArrayList<>();
        String mainSql = "select id, title, intro from topic_main order by id desc";
        all.addAll(cast(DB.select(mainSql)).stream()
                .map(r -> item("topic_main", intVal(r.get("id")),
                        (String) r.get("title"),
                        clean((String) r.get("intro")),
                        "/topic/main?id=" + r.get("id"),
                        null,
                        6))
                .collect(Collectors.toList()));

        String subSql = "select id, title, intro from topic_sub order by id desc";
        all.addAll(cast(DB.select(subSql)).stream()
                .map(r -> item("topic_sub", intVal(r.get("id")),
                        (String) r.get("title"),
                        clean((String) r.get("intro")),
                        "/topic/sub?id=" + r.get("id"),
                        null,
                        5))
                .collect(Collectors.toList()));
        return all;
    }

    private List<Map<String, Object>> searchGames() {
        String sql = "select id, youximingcheng as title, youxitupian as cover, intro, xiangqing from youxi order by id desc";
        return cast(DB.select(sql)).stream()
                .map(r -> item("game", intVal(r.get("id")),
                        (String) r.get("title"),
                        clean(coalesce((String) r.get("intro"), (String) r.get("xiangqing"))),
                        "/youxi/detail?id=" + r.get("id"),
                        (String) r.get("cover"),
                        7))
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> searchProducts() {
        String sql = "select id, name as title, cover_url as cover, intro, tags from product order by id desc";
        return cast(DB.select(sql)).stream()
                .map(r -> item("product", intVal(r.get("id")),
                        (String) r.get("title"),
                        clean(coalesce((String) r.get("intro"), (String) r.get("tags"))),
                        "/mall/detail?id=" + r.get("id"),
                        (String) r.get("cover"),
                        5))
                .collect(Collectors.toList());
    }

    private Map<String, Object> item(String type, int id, String title, String summary, String link, String cover, int scoreBase) {
        Map<String, Object> m = new HashMap<>();
        m.put("type", type);
        m.put("id", id);
        m.put("title", title == null ? "" : title);
        m.put("summary", summary == null ? "" : summary);
        m.put("link", link);
        m.put("cover", cover);
        m.put("score", scoreBase + (title != null ? title.length() % 3 : 0));
        return m;
    }

    private String clean(String text) {
        if (text == null) {
            return "";
        }
        String noTag = TAG_PATTERN.matcher(text).replaceAll(" ");
        noTag = noTag.replaceAll("\\s+", " ").trim();
        if (noTag.length() > 120) {
            return noTag.substring(0, 120) + "...";
        }
        return noTag;
    }

    private String coalesce(String a, String b) {
        if (StrUtil.isNotBlank(a)) {
            return a;
        }
        if (StrUtil.isNotBlank(b)) {
            return b;
        }
        return "";
    }

    private Map<String, Object> R(String msg, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", msg == null ? 0 : 1);
        res.put("msg", msg == null ? "" : msg);
        res.put("data", data);
        return res;
    }

    private List<Map<String, Object>> rerankByAiIfConfigured(String query, List<Map<String, Object>> candidates) {
        if (!StrUtil.isNotBlank(aiApiKey)) {
            log.info("[AI] rerank skipped (no api-key), use local");
            return candidates;
        }
        try {
            String systemPrompt = "你是推荐助手。根据用户需求，从候选列表中挑出最相关的若干条，返回 JSON 数组，元素需包含 type、id、title、summary、link、cover。只返回 JSON，不要额外文字。";
            StringBuilder sb = new StringBuilder();
            sb.append("用户需求:").append(query).append("\\n");
            sb.append("候选列表(JSON):").append(JSON.toJSONString(candidates));

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", aiModel);
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(message("system", systemPrompt));
            messages.add(message("user", sb.toString()));
            payload.put("messages", messages);

            RequestBody body = RequestBody.create(JSON_MEDIA, JSON.toJSONString(payload));
            String url = joinUrl(aiBaseUrl, "/chat/completions");
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + aiApiKey)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = HTTP_CLIENT.newCall(request).execute()) {
                String respBody = response.body() == null ? "" : response.body().string();
                log.info("[AI] rerank http code={}, body={}", response.code(), respBody);
                if (!response.isSuccessful() || respBody.isEmpty()) {
                    return candidates;
                }
                Object respObj = JSON.parse(respBody);
                if (respObj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> respMap = (Map<String, Object>) respObj;
                    Object choicesObj = respMap.get("choices");
                    if (choicesObj instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Object> choices = (List<Object>) choicesObj;
                        if (!choices.isEmpty()) {
                            Object first = choices.get(0);
                            if (first instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> firstMap = (Map<String, Object>) first;
                                Object messageObj = firstMap.get("message");
                                if (messageObj instanceof Map) {
                                    @SuppressWarnings("unchecked")
                                    Map<String, Object> msg = (Map<String, Object>) messageObj;
                                    Object contentObj = msg.get("content");
                                    if (contentObj != null) {
                                        String cleaned = cleanAiResponse(String.valueOf(contentObj));
                                        Object parsed = JSON.parse(cleaned);
                                        if (parsed instanceof List) {
                                            @SuppressWarnings("unchecked")
                                            List<Map<String, Object>> list = (List<Map<String, Object>>) parsed;
                                            return list;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ignore) {
            log.warn("[AI] rerank call failed, fallback local: {}", ignore.getMessage());
        }
        return candidates;
    }

    private Map<String, String> message(String role, String content) {
        Map<String, String> msg = new HashMap<>();
        msg.put("role", role);
        msg.put("content", content);
        return msg;
    }

    private String joinUrl(String base, String path) {
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + path;
    }

    private String cleanAiResponse(String content) {
        if (!StrUtil.isNotBlank(content)) {
            return "[]";
        }
        return content.replaceAll("(?is)^```json\\s*", "")
                .replaceAll("(?is)^```\\s*", "")
                .replaceAll("(?is)```$", "")
                .trim();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> cast(List<?> raw) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (raw == null) {
            return list;
        }
        for (Object o : raw) {
            if (o instanceof Map) {
                list.add((Map<String, Object>) o);
            }
        }
        return list;
    }

    private int intVal(Object o) {
        if (o == null) {
            return 0;
        }
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(o));
        } catch (Exception e) {
            return 0;
        }
    }

    private List<String> parseCategory(String category) {
        String value = StrUtil.blankToDefault(category, "all").toLowerCase(Locale.ROOT);
        switch (value) {
            case "note":
                return Collections.singletonList("note");
            case "game":
                return Collections.singletonList("game");
            case "product":
                return Collections.singletonList("product");
            case "topic_main":
                return Collections.singletonList("topic");
            case "topic_sub":
                return Collections.singletonList("topic");
            case "all":
            default:
                return Arrays.asList("note", "topic", "game", "product");
        }
    }
}
