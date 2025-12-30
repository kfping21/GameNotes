<template>
    <div class="views-biji-index">
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">笔记列表</h3>
                    <div class="sidebar-widget-body">
                        <div class="floor_goods_wrap clearfix">
                            <div style="margin-bottom: 20px">
                                <form action="javascript:;" @submit="searchSubmit" class="form-search">
                                    <div class="search-box">
                                        <div class="search-main">
                                            <el-autocomplete
                                                v-model="search.keyword"
                                                :fetch-suggestions="querySearch"
                                                placeholder="请输入标题或内容关键词"
                                                style="width: 300px;"
                                                clearable
                                                @select="handleSelect"
                                                @clear="searchSubmit"
                                                @keyup.enter="searchSubmit"
                                            >
                                                <template #default="{ item }">
                                                    <div v-if="item.type === 'header'" class="search-suggestion-header" @click.stop>
                                                        <span>{{ item.value }}</span>
                                                    </div>
                                                    <div v-else class="search-suggestion-item">
                                                        <span class="suggestion-text">{{ item.value }}</span>
                                                        <span v-if="item.type === 'history'" class="delete-history" @click.stop="deleteHistoryItem(item)">
                                                            <el-icon><Close /></el-icon>
                                                        </span>
                                                        <span v-if="item.type === 'hot'" class="hot-tag">
                                                            <el-icon><TrendCharts /></el-icon>
                                                        </span>
                                                    </div>
                                                </template>
                                                <template #append>
                                                    <el-button @click="searchSubmit"><el-icon><Search /></el-icon></el-button>
                                                </template>
                                            </el-autocomplete>
                                            <el-button type="primary" link @click="showFilter = !showFilter" style="margin-left: 10px">
                                                {{ showFilter ? '收起筛选' : '高级筛选' }}
                                                <el-icon class="el-icon--right"><component :is="showFilter ? 'ArrowUp' : 'ArrowDown'" /></el-icon>
                                            </el-button>
                                        </div>
                                        
                                        <el-collapse-transition>
                                            <div v-show="showFilter" class="search-advanced">
                                                <table class="jd-search">
                                                    <tbody>
                                                        <tr>
                                                            <td class="label">关联游戏</td>
                                                            <td>
                                                                <p class="search-radio">
                                                                    <a href="javascript:;" @click="selectRadio('guanlianyouxi', '')" :class="{ active: !search.guanlianyouxi }">全部</a>
                                                                    <a href="javascript:;" v-for="r in mapyouxi1" :key="r.id" @click="selectRadio('guanlianyouxi', r.id)" :class="{ active: search.guanlianyouxi == r.id }" v-text="r.youximingcheng"> </a>
                                                                </p>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="label">标签</td>
                                                            <td>
                                                                <el-select v-model="search.biaoqian" placeholder="请选择标签" clearable @change="searchSubmit">
                                                                    <el-option label="全部" value=""></el-option>
                                                                    <e-select-option type="option" module="biaoqian" value="id" label="biaoqianmingcheng"></e-select-option>
                                                                </el-select>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="label">排序</td>
                                                            <td>
                                                                <div style="display: flex; justify-content: space-between; align-items: center;">
                                                                    <p class="search-radio">
                                                                        <a href="javascript:;" @click="selectRadio('orderby', 'id')" :class="{ active: search.orderby == 'id' }">最新发布</a>
                                                                        <a href="javascript:;" @click="selectRadio('orderby', 'zhongcaodu')" :class="{ active: search.orderby == 'zhongcaodu' }">热度</a>
                                                                    </p>
                                                                    <p class="search-radio">
                                                                        <a href="javascript:;" @click="selectRadio('sort', 'desc')" :class="{ active: search.sort == 'desc' }">倒序</a>
                                                                        <a href="javascript:;" @click="selectRadio('sort', 'asc')" :class="{ active: search.sort == 'asc' }">升序</a>
                                                                    </p>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </el-collapse-transition>
                                    </div>
                                </form>
                            </div>
                            <el-row :gutter="20">
                                <el-col :md="6" v-for="r in lists">
                                    <div class="floor_goods_wrap_li">
                                        <div class="floor_goods_wrap_b">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_img">
                                                <e-img :src="r.bijifengmian" pb="100"></e-img>
                                            </router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_tit" v-html="$substr(r.bijimingcheng, 20)"></router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_txt" v-html="$substr(r.xiangqing, 20)"></router-link>
                                        </div>
                                    </div>
                                </el-col>
                            </el-row>
                        </div>

                        <div style="margin-top: 10px; text-align: center">
                            <el-pagination @current-change="loadList" :page-sizes="[12, 24, 36, 48, 60]" v-model:current-page="search.page" v-model:page-size="search.pagesize" @size-change="sizeChange" layout="total, sizes, prev, pager, next" :total="totalCount"> </el-pagination>
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
    import DB from "@/utils/db";
    import router from "@/router";

    import { ref, reactive, watch, unref, onBeforeMount } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { canBijiSelect, useBijiSelect, canBijiDelete } from "@/module";
    import { extend } from "@/utils/extend";
    import { ElMessageBox, ElMessage } from "element-plus";
    import { Search, ArrowDown, ArrowUp, Close, TrendCharts } from '@element-plus/icons-vue';

    const route = useRoute();
    const showFilter = ref(false);
    const search = reactive({
        keyword: "",
        issh: "是",
        bijimingcheng: "",
        biaoqian: "",
        guanlianyouxi: "",
        xiangqing: "",
        page: 1, // 当前页
        pagesize: 12, // 每页行数
        orderby: "id", // 排序字段
        sort: "desc", // 排序类型
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
     * @type {EBiji[]}
     */
    const lists = ref([]);
    // 加载状态
    const loading = ref(false);

    // 排序操作
    const sortChange = (e) => {
        console.log(e);
        if (e.order == null) {
            search.orderby = "id";
            search.sort = "desc";
        } else {
            search.orderby = e.prop;
            search.sort = e.order == "ascending" ? "asc" : "desc";
        }
        loadList(1);
    };
    // 设置页数多少
    const sizeChange = (e) => {
        search.pagesize = e;
        loadList(1);
    };

    // 加载笔记列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/biji/selectPages", search).then(
            (res) => {
                loading.value = false;
                if (res.code == 0) {
                    var data = res.data;
                    lists.value = data.lists.records;
                    totalCount.value = data.lists.total;
                }
            },
            (err) => {
                loading.value = false;
                ElMessage.error(err.message);
            }
        );
    };

    onBeforeMount(() => {
        loadList(1);
    });
    const selectRadio = (target, name) => {
        search[target] = name;
        searchSubmit(1);
    };

    // 搜索历史相关
    const querySearch = (queryString, cb) => {
        lastSearchCallback = cb;
        const history = JSON.parse(localStorage.getItem('biji_search_history') || '[]');
        let results = [];

        if (queryString) {
            // 如果有输入，显示匹配的历史记录
            results = history.filter(item => item.toLowerCase().includes(queryString.toLowerCase()))
                .slice(0, 10)
                .map(item => ({ value: item, type: 'history' }));
        } else {
            // 如果没有输入，显示历史记录和热门搜索
            
            // 热门推荐 (放在前面或者后面，用户要求分开)
            // 通常热门在下，历史在上，或者反过来。
            // 附件图片显示的是混合的。
            // 用户说 "推荐和历史分开"。
            
            // 历史记录
            const historyItems = history.slice(0, 3).map(item => ({ value: item, type: 'history' }));
            if (historyItems.length > 0) {
                results.push({ value: '历史搜索', type: 'header' });
                results.push(...historyItems);
            }

            // 热门推荐
            const hotItems = getRandomHotSearches().map(item => ({ value: item, type: 'hot' }));
            if (hotItems.length > 0) {
                results.push({ value: '热门推荐', type: 'header' });
                results.push(...hotItems);
            }
        }
        cb(results);
    };

    const getRandomHotSearches = () => {
        const hotPool = ['攻略', '心得', '测评', '教程', '分享', '推荐', '避雷', '日常', '游戏', '周边'];
        // 随机取3个
        return hotPool.sort(() => 0.5 - Math.random()).slice(0, 3);
    };

    const handleSelect = (item) => {
        if (item.type === 'header') return;
        search.keyword = item.value;
        searchSubmit();
    };

    const saveHistory = (keyword) => {
        if (!keyword || !keyword.trim()) return;
        let history = JSON.parse(localStorage.getItem('biji_search_history') || '[]');
        // 删除已存在的相同关键词，确保最新的在前面
        history = history.filter(item => item !== keyword);
        history.unshift(keyword);
        // 最多保留10条
        if (history.length > 10) history.pop();
        localStorage.setItem('biji_search_history', JSON.stringify(history));
    };

    let lastSearchCallback = null;

    const deleteHistoryItem = (item) => {
        let history = JSON.parse(localStorage.getItem('biji_search_history') || '[]');
        history = history.filter(h => h !== item.value);
        localStorage.setItem('biji_search_history', JSON.stringify(history));
        if (lastSearchCallback) {
            querySearch(search.keyword, lastSearchCallback);
        }
    };

    const searchSubmit = (page = 1) => {
        saveHistory(search.keyword);
        loadList(1);
    };
    const mapyouxi1 = DB.name("youxi").field("id,youximingcheng").order("id desc").selectRef();
</script>

<style scoped lang="scss">
    .views-biji-index {
    }
    
    .search-box {
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
        
        .search-main {
            display: flex;
            align-items: center;
        }
        
        .search-advanced {
            border-top: 1px solid #eee;
            padding-top: 15px;
            margin-top: 15px;
        }
    }
</style>

<style lang="scss">
.search-suggestion-header {
    font-size: 12px;
    color: #999;
    padding: 5px 10px;
    background-color: #f5f7fa;
    border-bottom: 1px solid #eee;
    font-weight: bold;
    pointer-events: none;
    cursor: default;
}

.search-suggestion-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    
    .suggestion-text {
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    
    .delete-history {
        color: #999;
        cursor: pointer;
        font-size: 12px;
        &:hover {
            color: #f56c6c;
        }
    }
    
    .hot-tag {
        color: #f56c6c;
    }
}
</style>
