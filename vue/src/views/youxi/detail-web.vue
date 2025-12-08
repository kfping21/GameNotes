<template>
    <div class="views-youxi-web-detail">
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">详情</h3>
                    <div class="sidebar-widget-body">
                        <div class="">
                            <div class="goods-info clearfix">
                                <div class="gallery-list">
                                    <e-shangpinban :images="map.youxitupian"></e-shangpinban>
                                </div>
                                <div class="goods-right-content">
                                    <h3 class="title" v-text="map.youximingcheng"></h3>
                                    <div class="descount">
                                        <div>
                                            <span class="name"> 关联笔记： </span>
                                            <span class="val"> <e-select-view module="biji" :value="map.guanlianbiji" select="id" show="bijimingcheng"></e-select-view> </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="goods-content" v-html="map.zhuangbeiku"></div>
                        </div>
                    </div>
                    <!-- /.sidebar-widget-body -->
                </div>
            </e-container>
        </div>
        <div>
            <e-container>
                <e-container>
                    <el-tabs type="border-card">
                        <el-tab-pane label="详情">
                            <div>
                                <div v-html="map.xiangqing"></div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="笔记">
                            <div class="list-table">
                                <table width="100%" border="1" class="table table-list table-bordered table-hover">
                                    <thead>
                                        <tr align="center">
                                            <th>笔记名称</th>
                                            <th>标签</th>
                                            <th>关联游戏</th>

                                            <th width="80" align="center">操作</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="(r, i) in bijilist">
                                            <td>{{ r.bijimingcheng }}</td>
                                            <td><e-select-view module="biaoqian" :value="r.biaoqian" select="id" show="biaoqianmingcheng"></e-select-view></td>
                                            <td><e-select-view module="youxi" :value="r.guanlianyouxi" select="id" show="youximingcheng"></e-select-view></td>
                                            <td align="center">
                                                <el-button @click="$goto('/biji/detail?id=' + r.id)"> 详细 </el-button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </e-container>
            </e-container>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import EShangpinban from "@/components/shangpin/shangpinban.vue";

    import { ref, reactive, watch, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { extend } from "@/utils/extend";
    import { useYouxiFindById, canYouxiFindById } from "@/module";

    const route = useRoute();
    const props = defineProps({
        id: {
            type: [Number, String],
        },
        isShowBtn: {
            type: Boolean,
            default: true,
        },
    });

    /**
     * 获取详情页面的一行数据,当url参数id变更时，当url参数id变更时，自动更新map中的数据
     * @type {EYouxi}
     */
    const map = useYouxiFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canYouxiFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据

    /**
     * 定义响应式变量bijilist
     * @type {UnwrapNestedRefs<EBiji[]>}
     */
    const bijilist = ref([]);
    const getbijilist = async () => {
        // 获取笔记数据,并赋值给bijilist变量
        bijilist.value = await DB.name("biji")
            .where("id", "=", map.guanlianbiji)


            .where("issh", "是")
            .limit("4").order("id desc").select();
    };
    // 监听map值变化后，并重新获取数据笔记模块的数据
    watch(() => map, getbijilist, { immediate: true, deep: true });
</script>

<style scoped lang="scss">
    .views-youxi-web-detail {
    }
</style>
