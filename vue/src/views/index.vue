<template>
    <div class="views-zhuye">
        <div>
            <el-carousel indicator-position="outside" height="600px">
                <el-carousel-item v-for="item in bhtList" :key="item.id">
                    <a href="javascript:;">
                        <div style="background-size: cover" @click="$goto(item.url)" :style="{ 'background-image': 'url(' + $formatImageSrc(item.image) + ')', width: '100%', height: '600px' }"></div>
                    </a>
                </el-carousel-item>
            </el-carousel>
        </div>
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">
                        <div class="more">
                            <router-link to="/biji">更多>></router-link>
                        </div>
                        推荐笔记
                    </h3>
                    <div class="sidebar-widget-body">
                        <div class="floor_goods_wrap clearfix">
                            <el-row :gutter="20">
                                <el-col :md="6" v-for="r in bijilist">
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
                    <!-- /.sidebar-widget-body -->
                </div>
            </e-container>
        </div>
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">
                        <div class="more">
                            <router-link to="/biji">更多>></router-link>
                        </div>
                        关注量
                    </h3>
                    <div class="sidebar-widget-body">
                        <div class="floor_goods_wrap clearfix">
                            <el-row :gutter="20">
                                <el-col :md="6" v-for="r in bijilist1">
                                    <div class="floor_goods_wrap_li">
                                        <div class="floor_goods_wrap_b">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_img">
                                                <e-img :src="r.bijifengmian" pb="100"></e-img>
                                            </router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_tit" v-html="$substr(r.bijimingcheng, 20)"></router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_txt" v-html="$substr(r.xiangqing, 20)"></router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_price"> 关注量:{{ r.zhongcaodu }} </router-link>
                                        </div>
                                    </div>
                                </el-col>
                            </el-row>
                        </div>
                    </div>
                    <!-- /.sidebar-widget-body -->
                </div>
            </e-container>
        </div>
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">
                        <div class="more">
                            <router-link to="/biji">更多>></router-link>
                        </div>
                        热门笔记
                    </h3>
                    <div class="sidebar-widget-body">
                        <div class="floor_goods_wrap clearfix">
                            <el-row :gutter="20">
                                <el-col :md="6" v-for="r in bijilist2">
                                    <div class="floor_goods_wrap_li">
                                        <div class="floor_goods_wrap_b">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_img">
                                                <e-img :src="r.bijifengmian" pb="100"></e-img>
                                            </router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_tit" v-html="$substr(r.bijimingcheng, 20)"></router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_txt" v-html="$substr(r.xiangqing, 20)"></router-link>
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="floor_goods_price"> 热度:{{ r.zhongcaodu }} </router-link>
                                        </div>
                                    </div>
                                </el-col>
                            </el-row>
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

import { ref, reactive } from "vue";
import { useRoute } from "vue-router";
import { session } from "@/utils/utils";

const route = useRoute();
// 获取轮播图信息
const bhtList = DB.name("lunbotu").order("id desc").limit(5).selectRef();

// 获取用户个性化推荐的笔记列表
const bijilist = ref([]);

// 调用后端推荐接口获取个性化推荐笔记
const getRecommendData = async () => {
    try {
        const res = await http.get('/api/biji/recommendByUserTagsAndGames');
        if (res.code === 0) {
            bijilist.value = res.data.list || [];
        } else {
            console.error('获取推荐数据失败:', res.msg);
            // 如果推荐获取失败，回退到默认的笔记列表
            DB.name("biji").where("issh", "是").limit("4").order("id desc").select().then(data => {
                bijilist.value = data;
            }).catch(err => {
                console.error('获取默认笔记列表失败:', err);
                bijilist.value = [];
            });
        }
    } catch (error) {
        console.error('获取推荐数据出错:', error);
        // 如果调用失败，回退到默认的笔记列表
        DB.name("biji").where("issh", "是").limit("4").order("id desc").select().then(data => {
            bijilist.value = data;
        }).catch(err => {
            console.error('获取默认笔记列表失败:', err);
            bijilist.value = [];
        });
    }
};

// 页面加载时获取推荐数据
getRecommendData();

/**
 * 定义响应式变量bijilist1,并获取数据笔记模块的数据
 * @type {UnwrapNestedRefs<EBiji[]>}
 */
const bijilist1 = DB.name("biji").where("issh", "是").limit("4").order("zhongcaodu desc").selectRef();

/**
 * 定义响应式变量bijilist2,并获取数据笔记模块的数据
 * @type {UnwrapNestedRefs<EBiji[]>}
 */
const bijilist2 = DB.name("biji").where("issh", "是").limit("4").order("zhongcaodu desc").selectRef();
</script>

<style scoped lang="scss">
.views-zhuye {
}
</style>
