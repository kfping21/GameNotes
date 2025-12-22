<template>
    <div class="views-mall-order-detail">
        <e-container>
            <div class="order-box" v-loading="loading">
                <div class="page-header" style="display: flex; justify-content: space-between; align-items: center;">
                    <el-page-header @back="$router.go(-1)" content="订单详情" />
                    <div>
                        <el-button type="success" @click="$router.push('/mall/products')">继续购物</el-button>
                        <el-button type="primary" @click="$router.push('/mall/cart')">我的购物车</el-button>
                    </div>
                </div>

                <div v-if="order" class="order-content">
                    <div class="status-bar">
                        <div class="status-text">
                            当前状态：<span class="status">{{ order.status || order.state || order.orderStatus || (order.order && order.order.status) }}</span>
                        </div>
                        <div class="order-meta">
                            <span>订单号：{{ order.id || order.orderId || order.order_id || (order.order && (order.order.id || order.order.orderId || order.order.order_id)) }}</span>
                            <span>下单时间：{{ order.addtime || order.create_time || order.created_at || order.createdAt || order.add_time || order.createTime || order.time || order.date || (order.order && (order.order.addtime || order.order.create_time || order.order.created_at || order.order.createdAt || order.order.add_time || order.order.createTime || order.order.time || order.order.date)) }}</span>
                        </div>
                    </div>

                    <div class="goods-list">
                        <h4>商品清单</h4>
                        <el-table :data="order.items || (order.order && order.order.items) || []" border>
                            <el-table-column label="商品" min-width="300">
                                <template #default="{ row }">
                                    <div class="goods-info">
                                        <div class="goods-img-wrapper">
                                            <e-img :src="row.cover_url" pb="100" />
                                        </div>
                                        <div class="goods-name">{{ row.name }}</div>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column prop="price" label="单价" width="120">
                                <template #default="{ row }">￥{{ row.price || row.unit_price || row.money }}</template>
                            </el-table-column>
                            <el-table-column prop="quantity" label="数量" width="100"></el-table-column>
                            <el-table-column label="小计" width="120">
                                <template #default="{ row }">￥{{ ((row.price || row.unit_price || row.money || 0) * row.quantity).toFixed(2) }}</template>
                            </el-table-column>
                        </el-table>
                    </div>

                    <div class="order-footer">
                        <div class="total-row">
                            <span class="label">订单总额：</span>
                            <span class="amount">￥{{ order.total_amount || order.totalAmount || order.amount || order.total_price || order.totalPrice || order.total || order.sum || (order.order && (order.order.total_amount || order.order.totalAmount || order.order.amount || order.order.total_price || order.order.totalPrice || order.order.total || order.order.sum)) }}</span>
                        </div>
                    </div>
                </div>
                
                <el-empty v-else-if="!loading" description="未找到订单信息"></el-empty>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import http from "@/utils/ajax/http";
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { ElMessage } from "element-plus";

const route = useRoute();
const order = ref(null);
const loading = ref(false);

const loadOrder = async () => {
    const id = route.query.id;
    if (!id) return;
    
    loading.value = true;
    try {
        const res = await http.get("/api/order/detail", { id });
        if (res.code === 0) {
            order.value = res.data;
        } else {
            ElMessage.error(res.msg || "加载订单失败");
        }
    } catch (e) {
        console.error(e);
        ElMessage.error("加载异常");
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    loadOrder();
});
</script>

<style scoped>
.order-box {
    background: #fff;
    padding: 20px;
    min-height: 500px;
}
.page-header {
    margin-bottom: 20px;
    border-bottom: 1px solid #eee;
    padding-bottom: 15px;
}
.status-bar {
    background: #f5f7fa;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;
}
.status-text {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
}
.status {
    color: #409eff;
}
.order-meta {
    color: #666;
    font-size: 14px;
}
.order-meta span {
    margin-right: 30px;
}
.goods-list h4 {
    margin-bottom: 15px;
    border-left: 4px solid #409eff;
    padding-left: 10px;
}
.goods-info {
    display: flex;
    align-items: center;
}
.goods-img-wrapper {
    width: 60px;
    height: 60px;
    margin-right: 10px;
    border: 1px solid #eee;
    background-color: #fff;
    flex-shrink: 0;
}
.goods-img-wrapper :deep(.img) {
    background-size: contain !important;
}
.order-footer {
    margin-top: 20px;
    text-align: right;
    padding-top: 20px;
    border-top: 1px solid #eee;
}
.total-row {
    font-size: 16px;
}
.amount {
    font-size: 24px;
    color: #f56c6c;
    font-weight: bold;
}
</style>
