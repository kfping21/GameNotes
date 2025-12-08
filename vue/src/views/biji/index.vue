<template>
    <div class="views-biji-index">
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">笔记列表</h3>
                    <div class="sidebar-widget-body">
                        <div class="floor_goods_wrap clearfix">
                            <div style="margin-bottom: 20px">
                                <form action="javascript:;" @submit="searchSubmit" class="form-search">
                                    <table class="jd-search">
                                        <tbody>
                                            <tr>
                                                <td class="label">关键字</td>
                                                <td>
                                                    <el-input type="text" name="keyword" v-model="search.keyword" placeholder="输入关键字"></el-input>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td class="label">关联游戏</td>
                                                <td>
                                                    <p class="search-radio">
                                                        <a href="javascript:;" @click="selectRadio('guanlianyouxi', '')" :class="{ active: !search.guanlianyouxi }">全部</a>
                                                        <a href="javascript:;" v-for="r in mapyouxi1" @click="selectRadio('guanlianyouxi', r.id)" :class="{ active: search.guanlianyouxi == r.id }" v-text="r.youximingcheng"> </a>
                                                    </p>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="label">标签</td>
                                                <td>
                                                    <el-select v-model="search.biaoqian"><el-option label="请选择" value=""></el-option><e-select-option type="option" module="biaoqian" value="id" label="biaoqianmingcheng"></e-select-option></el-select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="label">笔记名称</td>
                                                <td>
                                                    <el-input type="text" style="width: 150px" v-model="search.bijimingcheng" placeholder="请输入关键词"> </el-input>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="label">排序</td>
                                                <td>
                                                    <div style="display: flex; justify-content: space-between">
                                                        <p class="search-radio">
                                                            <a href="javascript:;" @click="selectRadio('orderby', 'id')" :class="{ active: search.orderby == 'id' }">发布时间</a>
                                                            <a href="javascript:;" @click="selectRadio('orderby', 'guanzhuliang')" :class="{ active: search.orderby == 'guanzhuliang' }">关注量</a>
                                                            <a href="javascript:;" @click="selectRadio('orderby', 'zhongcaodu')" :class="{ active: search.orderby == 'zhongcaodu' }">种草度</a>
                                                        </p>
                                                        <p class="search-radio">
                                                            <a href="javascript:;" @click="selectRadio('sort', 'desc')" :class="{ active: search.sort == 'desc' }">倒序</a>
                                                            <a href="javascript:;" @click="selectRadio('sort', 'asc')" :class="{ active: search.sort == 'asc' }">升序</a>
                                                        </p>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td>
                                                    <el-button type="success" @click="searchSubmit">搜索</el-button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                            <el-row :gutter="20">
                                <el-col :md="6" v-for="r in lists">
                                    <div class="floor_goods_wrap_li">
                                        <div class="floor_goods_wrap_b">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_img">
                                                <e-img :src="r.bijifengmian" pb="100"></e-img>
                                            </router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_tit" v-html="$substr(r.bijimingcheng, 20)"></router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_txt" v-html="$substr(r.xiangqing, 20)"></router-link>
                                        </div>
                                    </div>
                                </el-col>
                            </el-row>
                        </div>

                        <div style="margin-top: 10px; text-align: center">
                            <el-pagination @current-change="loadList" :page-sizes="[12, 24, 36, 48, 60]" v-model:current-page="search.page" v-model:page-size="search.pagesize" @size-change="sizeChange" layout="total, sizes, prev, pager, next" :total="totalCount"> </el-pagination>
                        </div>
                    </div>
                    <!-- /.sidebar-widget-body -->
                </div>
            </e-container>
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
    import { canBijiSelect, useBijiSelect, canBijiDelete } from "@/module";
    import { extend } from "@/utils/extend";
    import { ElMessageBox, ElMessage } from "element-plus";

    const route = useRoute();
    const search = reactive({
        keyword: "",
        issh: "是",
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

    // 加载笔记列表方法
    const loadList = (page) => {
        // 加载
        if (unref(loading)) return;
        loading.value = true;
        search.page = page;

        http.post("/api/biji/selectPages", search).then(
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
    const selectRadio = (target, name) => {
        search[target] = name;
        searchSubmit(1);
    };

    const searchSubmit = (page = 1) => {
        loadList(1);
    };
    const mapyouxi1 = DB.name("youxi").field("id,youximingcheng").order("id desc").selectRef();
</script>

<style scoped lang="scss">
    .views-biji-index {
    }
</style>
