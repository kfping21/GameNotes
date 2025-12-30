SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 管理员
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins` (
                          `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                          `username` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '帐号',
                          `pwd` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '密码',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='管理员';

-- 标签
DROP TABLE IF EXISTS `biaoqian`;
CREATE TABLE `biaoqian` (
                            `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                            `biaoqianmingcheng` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '标签名称',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='标签';

-- 用户
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu` (
                          `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                          `zhanghao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '账号',
                          `mima` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '密码',
                          `mingcheng` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '名称',
                          `xingbie` VARCHAR(10) NOT NULL DEFAULT '' COMMENT '性别',
                          `chushengriqi` VARCHAR(25) NOT NULL DEFAULT '' COMMENT '出生日期',
                          `shouji` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '手机',
                          `youxiang` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '邮箱',
                          `gexingqianming` LONGTEXT NOT NULL COMMENT '个性签名',
                          `biaoqian` TEXT NOT NULL COMMENT '标签',
                          `wozaiwan` TEXT NOT NULL COMMENT '我在玩',
                          `touxiang` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '头像',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='用户';

-- 游戏分类
DROP TABLE IF EXISTS `game_category`;
CREATE TABLE `game_category` (
                                 `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `name` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '分类名称',
                                 `sort` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
                                 `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏分类';

-- 游戏
DROP TABLE IF EXISTS `youxi`;
CREATE TABLE `youxi` (
                         `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                         `youximingcheng` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '游戏名称',
                         `guanlianbiji` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联笔记',
                         `game_category_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '游戏分类ID',
                         `youxitupian` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '游戏图片',
                         `release_date` VARCHAR(25) NOT NULL DEFAULT '' COMMENT '发行日期',
                         `xiangqing` LONGTEXT NOT NULL COMMENT '详情',
                         `intro` LONGTEXT NULL COMMENT '简介',
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `guanlianbiji_index`(`guanlianbiji`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='游戏';

-- 笔记
DROP TABLE IF EXISTS `biji`;
CREATE TABLE `biji` (
                        `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                        `bijibianhao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '笔记编号',
                        `bijimingcheng` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '笔记名称',
                        `bijifengmian` TEXT NOT NULL COMMENT '笔记封面',
                        `biaoqian` TEXT NOT NULL COMMENT '标签',
                        `guanlianyouxi` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联游戏',
                        `shipin` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '视频',
                        `zhongcaodu` INT(11) NOT NULL DEFAULT 0 COMMENT '种草度',
                        `xiangqing` LONGTEXT NOT NULL COMMENT '详情',
                        `tianjiaren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '添加人',
                        `issh` VARCHAR(10) NOT NULL DEFAULT '否' COMMENT '是否审核',
                        PRIMARY KEY (`id`) USING BTREE,
                        INDEX `guanlianyouxi_index`(`guanlianyouxi`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='笔记';

-- 种草
DROP TABLE IF EXISTS `zhongcao`;
CREATE TABLE `zhongcao` (
                            `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                            `bijiid` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '笔记id',
                            `bijibianhao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '笔记编号',
                            `bijimingcheng` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '笔记名称',
                            `biaoqian` TEXT NOT NULL COMMENT '标签',
                            `guanlianyouxi` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '关联游戏',
                            `tianjiaren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '添加人',
                            `zhongcaoren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '种草人',
                            `addtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `biji_index`(`bijiid`) USING BTREE,
                            INDEX `guanlianyouxi_index`(`guanlianyouxi`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='种草';

-- 点赞
DROP TABLE IF EXISTS `dianzan`;
CREATE TABLE `dianzan` (
                           `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                           `username` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户',
                           `biaoid` INT(11) NOT NULL DEFAULT 0 COMMENT '关联表id',
                           `biao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '关联表',
                           `biaoti` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '标题',
                           `addtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='点赞';

-- 收藏
DROP TABLE IF EXISTS `shoucang`;
CREATE TABLE `shoucang` (
                            `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                            `username` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户',
                            `xwid` INT(11) NOT NULL DEFAULT 0 COMMENT '关联表id',
                            `biao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '关联表',
                            `biaoti` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '标题',
                            `addtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='收藏';

-- 评论
DROP TABLE IF EXISTS `pinglun`;
CREATE TABLE `pinglun` (
                           `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                           `biao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '表',
                           `biaoid` INT(11) NOT NULL DEFAULT 0 COMMENT '表id',
                           `biaoti` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '标题',
                           `pingfen` INT(11) NOT NULL DEFAULT 0 COMMENT '评分',
                           `pinglunneirong` TEXT NOT NULL COMMENT '评论内容',
                           `pinglunren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '评论人',
                           `addtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='评论';

-- 评论回复
DROP TABLE IF EXISTS `pinglunhuifu`;
CREATE TABLE `pinglunhuifu` (
                                `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                `pinglunid` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论id',
                                `biao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '表',
                                `biaoid` INT(11) NOT NULL DEFAULT 0 COMMENT '表id',
                                `biaoti` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '标题',
                                `pinglunneirong` TEXT NOT NULL COMMENT '评论内容',
                                `pinglunren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '评论人',
                                `huifuneirong` TEXT NOT NULL COMMENT '回复内容',
                                `huifuren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '回复人',
                                `addtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `pinglun_index`(`pinglunid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='评论回复';

-- 关注
DROP TABLE IF EXISTS `guanzhu`;
CREATE TABLE `guanzhu` (
                           `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                           `follower` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '关注者',
                           `followee` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '被关注者',
                           `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1=有效，0=取消',
                           `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
                           `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE KEY `uniq_follower_followee` (`follower`, `followee`) USING BTREE,
                           KEY `idx_follower` (`follower`) USING BTREE,
                           KEY `idx_followee` (`followee`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用户关注';

-- 私聊
DROP TABLE IF EXISTS `siliao`;
CREATE TABLE `siliao` (
                          `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                          `bianhao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '编号',
                          `shouxinren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '收信人',
                          `faxinren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '发信人',
                          `addtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息最后��间',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='私聊';

-- 消息
DROP TABLE IF EXISTS `xiaoxi`;
CREATE TABLE `xiaoxi` (
                          `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                          `siliaoid` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '私聊id',
                          `bianhao` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '编号',
                          `neirong` LONGTEXT NOT NULL COMMENT '内容',
                          `fasongren` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '发送人',
                          `fasongshijian` VARCHAR(25) NOT NULL DEFAULT '' COMMENT '发送时间',
                          `shifouzhakan` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '是否查看',
                          PRIMARY KEY (`id`) USING BTREE,
                          INDEX `siliao_index`(`siliaoid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='消息';

-- 通知
DROP TABLE IF EXISTS `tongzhi`;
CREATE TABLE `tongzhi` (
                           `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                           `tongzhineirong` LONGTEXT NOT NULL COMMENT '通知内容',
                           `yonghu` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '用户',
                           `addtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                           `issh` VARCHAR(10) NOT NULL DEFAULT '否' COMMENT '是否审核',
                           `isread` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读 0 未读 1 已读',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='通知';

-- 游戏装备
DROP TABLE IF EXISTS `game_equipment`;
CREATE TABLE `game_equipment` (
                                  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `game_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'youxi.id',
                                  `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '装备名称',
                                  `cover_url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '封面',
                                  `type` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '类型',
                                  `stats_json` LONGTEXT NULL COMMENT '属性/数值（JSON）',
                                  `intro` LONGTEXT NULL COMMENT '简介',
                                  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `game_idx`(`game_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='游戏装备库';

-- 游戏角色
DROP TABLE IF EXISTS `game_character`;
CREATE TABLE `game_character` (
                                  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `game_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'youxi.id',
                                  `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '人物名称',
                                  `avatar_url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '头像',
                                  `role` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '职业/定位',
                                  `skills_json` LONGTEXT NULL COMMENT '技能/属性（JSON）',
                                  `intro` LONGTEXT NULL COMMENT '简介',
                                  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `game_idx`(`game_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='游戏人物库';

-- 游戏笔记关联
DROP TABLE IF EXISTS `game_note_map`;
CREATE TABLE `game_note_map` (
                                 `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `game_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'youxi.id',
                                 `note_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'biji.id',
                                 `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `game_idx`(`game_id`) USING BTREE,
                                 INDEX `note_idx`(`note_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='游戏-笔记关联';

-- 话题
DROP TABLE IF EXISTS `topic_main`;
CREATE TABLE `topic_main` (
                              `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                              `title` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '大话题标题',
                              `intro` LONGTEXT NULL COMMENT '简介',
                              `tag_ids` TEXT NOT NULL COMMENT '标签ID列表',
                              `created_by` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
                              `status` VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态',
                              `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='大话题';

DROP TABLE IF EXISTS `topic_note_map`;
CREATE TABLE `topic_note_map` (
                                  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `topic_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'topic_main/topic_sub id',
                                  `note_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'biji.id',
                                  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `topic_idx`(`topic_id`) USING BTREE,
                                  INDEX `note_idx`(`note_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='话题-笔记关联';

DROP TABLE IF EXISTS `topic_chat_room`;
CREATE TABLE `topic_chat_room` (
                                   `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                   `topic_main_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '大话题ID',
                                   `topic_sub_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '小话题ID',
                                   `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '聊天室名称',
                                   `owner_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '房主用户ID',
                                   `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `main_idx`(`topic_main_id`) USING BTREE,
                                   INDEX `sub_idx`(`topic_sub_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='话题聊天室';

DROP TABLE IF EXISTS `topic_chat_message`;
CREATE TABLE `topic_chat_message` (
                                      `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                      `room_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'topic_chat_room.id',
                                      `sender_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '发送人',
                                      `content` LONGTEXT NULL COMMENT '消息内容',
                                      `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `room_idx`(`room_id`) USING BTREE,
                                      INDEX `sender_idx`(`sender_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='话题聊天室消息';

DROP TABLE IF EXISTS `topic_discussion`;
CREATE TABLE `topic_discussion` (
                                    `id` INT NOT NULL AUTO_INCREMENT,
                                    `topic_id` INT NOT NULL DEFAULT 0 COMMENT '对应 topic_main.id',
                                    `content` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '讨论内容',
                                    `created_by` INT NOT NULL DEFAULT 0 COMMENT '发布者用户id',
                                    `status` VARCHAR(32) NOT NULL DEFAULT 'normal' COMMENT '状态',
                                    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`id`),
                                    KEY `idx_topic_id` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='话题讨论表';

-- AI 查询日志
DROP TABLE IF EXISTS `ai_query_log`;
CREATE TABLE `ai_query_log` (
                                `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                `user_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'yonghu.id',
                                `query_text` LONGTEXT NULL COMMENT '用户需求描述',
                                `limit_count` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '请求条数',
                                `result_type` VARCHAR(20) NOT NULL DEFAULT '' COMMENT 'note/topic等',
                                `result_ids` LONGTEXT NULL COMMENT '返回的ID列表(JSON)',
                                `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='AI推荐请求日志';

-- 周边分类
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
                                    `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                    `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '分类名称',
                                    `parent_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级ID',
                                    `sort` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
                                    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='周边分类';

-- 周边商品
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
                           `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                           `game_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'youxi.id，可为空',
                           `category_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'product_category.id',
                           `name` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '商品名称',
                           `cover_url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '封面',
                           `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '价格',
                           `stock` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '库存',
                           `tags` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '标签',
                           `intro` LONGTEXT NULL COMMENT '简介',
                           `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `game_idx`(`game_id`) USING BTREE,
                           INDEX `category_idx`(`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='周边商品';

-- 商品图片
DROP TABLE IF EXISTS `product_image`;
CREATE TABLE `product_image` (
                                 `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                 `product_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'product.id',
                                 `url` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '图片地址',
                                 `sort` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `product_idx`(`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='商品图片';

-- 购物车表（存每个用户加入的商品）
CREATE TABLE IF NOT EXISTS cart (
                                    id int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                    product_id int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'product.id',
                                    quantity int(10) UNSIGNED NOT NULL DEFAULT 1 COMMENT '数量',
                                    username varchar(50) NOT NULL DEFAULT '' COMMENT '用户名（session 存放的用户名）',
                                    added_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
                                    PRIMARY KEY (id) USING BTREE,
                                    INDEX idx_user(username) USING BTREE, INDEX idx_product(product_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='购物车' ROW_FORMAT=Dynamic;
-- 订单主表（简单记录，订单创建即写入）
CREATE TABLE IF NOT EXISTS orders (
                                      id int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
                                      user varchar(50) NOT NULL DEFAULT '' COMMENT '下单用户名',
                                      total_amount decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '订单总额',
                                      status varchar(32) NOT NULL DEFAULT 'pending' COMMENT '订单状态：pending/paid/shipped/cancelled',
                                      created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
                                      PRIMARY KEY (id) USING BTREE, INDEX idx_user(user) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单' ROW_FORMAT=Dynamic;


-- 管理员（保持不变）
INSERT INTO `admins` (`id`, `username`, `pwd`) VALUES
    (1, 'admin', '123456');

-- 标签（四个）
INSERT INTO `biaoqian` (`id`, `biaoqianmingcheng`) VALUES
                                                       (1, '战力攻略'),
                                                       (2, '探索发现'),
                                                       (3, '社交互动'),
                                                       (4, '竞技对决');

-- 用户（8个用户，关联热门游戏）
INSERT INTO `yonghu` (`id`, `zhanghao`, `mima`, `mingcheng`, `xingbie`, `chushengriqi`, `shouji`, `youxiang`, `gexingqianming`, `biaoqian`, `wozaiwan`, `touxiang`) VALUES
                                                                                                                                                                        (1, 'king001', 'pass001', '荣耀战神', '男', '1995-08-15', '13811111111', 'king001@qq.com', '国服韩信，主玩打野与节奏带飞。', '战力攻略,竞技对决', '王者荣耀、崩坏：星穹铁道', '/upload/yonghu1.png'),
                                                                                                                                                                        (2, 'genshin002', 'pass002', '原神探索者', '女', '1998-03-22', '13822222222', 'genshin002@qq.com', '全图鉴收集，擅长解谜与剧情挖掘。', '探索发现', '原神、崩坏：星穹铁道', '/upload/yonghu2.png'),
                                                                                                                                                                        (3, 'social003', 'pass003', '社交达人', '男', '1996-11-05', '13833333333', 'social003@qq.com', '王者五排队长，擅长组织活动与交友。', '社交互动', '王者荣耀、逆水寒', '/upload/yonghu3.png'),
                                                                                                                                                                        (4, 'pvp004', 'pass004', '竞技狂魔', '男', '1994-06-18', '13844444444', 'pvp004@qq.com', '巅峰赛常年前百，主打对抗路与单挑。', '竞技对决', '王者荣耀、永劫无间', '/upload/yonghu4.png'),
                                                                                                                                                                        (5, 'explore005', 'pass005', '星穹旅人', '女', '1997-09-30', '13855555555', 'explore005@qq.com', '崩铁全成就玩家，专注地图探索与隐藏任务。', '探索发现', '崩坏：星穹铁道、原神', '/upload/yonghu5.png'),
                                                                                                                                                                        (6, 'power006', 'pass006', '战力天花板', '女', '1999-12-12', '13866666666', 'power006@qq.com', '全英雄国服，追求极致战力与冲榜。', '战力攻略', '王者荣耀、崩坏：星穹铁道', '/upload/yonghu6.png'),
                                                                                                                                                                        (7, 'team007', 'pass007', '团队指挥官', '男', '1993-04-25', '13877777777', 'team007@qq.com', '擅长战术协同与团队指挥，王者荣耀战队队长。', '战力攻略,社交互动', '王者荣耀、永劫无间', '/upload/yonghu7.png'),
                                                                                                                                                                        (8, 'peace008', 'pass008', '和平使者', '女', '2000-07-07', '13888888888', 'peace008@qq.com', '原神风景党，热爱截图与社交互动。', '探索发现,社交互动', '原神、星落之城', '/upload/yonghu8.png');

-- 游戏分类（角色扮演大类下细分）
INSERT INTO `game_category` (`id`, `name`, `sort`, `created_at`) VALUES
                                                                     (1, 'MOBA竞技角色扮演', 10, '2025-01-05 09:00:00'),
                                                                     (2, '开放世界角色扮演', 20, '2025-01-05 09:05:00'),
                                                                     (3, '回合制角色扮演', 30, '2025-01-05 09:10:00'),
                                                                     (4, '武侠社交角色扮演', 40, '2025-01-05 09:15:00'),
                                                                     (5, '动作竞技角色扮演', 50, '2025-01-05 09:20:00');

-- 游戏（热门角色扮演类游戏）
INSERT INTO `youxi` (`id`, `youximingcheng`, `guanlianbiji`, `game_category_id`, `youxitupian`, `release_date`, `xiangqing`, `intro`) VALUES
                                                                                                                                          (1, '王者荣耀', 3, 1, '/upload/wzry.jpg', '2015-11-26', '<p>5V5团队竞技MOBA手游，英雄多样，战术丰富，主打团队配合与个人操作。</p>', '<p>国民级MOBA手游，拥有上百位英雄，支持排位赛、巅峰赛等多种竞技模式。</p>'),
                                                                                                                                          (2, '原神', 4, 2, '/upload/yuanshen.jpg', '2020-09-28', '<p>开放世界冒险RPG，七国探索，元素反应，剧情与解谜并重。</p>', '<p>高自由度奇幻冒险游戏，支持多人联机，持续更新地图与剧情。</p>'),
                                                                                                                                          (3, '崩坏：星穹铁道', 3, 3, '/upload/bhtq.jpg', '2023-04-26', '<p>回合制宇宙冒险RPG，列车旅行，策略战斗，角色养成与剧情推进。</p>', '<p>崩坏系列续作，主打回合制战斗与多世界探索，剧情深度丰富。</p>'),
                                                                                                                                          (4, '逆水寒', 2, 4, '/upload/nsh.jpg', '2023-06-30', '<p>武侠开放世界社交RPG，帮派争霸，市集互动，身份扮演。</p>', '<p>沉浸式武侠社交游戏，支持自由交易、帮会战与大型活动。</p>'),
                                                                                                                                          (5, '永劫无间手游', 2, 5, '/upload/yjjwj.jpg', '2024-08-08', '<p>动作竞技角色扮演，冷兵器格斗，多人竞技，战术生存。</p>', '<p>武侠风格大逃杀游戏，强调操作技巧与团队配合。</p>');

-- 笔记（12条笔记，覆盖所有标签和游戏）
INSERT INTO `biji` (`id`, `bijibianhao`, `bijimingcheng`, `bijifengmian`, `biaoqian`, `guanlianyouxi`, `shipin`, `zhongcaodu`, `xiangqing`, `tianjiaren`, `issh`) VALUES
                                                                                                                                                                      (1, 'BJ202501001', '韩信打野节奏全攻略', '/upload/biji1.jpg', '战力攻略', 1, '/upload/video1.mp4', 92, '<p>从开局反野到中期带线，全面解析韩信打野节奏与出装思路。</p>', 'king001', '是'),
                                                                                                                                                                      (2, 'BJ202501002', '原神须弥全宝箱地图', '/upload/biji2.jpg', '探索发现', 2, '/upload/video2.mp4', 88, '<p>标注须弥地区所有宝箱、神瞳与隐藏任务触发点。</p>', 'genshin002', '是'),
                                                                                                                                                                      (3, 'BJ202501003', '王者五排车队组建指南', '/upload/biji3.jpg', '社交互动', 1, '/upload/video3.mp4', 85, '<p>如何组建高效五排车队，分工配合与沟通技巧详解。</p>', 'social003', '是'),
                                                                                                                                                                      (4, 'BJ202501004', '对抗路单杀技巧大全', '/upload/biji4.jpg', '竞技对决', 1, '/upload/video4.mp4', 90, '<p>对抗路英雄对线细节、斩杀线与单杀时机分析。</p>', 'pvp004', '是'),
                                                                                                                                                                      (5, 'BJ202501005', '崩铁全成就达成路线', '/upload/biji5.jpg', '探索发现', 3, '/upload/video5.mp4', 86, '<p>崩坏星穹铁道全成就获取路线与隐藏条件解析。</p>', 'explore005', '是'),
                                                                                                                                                                      (6, 'BJ202501006', '战力冲榜资源分配指南', '/upload/biji6.jpg', '战力攻略', 1, '/upload/video6.mp4', 94, '<p>如何合理分配资源快速提升战力，冲榜节奏把控。</p>', 'power006', '是'),
                                                                                                                                                                      (7, 'BJ202501007', '团队指挥与战术协同', '/upload/biji7.jpg', '战力攻略', 1, '/upload/video7.mp4', 87, '<p>团队指挥核心技巧，战术协同与局势判断实战解析。</p>', 'team007', '是'),
                                                                                                                                                                      (8, 'BJ202501008', '原神风景打卡地图', '/upload/biji8.jpg', '社交互动', 2, '/upload/video8.mp4', 82, '<p>提瓦特最美风景打卡点坐标与拍摄技巧分享。</p>', 'peace008', '是'),
                                                                                                                                                                      (9, 'BJ202501009', '崩铁模拟宇宙速通攻略', '/upload/biji9.jpg', '战力攻略', 3, '/upload/video9.mp4', 89, '<p>模拟宇宙高效速通路线、祝福选择与阵容搭配。</p>', 'explore005', '是'),
                                                                                                                                                                      (10, 'BJ202501010', '逆水寒帮会战指挥手册', '/upload/biji10.jpg', '社交互动', 4, '/upload/video10.mp4', 84, '<p>帮会战指挥策略、人员分工与战术执行要点。</p>', 'social003', '是'),
                                                                                                                                                                      (11, 'BJ202501011', '永劫无间武器连招大全', '/upload/biji11.jpg', '竞技对决', 5, '/upload/video11.mp4', 91, '<p>全武器连招技巧、取消后摇与实战应用场景。</p>', 'pvp004', '是'),
                                                                                                                                                                      (12, 'BJ202501012', '原神元素反应进阶解析', '/upload/biji12.jpg', '战力攻略', 2, '/upload/video12.mp4', 88, '<p>高阶元素反应机制、队伍搭配与输出循环优化。</p>', 'genshin002', '是');

-- 种草（12条种草记录）
INSERT INTO `zhongcao` (`id`, `bijiid`, `bijibianhao`, `bijimingcheng`, `biaoqian`, `guanlianyouxi`, `tianjiaren`, `zhongcaoren`, `addtime`) VALUES
                                                                                                                                                 (1, 1, 'BJ202501001', '韩信打野节奏全攻略', '战力攻略', 1, 'king001', 'power006', '2025-01-06 14:20:00'),
                                                                                                                                                 (2, 2, 'BJ202501002', '原神须弥全宝箱地图', '探索发现', 2, 'genshin002', 'peace008', '2025-01-07 09:30:00'),
                                                                                                                                                 (3, 3, 'BJ202501003', '王者五排车队组建指南', '社交互动', 1, 'social003', 'team007', '2025-01-07 15:40:00'),
                                                                                                                                                 (4, 4, 'BJ202501004', '对抗路单杀技巧大全', '竞技对决', 1, 'pvp004', 'king001', '2025-01-08 11:15:00'),
                                                                                                                                                 (5, 5, 'BJ202501005', '崩铁全成就达成路线', '探索发现', 3, 'explore005', 'genshin002', '2025-01-08 16:50:00'),
                                                                                                                                                 (6, 6, 'BJ202501006', '战力冲榜资源分配指南', '战力攻略', 1, 'power006', 'king001', '2025-01-09 10:30:00'),
                                                                                                                                                 (7, 7, 'BJ202501007', '团队指挥与战术协同', '战力攻略', 1, 'team007', 'social003', '2025-01-09 14:20:00'),
                                                                                                                                                 (8, 8, 'BJ202501008', '原神风景打卡地图', '社交互动', 2, 'peace008', 'genshin002', '2025-01-10 08:45:00'),
                                                                                                                                                 (9, 9, 'BJ202501009', '崩铁模拟宇宙速通攻略', '战力攻略', 3, 'explore005', 'power006', '2025-01-10 13:10:00'),
                                                                                                                                                 (10, 10, 'BJ202501010', '逆水寒帮会战指挥手册', '社交互动', 4, 'social003', 'team007', '2025-01-11 09:50:00'),
                                                                                                                                                 (11, 11, 'BJ202501011', '永劫无间武器连招大全', '竞技对决', 5, 'pvp004', 'king001', '2025-01-11 17:30:00'),
                                                                                                                                                 (12, 12, 'BJ202501012', '原神元素反应进阶解析', '战力攻略', 2, 'genshin002', 'explore005', '2025-01-12 10:15:00');

-- 点赞（16条点赞记录）
INSERT INTO `dianzan` (`id`, `username`, `biaoid`, `biao`, `biaoti`, `addtime`) VALUES
                                                                                    (1, 'king001', 1, 'biji', '韩信打野节奏全攻略', '2025-01-06 14:25:00'),
                                                                                    (2, 'power006', 1, 'biji', '韩信打野节奏全攻略', '2025-01-06 14:30:00'),
                                                                                    (3, 'genshin002', 2, 'biji', '原神须弥全宝箱地图', '2025-01-07 09:35:00'),
                                                                                    (4, 'peace008', 2, 'biji', '原神须弥全宝箱地图', '2025-01-07 09:40:00'),
                                                                                    (5, 'social003', 3, 'biji', '王者五排车队组建指南', '2025-01-07 15:45:00'),
                                                                                    (6, 'team007', 3, 'biji', '王者五排车队组建指南', '2025-01-07 15:50:00'),
                                                                                    (7, 'pvp004', 4, 'biji', '对抗路单杀技巧大全', '2025-01-08 11:20:00'),
                                                                                    (8, 'king001', 4, 'biji', '对抗路单杀技巧大全', '2025-01-08 11:25:00'),
                                                                                    (9, 'explore005', 5, 'biji', '崩铁全成就达成路线', '2025-01-08 16:55:00'),
                                                                                    (10, 'genshin002', 5, 'biji', '崩铁全成就达成路线', '2025-01-08 17:00:00'),
                                                                                    (11, 'power006', 6, 'biji', '战力冲榜资源分配指南', '2025-01-09 10:35:00'),
                                                                                    (12, 'king001', 6, 'biji', '战力冲榜资源分配指南', '2025-01-09 10:40:00'),
                                                                                    (13, 'team007', 7, 'biji', '团队指挥与战术协同', '2025-01-09 14:25:00'),
                                                                                    (14, 'social003', 7, 'biji', '团队指挥与战术协同', '2025-01-09 14:30:00'),
                                                                                    (15, 'pvp004', 11, 'biji', '永劫无间武器连招大全', '2025-01-11 17:35:00'),
                                                                                    (16, 'king001', 11, 'biji', '永劫无间武器连招大全', '2025-01-11 17:40:00');

-- 收藏（10条收藏记录）
INSERT INTO `shoucang` (`id`, `username`, `xwid`, `biao`, `biaoti`, `addtime`) VALUES
                                                                                   (1, 'power006', 1, 'biji', '韩信打野节奏全攻略', '2025-01-06 14:50:00'),
                                                                                   (2, 'peace008', 2, 'biji', '原神须弥全宝箱地图', '2025-01-07 10:00:00'),
                                                                                   (3, 'team007', 3, 'biji', '王者五排车队组建指南', '2025-01-07 16:00:00'),
                                                                                   (4, 'king001', 4, 'biji', '对抗路单杀技巧大全', '2025-01-08 12:00:00'),
                                                                                   (5, 'genshin002', 5, 'biji', '崩铁全成就达成路线', '2025-01-08 18:00:00'),
                                                                                   (6, 'king001', 6, 'biji', '战力冲榜资源分配指南', '2025-01-09 11:00:00'),
                                                                                   (7, 'social003', 7, 'biji', '团队指挥与战术协同', '2025-01-09 15:00:00'),
                                                                                   (8, 'genshin002', 8, 'biji', '原神风景打卡地图', '2025-01-10 09:00:00'),
                                                                                   (9, 'power006', 9, 'biji', '崩铁模拟宇宙速通攻略', '2025-01-10 14:00:00'),
                                                                                   (10, 'pvp004', 11, 'biji', '永劫无间武器连招大全', '2025-01-11 18:00:00');

-- 评论（10条评论）
INSERT INTO `pinglun` (`id`, `biao`, `biaoid`, `biaoti`, `pingfen`, `pinglunneirong`, `pinglunren`, `addtime`) VALUES
                                                                                                                   (1, 'biji', 1, '韩信打野节奏全攻略', 5, '学到了，开局反蓝真的强！', 'power006', '2025-01-06 15:00:00'),
                                                                                                                   (2, 'biji', 2, '原神须弥全宝箱地图', 5, '感谢分享，找宝箱快多了！', 'peace008', '2025-01-07 10:30:00'),
                                                                                                                   (3, 'biji', 3, '王者五排车队组建指南', 4, '很实用，车队胜率提升明显！', 'team007', '2025-01-07 16:30:00'),
                                                                                                                   (4, 'biji', 4, '对抗路单杀技巧大全', 5, '对线细节讲得太好了！', 'king001', '2025-01-08 12:30:00'),
                                                                                                                   (5, 'biji', 5, '崩铁全成就达成路线', 5, '照着做全成就已达成！', 'genshin002', '2025-01-08 18:30:00'),
                                                                                                                   (6, 'biji', 6, '战力冲榜资源分配指南', 5, '资源分配策略很科学！', 'king001', '2025-01-09 11:30:00'),
                                                                                                                   (7, 'biji', 7, '团队指挥与战术协同', 4, '指挥技巧很受用！', 'social003', '2025-01-09 15:30:00'),
                                                                                                                   (8, 'biji', 8, '原神风景打卡地图', 5, '风景太美了，感谢推荐！', 'genshin002', '2025-01-10 09:30:00'),
                                                                                                                   (9, 'biji', 9, '崩铁模拟宇宙速通攻略', 4, '速通效率提升明显！', 'power006', '2025-01-10 14:30:00'),
                                                                                                                   (10, 'biji', 11, '永劫无间武器连招大全', 5, '连招连招太实用了！', 'pvp004', '2025-01-11 18:30:00');

-- 评论回复（5条回复）
INSERT INTO `pinglunhuifu` (`id`, `pinglunid`, `biao`, `biaoid`, `biaoti`, `pinglunneirong`, `pinglunren`, `huifuneirong`, `huifuren`, `addtime`) VALUES
                                                                                                                                                      (1, 1, 'biji', 1, '韩信打野节奏全攻略', '学到了，开局反蓝真的强！', 'power006', '记得注意对方辅助位置！', 'king001', '2025-01-06 15:15:00'),
                                                                                                                                                      (2, 2, 'biji', 2, '原神须弥全宝箱地图', '感谢分享，找宝箱快多了！', 'peace008', '不客气，后续还会更新枫丹地图！', 'genshin002', '2025-01-07 11:00:00'),
                                                                                                                                                      (3, 4, 'biji', 4, '对抗路单杀技巧大全', '对线细节讲得太好了！', 'king001', '建议多练习斩杀线计算！', 'pvp004', '2025-01-08 13:00:00'),
                                                                                                                                                      (4, 6, 'biji', 6, '战力冲榜资源分配指南', '资源分配策略很科学！', 'king001', '记得优先升级核心装备！', 'power006', '2025-01-09 12:00:00'),
                                                                                                                                                      (5, 10, 'biji', 11, '永劫无间武器连招大全', '连招太实用了！', 'pvp004', '实战中要注意地形利用！', 'king001', '2025-01-11 19:00:00');

-- 关注（8条关注记录）
INSERT INTO `guanzhu` (`id`, `follower`, `followee`, `status`, `created_at`, `updated_at`) VALUES
                                                                                               (1, 'king001', 'power006', 1, '2025-01-05 10:00:00', '2025-01-05 10:00:00'),
                                                                                               (2, 'genshin002', 'peace008', 1, '2025-01-06 09:30:00', '2025-01-06 09:30:00'),
                                                                                               (3, 'social003', 'team007', 1, '2025-01-06 14:00:00', '2025-01-06 14:00:00'),
                                                                                               (4, 'pvp004', 'king001', 1, '2025-01-07 08:15:00', '2025-01-07 08:15:00'),
                                                                                               (5, 'power006', 'king001', 1, '2025-01-08 11:00:00', '2025-01-08 11:00:00'),
                                                                                               (6, 'explore005', 'genshin002', 1, '2025-01-09 10:00:00', '2025-01-09 10:00:00'),
                                                                                               (7, 'team007', 'social003', 1, '2025-01-10 09:00:00', '2025-01-10 09:00:00'),
                                                                                               (8, 'peace008', 'genshin002', 1, '2025-01-11 08:00:00', '2025-01-11 08:00:00');

-- 私聊（2个私聊房间）
INSERT INTO `siliao` (`id`, `bianhao`, `shouxinren`, `faxinren`, `addtime`) VALUES
                                                                                (1, 'ROOM2501001', 'king001', 'power006', '2025-01-08 20:30:00'),
                                                                                (2, 'ROOM2501002', 'genshin002', 'peace008', '2025-01-09 21:15:00');

-- 消息（4条消息）
INSERT INTO `xiaoxi` (`id`, `siliaoid`, `bianhao`, `neirong`, `fasongren`, `fasongshijian`, `shifouzhakan`) VALUES
                                                                                                                (1, 1, 'MSG25010011', '今晚一起打巅峰赛吗？', 'power006', '2025-01-08 20:31:00', '是'),
                                                                                                                (2, 1, 'MSG25010012', '好，我玩打野你玩辅助。', 'king001', '2025-01-08 20:32:00', '是'),
                                                                                                                (3, 2, 'MSG25010021', '明天一起探索枫丹新地图？', 'peace008', '2025-01-09 21:16:00', '是'),
                                                                                                                (4, 2, 'MSG25010022', '没问题，我带队！', 'genshin002', '2025-01-09 21:17:00', '是');

-- 通知（仅2条，用于示例）
INSERT INTO `tongzhi` (`id`, `tongzhineirong`, `yonghu`, `addtime`, `issh`, `isread`) VALUES
                                                                                          (1, '你的笔记【韩信打野节奏全攻略】获得新的收藏。', 'king001', '2025-01-06 14:51:00', '是', 1),
                                                                                          (2, '你的笔记【原神须弥全宝箱地图】收到了新的评论。', 'genshin002', '2025-01-07 10:31:00', '是', 1);

-- 游戏装备（关联热门游戏）
INSERT INTO `game_equipment` (`id`, `game_id`, `name`, `cover_url`, `type`, `stats_json`, `intro`, `created_at`) VALUES
                                                                                                                     (1, 1, '破军', '/upload/eq1.jpg', '武器', '{\"attack\":180,\"crit_rate\":\"20%\",\"effect\":\"对生命值低于50%的敌人额外造成30%伤害\"}', '王者荣耀核心输出装备，适合刺客与战士。', '2025-01-06 11:00:00'),
                                                                                                                     (2, 1, '贤者之书', '/upload/eq2.jpg', '法术', '{\"magic\":400,\"hp\":1600,\"effect\":\"技能伤害提升12%\"}', '法师核心装备，大幅提升法术强度与生命值。', '2025-01-06 11:02:00'),
                                                                                                                     (3, 2, '雾切之回光', '/upload/eq3.jpg', '单手剑', '{\"attack\":674,\"crit_dmg\":\"44.1%\",\"effect\":\"元素爆发伤害提升\"}', '原神五星单手剑，适合爆发型输出角色。', '2025-01-07 09:10:00'),
                                                                                                                     (4, 2, '翠绿之影', '/upload/eq4.jpg', '圣遗物', '{\"set_effect\":\"风套\",\"effect\":\"扩散反应伤害提升60%\"}', '风系角色核心圣遗物套装。', '2025-01-07 09:12:00'),
                                                                                                                     (5, 3, '星神陨落', '/upload/eq5.jpg', '光锥', '{\"attack\":582,\"crit_rate\":\"18%\",\"effect\":\"对低血量目标伤害提升\"}', '崩坏星穹铁道五星光锥，适合巡猎命途。', '2025-01-08 10:00:00'),
                                                                                                                     (6, 4, '秋水无痕剑', '/upload/eq6.jpg', '武器', '{\"attack\":488,\"hit_rate\":\"15%\",\"effect\":\"攻击时概率附加减速效果\"}', '逆水寒武侠武器，适合PVP与PVE。', '2025-01-09 08:00:00');

-- 游戏角色（关联热门游戏）
INSERT INTO `game_character` (`id`, `game_id`, `name`, `avatar_url`, `role`, `skills_json`, `intro`, `created_at`) VALUES
                                                                                                                       (1, 1, '韩信', '/upload/char1.jpg', '刺客/打野', '[{\"skill\":\"无情冲锋\",\"effect\":\"位移与控制\"},{\"skill\":\"背水一战\",\"effect\":\"反杀神技\"}]', '王者荣耀高机动性打野英雄，擅长带线与收割。', '2025-01-06 11:20:00'),
                                                                                                                       (2, 1, '貂蝉', '/upload/char2.jpg', '法师/刺客', '[{\"skill\":\"落红雨\",\"effect\":\"范围伤害\"},{\"skill\":\"绽风华\",\"effect\":\"真实伤害领域\"}]', '灵活的法刺英雄，团战持续输出能力强。', '2025-01-06 11:21:00'),
                                                                                                                       (3, 2, '雷电将军', '/upload/char3.jpg', '雷元素/输出', '[{\"skill\":\"神变恶曜开眼\",\"effect\":\"元素爆发充能\"}]', '原神雷神，擅长团队充能与爆发输出。', '2025-01-07 09:30:00'),
                                                                                                                       (4, 2, '钟离', '/upload/char4.jpg', '岩元素/护盾', '[{\"skill\":\"地心\",\"effect\":\"护盾与控制\"}]', '最强护盾角色，提供无缝护盾与石化控制。', '2025-01-07 13:20:00'),
                                                                                                                       (5, 3, '姬子', '/upload/char5.jpg', '火属性/输出', '[{\"skill\":\"燃烧吧\",\"effect\":\"范围火伤\"}]', '崩坏星穹铁道火系输出角色，擅长群体伤害。', '2025-01-08 10:15:00'),
                                                                                                                       (6, 4, '李师师', '/upload/char6.jpg', '辅助/治疗', '[{\"skill\":\"霓裳羽衣曲\",\"effect\":\"群体治疗\"}]', '逆水寒治疗型角色，团队生存核心。', '2025-01-09 08:15:00');

-- 游戏笔记关联（12条关联）
INSERT INTO `game_note_map` (`id`, `game_id`, `note_id`, `created_at`) VALUES
                                                                           (1, 1, 1, '2025-01-06 09:30:00'),
                                                                           (2, 1, 3, '2025-01-07 10:00:00'),
                                                                           (3, 1, 4, '2025-01-08 11:00:00'),
                                                                           (4, 1, 6, '2025-01-09 10:00:00'),
                                                                           (5, 1, 7, '2025-01-09 14:00:00'),
                                                                           (6, 2, 2, '2025-01-07 09:20:00'),
                                                                           (7, 2, 8, '2025-01-10 08:30:00'),
                                                                           (8, 2, 12, '2025-01-12 10:00:00'),
                                                                           (9, 3, 5, '2025-01-08 16:40:00'),
                                                                           (10, 3, 9, '2025-01-10 13:00:00'),
                                                                           (11, 4, 10, '2025-01-11 09:40:00'),
                                                                           (12, 5, 11, '2025-01-11 17:20:00');

-- 话题（3个大话题）
INSERT INTO `topic_main` (`id`, `title`, `intro`, `tag_ids`, `created_by`, `status`, `created_at`) VALUES
                                                                                                       (1, '王者荣耀上分攻略集中营', '<p>分享各位置英雄技巧、出装思路与团队配合。</p>', '1,4', 1, 'active', '2025-01-08 09:20:00'),
                                                                                                       (2, '原神探索与剧情交流', '<p>讨论地图探索、剧情解析与角色培养心得。</p>', '2,3', 2, 'active', '2025-01-08 15:00:00'),
                                                                                                       (3, '崩铁与回合制RPG深度研讨', '<p>专注崩坏星穹铁道及其他回合制RPG的策略讨论。</p>', '1,2', 5, 'active', '2025-01-09 10:30:00');

-- 话题笔记关联（8条关联）
INSERT INTO `topic_note_map` (`id`, `topic_id`, `note_id`, `created_at`) VALUES
                                                                             (1, 1, 1, '2025-01-08 09:40:00'),
                                                                             (2, 1, 3, '2025-01-08 10:00:00'),
                                                                             (3, 1, 4, '2025-01-08 11:20:00'),
                                                                             (4, 2, 2, '2025-01-08 15:10:00'),
                                                                             (5, 2, 8, '2025-01-10 09:00:00'),
                                                                             (6, 2, 12, '2025-01-12 10:30:00'),
                                                                             (7, 3, 5, '2025-01-09 10:50:00'),
                                                                             (8, 3, 9, '2025-01-10 13:30:00');

-- 话题聊天室（3个）
INSERT INTO `topic_chat_room` (`id`, `topic_main_id`, `topic_sub_id`, `name`, `owner_id`, `created_at`) VALUES
                                                                                                            (1, 1, 0, '王者五排车队招募区', 1, '2025-01-08 10:00:00'),
                                                                                                            (2, 2, 0, '原神联机探索组', 2, '2025-01-08 16:00:00'),
                                                                                                            (3, 3, 0, '崩铁模拟宇宙攻略组', 5, '2025-01-09 11:00:00');

-- 话题聊天室消息（6条消息）
INSERT INTO `topic_chat_message` (`id`, `room_id`, `sender_id`, `content`, `created_at`) VALUES
                                                                                             (1, 1, 1, '今晚有人组五排车队吗？缺打野和辅助。', '2025-01-08 10:05:00'),
                                                                                             (2, 1, 3, '我来辅助，找个稳定车队。', '2025-01-08 10:06:00'),
                                                                                             (3, 2, 2, '明天新版本更新，一起开荒枫丹？', '2025-01-08 16:10:00'),
                                                                                             (4, 2, 8, '加我一个，我玩钟离护盾！', '2025-01-08 16:12:00'),
                                                                                             (5, 3, 5, '模拟宇宙新祝福怎么选？求大佬指点。', '2025-01-09 11:05:00'),
                                                                                             (6, 3, 6, '建议选巡猎祝福，输出最大化。', '2025-01-09 11:06:00');

-- 话题讨论（6条讨论）
INSERT INTO `topic_discussion` (`id`, `topic_id`, `content`, `created_by`, `status`, `created_at`) VALUES
                                                                                                       (1, 1, '当前版本打野梯度：韩信、赵云、李白前三。', 1, 'normal', '2025-01-08 12:30:00'),
                                                                                                       (2, 1, '辅助装调整后，张飞和牛魔成为T0选择。', 3, 'normal', '2025-01-08 12:45:00'),
                                                                                                       (3, 2, '枫丹水下地图探索技巧：建议带早柚或心海。', 2, 'normal', '2025-01-08 17:00:00'),
                                                                                                       (4, 2, '雷电将军国家队依然是深渊最强阵容之一。', 8, 'normal', '2025-01-09 09:00:00'),
                                                                                                       (5, 3, '模拟宇宙速通阵容：希儿+银狼+布洛妮娅+罗刹。', 5, 'normal', '2025-01-09 11:30:00'),
                                                                                                       (6, 3, '新角色黑天鹅即将上线，预计改变DOT体系。', 6, 'normal', '2025-01-10 10:00:00');

-- AI 查询日志（3条）
INSERT INTO `ai_query_log` (`id`, `user_id`, `query_text`, `limit_count`, `result_type`, `result_ids`, `created_at`) VALUES
                                                                                                                         (1, 1, '推荐王者荣耀打野攻略', 5, 'note', '[1,4,7]', '2025-01-08 18:00:00'),
                                                                                                                         (2, 2, '原神隐藏任务攻略', 3, 'note', '[2,8]', '2025-01-09 10:30:00'),
                                                                                                                         (3, 5, '崩铁模拟宇宙怎么打', 5, 'note', '[5,9]', '2025-01-10 14:00:00');

-- 周边分类（3类）
INSERT INTO `product_category` (`id`, `name`, `parent_id`, `sort`, `created_at`) VALUES
                                                                                     (1, '游戏手办', 0, 10, '2025-01-06 12:50:00'),
                                                                                     (2, '游戏服饰', 0, 20, '2025-01-06 12:50:00'),
                                                                                     (3, '电竞外设', 0, 30, '2025-01-06 12:50:00');

-- 周边商品（4件）
INSERT INTO `product` (`id`, `game_id`, `category_id`, `name`, `cover_url`, `price`, `stock`, `tags`, `intro`, `created_at`) VALUES
                                                                                                                                 (1, 1, 1, '韩信街头霸王手办', '/upload/prod1.jpg', 299.00, 50, '王者荣耀,手办', '<p>高精度还原，附带武器配件与底座。</p>', '2025-01-06 13:00:00'),
                                                                                                                                 (2, 2, 2, '原神可莉联名T恤', '/upload/prod2.jpg', 129.00, 200, '原神,服饰', '<p>纯棉材质，印花清晰，多尺码可选。</p>', '2025-01-07 12:20:00'),
                                                                                                                                 (3, 1, 3, '王者荣耀电竞鼠标', '/upload/prod3.jpg', 199.00, 150, '电竞,外设', '<p>定制DPI，王者荣耀主题RGB灯效。</p>', '2025-01-08 11:10:00'),
                                                                                                                                 (4, 3, 1, '崩铁三月七手办', '/upload/prod4.jpg', 349.00, 80, '崩坏星穹铁道,手办', '<p>限量版，附带表情替换件与特效底座。</p>', '2025-01-09 09:30:00');

-- 商品图片（8张）
INSERT INTO `product_image` (`id`, `product_id`, `url`, `sort`) VALUES
                                                                    (1, 1, '/upload/prod1_1.jpg', 1),
                                                                    (2, 1, '/upload/prod1_2.jpg', 2),
                                                                    (3, 2, '/upload/prod2_1.jpg', 1),
                                                                    (4, 2, '/upload/prod2_2.jpg', 2),
                                                                    (5, 3, '/upload/prod3_1.jpg', 1),
                                                                    (6, 3, '/upload/prod3_2.jpg', 2),
                                                                    (7, 4, '/upload/prod4_1.jpg', 1),
                                                                    (8, 4, '/upload/prod4_2.jpg', 2);

SET FOREIGN_KEY_CHECKS = 1;
