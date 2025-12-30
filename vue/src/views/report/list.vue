<template>
    <div class="app-container">
        <div class="filter-container">
            <el-input v-model="listQuery.keyword" placeholder="举报内容/原因" style="width: 200px;" class="filter-item" />
            <el-select v-model="listQuery.status" placeholder="状态" clearable class="filter-item" style="width: 130px">
                <el-option label="待处理" value="pending" />
                <el-option label="已处理" value="resolved" />
            </el-select>
            <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
                搜索
            </el-button>
        </div>

        <el-table :data="list" border fit highlight-current-row style="width: 100%; margin-top: 20px;">
            <el-table-column label="ID" prop="id" align="center" width="80">
            </el-table-column>
            <el-table-column label="被举报内容" prop="content" align="center">
            </el-table-column>
            <el-table-column label="举报原因" prop="reason" align="center" width="150">
            </el-table-column>
            <el-table-column label="举报人" prop="reporter" align="center" width="120">
            </el-table-column>
            <el-table-column label="举报时间" prop="createTime" align="center" width="180">
            </el-table-column>
            <el-table-column label="状态" prop="status" align="center" width="100">
                <template #default="{ row }">
                    <el-tag :type="row.status === '已处理' ? 'success' : 'danger'">{{ row.status }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
                <template #default="{ row }">
                    <el-button v-if="row.status !== '已处理'" type="primary" size="small" @click="handleResolve(row)">
                        处理
                    </el-button>
                    <el-button type="info" size="small" @click="handleDetail(row)">
                        详情
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const list = ref([
    { id: 1, content: '这条评论涉及人身攻击...', reason: '辱骂/谩骂', reporter: 'user123', createTime: '2023-12-30 14:00:00', status: '待处理' },
    { id: 2, content: '发布虚假广告...', reason: '垃圾广告', reporter: 'user456', createTime: '2023-12-29 10:00:00', status: '已处理' },
]);

const listQuery = reactive({
    keyword: '',
    status: ''
});

const handleFilter = () => {
    console.log('Filter');
};

const handleResolve = (row) => {
    ElMessageBox.confirm('确认将该举报标记为已处理?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        row.status = '已处理';
        ElMessage.success('处理成功');
    });
};

const handleDetail = (row) => {
    console.log('Detail', row);
};
</script>
