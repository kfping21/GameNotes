<template>
    <div class="views-youxi-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 游戏详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="游戏名称"> {{ map.youximingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="关联笔记"> <e-select-view module="biji" :value="map.guanlianbiji" select="id" show="bijimingcheng"></e-select-view> </el-descriptions-item>
                        <el-descriptions-item label="游戏图片"> <e-img :src="map.youxitupian" class="detail-image" style="max-width: 120px" /> </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions direction="vertical" class="margin-top" :column="1" border>
                        <el-descriptions-item label="装备库"> <div v-html="map.zhuangbeiku"></div> </el-descriptions-item>
                        <el-descriptions-item label="详情"> <div v-html="map.xiangqing"></div> </el-descriptions-item>
                    </el-descriptions>
                </div>

                <!-- 关联该游戏的笔记列表 -->
                <div>
                    <div class="title-modelbox-widget1">
                        <h3 class="section-title">相关笔记</h3>
                        <div class="sidebar-widget-body">
                            <div class="floor_goods_wrap clearfix">
                                <el-row :gutter="20">
                                    <el-col :md="6" v-for="r in relatedNotes" :key="r.id">
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
                        </div>
                    </div>
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

    // 获取与当前游戏关联的笔记
    const relatedNotes = ref([]);

    // 当游戏信息加载完成后，获取相关笔记
    watch(
        () => map.id,
        (gameId) => {
            if (gameId) {
                DB.name("biji")
                    .where("guanlianyouxi", "=", gameId)
                    .where("issh", "=", "是") // 只显示已审核的笔记
                    .limit("4")
                    .order("id desc")
                    .select()
                    .then(data => {
                        relatedNotes.value = data;
                    })
                    .catch(err => {
                        console.error('获取相关笔记失败:', err);
                        relatedNotes.value = [];
                    });
            } else {
                relatedNotes.value = [];
            }
        },
        { immediate: true }
    );
    // end 获取详情页面的一行数据
</script>

<style scoped lang="scss">
    .views-youxi-detail {
    }

    .title-modelbox-widget1 {
        margin-top: 30px;
    }

    .section-title {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 20px;
        padding-bottom: 10px;
        border-bottom: 2px solid #eee;
    }

    .floor_goods_wrap_li {
        margin-bottom: 20px;
    }

    .floor_goods_wrap_b {
        border: 1px solid #eee;
        border-radius: 5px;
        padding: 10px;
        transition: all 0.3s;
    }

    .floor_goods_wrap_b:hover {
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        border-color: #409EFF;
    }

    .floor_goods_img {
        display: block;
        margin-bottom: 10px;
    }

    .floor_goods_tit {
        display: block;
        font-weight: bold;
        margin-bottom: 5px;
        color: #333;
        text-decoration: none;
    }

    .floor_goods_tit:hover {
        color: #409EFF;
    }

    .floor_goods_txt {
        display: block;
        color: #666;
        font-size: 14px;
        text-decoration: none;
    }
</style>
