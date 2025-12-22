<template>
    <div class="views-youxi-category">
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">游戏详情</h3>
                    <div class="sidebar-widget-body">
                        <div class="floor_goods_wrap clearfix">
                            <div class="filter-header">
                                <div class="filter-left">
                                    <div class="filter-tabs">
                                        <span class="tab-item" :class="{ active: currentTab === 'recommend' }" @click="switchTab('recommend')">推荐</span>
                                        <span 
                                            class="tab-item" 
                                            v-for="cate in categories" 
                                            :key="cate.id" 
                                            :class="{ active: search.categoryId === cate.id }"
                                            @click="selectCategory(cate.id)"
                                        >
                                            {{ categoryLabel(cate) }}
                                        </span>
                                        <span class="tab-item" :class="{ active: currentTab === 'more' }" @click="switchTab('more')">更多</span>
                                    </div>
                                </div>
                                <div class="filter-right">
                                    <el-autocomplete
                                        v-model="search.keyword" 
                                        :fetch-suggestions="querySearch"
                                        placeholder="输入游戏名称关键字" 
                                        class="search-input"
                                        @select="handleSelect"
                                        @keyup.enter="searchSubmit"
                                        clearable
                                        @clear="searchSubmit"
                                        style="width: 100%"
                                    >
                                        <template #default="{ item }">
                                            <div class="suggestion-item">
                                                <span v-if="item.type === 'history'" style="color: #909399; margin-right: 8px;"><i class="fa fa-history"></i></span>
                                                <span v-else style="color: #f56c6c; margin-right: 8px;"><i class="fa fa-fire"></i></span>
                                                <span>{{ item.value }}</span>
                                            </div>
                                        </template>
                                        <template #append>
                                            <el-button @click="searchSubmit"><el-icon><Search /></el-icon></el-button>
                                        </template>
                                    </el-autocomplete>
                                </div>
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
    import { Search } from '@element-plus/icons-vue';

    const route = useRoute();
    const currentTab = ref('recommend'); // recommend, more, category
    const search = reactive({
        keyword: "",
        categoryId: "",
        orderby: "zhongcaodu",
        sort: "desc",
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
        lists.value = []; // 清空列表，避免显示旧数据

        if (currentTab.value === 'recommend') {
            // 推荐部分后端没有接口，直接按ID倒序显示（最新）
            search.orderby = 'id';
            search.sort = 'desc';
        }

        executeSearch();
    };

    const executeSearch = () => {
        // 构建查询参数
        const queryParams = {
            page: search.page,
            pagesize: search.pagesize,
            orderby: search.orderby,
            sort: search.sort
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
    
    const switchTab = (tab) => {
        currentTab.value = tab;
        search.categoryId = "";
        if (tab === 'recommend') {
            search.orderby = 'id';
            search.sort = 'desc';
        } else {
            search.orderby = 'id';
            search.sort = 'desc';
        }
        searchSubmit(1);
    };

    const selectCategory = (id) => {
        currentTab.value = 'category';
        search.categoryId = id;
        search.orderby = 'id';
        search.sort = 'desc';
        searchSubmit(1);
    };

    const searchSubmit = (page = 1) => {
        saveHistory(search.keyword);
        loadList(page);
    };

    const getRandomHotSearches = () => {
        const pool = ['王者荣耀', '原神', '英雄联盟', '绝地求生', '我的世界', '和平精英', 'Apex英雄', 'CS:GO', 'DOTA2', '永劫无间'];
        return pool.sort(() => 0.5 - Math.random()).slice(0, 5).map(item => ({ value: item, type: 'hot' }));
    };

    const getHistory = () => {
        try {
            const history = JSON.parse(localStorage.getItem('game_search_history') || '[]');
            return history.map(item => ({ value: item, type: 'history' }));
        } catch (e) {
            return [];
        }
    };

    const saveHistory = (keyword) => {
        if (!keyword) return;
        try {
            let history = JSON.parse(localStorage.getItem('game_search_history') || '[]');
            history = history.filter(h => h !== keyword);
            history.unshift(keyword);
            if (history.length > 10) history = history.slice(0, 10);
            localStorage.setItem('game_search_history', JSON.stringify(history));
        } catch (e) {
            console.error(e);
        }
    };

    const querySearch = (queryString, cb) => {
        let results = [];
        if (!queryString) {
            const history = getHistory();
            if (history.length > 0) {
                results = history;
            } else {
                results = getRandomHotSearches();
            }
        } else {
            const history = getHistory();
            results = history.filter(item => item.value.toLowerCase().includes(queryString.toLowerCase()));
            if (results.length === 0) {
                results = getRandomHotSearches().filter(item => item.value.toLowerCase().includes(queryString.toLowerCase()));
            }
        }
        cb(results);
    };

    const handleSelect = (item) => {
        search.keyword = item.value;
        searchSubmit(1);
    };
</script>

<style scoped lang="scss">
    .views-youxi-category {
        min-height: 400px;
    }
    
    .filter-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
        margin-bottom: 20px;
        flex-wrap: wrap;
        gap: 20px;
        
        .filter-left {
            display: flex;
            align-items: center;
            flex: 1;
            min-width: 300px;
            
            .filter-label {
                font-weight: bold;
                margin-right: 15px;
                color: #333;
                white-space: nowrap;
            }
            
            .filter-tabs {
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
                
                .tab-item {
                    cursor: pointer;
                    padding: 6px 16px;
                    border-radius: 20px;
                    font-size: 14px;
                    color: #666;
                    transition: all 0.3s;
                    background: #f5f7fa;
                    
                    &:hover {
                        color: var(--theme-primary-color);
                        background: rgba(var(--theme-primary-rgb), 0.1);
                    }
                    
                    &.active {
                        background: var(--theme-primary-color);
                        color: #fff;
                        box-shadow: 0 4px 12px rgba(var(--theme-primary-rgb), 0.3);
                    }
                }
            }
        }
        
        .filter-right {
            width: 300px;
            
            .search-input {
                width: 100%;
            }
        }
    }
    
    @media (max-width: 768px) {
        .filter-header {
            flex-direction: column;
            align-items: stretch;
            
            .filter-right {
                width: 100%;
            }
        }
    }
</style>

