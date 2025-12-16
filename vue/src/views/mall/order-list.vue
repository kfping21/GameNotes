<template>
    <div class="views-mall-order-list">
        <e-container>
            <div class="order-list-box">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px;">
                    <h3 class="section-title" style="margin-bottom: 0; border-bottom: none; padding-bottom: 0;">我的订单</h3>
                    <div>
                        <el-button type="success" @click="$router.push('/mall/products')">继续购物</el-button>
                        <el-button type="primary" @click="$router.push('/mall/cart')">我的购物车</el-button>
                    </div>
                </div>
                
                <el-empty v-if="!loading && !list.length" description="暂无订单"></el-empty>

                <div v-else v-loading="loading">
                    <div class="order-item" v-for="item in list" :key="item.id">
                        <div class="item-header">
                            <span class="time">{{ item.addtime }}</span>
                            <span class="order-no">订单号：{{ item.id }}</span>
                            <span class="status">{{ item.status }}</span>
                        </div>
                        <div class="item-body" @click="$router.push({ path: '/mall/order/detail', query: { id: item.id } })">
                            <div class="info-row">
                                <div class="total">
                                    总额：<span class="price">￥{{ item.total_amount }}</span>
                                </div>
                                <el-button type="primary" link>查看详情 ></el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import http from "@/utils/ajax/http";
import { ref, onMounted } from "vue";

const list = ref([]);
const loading = ref(false);

const loadList = async () => {
    loading.value = true;
    try {
        const res = await http.get("/api/order/list");
        if (res.code === 0) {
            list.value = res.data || [];
        }
    } catch (e) {
        console.error(e);
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    loadList();
});
</script>

<style scoped>
.order-list-box {
    background: #fff;
    padding: 20px;
    min-height: 500px;
}
.section-title {
    font-size: 20px;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #eee;
}
.order-item {
    border: 1px solid #eee;
    margin-bottom: 20px;
    border-radius: 4px;
}
.item-header {
    background: #f5f7fa;
    padding: 10px 15px;
    display: flex;
    justify-content: space-between;
    color: #666;
    font-size: 14px;
}
.item-body {
    padding: 20px;
    cursor: pointer;
}
.item-body:hover {
    background: #fcfcfc;
}
.info-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.price {
    color: #f56c6c;
    font-weight: bold;
    font-size: 18px;
}
.status {
    color: #409eff;
    font-weight: bold;
}
</style>
