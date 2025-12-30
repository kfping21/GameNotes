export default [
    {
        label: "用户管理",
        to: "",
        children: [
            {
                label: "管理员管理",
                to: { path: "/admin/admins" },
            },
            {
                label: "普通用户管理",
                to: { path: "/admin/yonghu" },
            },
        ],
    },
    {
        label: "内容管理",
        to: "",
        children: [
            {
                label: "笔记审核",
                to: { path: "/admin/biji" },
            },
            {
                label: "评论管理",
                to: { path: "/admin/pinglun" },
            },
            {
                label: "话题管理",
                to: { path: "/admin/topic/admin" },
            },
            {
                label: "游戏信息管理",
                to: { path: "/admin/youxi" },
            },
        ],
    },
    {
        label: "商城管理",
        to: "",
        children: [
            {
                label: "商品管理",
                to: { path: "/admin/mall/product" },
            },
            {
                label: "分类管理",
                to: { path: "/admin/mall/category" },
            },
            {
                label: "订单查看",
                to: { path: "/admin/mall/order" },
            },
            {
                label: "库存管理",
                to: { path: "/admin/mall/product" },
            },
        ],
    },
    {
        label: "数据统计",
        to: "",
        children: [
            {
                label: "用户统计",
                to: { path: "/admin/statistics" },
            },
            {
                label: "内容统计",
                to: { path: "/admin/statistics" },
            },
            {
                label: "互动统计",
                to: { path: "/admin/statistics" },
            },
            {
                label: "ECharts可视化",
                to: { path: "/admin/statistics" },
            },
        ],
    },
    {
        label: "运营管理",
        to: "",
        children: [
            {
                label: "公告通知发布",
                to: { path: "/admin/tongzhi" },
            },
            {
                label: "标签管理",
                to: { path: "/admin/biaoqian" },
            },
            {
                label: "举报处理",
                to: { path: "/admin/report" },
            },
        ],
    },
];
