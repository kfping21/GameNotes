<template>
    <div class="views-mall-list">
        <e-container>
            <div class="title-modelbox-widget1">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px;">
                    <h3 class="section-title" style="margin-bottom: 0; border-bottom: none; padding-bottom: 0;">周边商城</h3>
                    <div>
                        <el-button type="primary" @click="$router.push('/mall/cart')">我的购物车</el-button>
                        <el-button type="warning" @click="$router.push('/mall/order/list')">我的订单</el-button>
                    </div>
                </div>
                <div class="sidebar-widget-body">
                    <div class="floor_goods_wrap clearfix">
                        <div style="margin-bottom: 20px">
                            <form action="javascript:;" @submit="searchSubmit" class="form-search">
                                <table class="jd-search">
                                    <tbody>
                                        <tr>
                                            <td class="label">关键词</td>
                                            <td>
                                                <div style="display: flex; gap: 10px;">
                                                    <el-input type="text" v-model="search.keyword" placeholder="请输入商品名称或关键词" style="width: 300px;" clearable @clear="searchSubmit" @keyup.enter="searchSubmit"></el-input>
                                                    <el-button type="success" @click="searchSubmit">搜索</el-button>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="label">分类</td>
                                            <td>
                                                <p class="search-radio">
                                                    <a href="javascript:;" @click="selectCategory('')" :class="{ active: !search.categoryId }">全部</a>
                                                    <a href="javascript:;" v-for="c in categories" :key="c.id" @click="selectCategory(c.id)" :class="{ active: search.categoryId == c.id }">{{ c.name }}</a>
                                                </p>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                        
                        <el-empty v-if="!lists.length" description="暂无商品"></el-empty>
                        <el-row v-else :gutter="20">
                            <el-col :md="6" :sm="12" :xs="24" v-for="r in lists" :key="r.id">
                                <div class="floor_goods_wrap_li">
                                    <div class="floor_goods_wrap_b">
                                        <router-link :to="{ path: '/mall/detail', query: { id: r.id } }" class="floor_goods_img">
                                            <e-img :src="r.cover_url" pb="100"></e-img>
                                        </router-link>
                                        <router-link :to="{ path: '/mall/detail', query: { id: r.id } }" class="floor_goods_tit">{{ r.name }}</router-link>
                                        <div class="floor_goods_txt" style="color: #f56c6c; font-weight: bold;">￥{{ r.price }}</div>
                                    </div>
                                </div>
                            </el-col>
                        </el-row>
                    </div>

                    <div style="margin-top: 10px; text-align: center">
                        <el-pagination @current-change="loadList" :page-sizes="[12, 24, 36, 48, 60]" v-model:current-page="search.page" v-model:page-size="search.size" @size-change="loadList" layout="total, sizes, prev, pager, next" :total="totalCount"> </el-pagination>
                    </div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import http from "@/utils/ajax/http";
import DB from "@/utils/db";
import { ref, reactive, watch, onMounted } from "vue";
import { useRoute } from "vue-router";
import { extend } from "@/utils/extend";

const route = useRoute();
const search = reactive({
    keyword: "",
    categoryId: "",
    gameId: "",
    page: 1,
    size: 12,
});

const lists = ref([]);
const totalCount = ref(0);
const categories = ref([]);

// 初始化参数
extend(search, route.query);

// 监听路由参数变化
watch(
    () => route.query,
    () => {
        extend(search, route.query);
        loadList();
    },
    { deep: true }
);

const loadCategories = async () => {
    // 尝试获取分类，如果后端没有专门接口，可以使用 DB 工具查询 product_category 表
    try {
        // 假设表名为 product_category
        const res = await DB.name("product_category").order("sort asc").select();
        categories.value = res;
    } catch (e) {
        console.error("加载分类失败", e);
    }
};

const loadList = async () => {
    try {
        const res = await http.get("/api/mall/products", search);
        if (res.code === 0) {
            lists.value = res.data.records;
            totalCount.value = res.data.total;
        } else {
            // 如果后端返回非0，可能是接口未就绪或报错，尝试清空列表
            lists.value = [];
            totalCount.value = 0;
            console.warn("获取商品列表失败:", res.msg);
        }
    } catch (e) {
        console.error("加载商品列表失败", e);
        // 发生异常（如404）时，确保列表为空，避免UI卡死
        lists.value = [];
        totalCount.value = 0;
    }
};

const searchSubmit = () => {
    search.page = 1;
    loadList();
};

const selectCategory = (id) => {
    search.categoryId = id;
    searchSubmit();
};

onMounted(() => {
    loadCategories();
    loadList();
});
</script>

<style scoped>
.floor_goods_wrap_li {
    margin-bottom: 20px;
    background: #fff;
    transition: all 0.3s;
}
.floor_goods_wrap_li:hover {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
}
.floor_goods_wrap_b {
    padding: 10px;
}
.floor_goods_img {
    display: block;
    width: 100%;
    overflow: hidden;
    border-radius: 4px;
}
.floor_goods_tit {
    display: block;
    margin-top: 10px;
    font-size: 16px;
    color: #333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-decoration: none;
}
.floor_goods_tit:hover {
    color: #409eff;
}
.floor_goods_txt {
    margin-top: 5px;
    font-size: 14px;
    color: #666;
}
.jd-search {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}
.jd-search td {
    padding: 10px;
    border-bottom: 1px solid #f0f0f0;
}
.jd-search .label {
    width: 80px;
    font-weight: bold;
    color: #666;
}
.search-radio a {
    display: inline-block;
    margin-right: 15px;
    color: #666;
    text-decoration: none;
    padding: 2px 8px;
    border-radius: 4px;
}
.search-radio a.active,
.search-radio a:hover {
    color: #fff;
    background-color: #409eff;
}
</style>
