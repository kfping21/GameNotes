export default [
    {
        label: "笔记管理",
        to: "",
        children: [
            {
                label: "笔记添加",
                to: { path: "/admin/biji/add" },
            },
            {
                label: "笔记查询",
                to: { path: "/admin/biji/tianjiaren" },
            },
        ],
    },
    {
        label: "种草管理",
        to: "",
        children: [
            {
                label: "ta种草我",
                to: { path: "/admin/zhongcao/tianjiaren" },
            },
            {
                label: "我的种草记录",
                to: { path: "/admin/zhongcao/zhongcaoren" },
            },
        ],
    },


    {
        label: "评论管理",
        to: "",
        children: [
            {
                label: "我的评论",
                to: { path: "/admin/pinglun/pinglunren" },
            },
            {
                label: "ta回复我",
                to: { path: "/admin/pinglunhuifu/pinglunren" },
            },
            {
                label: "我回复ta",
                to: { path: "/admin/pinglunhuifu/huifuren" },
            },
        ],
    },
    {
        label: "个人中心",
        to: "",
        children: [
            {
                label: "修改个人资料",
                to: { path: "/admin/yonghu/updtself" },
            },
            {
                label: "修改密码",
                to: { path: "/admin/mod" },
            },
            {
                label: "我的收藏",
                to: { path: "/admin/shoucang/username" },
            },
            {
                label: "我的点赞",
                to: { path: "/admin/dianzan/username" },
            },
            {
                label: "我的关注",
                to: { path: "/admin/guanzhu/guanzhuren" },
            },
            {
                label: "通知记录",
                to: { path: "/admin/tongzhi/yonghu" },
            },
        ],
    },
];
