<template>
    <div class="views-pinglun-list">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 收到的评论 </span>
                    </div>
                </template>

                <el-table border :data="lists" style="width: 100%" @sort-change="sortChange" highlight-current-row>
                    <el-table-column type="index" label="#"></el-table-column>
                    <!-- 序号 -->

                    <el-table-column prop="biaoti" label="标题">
                        <template #default="{ row }"> {{ row.biaoti }} </template>
                    </el-table-column>
                    <el-table-column prop="pingfen" label="评分">
                        <template #default="{ row }"> <el-rate v-model="row.pingfen" disabled /> </template>
                    </el-table-column>
                    <el-table-column prop="pinglunneirong" label="评论内容">
                        <template #default="{ row }"> {{ row.pinglunneirong }} </template>
                    </el-table-column>
                    <el-table-column prop="pinglunren" label="评论人" width="180">
                        <template #default="{ row }"> {{ row.pinglunren }} </template>
                    </el-table-column>
                    <el-table-column prop="addtime" label="评论时间">
                        <template #default="{ row }"> {{ row.addtime.substring(0, 19) }} </template>
                    </el-table-column>

                    <el-table-column label="操作" fixed="right" width="250">
                        <template #default="{ row }">
                            <el-button-group>
                                <el-button size="small" @click="$router.push('/' + row.biao + '/detail?id=' + row.biaoid)">详情</el-button>
                            </el-button-group>
                        </template>
                    </el-table-column>
                </el-table>
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
    import { Delete, Plus, Search, Edit } from "@element-plus/icons-vue";

    const route = useRoute();
    const search = reactive({
        page: 1,
        pagesize: 12,
    });
    const lists = ref([]);
    const totalCount = ref(0);
    const loading = ref(false);

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

    // 加载评论列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/pinglun/selectTianjiaren", search).then(
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
</script>

<style scoped lang="scss">
    .views-pinglun-list {
    }
</style>
