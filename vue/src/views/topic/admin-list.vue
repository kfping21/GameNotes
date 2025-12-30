<template>
    <div class="app-container">
        <div class="filter-container">
            <el-input v-model="listQuery.keyword" placeholder="话题名称" style="width: 220px;" class="filter-item" />
            <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
            <el-button class="filter-item" type="primary" icon="Plus" @click="handleCreate">新建话题</el-button>
        </div>

        <el-table :data="list" border fit highlight-current-row style="width: 100%; margin-top: 20px;" v-loading="loading">
            <el-table-column label="ID" prop="id" align="center" width="80"></el-table-column>
            <el-table-column label="话题标题" prop="title" align="center"></el-table-column>
            <el-table-column label="简介" prop="intro" align="center"></el-table-column>
            <el-table-column label="标签ID" prop="tag_ids" align="center" width="160"></el-table-column>
            <el-table-column label="状态" prop="status" align="center" width="120">
                <template #default="{ row }">
                    <el-tag :type="row.status === 'active' ? 'success' : 'info'">{{ row.status || 'active' }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="240">
                <template #default="{ row }">
                    <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
                    <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
            v-model:current-page="listQuery.page"
            v-model:page-size="listQuery.pagesize"
            :total="total"
            layout="total, prev, pager, next"
            background
            style="margin-top: 16px;"
            @current-change="getList"
            @size-change="getList"
        />

        <el-dialog v-model="dialogVisible" :title="form.id ? '编辑话题' : '新建话题'" width="520px">
            <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
                <el-form-item label="话题标题" prop="title" required>
                    <el-input v-model="form.title" placeholder="请输入话题标题" />
                </el-form-item>
                <el-form-item label="简介" prop="intro">
                    <el-input type="textarea" v-model="form.intro" placeholder="简要介绍" rows="3" />
                </el-form-item>
                <el-form-item label="标签ID" prop="tag_ids">
                    <el-input v-model="form.tag_ids" placeholder="用逗号分隔多个标签ID，如 1,2" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择">
                        <el-option label="active" value="active" />
                        <el-option label="inactive" value="inactive" />
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="submitForm">保存</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted, reactive, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { adminTopicList, adminTopicSave, adminTopicDelete } from "@/module/topic";

const list = ref([]);
const loading = ref(false);
const formRef = ref();
const dialogVisible = ref(false);
const form = reactive({ id: null, title: "", intro: "", tag_ids: "", status: "active" });
const rules = {
    title: [{ required: true, message: "请输入话题标题", trigger: "blur" }],
};

const listQuery = reactive({
    keyword: '',
    page: 1,
    pagesize: 10
});
const total = ref(0);

onMounted(() => {
    getList();
});

const getList = async () => {
    loading.value = true;
    const res = await adminTopicList(listQuery);
    if (res.code === 0 || res.code === '0') {
        const lists = res?.data?.lists || res?.data?.records || [];
        list.value = Array.isArray(lists) ? lists : [];
        total.value = res?.data?.total || res?.data?.count || list.value.length;
    } else {
        ElMessage.error(res.msg || '获取数据失败');
    }
    loading.value = false;
};

const handleFilter = () => {
    listQuery.page = 1;
    getList();
};

const resetForm = () => {
    form.id = null;
    form.title = "";
    form.intro = "";
    form.tag_ids = "";
    form.status = "active";
    nextTick(() => formRef.value?.clearValidate());
};

const handleCreate = () => {
    resetForm();
    dialogVisible.value = true;
};

const handleEdit = (row) => {
    resetForm();
    Object.assign(form, row);
    dialogVisible.value = true;
};

const submitForm = () => {
    formRef.value.validate(async (valid) => {
        if (!valid) return;
        const payload = { ...form };
        const res = await adminTopicSave(payload);
        if (res.code === 0 || res.code === '0') {
            ElMessage.success('保存成功');
            dialogVisible.value = false;
            getList();
        } else {
            ElMessage.error(res.msg || '保存失败');
        }
    });
};

const handleDelete = (row) => {
    ElMessageBox.confirm(`确认删除话题【${row.title}】?`, '提示').then(async () => {
        const res = await adminTopicDelete(row.id);
        if (res.code === 0 || res.code === '0') {
            ElMessage.success('删除成功');
            getList();
        } else {
            ElMessage.error(res.msg || '删除失败');
        }
    }).catch(() => {});
};
</script>
