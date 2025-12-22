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
                            <div class="suggestion-item">
                                <span v-if="item.type === 'history'" style="color: #909399; margin-right: 8px;"><i class="fa fa-history"></i></span>
                                <span v-else style="color: #f56c6c; margin-right: 8px;"><i class="fa fa-fire"></i></span>
                                <span>{{ item.value }}</span>
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
import { ShoppingCart, List } from '@element-plus/icons-vue';

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
    const pool = ['手办', 'T恤', '徽章', '抱枕', '钥匙扣', '鼠标垫', '海报', '公仔', '模型', '卡牌'];
    return pool.sort(() => 0.5 - Math.random()).slice(0, 5).map(item => ({ value: item, type: 'hot' }));
};

const getHistory = () => {
    try {
        const history = JSON.parse(localStorage.getItem('mall_search_history') || '[]');
        return history.map(item => ({ value: item, type: 'history' }));
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
            let records = res.data.records;
            
            // 尝试手动获取图片（如果接口没返回 images）
            try {
                const ids = records.map(r => r.id);
                if (ids.length > 0) {
                    // 尝试从 product_image 表获取图片
                    const images = await DB.name("product_image")
                        .where("product_id", "in", ids)
                        .order("sort asc")
                        .select();
                    
                    console.log("列表页手动获取图片:", images);

                    if (images && Array.isArray(images)) {
                        records.forEach(r => {
                            // 将图片关联到商品
                            const productImages = images.filter(img => img.product_id == r.id);
                            if (productImages.length > 0) {
                                r.images = productImages;
                            }
                        });
                    }
                }
            } catch (err) {
                console.warn("尝试加载 product_images 失败，可能表不存在", err);
            }

            lists.value = records;
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
