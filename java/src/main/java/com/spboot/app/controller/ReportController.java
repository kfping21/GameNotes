package com.spboot.app.controller;

import com.spboot.app.utils.R;
import com.jntoo.db.DB;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = { "举报管理控制器" })
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @ApiOperation(value = "获取举报列表", httpMethod = "POST")
    @RequestMapping("/list")
    public R<Object> list(@RequestBody Map<String, Object> req) {
        try {
            int page = req.get("page") == null ? 1 : Integer.parseInt(String.valueOf(req.get("page")));
            int size = req.get("size") == null ? 20 : Integer.parseInt(String.valueOf(req.get("size")));
            String type = req.get("type") == null ? "" : String.valueOf(req.get("type"));
            String status = req.get("status") == null ? "" : String.valueOf(req.get("status"));

            String where = " WHERE 1=1 ";
            if (!type.isEmpty()) where += " AND type='" + type + "' ";
            if (!status.isEmpty()) where += " AND status='" + status + "' ";

            String countSql = "SELECT COUNT(*) as c FROM report " + where;
            Number total = DB.find(countSql) == null ? 0 : (Number) ((Map)DB.find(countSql)).get("c");
            int offset = (page - 1) * size;
            List<Map<String, Object>> rows = DB.select("SELECT * FROM report " + where + " ORDER BY created_at DESC LIMIT " + offset + "," + size);

            Map<String, Object> out = new HashMap<>();
            out.put("total", total == null ? 0 : total.longValue());
            out.put("page", page);
            out.put("size", size);
            out.put("records", rows == null ? new java.util.ArrayList<>() : rows);
            return R.success(out);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @ApiOperation(value = "处理举报", httpMethod = "POST")
    @RequestMapping("/handle")
    public R<Object> handle(@RequestBody Map<String, Object> body) {
        try {
            Integer id = body.get("id") == null ? null : Integer.parseInt(String.valueOf(body.get("id")));
            String result = body.get("result") == null ? "" : String.valueOf(body.get("result"));
            String reply = body.get("reply") == null ? "" : String.valueOf(body.get("reply"));
            if (id == null) return R.error("缺少 id");
            Map<String, Object> report = DB.name("report").find(id);
            if (report == null) return R.error("举报记录不存在");
            Map<String, Object> upd = new HashMap<>();
            upd.put("status", "handled");
            upd.put("result", result);
            upd.put("reply", reply);
            upd.put("handled_by", com.spboot.app.utils.SessionFactory.getUsername());
            upd.put("handled_at", com.spboot.app.utils.Info.getDateStr());
            DB.name("report").where("id", id).update(upd);

            // 简单执行 result 操作：例如封禁用户、删除内容等（此处留为人工或后续扩展）
            return R.ok();
        } catch (Exception e) {
            return R.error("处理失败");
        }
    }
}

