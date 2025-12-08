<template>
    <div class="views-sixin-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 私信详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="笔记编号"> {{ map.bijibianhao }} </el-descriptions-item>
                        <el-descriptions-item label="笔记名称"> {{ map.bijimingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="添加人"> {{ map.tianjiaren }} </el-descriptions-item>
                        <el-descriptions-item label="图片"> <e-img :src="map.tupian" class="detail-image" style="max-width: 120px" /> </el-descriptions-item>
                        <el-descriptions-item label="私信人"> {{ map.sixinren }} </el-descriptions-item>
                        <el-descriptions-item label="私信状态"> {{ map.sixinzhuangtai }} </el-descriptions-item>
                        <el-descriptions-item label="发送时间"> {{ map.addtime }} </el-descriptions-item>
                        <el-descriptions-item label="回复时间"> {{ map.huifushijian }} </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions direction="vertical" class="margin-top" :column="1" border>
                        <el-descriptions-item label="私信内容"> {{ map.sixinneirong }} </el-descriptions-item>
                        <el-descriptions-item label="私信回复"> {{ map.sixinhuifu }} </el-descriptions-item>
                    </el-descriptions>
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
    import { useSixinFindById, canSixinFindById } from "@/module";

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
     * @type {ESixin}
     */
    const map = useSixinFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canSixinFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );
    // end 获取详情页面的一行数据
</script>

<style scoped lang="scss">
    .views-sixin-detail {
    }
</style>
