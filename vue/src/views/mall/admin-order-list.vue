<template>
    <div class="app-container">
        <el-table :data="list" border fit highlight-current-row style="width: 100%">
            <el-table-column label="订单号" prop="orderNo" align="center" width="180">
            </el-table-column>
            <el-table-column label="用户" prop="username" align="center" width="120">
            </el-table-column>
            <el-table-column label="总金额" prop="totalAmount" align="center" width="100">
            </el-table-column>
            <el-table-column label="状态" prop="status" align="center" width="100">
                <template #default="{ row }">
                    <el-tag :type="row.status === '已完成' ? 'success' : 'warning'">{{ row.status }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="下单时间" prop="createTime" align="center" width="180">
            </el-table-column>
            <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                <template #default="{ row }">
                    <el-button type="primary" size="small" @click="handleDetail(row)">
                        查看详情
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="detailVisible" title="订单详情" width="640px">
            <div v-if="current">
                <div style="margin-bottom: 12px; color: #606266;">
                    <div>订单号：{{ current.orderNo }}</div>
                    <div>用户：{{ current.username }}</div>
                    <div>状态：{{ current.status }}</div>
                    <div>下单时间：{{ current.createTime }}</div>
                    <div>总金额：￥{{ current.totalAmount }}</div>
                </div>
                <el-table :data="current.items || []" size="small" border>
                    <el-table-column label="商品" prop="name" min-width="200"></el-table-column>
                    <el-table-column label="单价" width="120">
                        <template #default="{ row }">￥{{ row.price }}</template>
                    </el-table-column>
                    <el-table-column label="数量" prop="quantity" width="100"></el-table-column>
                    <el-table-column label="小计" width="120">
                        <template #default="{ row }">￥{{ (row.price * row.quantity).toFixed(2) }}</template>
                    </el-table-column>
                </el-table>
            </div>
            <template #footer>
                <el-button @click="detailVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { listOrders, getOrderDetail } from '@/module/mall-mock';

const list = ref([]);
const detailVisible = ref(false);
const current = ref(null);

onMounted(() => {
    getList();
});

const getList = () => {
    list.value = listOrders();
};

const handleDetail = (row) => {
    current.value = getOrderDetail(row.id || row.orderNo);
    detailVisible.value = true;
};
</script>
