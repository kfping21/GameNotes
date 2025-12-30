<template>
    <div class="app-container">
        <div class="filter-container">
            <el-button class="filter-item" type="primary" icon="Plus" @click="handleCreate">
                添加分类
            </el-button>
        </div>

        <el-table :data="list" border fit highlight-current-row style="width: 100%; margin-top: 20px;">
            <el-table-column label="ID" prop="id" align="center" width="80">
            </el-table-column>
            <el-table-column label="分类名称" prop="name" align="center">
            </el-table-column>
            <el-table-column label="排序" prop="sort" align="center" width="100">
            </el-table-column>
            <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
                <template #default="{ row }">
                    <el-button type="primary" size="small" @click="handleUpdate(row)">
                        编辑
                    </el-button>
                    <el-button type="danger" size="small" @click="handleDelete(row)">
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '添加分类'" width="420px">
            <el-form :model="form" label-width="80px">
                <el-form-item label="名称">
                    <el-input v-model="form.name" placeholder="请输入分类名称" />
                </el-form-item>
                <el-form-item label="排序">
                    <el-input v-model.number="form.sort" type="number" placeholder="请输入排序" />
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submit">确定</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { listCategories, saveCategory, deleteCategory } from '@/module/mall-mock';

const list = ref(listCategories());
const dialogVisible = ref(false);
const isEdit = ref(false);
const form = ref({ id: null, name: '', sort: 1 });

const refresh = () => {
    list.value = listCategories();
};

const handleCreate = () => {
    form.value = { id: null, name: '', sort: list.value.length + 1 };
    isEdit.value = false;
    dialogVisible.value = true;
};

const handleUpdate = (row) => {
    form.value = { ...row };
    isEdit.value = true;
    dialogVisible.value = true;
};

const submit = () => {
    if (!form.value.name) {
        ElMessage.warning('请输入分类名称');
        return;
    }
    saveCategory({ id: isEdit.value ? form.value.id : undefined, name: form.value.name, sort: Number(form.value.sort) || 1 });
    ElMessage.success(isEdit.value ? '已更新' : '已添加');
    dialogVisible.value = false;
    refresh();
};

const handleDelete = (row) => {
    ElMessageBox.confirm('确认删除该分类?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        deleteCategory(row.id);
        ElMessage.success('删除成功');
        refresh();
    });
};
</script>
