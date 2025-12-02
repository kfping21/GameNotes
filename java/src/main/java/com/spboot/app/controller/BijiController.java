package com.spboot.app.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jntoo.db.DB;
import com.jntoo.db.utils.StringUtil;
import com.spboot.app.config.Configure;
import com.spboot.app.mapper.BijiMapper;
import com.spboot.app.pojo.Biji;
import com.spboot.app.service.BijiService;
import com.spboot.app.utils.R;
import com.spboot.app.utils.SessionFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = { "笔记控制器" })
@RestController
@RequestMapping("/api/biji")
public class BijiController {

    @Autowired
    public BijiService bijiService;

    @ApiOperation(value = "获取全部笔记", httpMethod = "GET")
    @RequestMapping("/selectAll")
    public R<List<Biji>> selectAll() {
        return bijiService.selectAll();
    }

    @ApiOperation(value = "根据条件筛选获取管理员列表，并分页", httpMethod = "POST")
    @RequestMapping("/selectPages")
    public R selectPages(@RequestBody Map<String, Object> req) {
        return bijiService.selectPages(req);
    }

    @ApiOperation(value = "根据条件筛选获取添加人字段值为当前用户列表并分页", httpMethod = "POST")
    @RequestMapping("/selectTianjiaren")
    public R selectTianjiaren(@RequestBody Map<String, Object> req) {
        return bijiService.selectPagesTianjiaren(req);
    }

    /**
     *   更新数据
     */
    @ApiOperation(value = "设置数据是否为审核状态", httpMethod = "GET")
    @RequestMapping("/checkIssh")
    public R<Object> checkIssh(@RequestParam("id") Integer id, @RequestParam("value") String value) {
        return bijiService.checkIssh(id, value);
    }

    @ApiOperation(value = "根据id获取信息", httpMethod = "GET")
    @RequestMapping("/findById")
    @ApiImplicitParam(name = "id", value = "笔记对应的id", dataType = "Integer")
    public R findById(@RequestParam Integer id) {
        return bijiService.findById(id);
    }

    @ApiOperation(value = "根据id更新数据", httpMethod = "POST")
    @RequestMapping("/update")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Biji.class, paramType = "body")
    public R update(@RequestBody Map data) {
        Biji post = BeanUtil.mapToBean(data, Biji.class, true);
        return bijiService.update(post, data);
    }

    @ApiOperation(value = "插入一行数据，返回插入后的数据", httpMethod = "POST")
    @RequestMapping("/insert")
    @ApiImplicitParam(name = "data", value = "使用json数据提交", type = "json", dataTypeClass = Biji.class, paramType = "body")
    public R insert(@RequestBody Map data) {
        Biji post = BeanUtil.mapToBean(data, Biji.class, true);
        return bijiService.insert(post, data);
    }

    @ApiOperation(value = "根据id列表删除数据", httpMethod = "POST")
    @RequestMapping("/delete")
    @ApiImplicitParam(name = "id", value = "笔记对应的id", type = "json", dataTypeClass = List.class)
    public R delete(@RequestBody List<Integer> id) {
        return bijiService.delete(id);
    }

    @ApiOperation(value = "获取收藏", httpMethod = "GET")
    @GetMapping("/getshoucang")
    @ApiImplicitParam(name = "id", value = "笔记对应的id", type = "Integer", dataTypeClass = Integer.class)
    public R getshoucang(@RequestParam("id") Integer id) {
        long shoucangCount = DB.name("shoucang").where("biao", "biji").where("xwid", id).count();

        long is_shoucang = DB.name("shoucang").where("biao", "biji").where("xwid", id).where("username", SessionFactory.getUsername()).count();

        return R.success(R.result().set("shoucangCount", shoucangCount).set("is_shoucang", is_shoucang));
    }

    @ApiOperation(value = "获取点赞", httpMethod = "GET")
    @GetMapping("/getdianzan")
    @ApiImplicitParam(name = "id", value = "笔记对应的id", type = "Integer", dataTypeClass = Integer.class)
    public R getdianzan(@RequestParam("id") Integer id) {
        long dianzanCount = DB.name("dianzan").where("biao", "biji").where("biaoid", id).count();

        long is_dianzan = DB.name("dianzan").where("biao", "biji").where("biaoid", id).where("username", SessionFactory.getUsername()).count();

        return R.success(R.result().set("dianzanCount", dianzanCount).set("is_dianzan", is_dianzan));
    }

    @ApiOperation(value = "根据用户的标签和游戏偏好推荐笔记", httpMethod = "GET")
    @GetMapping("/recommendByUserTagsAndGames")
    public R recommendByUserTagsAndGames() {
        String currentUsername = SessionFactory.getUsername();
        if (currentUsername == null || currentUsername.isEmpty()) {
            return R.error("用户未登录");
        }

        // 获取当前用户信息
        Map<String, Object> userCondition = new HashMap<>();
        userCondition.put("zhanghao", currentUsername);
        List<Map<String, Object>> users = DB.name("yonghu").where(userCondition).select();

        if (users.isEmpty()) {
            return R.error("用户不存在");
        }

        Map<String, Object> user = users.get(0);
        String userTags = (String) user.get("biaoqian"); // 用户标签
        String userGames = (String) user.get("wozaiwan"); // 我在玩的游戏

        // 构建推荐查询
        List<Map<String, Object>> recommendations = new ArrayList<>();

        // 首先查找与用户标签匹配的笔记
        if (userTags != null && !userTags.trim().isEmpty()) {
            // 将标签分割成数组，支持多种分隔符
            String[] tags = userTags.split("[,，;；|]");
            for (String tag : tags) {
                if (tag != null && !tag.trim().isEmpty()) {
                    // 查找包含该标签的笔记
                    List<Map<String, Object>> notesByTag = DB.name("biji")
                        .where("biaoqian", "LIKE", "%" + tag.trim() + "%")
                        .where("issh", "是") // 只推荐已审核的笔记
                        .limit(5) // 每个标签最多5个笔记
                        .select();

                    for (Map<String, Object> note : notesByTag) {
                        // 检查是否已经添加过该笔记
                        boolean exists = false;
                        for (Map<String, Object> existing : recommendations) {
                            if (existing.get("id").equals(note.get("id"))) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            recommendations.add(note);
                        }
                    }
                }
            }
        }

        // 然后查找与用户正在玩的游戏相关的笔记
        if (userGames != null && !userGames.trim().isEmpty()) {
            // 将游戏分割成数组
            String[] games = userGames.split("[,，;；|]");
            for (String game : games) {
                if (game != null && !game.trim().isEmpty()) {
                    // 先找到游戏ID
                    List<Map<String, Object>> gamesList = DB.name("youxi")
                        .where("youximingcheng", "LIKE", "%" + game.trim() + "%")
                        .select();

                    for (Map<String, Object> gameInfo : gamesList) {
                        Integer gameId = (Integer) gameInfo.get("id");
                        // 查找关联该游戏的笔记
                        List<Map<String, Object>> notesByGame = DB.name("biji")
                            .where("guanlianyouxi", gameId)
                            .where("issh", "是") // 只推荐已审核的笔记
                            .limit(5) // 每个游戏最多5个笔记
                            .select();

                        for (Map<String, Object> note : notesByGame) {
                            // 检查是否已经添加过该笔记
                            boolean exists = false;
                            for (Map<String, Object> existing : recommendations) {
                                if (existing.get("id").equals(note.get("id"))) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                recommendations.add(note);
                            }
                        }
                    }
                }
            }
        }

        // 如果推荐数量不足，可以添加热门笔记作为补充
        if (recommendations.size() < 10) {
            int remaining = 10 - recommendations.size();
            List<Map<String, Object>> popularNotes = DB.name("biji")
                .where("issh", "是") // 只推荐已审核的笔记
                .order("zhongcaodu DESC, guanzhuliang DESC, id DESC")
                .limit(remaining)
                .select();

            for (Map<String, Object> note : popularNotes) {
                // 检查是否已经添加过该笔记
                boolean exists = false;
                for (Map<String, Object> existing : recommendations) {
                    if (existing.get("id").equals(note.get("id"))) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    recommendations.add(note);
                }
            }
        }

        // 限制最终结果为10条
        if (recommendations.size() > 10) {
            recommendations = recommendations.subList(0, 10);
        }

        return R.success(R.result().set("list", recommendations));
    }
}
