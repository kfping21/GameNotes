<template>
    <div class="views-guanzhu-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 关注详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="笔记编号"> {{ map.bijibianhao }} </el-descriptions-item>
                        <el-descriptions-item label="笔记名称"> {{ map.bijimingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="标签"> <e-select-view module="biaoqian" :value="map.biaoqian" select="id" show="biaoqianmingcheng"></e-select-view> </el-descriptions-item>
                        <el-descriptions-item label="关联游戏"> <e-select-view module="youxi" :value="map.guanlianyouxi" select="id" show="youximingcheng"></e-select-view> </el-descriptions-item>
                        <el-descriptions-item label="添加人"> {{ map.tianjiaren }} </el-descriptions-item>
                        <el-descriptions-item label="关注人"> {{ map.guanzhuren }} </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions direction="vertical" class="margin-top" :column="1" border> </el-descriptions>
                </div>
                <div class="no-print" v-if="isShowBtn">
                    <el-button @click="$router.go(-1)">返回</el-button>
                    <el-button @click="$print('#printdetail')">打印</el-button>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";

    import { ref, reactive, watch, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { extend } from "@/utils/extend";
    import { useGuanzhuFindById, canGuanzhuFindById } from "@/module";

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
     * @type {EGuanzhu}
     */
    const map = useGuanzhuFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canGuanzhuFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据
</script>

<style scoped lang="scss">
    .views-guanzhu-detail {
    }
</style>
