package com.spboot.app.controller;

import com.spboot.app.utils.R;
import com.jntoo.db.DB;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = { "统计控制器" })
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @ApiOperation(value = "仪表盘数据", httpMethod = "GET")
    @GetMapping("/dashboard")
    public R<Object> dashboard() {
        try {
            Number totalUsers = (Number) DB.find("SELECT COUNT(*) AS c FROM yonghu").get("c");
            Number totalOrders = (Number) DB.find("SELECT COUNT(*) AS c FROM orders").get("c");
            Number totalSales = (Number) DB.find("SELECT IFNULL(SUM(total_amount),0) AS c FROM orders").get("c");
            // 简单定义今日活跃为今天有登录/下单/笔记的去重用户数（这里只统计下单用户）
            Number todayActive = 0;
            try {
                todayActive = (Number) DB.find("SELECT COUNT(DISTINCT user) AS c FROM orders WHERE DATE(created_at) = CURDATE()").get("c");
            } catch (Exception ex) { }

            Map<String, Object> out = new HashMap<>();
            out.put("totalUsers", totalUsers == null ? 0 : totalUsers.longValue());
            out.put("totalOrders", totalOrders == null ? 0 : totalOrders.longValue());
            out.put("totalSales", totalSales == null ? 0 : ((Number)totalSales).doubleValue());
            out.put("todayActive", todayActive == null ? 0 : todayActive.longValue());
            return R.success(out);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    @ApiOperation(value = "图表数据", httpMethod = "GET")
    @GetMapping("/chart")
    public R<Object> chart(@RequestParam String type, @RequestParam String range) {
        try {
            // type: user/order/income  range: week/month
            int days = "week".equals(range) ? 7 : 30;
            List<String> labels = new ArrayList<>();
            List<Number> values = new ArrayList<>();
            for (int i = days - 1; i >= 0; i--) {
                labels.add(java.time.LocalDate.now().minusDays(i).toString());
            }
            String sql = "";
            if ("user".equals(type)) {
                sql = "SELECT DATE(created_at) as d, COUNT(*) as c FROM yonghu WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL " + days + " DAY) GROUP BY DATE(created_at)";
            } else if ("order".equals(type)) {
                sql = "SELECT DATE(created_at) as d, COUNT(*) as c FROM orders WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL " + days + " DAY) GROUP BY DATE(created_at)";
            } else if ("income".equals(type)) {
                sql = "SELECT DATE(created_at) as d, IFNULL(SUM(total_amount),0) as c FROM orders WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL " + days + " DAY) GROUP BY DATE(created_at)";
            } else {
                return R.error("未知类型");
            }
            List<Map<String, Object>> rows = DB.select(sql);
            Map<String, Number> map = new HashMap<>();
            if (rows != null) for (Map<String, Object> r : rows) map.put(String.valueOf(r.get("d")), (Number) r.get("c"));
            for (String d : labels) values.add(map.getOrDefault(d, 0));
            Map<String, Object> out = new HashMap<>();
            out.put("labels", labels);
            out.put("values", values);
            return R.success(out);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }
}

