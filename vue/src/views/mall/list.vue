<template>
    <div class="views-mall-list">
        <e-container>
            <div class="mall-header">
                <div class="header-content">
                    <h3 class="page-title">周边商城</h3>
                    <p class="page-desc">精选优质周边，打造专属生活方式</p>
                </div>
                <div class="header-actions">
                    <el-button type="primary" plain round @click="$router.push('/mall/cart')">
                        <el-icon class="el-icon--left"><ShoppingCart /></el-icon>
                        购物车
                    </el-button>
                    <el-button type="warning" plain round @click="$router.push('/mall/order/list')">
                        <el-icon class="el-icon--left"><List /></el-icon>
                        我的订单
                    </el-button>
                </div>
            </div>
            
            <div class="filter-container">
                <div class="filter-search">
                    <el-autocomplete
                        v-model="search.keyword" 
                        :fetch-suggestions="querySearch"
                        placeholder="搜索心仪的商品..." 
                        class="custom-search-input"
                        clearable 
                        @select="handleSelect"
                        @clear="searchSubmit" 
                        @keyup.enter="searchSubmit"
                        style="width: 100%"
                    >
                        <template #prefix>
                            <i class="fa fa-search"></i>
                        </template>
                        <template #default="{ item }">
                            <div v-if="item.type === 'header'" class="search-suggestion-header" @click.stop>
                                <span>{{ item.value }}</span>
                            </div>
                            <div v-else class="suggestion-item">
                                <span v-if="item.type === 'history'" style="color: #909399; margin-right: 8px;"><i class="fa fa-history"></i></span>
                                <span v-else style="color: #f56c6c; margin-right: 8px;"><i class="fa fa-fire"></i></span>
                                <span style="flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ item.value }}</span>
                                <span v-if="item.type === 'history'" class="delete-history" @click.stop="deleteHistoryItem(item)">
                                    <el-icon><Close /></el-icon>
                                </span>
                            </div>
                        </template>
                        <template #append>
                            <el-button @click="searchSubmit">搜索</el-button>
                        </template>
                    </el-autocomplete>
                </div>
                
                <div class="filter-categories">
                    <span class="cat-label">分类：</span>
                    <div class="cat-list">
                        <a href="javascript:;" 
                           @click="selectCategory('')" 
                           class="cat-item" 
                           :class="{ active: !search.categoryId }">
                           全部
                        </a>
                        <a href="javascript:;" 
                           v-for="c in categories" 
                           :key="c.id" 
                           @click="selectCategory(c.id)" 
                           class="cat-item" 
                           :class="{ active: search.categoryId == c.id }">
                           {{ c.name }}
                        </a>
                    </div>
                </div>
            </div>

            <div class="goods-section">
                <el-empty v-if="!lists.length" description="暂无商品" :image-size="200"></el-empty>
                
                <div v-else class="goods-grid">
                    <div class="goods-card-wrapper" v-for="r in lists" :key="r.id">
                        <div class="goods-card" @click="$router.push({ path: '/mall/detail', query: { id: r.id } })">
                            <div class="card-image">
                                <e-img :src="getProductImage(r)" class="product-img"></e-img>
                                <div class="hover-overlay">
                                    <span class="view-btn">查看详情</span>
                                </div>
                            </div>
                            <div class="card-info">
                                <h4 class="goods-title" :title="r.name">{{ r.name }}</h4>
                                <div class="goods-meta">
                                    <div class="price-box">
                                        <span class="currency">￥</span>
                                        <span class="amount">{{ r.price }}</span>
                                    </div>
                                    <span class="sales-info" v-if="r.sales">销量 {{ r.sales }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="pagination-wrapper">
                    <el-pagination 
                        background
                        @current-change="loadList" 
                        :page-sizes="[12, 24, 36, 48]" 
                        v-model:current-page="search.page" 
                        v-model:page-size="search.size" 
                        @size-change="loadList" 
                        layout="prev, pager, next, sizes, jumper" 
                        :total="totalCount"> 
                    </el-pagination>
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
import { ShoppingCart, List, Close } from '@element-plus/icons-vue';

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

const getRandomHotSearches = () => {
    const pool = ['黑神话：悟空', '原神', '王者荣耀', '只狼', '艾尔登法环', '生化危机8', '战神5', '手办', 'T恤', '鼠标', '挂件', '巫师3', '最终幻想14', '博德之门3'];
    return pool.sort(() => 0.5 - Math.random()).slice(0, 3);
};

const getHistory = () => {
    try {
        return JSON.parse(localStorage.getItem('mall_search_history') || '[]');
    } catch (e) {
        return [];
    }
};

const saveHistory = (keyword) => {
    if (!keyword) return;
    try {
        let history = JSON.parse(localStorage.getItem('mall_search_history') || '[]');
        history = history.filter(h => h !== keyword);
        history.unshift(keyword);
        if (history.length > 10) history = history.slice(0, 10);
        localStorage.setItem('mall_search_history', JSON.stringify(history));
    } catch (e) {
        console.error(e);
    }
};

let lastSearchCallback = null;

const deleteHistoryItem = (item) => {
    try {
        let history = JSON.parse(localStorage.getItem('mall_search_history') || '[]');
        history = history.filter(h => h !== item.value);
        localStorage.setItem('mall_search_history', JSON.stringify(history));
        if (lastSearchCallback) {
            querySearch(search.keyword, lastSearchCallback);
        }
    } catch (e) {
        console.error(e);
    }
};

const querySearch = (queryString, cb) => {
    lastSearchCallback = cb;
    let results = [];
    const history = getHistory();

    if (!queryString) {
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
    } else {
        const historyItems = history.filter(item => item.toLowerCase().includes(queryString.toLowerCase()))
            .slice(0, 10)
            .map(item => ({ value: item, type: 'history' }));
        results = historyItems;
    }
    cb(results);
};

const handleSelect = (item) => {
    if (item.type === 'header') return;
    search.keyword = item.value;
    searchSubmit();
};

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

const mockCategories = [
    { id: 1, name: "手办模型" },
    { id: 2, name: "潮流服饰" },
    { id: 3, name: "数码外设" },
    { id: 4, name: "生活周边" },
    { id: 5, name: "挂件饰品" }
];

const mockProducts = [
    { id: 1, categoryId: 1, name: "韩信街头霸王手办", images: [{url: '/upload/prod1.jpg'}], price: 299, sales: 120 },
    { id: 2, categoryId: 2, name: "原神可莉联名T恤", images: [{url: '/upload/prod2.jpg'}], price: 128, sales: 450 },
    { id: 3, categoryId: 3, name: "王者荣耀电竞鼠标", images: [{url: '/upload/prod3.jpg'}], price: 399, sales: 88 },
    { id: 4, categoryId: 1, name: "崩铁三月七手办", images: [{url: '/upload/prod4.jpg'}], price: 599, sales: 60 },
    { id: 5, categoryId: 4, name: "黑神话：悟空 典藏礼盒套装", images: [{url: '/upload/prod5.jpg'}], price: 888, sales: 2000 },
    { id: 6, categoryId: 1, name: "黑神话：悟空 主题角色手办", images: [{url: '/upload/prod6.jpg'}], price: 450, sales: 300 },
    { id: 7, categoryId: 1, name: "只狼 主题手办摆件", images: [{url: '/upload/prod7.jpg'}], price: 320, sales: 150 },
    { id: 8, categoryId: 5, name: "只狼 小太刀钥匙扣", images: [{url: '/upload/prod8.jpg'}], price: 29, sales: 800 },
    { id: 9, categoryId: 2, name: "艾尔登法环 主题T恤礼盒", images: [{url: '/upload/prod9.jpg'}], price: 159, sales: 220 },
    { id: 10, categoryId: 1, name: "艾尔登法环 角色手办摆件", images: [{url: '/upload/prod10.jpg'}], price: 680, sales: 90 },
    { id: 11, categoryId: 4, name: "黑暗之魂3 主题装饰画海报", images: [{url: '/upload/prod11.jpg'}], price: 49, sales: 300 },
    { id: 12, categoryId: 1, name: "黑暗之魂3 Q版骑士手办", images: [{url: '/upload/prod12.jpg'}], price: 120, sales: 400 },
    { id: 13, categoryId: 4, name: "鬼泣 主题马克杯套装", images: [{url: '/upload/prod13.jpg'}], price: 79, sales: 180 },
    { id: 14, categoryId: 1, name: "鬼泣 主题角色手办", images: [{url: '/upload/prod14.jpg'}], price: 550, sales: 70 },
    { id: 15, categoryId: 5, name: "明末：渊虚之羽 主题挂件", images: [{url: '/upload/prod15.jpg'}], price: 35, sales: 110 },
    { id: 16, categoryId: 5, name: "明末：渊虚之羽 纪念徽章摆件", images: [{url: '/upload/prod16.jpg'}], price: 58, sales: 95 },
    { id: 17, categoryId: 1, name: "巫师3 杰洛特摆件手办", images: [{url: '/upload/prod17.jpg'}], price: 420, sales: 130 },
    { id: 18, categoryId: 4, name: "巫师3 银剑模型摆件", images: [{url: '/upload/prod18.jpg'}], price: 199, sales: 60 },
    { id: 19, categoryId: 4, name: "最终幻想14 主题海报明信片套装", images: [{url: '/upload/prod19.jpg'}], price: 25, sales: 500 },
    { id: 20, categoryId: 2, name: "最终幻想14 主题长袍服饰", images: [{url: '/upload/prod20.jpg'}], price: 220, sales: 150 },
    { id: 21, categoryId: 4, name: "生化危机8 装备微缩模型套装", images: [{url: '/upload/prod21.jpg'}], price: 180, sales: 80 },
    { id: 22, categoryId: 5, name: "生化危机8 暗黑钥匙扣挂件", images: [{url: '/upload/prod22.jpg'}], price: 19, sales: 600 },
    { id: 23, categoryId: 1, name: "博德之门3 队伍手办套装", images: [{url: '/upload/prod23.jpg'}], price: 999, sales: 40 },
    { id: 24, categoryId: 4, name: "博德之门3 主题艺术海报", images: [{url: '/upload/prod24.jpg'}], price: 39, sales: 210 },
    { id: 25, categoryId: 4, name: "战神5 典藏礼盒套装", images: [{url: '/upload/prod25.jpg'}], price: 666, sales: 300 },
    { id: 26, categoryId: 5, name: "战神5 主题武器钥匙扣套装", images: [{url: '/upload/prod26.jpg'}], price: 45, sales: 400 }
];

const loadCategories = async () => {
    categories.value = mockCategories;
};

const loadList = async () => {
    // 模拟筛选和分页
    let result = mockProducts;
    
    if (search.keyword) {
        result = result.filter(p => p.name.toLowerCase().includes(search.keyword.toLowerCase()));
    }
    
    if (search.categoryId) {
        result = result.filter(p => p.categoryId == search.categoryId);
    }
    
    totalCount.value = result.length;
    
    // 分页
    const start = (search.page - 1) * search.size;
    const end = start + search.size;
    lists.value = result.slice(start, end);
};

const searchSubmit = () => {
    saveHistory(search.keyword);
    search.page = 1;
    loadList();
};

const selectCategory = (id) => {
    search.categoryId = id;
    searchSubmit();
};

const getProductImage = (item) => {
    // 优先使用非默认命名的图片（假设默认命名包含 product_，而上传的图片是哈希名）
    const isPreferred = (url) => {
        if (!url) return false;
        const s = String(url);
        return s.indexOf('product_') === -1 && s.indexOf('/upload/') !== -1;
    };

    // 1. 尝试从 images 数组中找
    if (item.images && Array.isArray(item.images)) {
        const found = item.images.find(img => isPreferred(img.url || img));
        if (found) return found.url || found;
    }

    // 2. 检查 cover_url
    if (isPreferred(item.cover_url)) return item.cover_url;

    // 3. 再次尝试 images 数组的第一个
    if (item.images && Array.isArray(item.images) && item.images.length > 0) {
        return item.images[0].url || item.images[0];
    }

    // 4. 兜底
    return item.cover_url || item.image || item.tupian || item.fengmian || item.img || "";
};

onMounted(() => {
    loadCategories();
    loadList();
});
</script>

<style scoped lang="scss">
.views-mall-list {
    padding-bottom: 40px;
    background-color: var(--theme-background-color, #f6f7fb);
    min-height: 100vh;
}

.mall-header {
    padding: 40px 0 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-content {
        text-align: left;
    }

    .header-actions {
        display: flex;
        gap: 15px;
    }
    
    .page-title {
        font-size: 28px;
        font-weight: 700;
        color: var(--theme-color, #2b2d31);
        margin: 0 0 10px;
        letter-spacing: 1px;
    }
    
    .page-desc {
        font-size: 14px;
        color: #888;
        margin: 0;
    }
}

.filter-container {
    background: #fff;
    border-radius: var(--my-radius, 12px);
    padding: 24px;
    margin-bottom: 30px;
    box-shadow: var(--my-shadow, 0 8px 24px rgba(31, 41, 55, 0.05));
    
    .filter-search {
        max-width: 600px;
        margin: 0 auto 24px;
        
        :deep(.el-input__wrapper) {
            border-radius: 20px 0 0 20px;
            box-shadow: none;
            background: #f5f7fa;
            padding-left: 15px;
            
            &.is-focus {
                background: #fff;
                box-shadow: 0 0 0 1px var(--theme-primary-color) inset;
            }
        }
        
        :deep(.el-input-group__append) {
            border-radius: 0 20px 20px 0;
            background: var(--theme-primary-color);
            border: none;
            color: #fff;
            box-shadow: none;
            
            button {
                color: #fff;
                border: none;
                padding: 12px 24px;
                font-weight: 500;
                
                &:hover {
                    background: rgba(255,255,255,0.1);
                }
            }
        }
    }
    
    .filter-categories {
        display: flex;
        align-items: flex-start;
        justify-content: center;
        flex-wrap: wrap;
        gap: 10px;
        
        .cat-label {
            font-weight: 600;
            color: #333;
            margin-top: 6px;
            margin-right: 5px;
        }
        
        .cat-list {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }
        
        .cat-item {
            padding: 6px 16px;
            border-radius: 20px;
            color: #666;
            background: #f5f7fa;
            font-size: 14px;
            transition: all 0.3s ease;
            text-decoration: none;
            
            &:hover {
                color: var(--theme-primary-color);
                background: rgba(51, 204, 204, 0.1);
            }
            
            &.active {
                background: var(--theme-primary-color);
                color: #fff;
                box-shadow: 0 4px 12px rgba(51, 204, 204, 0.3);
            }
        }
    }
}

.goods-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 24px;
    margin-bottom: 40px;
}

.goods-card {
    background: #fff;
    border-radius: var(--my-radius, 12px);
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
    border: 1px solid transparent;
    height: 100%;
    display: flex;
    flex-direction: column;
    
    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 32px rgba(0, 0, 0, 0.08);
        border-color: rgba(0,0,0,0.02);
        
        .hover-overlay {
            opacity: 1;
        }
        
        .product-img {
            transform: scale(1.05);
        }
    }
    
    .card-image {
        position: relative;
        padding-bottom: 100%; /* 1:1 Aspect Ratio */
        overflow: hidden;
        background: #f9f9f9;
        
        .product-img {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.5s ease;
        }
        
        .hover-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0,0,0,0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transition: opacity 0.3s ease;
            
            .view-btn {
                background: #fff;
                color: #333;
                padding: 8px 20px;
                border-radius: 20px;
                font-size: 13px;
                font-weight: 600;
                transform: translateY(10px);
                transition: transform 0.3s;
            }
        }
    }
    
    &:hover .view-btn {
        transform: translateY(0);
    }
    
    .card-info {
        padding: 16px;
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        
        .goods-title {
            font-size: 15px;
            color: #333;
            margin: 0 0 10px;
            line-height: 1.5;
            height: 44px; /* 2 lines */
            overflow: hidden;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
        }
        
        .goods-meta {
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            
            .price-box {
                color: #ff4d4f;
                font-weight: 700;
                line-height: 1;
                
                .currency {
                    font-size: 14px;
                    margin-right: 2px;
                }
                
                .amount {
                    font-size: 20px;
                }
            }
            
            .sales-info {
                font-size: 12px;
                color: #999;
            }
        }
    }
}

.pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 40px;
    
    :deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
        background-color: var(--theme-primary-color);
    }
}

@media (max-width: 768px) {
    .filter-container {
        padding: 16px;
        
        .filter-search {
            margin-bottom: 16px;
        }
        
        .filter-categories {
            justify-content: flex-start;
            
            .cat-label {
                width: 100%;
                margin-bottom: 8px;
            }
        }
    }
    
    .goods-grid {
        grid-template-columns: repeat(2, 1fr);
        gap: 12px;
    }
    
    .goods-card .card-info {
        padding: 10px;
        
        .goods-title {
            font-size: 13px;
            height: 38px;
        }
        
        .price-box .amount {
            font-size: 16px;
        }
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

.suggestion-item {
    display: flex;
    align-items: center;
    width: 100%;
    
    .delete-history {
        color: #999;
        cursor: pointer;
        font-size: 12px;
        margin-left: 10px;
        &:hover {
            color: #f56c6c;
        }
    }
}
</style>
