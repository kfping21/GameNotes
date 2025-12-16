<template>
    <div class="views-mall-cart">
        <e-container>
            <div class="cart-box">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px;">
                    <h3 class="section-title" style="margin-bottom: 0; border-bottom: none; padding-bottom: 0;">我的购物车</h3>
                    <div>
                        <el-button type="success" @click="$router.push('/mall/products')">继续购物</el-button>
                        <el-button type="warning" @click="$router.push('/mall/order/list')">我的订单</el-button>
                    </div>
                </div>
                
                <el-empty v-if="!loading && !list.length" description="购物车空空如也">
                    <el-button type="primary" @click="$router.push('/mall/products')">去逛逛</el-button>
                </el-empty>

                <div v-else>
                    <el-table :data="list" style="width: 100%" v-loading="loading">
                        <el-table-column label="商品信息" min-width="300">
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
                            <template #default="{ row }">￥{{ row.price }}</template>
                        </el-table-column>
                        <el-table-column label="数量" width="180">
                            <template #default="{ row }">
                                <el-input-number 
                                    v-model="row.quantity" 
                                    :min="1" 
                                    :max="row.stock" 
                                    size="small"
                                    @change="(val) => updateQuantity(row, val)"
                                />
                                <div class="stock-tip">库存: {{ row.stock }}</div>
                            </template>
                        </el-table-column>
                        <el-table-column label="小计" width="120">
                            <template #default="{ row }">
                                <span class="subtotal">￥{{ (row.price * row.quantity).toFixed(2) }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="100">
                            <template #default="{ row }">
                                <el-button type="danger" link @click="removeItem(row)">删除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                    <div class="cart-footer">
                        <div class="left">
                            <el-button @click="clearCart">清空购物车</el-button>
                        </div>
                        <div class="right">
                            <span class="total-label">合计：</span>
                            <span class="total-price">￥{{ totalPrice }}</span>
                            <el-button type="primary" size="large" class="checkout-btn" @click="checkout" :loading="submitting">去结算</el-button>
                        </div>
                    </div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import http from "@/utils/ajax/http";
import { ref, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import router from "@/router";

const list = ref([]);
const loading = ref(false);
const submitting = ref(false);

const totalPrice = computed(() => {
    let total = 0;
    list.value.forEach(item => {
        total += item.price * item.quantity;
    });
    return total.toFixed(2);
});

const loadCart = async () => {
    loading.value = true;
    try {
        const res = await http.get("/api/cart/list");
        if (res.code === 0) {
            // 兼容处理：如果返回的是 { records: [...] } 结构，则取 records
            if (res.data && Array.isArray(res.data.records)) {
                list.value = res.data.records;
            } else if (Array.isArray(res.data)) {
                list.value = res.data;
            } else {
                list.value = [];
            }
            console.log("购物车数据:", list.value);
        } else {
            console.warn("获取购物车失败:", res.msg);
        }
    } catch (e) {
        console.error("加载购物车异常:", e);
    } finally {
        loading.value = false;
    }
};

const updateQuantity = async (row, val) => {
    try {
        await http.post("/api/cart/update", { productId: row.product_id, quantity: val });
    } catch (e) {
        console.error(e);
        ElMessage.error("更新数量失败");
        loadCart(); // 失败回滚
    }
};

const removeItem = (row) => {
    ElMessageBox.confirm('确定要从购物车删除该商品吗？', '提示', {
        type: 'warning'
    }).then(async () => {
        try {
            const res = await http.post("/api/cart/remove", { productId: row.product_id });
            if (res.code === 0) {
                ElMessage.success("删除成功");
                loadCart();
            }
        } catch (e) {
            console.error(e);
        }
    }).catch(() => {});
};

const clearCart = () => {
    ElMessageBox.confirm('确定要清空购物车吗？', '提示', {
        type: 'warning'
    }).then(async () => {
        try {
            const res = await http.post("/api/cart/clear");
            if (res.code === 0) {
                ElMessage.success("清空成功");
                list.value = [];
            }
        } catch (e) {
            console.error(e);
        }
    }).catch(() => {});
};

const checkout = async () => {
    if (!list.value.length) return;
    
    submitting.value = true;
    try {
        const res = await http.post("/api/order/createFromCart");
        if (res.code === 0) {
            ElMessage.success("下单成功");
            const orderId = res.data.orderId;
            router.push({ path: '/mall/order/detail', query: { id: orderId } });
        } else {
            ElMessage.error(res.msg || "下单失败");
        }
    } catch (e) {
        console.error(e);
        ElMessage.error("下单异常");
    } finally {
        submitting.value = false;
    }
};

onMounted(() => {
    loadCart();
});
</script>

<style scoped>
.cart-box {
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
.goods-info {
    display: flex;
    align-items: center;
}
.goods-img-wrapper {
    width: 70px;
    height: 70px;
    margin-right: 15px;
    border-radius: 4px;
    border: 1px solid #eee;
    background-color: #fff;
    flex-shrink: 0;
}
.goods-img-wrapper :deep(.img) {
    background-size: contain !important;
}
.goods-name {
    font-size: 14px;
    color: #333;
}
.stock-tip {
    font-size: 12px;
    color: #999;
    margin-top: 4px;
}
.subtotal {
    color: #f56c6c;
    font-weight: bold;
}
.cart-footer {
    margin-top: 30px;
    padding: 20px;
    background: #fcfcfc;
    border: 1px solid #eee;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.total-label {
    font-size: 14px;
}
.total-price {
    font-size: 24px;
    color: #f56c6c;
    font-weight: bold;
    margin-right: 20px;
}
.checkout-btn {
    width: 120px;
}
</style>
