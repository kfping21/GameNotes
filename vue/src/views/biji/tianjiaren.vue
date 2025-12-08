<template>
    <div class="views-biji-list">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 笔记查询 </span>
                    </div>
                </template>

                <div class="form-search">
                    <el-form @submit.prevent.stop :inline="true" size="small">
                        <el-form-item label="关键字">
                            <el-input v-model="search.keyword" style="width: 150px" placeholder="搜索关键字"></el-input>
                        </el-form-item>
                        <el-form-item label="笔记名称">
                            <el-input v-model="search.bijimingcheng"></el-input>
                        </el-form-item>
                        <el-form-item label="标签">
                            <el-select v-model="search.biaoqian"><el-option label="请选择" value=""></el-option><e-select-option type="option" module="biaoqian" value="id" label="biaoqianmingcheng"></e-select-option></el-select>
                        </el-form-item>
                        <el-form-item label="关联游戏">
                            <el-select v-model="search.guanlianyouxi"><el-option label="请选择" value=""></el-option><e-select-option type="option" module="youxi" value="id" label="youximingcheng"></e-select-option></el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="searchSubmit" icon="el-icon-search">查询</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <el-table border :data="lists" style="width: 100%" @sort-change="sortChange" highlight-current-row>
                    <el-table-column type="index" label="#"></el-table-column>
                    <!-- 序号 -->

                    <el-table-column prop="bijimingcheng" label="笔记名称">
                        <template #default="{ row }"> {{ row.bijimingcheng }} </template>
                    </el-table-column>
                    <el-table-column prop="bijifengmian" label="笔记封面" width="100">
                        <template #default="{ row }">
                            <div style="width: 100%; max-width: 120px">
                                <e-img :src="row.bijifengmian" type="list"></e-img>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="biaoqian" label="标签">
                        <template #default="{ row }"> <e-select-view module="biaoqian" :value="row.biaoqian" select="id" show="biaoqianmingcheng"></e-select-view> </template>
                    </el-table-column>
                    <el-table-column prop="guanlianyouxi" label="关联游戏" width="100">
                        <template #default="{ row }"> <e-select-view module="youxi" :value="row.guanlianyouxi" select="id" show="youximingcheng"></e-select-view> </template>
                    </el-table-column>
                    <el-table-column prop="shipin" label="视频">
                        <template #default="{ row }"> <e-file-list v-model="row.shipin"></e-file-list> </template>
                    </el-table-column>
                    <el-table-column sortable="custom" prop="guanzhuliang" label="关注量" width="80">
                        <template #default="{ row }"> {{ row.guanzhuliang }} </template>
                    </el-table-column>
                    <el-table-column sortable="custom" prop="zhongcaodu" label="种草度" width="80">
                        <template #default="{ row }"> {{ row.zhongcaodu }} </template>
                    </el-table-column>
                    <el-table-column prop="tianjiaren" label="添加人" width="180">
                        <template #default="{ row }"> {{ row.tianjiaren }} </template>
                    </el-table-column>

                    <el-table-column width="80" label="审核">
                        <template #default="{ row }">
                            {{ row.issh }}
                        </template>
                    </el-table-column>

                    <el-table-column label="操作" fixed="right" width="250">
                        <template #default="{ row }">
                            <el-button-group>
                                <el-button type="primary" :icon="Plus" size="small" @click="$router.push('/admin/sixin/add?id=' + row.id)">添加私信</el-button>

                                <el-button type="default" :icon="Search" size="small" @click="$router.push('/admin/sixin?bijiid=' + row.id)">查询私信</el-button>

                                <el-button type="primary" :icon="Plus" size="small" @click="$router.push('/admin/guanzhu/add?id=' + row.id)">添加关注</el-button>

                                <el-button type="default" :icon="Search" size="small" @click="$router.push('/admin/guanzhu?bijiid=' + row.id)">查询关注</el-button>

                                <el-tooltip effect="dark" content="详情" placement="top-start"><el-button type="info" :icon="InfoFilled" size="small" @click="$router.push('/admin/biji/detail?id=' + row.id)">详情</el-button> </el-tooltip>
                                <el-tooltip effect="dark" content="编辑" placement="top-start"><el-button type="success" :icon="Edit" size="small" @click="$router.push('/admin/biji/updt?id=' + row.id)">编辑</el-button> </el-tooltip>
                                <el-tooltip effect="dark" content="删除" placement="top-start"><el-button type="danger" :icon="Delete" size="small" @click="deleteItems(row.id)">删除</el-button> </el-tooltip>
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
    import router from "@/router";

    import { ref, reactive, watch, unref, onBeforeMount } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { canBijiSelect, useBijiSelect, canBijiDelete, canBijiCheckIssh } from "@/module";
    import { extend } from "@/utils/extend";
    import { ElMessageBox, ElMessage } from "element-plus";
    import { Plus, Search, InfoFilled, Edit, Delete } from "@element-plus/icons-vue";

    const route = useRoute();
    const search = reactive({
        keyword: "",
        bijimingcheng: "",
        biaoqian: "",
        guanlianyouxi: "",
        xiangqing: "",
        page: 1, // 当前页
        pagesize: 12, // 每页行数
        orderby: "id", // 排序字段
        sort: "desc", // 排序类型
    });
    extend(search, route.query);
    // 链接参数变化时更新这些内容
    watch(
        () => route.query,
        () => {
            extend(search, route.query);
            loadList(1);
        },
        { deep: true }
    );

    // 总行数
    const totalCount = ref(0);
    /**
     * 列表数据
     * @type {EBiji[]}
     */
    const lists = ref([]);
    // 加载状态
    const loading = ref(false);

    // 排序操作
    const sortChange = (e) => {
        console.log(e);
        if (e.order == null) {
            search.orderby = "id";
            search.sort = "desc";
        } else {
            search.orderby = e.prop;
            search.sort = e.order == "ascending" ? "asc" : "desc";
        }
        loadList(1);
    };
    // 设置页数多少
    const sizeChange = (e) => {
        search.pagesize = e;
        loadList(1);
    };

    const deleteItems = (ids) => {
        return new Promise((resolve, reject) => {
            ElMessageBox.confirm("确定删除？")
                .then((res) => {
                    canBijiDelete(ids).then((res) => {
                        if (res.code == 0) {
                            ElMessage.success("删除成功");
                            loadList(search.page);
                            resolve(res.data);
                        } else {
                            ElMessage.error(res.msg);
                        }
                    });
                })
                .catch((err) => {
                    reject(err);
                });
        });
    };

    // 加载笔记列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/biji/selectTianjiaren", search).then(
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
    .views-biji-list {
    }
</style>
