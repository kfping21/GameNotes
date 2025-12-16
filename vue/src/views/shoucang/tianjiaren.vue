<template>
    <div class="views-shoucang-list">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 收到的收藏 </span>
                    </div>
                </template>

                <div class="form-search">
                    <el-form @submit.prevent.stop :inline="true" size="small">
                        <el-form-item label="标题">
                            <el-input v-model="search.biaoti"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="searchSubmit" icon="el-icon-search">查询</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <el-table border :data="lists" style="width: 100%" @sort-change="sortChange" highlight-current-row @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55"> </el-table-column
                    ><!-- 批量选择 -->

                    <el-table-column prop="username" label="收藏人" width="180">
                        <template #default="{ row }"> {{ row.username }} </template>
                    </el-table-column>
                    <el-table-column prop="biaoti" label="标题">
                        <template #default="{ row }"> {{ row.biaoti }} </template>
                    </el-table-column>
                    <el-table-column prop="addtime" label="收藏时间">
                        <template #default="{ row }"> {{ row.addtime.substring(0, 19) }} </template>
                    </el-table-column>

                    <el-table-column label="操作" fixed="right" width="250">
                        <template #default="{ row }">
                            <el-button-group>
                                <el-button size="small" @click="$router.push('/' + row.biao + '/detail?id=' + row.xwid)">详情</el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
                <div style="margin-top: 10px">
                    <el-button type="default" @click="batchDelete" :disabled="multipleSelection.length == 0">批量删除</el-button>
                </div>
                <div class="e-pages" style="margin-top: 10px; text-align: center">
                    <el-pagination @current-change="loadList" :page-sizes="[12, 24, 36, 48, 60]" v-model:current-page="search.page" v-model:page-size="search.pagesize" @size-change="sizeChange" layout="total, sizes, prev, pager, next" :total="totalCount"> </el-pagination>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import { ref, reactive, watch, unref, onBeforeMount } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { Delete, Plus, Search } from "@element-plus/icons-vue";

    const route = useRoute();
    const search = reactive({
        biaoti: "",
        page: 1,
        pagesize: 12,
    });
    const lists = ref([]);
    const totalCount = ref(0);
    const loading = ref(false);
    const multipleSelection = ref([]);

    const handleSelectionChange = (val) => {
        multipleSelection.value = val;
    };

    const deleteItems = (ids) => {
        ElMessageBox.confirm("确定删除？", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
        }).then(() => {
            http.post("/api/shoucang/delete", ids).then((res) => {
                ElMessage.success("删除成功");
                loadList(search.page);
            });
        });
    };

    const batchDelete = () => {
        if (multipleSelection.value.length == 0) return;
        deleteItems(multipleSelection.value.map((o) => o.id));
    };

    const sortChange = (e) => {
        console.log(e);
        if (e.order) {
            search.order = e.prop;
            search.sort = e.order == "ascending" ? "asc" : "desc";
        } else {
            search.order = "";
            search.sort = "";
        }
        loadList(1);
    };

    const sizeChange = (val) => {
        search.pagesize = val;
        loadList(1);
    };

    // 加载收藏列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/shoucang/selectTianjiaren", search).then(
            (res) => {
                loading.value = false;
                if (res.code == 0) {
                    var data = res.data;
                    lists.value = data.lists.records;
                    totalCount.value = data.lists.total;
                }
            },
            (err) => {
                loading.value = false;
                ElMessage.error(err.message);
            }
        );
    };

    onBeforeMount(() => {
        loadList(1);
    });
    const searchSubmit = () => {
        loadList(1);
    };
</script>

<style scoped lang="scss">
    .views-shoucang-list {
    }
</style>
