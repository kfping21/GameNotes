<template>
    <div class="views-youxi-category">
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">游戏详情</h3>
                    <div class="sidebar-widget-body">
                        <div class="floor_goods_wrap clearfix">
                            <div style="margin-bottom: 20px">
                                <form action="javascript:;" @submit="searchSubmit" class="form-search">
                                    <table class="jd-search">
                                        <tbody>
                                            <tr>
                                                <td class="label">游戏搜索</td>
                                                <td>
                                                    <el-input type="text" name="keyword" v-model="search.keyword" placeholder="输入游戏名称关键字"></el-input>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td class="label">分类查询</td>
                                                <td>
                                                    <p class="search-radio">
                                                        <a href="javascript:;" @click="selectCategory()" :class="{ active: !search.categoryId }">全部</a>
                                                        <a
                                                            href="javascript:;"
                                                            v-for="cate in categories"
                                                            :key="cate.id"
                                                            @click="selectCategory(cate.id)"
                                                            :class="{ active: search.categoryId === cate.id }"
                                                        >
                                                            {{ categoryLabel(cate) }}
                                                        </a>
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td>
                                                    <el-button type="success" @click="searchSubmit">搜索</el-button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                            <el-row :gutter="20">
                                <el-col :md="6" v-for="r in lists" :key="r.id">
                                    <div class="floor_goods_wrap_li">
                                        <div class="floor_goods_wrap_b">
                                            <router-link :to="{ path: '/youxi/detail', query: { id: r.id } }" class="floor_goods_img">
                                                <e-img :src="r.youxitupian" pb="100"></e-img>
                                            </router-link>
                                            <router-link :to="{ path: '/youxi/detail', query: { id: r.id } }" class="floor_goods_tit">
                                                <span v-html="$substr(r.youximingcheng, 20)"></span>
                                            </router-link>
                                        </div>
                                    </div>
                                </el-col>
                            </el-row>
                        </div>

                        <div style="margin-top: 10px; text-align: center">
                            <el-pagination 
                                @current-change="loadList" 
                                :page-sizes="[12, 24, 36, 48, 60]" 
                                :current-page="search.page" 
                                :page-size="search.pagesize" 
                                @update:current-page="search.page = $event"
                                @update:page-size="search.pagesize = $event"
                                @size-change="sizeChange" 
                                layout="total, sizes, prev, pager, next" 
                                :total="totalCount"> 
                            </el-pagination>
                        </div>
                    </div>
                    <!-- /.sidebar-widget-body -->
                </div>
            </e-container>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import { ref, reactive, watch, unref, onBeforeMount } from "vue";
    import { useRoute } from "vue-router";
    import { extend } from "@/utils/extend";
    import { ElMessage } from "element-plus";

    const route = useRoute();
    const search = reactive({
        keyword: "",
        categoryId: undefined,
        page: 1, // 当前页
        pagesize: 12, // 每页行数
    });
    extend(search, route.query);
    // 链接参数变化时更新这些内容
    watch(
        () => route.query,
        () => {
            extend(search, route.query);
            loadList(1);
        },
        { deep: true }
    );

    // 总行数
    const totalCount = ref(0);
    /**
     * 列表数据
     * @type {EYouxi[]}
     */
    const lists = ref([]);
    // 加载状态
    const loading = ref(false);

    const categories = ref([]);
    const normalizeLists = (payload) => payload?.lists?.records || payload?.lists || payload?.records || payload || [];
    const categoryLabel = (cate) => cate?.name || cate?.title || cate?.categoryName || cate?.mingcheng || cate?.label || `分类${cate?.id ?? ""}`;

    const loadCategories = async () => {
        try {
            const res = await http.get("/api/youxi/categories");
            if (res.code === 0) {
                categories.value = Array.isArray(res.data) ? res.data : normalizeLists(res.data);
            } else {
                categories.value = [];
                ElMessage.error(res.msg || "获取分类失败");
            }
        } catch (error) {
            categories.value = [];
            console.warn("加载游戏分类失败", error);
        }
    };

    // 设置页数多少
    const sizeChange = (e) => {
        search.pagesize = e;
        loadList(1);
    };

    // 加载游戏列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        // 构建查询参数
        const queryParams = {
            page: search.page,
            pagesize: search.pagesize,
        };
        if (search.keyword) {
            queryParams.keyword = search.keyword;
        }
        if (search.categoryId) {
            queryParams.categoryId = search.categoryId;
        }

        http.post("/api/youxi/selectPages", queryParams).then(
            (res) => {
                loading.value = false;
                if (res.code == 0) {
                    var data = res.data;
                    const listData = data.lists?.records || data.lists || [];
                    lists.value = listData;
                    totalCount.value = data.lists?.total || listData.length;
                }
            },
            (err) => {
                loading.value = false;
                ElMessage.error(err.message);
            }
        );
    };

    onBeforeMount(() => {
        loadCategories();
        loadList(1);
    });
    const selectCategory = (id) => {
        search.categoryId = id;
        searchSubmit(1);
    };

    const searchSubmit = (page = 1) => {
        loadList(page);
    };
</script>

<style scoped lang="scss">
    .views-youxi-category {
        min-height: 400px;
    }
</style>

