import { session } from "@/utils/utils";

export default [
    {
        path: "admins",
        name: "AdminadminsList",
        component: () => import("@/views/admins/list.vue"),
        meta: { title: "管理员列表", authLogin: true },
    },

    {
        path: "admins/add",
        name: "AdminadminsAdd",
        component: () => import("@/views/admins/add.vue"),
        meta: { title: "添加管理员", authLogin: true },
    },
    {
        path: "admins/updt",
        name: "AdminadminsUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/admins/updt.vue"),
        meta: { title: "编辑管理员", authLogin: true },
    },
    {
        path: "admins/updtself",
        name: "AdminadminsUpdtSelf",
        props: (route) => ({ id: session("id") }),
        component: () => import("@/views/admins/updtself.vue"),
        meta: { title: "更新个人资料", authLogin: true },
    },
    {
        path: "yonghu",
        name: "AdminyonghuList",
        component: () => import("@/views/yonghu/list.vue"),
        meta: { title: "用户列表", authLogin: true },
    },

    {
        path: "yonghu/add",
        name: "AdminyonghuAdd",
        component: () => import("@/views/yonghu/add.vue"),
        meta: { title: "添加用户", authLogin: true },
    },
    {
        path: "yonghu/updt",
        name: "AdminyonghuUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/yonghu/updt.vue"),
        meta: { title: "编辑用户", authLogin: true },
    },
    {
        path: "yonghu/updtself",
        name: "AdminyonghuUpdtSelf",
        props: (route) => ({ id: session("id") }),
        component: () => import("@/views/yonghu/updtself.vue"),
        meta: { title: "更新个人资料", authLogin: true },
    },
    {
        path: "yonghu/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminyonghuDetail",
        component: () => import("@/views/yonghu/detail.vue"),
        meta: { title: "用户详情", authLogin: true },
    },
    {
        path: "lunbotu",
        name: "AdminlunbotuList",
        component: () => import("@/views/lunbotu/list.vue"),
        meta: { title: "轮播图列表", authLogin: true },
    },

    {
        path: "lunbotu/add",
        name: "AdminlunbotuAdd",
        component: () => import("@/views/lunbotu/add.vue"),
        meta: { title: "添加轮播图", authLogin: true },
    },
    {
        path: "lunbotu/updt",
        name: "AdminlunbotuUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/lunbotu/updt.vue"),
        meta: { title: "编辑轮播图", authLogin: true },
    },
    {
        path: "shoucang",
        name: "AdminshoucangList",
        component: () => import("@/views/shoucang/list.vue"),
        meta: { title: "收藏列表", authLogin: true },
    },

    {
        path: "shoucang/username",
        name: "AdminshoucangListusername",
        component: () => import("@/views/shoucang/username.vue"),
        meta: { title: "收藏列表", authLogin: true },
    },

    {
        path: "dianzan",
        name: "AdmindianzanList",
        component: () => import("@/views/dianzan/list.vue"),
        meta: { title: "点赞列表", authLogin: true },
    },

    {
        path: "dianzan/username",
        name: "AdmindianzanListusername",
        component: () => import("@/views/dianzan/username.vue"),
        meta: { title: "点赞列表", authLogin: true },
    },

    {
        path: "biji",
        name: "AdminbijiList",
        component: () => import("@/views/biji/list.vue"),
        meta: { title: "笔记列表", authLogin: true },
    },

    {
        path: "biji/tianjiaren",
        name: "AdminbijiListtianjiaren",
        component: () => import("@/views/biji/tianjiaren.vue"),
        meta: { title: "笔记列表", authLogin: true },
    },

    {
        path: "biji/add",
        name: "AdminbijiAdd",
        component: () => import("@/views/biji/add.vue"),
        meta: { title: "添加笔记", authLogin: true },
    },
    {
        path: "biji/updt",
        name: "AdminbijiUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/biji/updt.vue"),
        meta: { title: "编辑笔记", authLogin: true },
    },
    {
        path: "biji/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminbijiDetail",
        component: () => import("@/views/biji/detail.vue"),
        meta: { title: "笔记详情", authLogin: true },
    },
    {
        path: "youxi",
        name: "AdminyouxiList",
        component: () => import("@/views/youxi/list.vue"),
        meta: { title: "游戏列表", authLogin: true },
    },

    {
        path: "youxi/add",
        name: "AdminyouxiAdd",
        component: () => import("@/views/youxi/add.vue"),
        meta: { title: "添加游戏", authLogin: true },
    },
    {
        path: "youxi/updt",
        name: "AdminyouxiUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/youxi/updt.vue"),
        meta: { title: "编辑游戏", authLogin: true },
    },
    {
        path: "youxi/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminyouxiDetail",
        component: () => import("@/views/youxi/detail.vue"),
        meta: { title: "游戏详情", authLogin: true },
    },
    {
        path: "biaoqian",
        name: "AdminbiaoqianList",
        component: () => import("@/views/biaoqian/list.vue"),
        meta: { title: "标签列表", authLogin: true },
    },

    {
        path: "biaoqian/add",
        name: "AdminbiaoqianAdd",
        component: () => import("@/views/biaoqian/add.vue"),
        meta: { title: "添加标签", authLogin: true },
    },
    {
        path: "biaoqian/updt",
        name: "AdminbiaoqianUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/biaoqian/updt.vue"),
        meta: { title: "编辑标签", authLogin: true },
    },
    {
        path: "zhongcao",
        name: "AdminzhongcaoList",
        component: () => import("@/views/zhongcao/list.vue"),
        meta: { title: "种草列表", authLogin: true },
    },

    {
        path: "zhongcao/tianjiaren",
        name: "AdminzhongcaoListtianjiaren",
        component: () => import("@/views/zhongcao/tianjiaren.vue"),
        meta: { title: "种草列表", authLogin: true },
    },
    {
        path: "zhongcao/zhongcaoren",
        name: "AdminzhongcaoListzhongcaoren",
        component: () => import("@/views/zhongcao/zhongcaoren.vue"),
        meta: { title: "种草列表", authLogin: true },
    },

    {
        path: "zhongcao/add",
        name: "AdminzhongcaoAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/zhongcao/add.vue"),
        meta: { title: "添加种草", authLogin: true },
    },
    {
        path: "zhongcao/updt",
        name: "AdminzhongcaoUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/zhongcao/updt.vue"),
        meta: { title: "编辑种草", authLogin: true },
    },
    {
        path: "zhongcao/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminzhongcaoDetail",
        component: () => import("@/views/zhongcao/detail.vue"),
        meta: { title: "种草详情", authLogin: true },
    },
    {
        path: "pinglun",
        name: "AdminpinglunList",
        component: () => import("@/views/pinglun/list.vue"),
        meta: { title: "评论列表", authLogin: true },
    },

    {
        path: "pinglun/pinglunren",
        name: "AdminpinglunListpinglunren",
        component: () => import("@/views/pinglun/pinglunren.vue"),
        meta: { title: "评论列表", authLogin: true },
    },

    {
        path: "pinglun/updt",
        name: "AdminpinglunUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/pinglun/updt.vue"),
        meta: { title: "编辑评论", authLogin: true },
    },
    {
        path: "pinglunhuifu",
        name: "AdminpinglunhuifuList",
        component: () => import("@/views/pinglunhuifu/list.vue"),
        meta: { title: "评论回复列表", authLogin: true },
    },

    {
        path: "pinglunhuifu/pinglunren",
        name: "AdminpinglunhuifuListpinglunren",
        component: () => import("@/views/pinglunhuifu/pinglunren.vue"),
        meta: { title: "评论回复列表", authLogin: true },
    },
    {
        path: "pinglunhuifu/huifuren",
        name: "AdminpinglunhuifuListhuifuren",
        component: () => import("@/views/pinglunhuifu/huifuren.vue"),
        meta: { title: "评论回复列表", authLogin: true },
    },

    {
        path: "pinglunhuifu/updt",
        name: "AdminpinglunhuifuUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/pinglunhuifu/updt.vue"),
        meta: { title: "编辑评论回复", authLogin: true },
    },
    {
        path: "tongzhi",
        name: "AdmintongzhiList",
        component: () => import("@/views/tongzhi/list.vue"),
        meta: { title: "通知列表", authLogin: true },
    },

    {
        path: "tongzhi/yonghu",
        name: "AdmintongzhiListyonghu",
        component: () => import("@/views/tongzhi/yonghu.vue"),
        meta: { title: "通知列表", authLogin: true },
    },

    {
        path: "tongzhi/add",
        name: "AdmintongzhiAdd",
        component: () => import("@/views/tongzhi/add.vue"),
        meta: { title: "添加通知", authLogin: true },
    },
    {
        path: "tongzhi/updt",
        name: "AdmintongzhiUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/tongzhi/updt.vue"),
        meta: { title: "编辑通知", authLogin: true },
    },
    {
        path: "tongzhi/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdmintongzhiDetail",
        component: () => import("@/views/tongzhi/detail.vue"),
        meta: { title: "通知详情", authLogin: true },
    },
    {
        path: "guanzhu",
        name: "AdminguanzhuList",
        component: () => import("@/views/guanzhu/list.vue"),
        meta: { title: "关注列表", authLogin: true },
    },

    {
        path: "guanzhu/tianjiaren",
        name: "AdminguanzhuListtianjiaren",
        component: () => import("@/views/guanzhu/tianjiaren.vue"),
        meta: { title: "关注列表", authLogin: true },
    },
    {
        path: "guanzhu/guanzhuren",
        name: "AdminguanzhuListguanzhuren",
        component: () => import("@/views/guanzhu/guanzhuren.vue"),
        meta: { title: "关注列表", authLogin: true },
    },

    {
        path: "guanzhu/add",
        name: "AdminguanzhuAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/guanzhu/add.vue"),
        meta: { title: "添加关注", authLogin: true },
    },
    {
        path: "guanzhu/updt",
        name: "AdminguanzhuUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/guanzhu/updt.vue"),
        meta: { title: "编辑关注", authLogin: true },
    },
    {
        path: "guanzhu/detail",
        props: (route) => ({ id: route.query.id }),
        name: "AdminguanzhuDetail",
        component: () => import("@/views/guanzhu/detail.vue"),
        meta: { title: "关注详情", authLogin: true },
    },
    {
        path: "siliao",
        name: "AdminsiliaoList",
        component: () => import("@/views/siliao/list.vue"),
        meta: { title: "私聊列表", authLogin: true },
    },

    {
        path: "siliao/faxinren",
        name: "AdminsiliaoListfaxinren",
        component: () => import("@/views/siliao/faxinren.vue"),
        meta: { title: "私聊列表", authLogin: true },
    },

    {
        path: "siliao/add",
        name: "AdminsiliaoAdd",
        component: () => import("@/views/siliao/add.vue"),
        meta: { title: "添加私聊", authLogin: true },
    },
    {
        path: "siliao/updt",
        name: "AdminsiliaoUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/siliao/updt.vue"),
        meta: { title: "编辑私聊", authLogin: true },
    },
    {
        path: "xiaoxi",
        name: "AdminxiaoxiList",
        component: () => import("@/views/xiaoxi/list.vue"),
        meta: { title: "消息列表", authLogin: true },
    },

    {
        path: "xiaoxi/add",
        name: "AdminxiaoxiAdd",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/xiaoxi/add.vue"),
        meta: { title: "添加消息", authLogin: true },
    },
    {
        path: "xiaoxi/updt",
        name: "AdminxiaoxiUpdt",
        props: (route) => ({ id: route.query.id }),
        component: () => import("@/views/xiaoxi/updt.vue"),
        meta: { title: "编辑消息", authLogin: true },
    },
];
