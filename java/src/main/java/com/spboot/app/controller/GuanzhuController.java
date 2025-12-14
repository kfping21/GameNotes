package com.spboot.app.controller;

import cn.hutool.core.bean.BeanUtil;
import com.spboot.app.pojo.Guanzhu;
import com.spboot.app.service.GuanzhuService;
import com.spboot.app.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "关注控制器" })
@RestController
@RequestMapping("/api/guanzhu")
public class GuanzhuController {

    @Autowired
    public GuanzhuService guanzhuService;

    @ApiOperation(value = "关注用户", httpMethod = "POST")
    @PostMapping("/insert")
    public R follow(@RequestBody Map<String, Object> data) {
        String followee = data.get("followee") == null ? "" : data.get("followee").toString();
        return guanzhuService.follow(followee);
    }

    @ApiOperation(value = "取消关注用户", httpMethod = "POST")
    @PostMapping("/delete")
    public R unfollow(@RequestBody Map<String, Object> data) {
        String followee = null;
        if (data.get("followee") != null) followee = data.get("followee").toString();
        // 兼容旧接口：如果传入的是 id 列表，则调用旧的删除
        if (data.get("id") != null) {
            try {
                List<Integer> ids = (List<Integer>) data.get("id");
                return guanzhuService.delete(ids);
            } catch (Exception e) {
                // ignore
            }
        }
        return guanzhuService.unfollow(followee);
    }

    @ApiOperation(value = "查询我关注的人（分页）", httpMethod = "POST")
    @PostMapping("/selectGuanzhuren")
    public R selectFollowing(@RequestBody Map<String, Object> req) {
        return guanzhuService.listFollowing(req);
    }

    @ApiOperation(value = "查询我的粉丝（分页）", httpMethod = "POST")
    @PostMapping("/selectTianjiaren")
    public R selectFollowers(@RequestBody Map<String, Object> req) {
        return guanzhuService.listFollowers(req);
    }

    @ApiOperation(value = "检查是否已关注", httpMethod = "GET")
    @GetMapping("/isFollow")
    public R isFollow(@RequestParam String followee, @RequestParam(required = false) String follower) {
        return guanzhuService.isFollow(followee, follower);
    }

    // 保留部分原有对旧 guanzhu 表的只读查询接口，便于历史数据查看
    @ApiOperation(value = "获取全部关注（历史数据）", httpMethod = "GET")
    @GetMapping("/selectAll")
    public R<List<Guanzhu>> selectAll() {
        return guanzhuService.selectAll();
    }

    @ApiOperation(value = "根据条件筛选获取关注历史，并分页", httpMethod = "POST")
    @PostMapping("/selectPages")
    public R selectPages(@RequestBody Map<String, Object> req) {
        return guanzhuService.selectPages(req);
    }

    @ApiOperation(value = "根据id获取历史关注信息", httpMethod = "GET")
    @GetMapping("/findById")
    @ApiImplicitParam(name = "id", value = "关注对应的id", dataType = "Integer")
    public R findById(@RequestParam Integer id) {
        return guanzhuService.findById(id);
    }

    @ApiOperation(value = "根据id更新历史关注数据", httpMethod = "POST")
    @PostMapping("/update")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Guanzhu.class, paramType = "body")
    public R update(@RequestBody Map<String, Object> data) {
        Guanzhu post = BeanUtil.mapToBean(data, Guanzhu.class, true);
        return guanzhuService.update(post, data);
    }

}
