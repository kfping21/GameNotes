<template>
    <div class="views-mall-detail">
        <e-container>
            <div class="product-detail-box">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                    <div class="back-link" @click="$router.push('/mall/products')" style="cursor: pointer; font-size: 14px; color: #606266; display: flex; align-items: center;">
                        <el-icon style="margin-right: 4px;"><ArrowLeft /></el-icon> 返回周边商城
                    </div>
                    <div>
                        <el-button type="primary" @click="$router.push('/mall/cart')">我的购物车</el-button>
                        <el-button type="warning" @click="$router.push('/mall/order/list')">我的订单</el-button>
                    </div>
                </div>
                <el-row :gutter="30">
                    <!-- 左侧图片 -->
                    <el-col :md="10" :sm="24">
                        <div class="product-gallery">
                            <div class="main-image">
                                <e-img :src="currentImage" style="width: 100%; border-radius: 4px;" />
                            </div>
                            <div class="thumb-list" v-if="product.images && product.images.length > 0">
                                <div 
                                    v-for="(img, index) in product.images" 
                                    :key="index" 
                                    class="thumb-item" 
                                    :class="{ active: currentImage === img.url }"
                                    @click="currentImage = img.url"
                                >
                                    <e-img :src="img.url" style="width: 100%; height: 100%; object-fit: cover;" />
                                </div>
                            </div>
                        </div>
                    </el-col>
                    
                    <!-- 右侧信息 -->
                    <el-col :md="14" :sm="24">
                        <div class="product-info">
                            <h1 class="product-title">{{ product.name }}</h1>
                            
                            <div class="product-meta">
                                <div class="price-box">
                                    <span class="currency">￥</span>
                                    <span class="price">{{ product.price }}</span>
                                </div>
                                <div class="stock-box">
                                    库存: {{ product.stock }}
                                </div>
                            </div>

                            <div class="product-tags" v-if="product.tags">
                                <el-tag v-for="tag in product.tags.split(',')" :key="tag" style="margin-right: 8px;">{{ tag }}</el-tag>
                            </div>

                            <div class="related-game" v-if="product.game">
                                <span class="label">关联游戏:</span>
                                <router-link :to="{ path: '/youxi/detail', query: { id: product.game.id } }" class="game-link">
                                    {{ product.game.youximingcheng }}
                                </router-link>
                            </div>

                            <div class="action-box">
                                <el-button type="primary" size="large" @click="buyNow">立即购买</el-button>
                                <el-button size="large" @click="addToCart">加入购物车</el-button>
                            </div>

                            <div class="product-intro-section">
                                <h3>商品详情</h3>
                                <div class="intro-content" v-html="product.intro || '暂无详情'"></div>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import http from "@/utils/ajax/http";
import { ref, onMounted, watch } from "vue";
import { useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";

import router from "@/router";
import { ArrowLeft } from '@element-plus/icons-vue';

const route = useRoute();
const product = ref({});
const currentImage = ref("");

const loadDetail = async (id) => {
    if (!id) return;
    try {
        const res = await http.get(`/api/mall/products/${id}`);
        if (res.code === 0) {
            product.value = res.data;
            // 设置默认图片
            currentImage.value = product.value.cover_url;
            // 如果有图集，且第一张不是封面，也可以考虑逻辑。这里简单处理：默认显示封面，点击图集切换
        } else {
            ElMessage.error(res.msg || "加载商品详情失败");
        }
    } catch (e) {
        console.error("加载详情异常", e);
        ElMessage.error("加载详情异常");
    }
};

const buyNow = async () => {
    // 立即购买逻辑：先加入购物车，然后跳转到购物车页面（或者直接结算，视需求而定）
    // 这里简化为：加入购物车 -> 跳转购物车
    if (!product.value.id) return;
    
    try {
        const res = await http.post("/api/cart/add", { productId: product.value.id, quantity: 1 });
        if (res.code === 0) {
            router.push('/mall/cart');
        } else {
            ElMessage.error(res.msg || "操作失败");
        }
    } catch (e) {
        console.error(e);
        // 检查是否是未登录导致的 401
        if (e.response && e.response.status === 401) {
             ElMessage.error("请先登录");
             router.push('/login');
        } else {
             ElMessage.error("操作异常，请稍后重试");
        }
    }
};

const addToCart = async () => {
    if (!product.value.id) return;
    
    try {
        const res = await http.post("/api/cart/add", { productId: product.value.id, quantity: 1 });
        if (res.code === 0) {
            ElMessage.success('成功加入购物车！');
        } else {
            ElMessage.error(res.msg || "加入失败");
        }
    } catch (e) {
        console.error(e);
        if (e.response && e.response.status === 401) {
             ElMessage.error("请先登录");
             router.push('/login');
        } else {
             ElMessage.error("操作异常，请稍后重试");
        }
    }
};

watch(() => route.query.id, (newId) => {
    if (newId) loadDetail(newId);
}, { immediate: true });

</script>

<style scoped>
.product-detail-box {
    background: #fff;
    padding: 30px;
    border-radius: 8px;
    min-height: 600px;
}
.product-title {
    font-size: 24px;
    color: #333;
    margin-bottom: 20px;
}
.product-meta {
    background: #f5f7fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    display: flex;
    align-items: baseline;
    justify-content: space-between;
}
.price-box {
    color: #f56c6c;
}
.currency {
    font-size: 18px;
}
.price {
    font-size: 28px;
    font-weight: bold;
}
.stock-box {
    color: #999;
    font-size: 14px;
}
.product-tags {
    margin-bottom: 20px;
}
.related-game {
    margin-bottom: 20px;
    font-size: 14px;
    color: #666;
}
.game-link {
    color: #409eff;
    text-decoration: none;
    margin-left: 8px;
}
.action-box {
    margin-bottom: 30px;
    border-bottom: 1px solid #eee;
    padding-bottom: 30px;
}
.product-intro-section h3 {
    font-size: 18px;
    margin-bottom: 15px;
    border-left: 4px solid #409eff;
    padding-left: 10px;
}
.intro-content {
    line-height: 1.6;
    color: #666;
}
.product-gallery {
    margin-bottom: 20px;
}
.main-image {
    margin-bottom: 10px;
    border: 1px solid #eee;
    padding: 5px;
}
.thumb-list {
    display: flex;
    gap: 10px;
    overflow-x: auto;
}
.thumb-item {
    width: 80px;
    height: 80px;
    border: 2px solid transparent;
    cursor: pointer;
    border-radius: 4px;
    overflow: hidden;
}
.thumb-item.active {
    border-color: #409eff;
}
</style>
