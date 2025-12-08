export default [
    {
        label: "账号管理",
        to: "",
        children: [
            {
                label: "管理员账号管理",
                to: { path: "/admin/admins" },
            },
            {
                label: "管理员账号添加",
                to: { path: "/admin/admins/add" },
            },
            {
                label: "密码修改",
                to: { path: "/admin/mod" },
            },
        ],
    },
    {
        label: "用户管理",
        to: "",
        children: [
            {
                label: "添加用户",
                to: { path: "/admin/yonghu/add" },
            },
            {
                label: "用户管理",
                to: { path: "/admin/yonghu" },
            },
            {
                label: "关注查询",
                to: { path: "/admin/guanzhu" },
            },
        ],
    },
    {
        label: "笔记管理",
        to: "",
        children: [
            {
                label: "标签添加",
                to: { path: "/admin/biaoqian/add" },
            },
            {
                label: "标签查询",
                to: { path: "/admin/biaoqian" },
            },
            {
                label: "游戏添加",
                to: { path: "/admin/youxi/add" },
            },
            {
                label: "游戏查询",
                to: { path: "/admin/youxi" },
            },
            {
                label: "笔记查询",
                to: { path: "/admin/biji" },
            },
        ],
    },
    {
        label: "种草管理",
        to: "",
        children: [
            {
                label: "种草查询",
                to: { path: "/admin/zhongcao" },
            },
        ],
    },


    {
        label: "评论管理",
        to: "",
        children: [
            {
                label: "评论查询",
                to: { path: "/admin/pinglun" },
            },
            {
                label: "回复查询",
                to: { path: "/admin/pinglunhuifu" },
            },
        ],
    },
    {
        label: "轮播图管理",
        to: "",
        children: [
            {
                label: "轮播图添加",
                to: { path: "/admin/lunbotu/add" },
            },
            {
                label: "轮播图查询",
                to: { path: "/admin/lunbotu" },
            },
        ],
    },
];
