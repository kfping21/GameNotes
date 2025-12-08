<template>
    <div class="views-biji-detail">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 笔记详情 </span>
                    </div>
                </template>

                <div id="printdetail">
                    <el-descriptions class="margin-top" :column="3" border>
                        <el-descriptions-item label="笔记编号"> {{ map.bijibianhao }} </el-descriptions-item>
                        <el-descriptions-item label="笔记名称"> {{ map.bijimingcheng }} </el-descriptions-item>
                        <el-descriptions-item label="标签"> <e-select-view module="biaoqian" :value="map.biaoqian" select="id" show="biaoqianmingcheng"></e-select-view> </el-descriptions-item>
                        <el-descriptions-item label="关联游戏"> <e-select-view module="youxi" :value="map.guanlianyouxi" select="id" show="youximingcheng"></e-select-view> </el-descriptions-item>
                        <el-descriptions-item label="关注量"> {{ map.guanzhuliang }} </el-descriptions-item>
                        <el-descriptions-item label="种草度"> {{ map.zhongcaodu }} </el-descriptions-item>
                        <el-descriptions-item label="添加人"> {{ map.tianjiaren }} </el-descriptions-item>
                    </el-descriptions>

                    <el-descriptions direction="vertical" class="margin-top" :column="1" border>
                        <el-descriptions-item label="笔记封面"> <e-images :src="map.bijifengmian" type="detail"></e-images> </el-descriptions-item>
                        <el-descriptions-item label="视频"> <vue3VideoPlay width="100%" v-if="map.shipin" :src="$formatImageSrc(map.shipin)" poster="" /> </el-descriptions-item>
                        <el-descriptions-item label="详情"> <div v-html="map.xiangqing"></div> </el-descriptions-item>
                    </el-descriptions>
                </div>

                <!-- 关联游戏的其他笔记 -->
                <div v-if="map.guanlianyouxi">
                    <div class="title-modelbox-widget1">
                        <h3 class="section-title">同游戏推荐</h3>
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
    import "vue3-video-play/dist/style.css";
    import vue3VideoPlay from "vue3-video-play";

    import { ref, reactive, watch, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { extend } from "@/utils/extend";
    import { useBijiFindById, canBijiFindById } from "@/module";

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
     * @type {EBiji}
     */
    const map = useBijiFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canBijiFindById(id).then((res) => {
                extend(map, res);
            });
        }
    );

    // 获取与当前笔记关联的同游戏的其他笔记
    const relatedNotes = ref([]);

    // 当笔记信息加载完成后，获取相关笔记
    watch(
        () => map.guanlianyouxi,
        (gameId) => {
            if (gameId) {
                DB.name("biji")
                    .where("guanlianyouxi", "=", gameId)
                    .where("id", "!=", map.id) // 排除当前笔记
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
    .views-biji-detail {
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
