<template>
    <div class="views-mall-detail">
        <e-container>
            <div class="product-detail-box">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                    <div class="back-link" @click="$router.push('/mall/products')" style="cursor: pointer; font-size: 14px; color: #606266; display: flex; align-items: center;">
                        <el-icon style="margin-right: 4px;"><ArrowLeft /></el-icon> 返回周边商城
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
import DB from "@/utils/db";
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
            let data = res.data;
            
            // 尝试手动获取图片
            try {
                console.log("正在查询 product_image, product_id =", id);
                const images = await DB.name("product_image")
                    .where("product_id", id)
                    .order("sort asc")
                    .select();
                console.log("详情页手动获取图片结果:", images);
                
                if (!images || images.length === 0) {
                    // 调试：查询所有图片看看
                    const allImages = await DB.name("product_image").limit(5).select();
                    console.log("调试：product_image 表的前5条数据:", allImages);
                }

                if (images && Array.isArray(images) && images.length > 0) {
                    data.images = images;
                }
            } catch (err) {
                console.warn("详情页尝试加载 product_image 失败", err);
            }

            product.value = data;
            // 设置默认图片
            let firstImage = product.value.cover_url;
            // 优先使用非 product_ 开头的图片
            if (product.value.images && product.value.images.length > 0) {
                const preferred = product.value.images.find(img => {
                    const url = img.url || img;
                    return url && String(url).indexOf('product_') === -1 && String(url).indexOf('/upload/') !== -1;
                });
                if (preferred) {
                    firstImage = preferred.url || preferred;
                } else if (!firstImage) {
                    firstImage = product.value.images[0].url || product.value.images[0];
                }
            }
            currentImage.value = firstImage;
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
