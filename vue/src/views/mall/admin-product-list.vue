<template>
    <div class="app-container">
        <div class="filter-container">
            <el-input v-model="listQuery.keyword" placeholder="商品名称" style="width: 200px;" class="filter-item" @keyup.enter="handleFilter" />
            <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
                搜索
            </el-button>
            <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="Edit" @click="handleCreate">
                添加商品
            </el-button>
        </div>

        <el-table :data="list" border fit highlight-current-row style="width: 100%; margin-top: 20px;">
            <el-table-column label="ID" prop="id" align="center" width="80">
            </el-table-column>
            <el-table-column label="商品名称" prop="name" align="center">
            </el-table-column>
            <el-table-column label="价格" prop="price" align="center" width="100">
            </el-table-column>
            <el-table-column label="库存" prop="stock" align="center" width="100">
            </el-table-column>
            <el-table-column label="分类" prop="categoryName" align="center" width="120">
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

        <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑商品' : '添加商品'" width="520px">
            <el-form :model="form" label-width="80px">
                <el-form-item label="名称">
                    <el-input v-model="form.name" placeholder="请输入商品名称" />
                </el-form-item>
                <el-form-item label="价格">
                    <el-input v-model.number="form.price" type="number" placeholder="请输入价格" />
                </el-form-item>
                <el-form-item label="库存">
                    <el-input v-model.number="form.stock" type="number" placeholder="请输入库存" />
                </el-form-item>
                <el-form-item label="分类">
                    <el-select v-model="form.categoryName" placeholder="请选择分类" style="width: 100%">
                        <el-option v-for="c in categoryOptions" :key="c.id" :label="c.name" :value="c.name" />
                    </el-select>
                </el-form-item>
                <el-form-item label="封面">
                    <el-input v-model="form.cover_url" placeholder="图片地址（可空）" />
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
import { ref, onMounted, reactive } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getProducts, saveProduct, deleteProduct, listCategories } from '@/module/mall-mock';

const list = ref([]);
const listQuery = reactive({
    keyword: '',
});
const categoryOptions = ref([]);
const dialogVisible = ref(false);
const isEdit = ref(false);
const form = reactive({
    id: null,
    name: '',
    price: 0,
    stock: 0,
    categoryName: '',
    cover_url: '',
});

onMounted(() => {
    loadCategories();
    getList();
});

const loadCategories = () => {
    categoryOptions.value = listCategories();
};

const getList = () => {
    list.value = getProducts(listQuery.keyword);
};

const handleFilter = () => {
    getList();
};

const resetForm = () => {
    form.id = null;
    form.name = '';
    form.price = 0;
    form.stock = 0;
    form.categoryName = '';
    form.cover_url = '';
};

const handleCreate = () => {
    resetForm();
    isEdit.value = false;
    dialogVisible.value = true;
};

const handleUpdate = (row) => {
    Object.assign(form, row);
    isEdit.value = true;
    dialogVisible.value = true;
};

const submit = () => {
    if (!form.name) {
        ElMessage.warning('请输入商品名称');
        return;
    }
    saveProduct({
        id: isEdit.value ? form.id : undefined,
        name: form.name,
        price: Number(form.price) || 0,
        stock: Number(form.stock) || 0,
        categoryName: form.categoryName || '周边',
        cover_url: form.cover_url,
    });
    ElMessage.success(isEdit.value ? '已更新' : '已添加');
    dialogVisible.value = false;
    getList();
};

const handleDelete = (row) => {
    ElMessageBox.confirm('确认删除该商品?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        deleteProduct(row.id);
        ElMessage.success('删除成功');
        getList();
    });
};
</script>
