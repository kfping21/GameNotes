export default [
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
                label: "我的点赞",
                to: { path: "/admin/dianzan/username" },
            },
            {
                label: "我的评论",
                to: { path: "/admin/pinglun/pinglunren" },
            },
            {
                label: "我的收藏",
                to: { path: "/admin/shoucang/username" },
            },
            {
                label: "我的种草",
                to: { path: "/admin/zhongcao/zhongcaoren" },
            },
            {
                label: "我的关注",
                to: { path: "/admin/guanzhu/guanzhuren" },
            },
        ],
    },
    {
        label: "创作中心",
        to: "",
        children: [
            {
                label: "我的笔记",
                to: { path: "/admin/biji/tianjiaren" },
            },
            {
                label: "笔记创作",
                to: { path: "/admin/biji/add" },
            },
            {
                label: "收到的关注",
                to: { path: "/admin/guanzhu/tianjiaren" },
            },
            {
                label: "收到的点赞",
                to: { path: "/admin/dianzan/tianjiaren" },
            },
            {
                label: "收到的评论",
                to: { path: "/admin/pinglun/tianjiaren" },
            },
            {
                label: "收到的收藏",
                to: { path: "/admin/shoucang/tianjiaren" },
            },
            {
                label: "收到的种草",
                to: { path: "/admin/zhongcao/tianjiaren" },
            },
        ],
    },
];
