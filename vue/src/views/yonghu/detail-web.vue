<template>
    <div class="views-yonghu-web-detail">
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">用户详情</h3>
                    <div class="sidebar-widget-body">
                        <div class="">
                            <div class="goods-info clearfix">
                                <div class="gallery-list">
                                    <e-shangpinban :images="map.touxiang"></e-shangpinban>
                                </div>
                                <div class="goods-right-content">
                                    <h3 class="title" v-text="map.mingcheng"></h3>
                                    <div class="descount">
                                        <div>
                                            <span class="name"> 账号： </span>
                                            <span class="val"> {{ map.zhanghao }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 性别： </span>
                                            <span class="val"> {{ map.xingbie }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 出生日期： </span>
                                            <span class="val"> {{ map.chushengriqi }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 手机： </span>
                                            <span class="val"> {{ map.shouji }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 邮箱： </span>
                                            <span class="val"> {{ map.youxiang }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 粉丝数： </span>
                                            <span class="val"> {{ map.fensishu }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 关注数： </span>
                                            <span class="val"> {{ map.guanzhushu }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 访客数： </span>
                                            <span class="val"> {{ map.fangkeshu }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 我在玩： </span>
                                            <span class="val"> <e-select-view module="youxi" :value="map.wozaiwan" select="id" show="youximingcheng"></e-select-view> </span>
                                        </div>


                                        <div>
                                            <span class="name"> 个性签名： </span>
                                            <span class="val"> <div v-html="map.gexingqianming"></div> </span>
                                        </div>


                                        <template v-if="$session.zhanghao != map.zhanghao">





                                            <el-button type="primary" @click="$router.push('/sixin/add?id='+map.id)">私信</el-button>




                                            <el-button

                                                v-if="map.guanzhuCount==0"   type="primary" @click="$router.push('/guanzhu/add?id='+map.id)">关注</el-button>

                                        </template>




                                    </div>
                                </div>
                            </div>
                            <div class="goods-content"></div>
                        </div>
                    </div>
                    <!-- /.sidebar-widget-body -->
                </div>
            </e-container>
        </div>
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">我的笔记</h3>
                    <div class="sidebar-widget-body">
                        <div class="paoche">
                            <el-row class="ul_model" :gutter="20">
                                <el-col :md="4" class="li_model" :key="r.id" v-for="r in bijilist">
                                    <div class="li_model">
                                        <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="img_model">
                                            <e-img :src="r.bijifengmian" pb="100"></e-img>
                                        </router-link>
                                        <div class="d_model">
                                            <div class="brand_model">
                                                <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="a_cen">
                                                    {{ $substr(r.bijimingcheng, 15) }}
                                                </router-link>
                                            </div>
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

        <!-- 用户"我在玩"的游戏相关的笔记 -->
        <div v-if="map.wozaiwan">
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">游戏相关推荐</h3>
                    <div class="sidebar-widget-body">
                        <div class="paoche">
                            <el-row class="ul_model" :gutter="20">
                                <el-col :md="4" class="li_model" :key="r.id" v-for="r in gameRelatedNotes">
                                    <div class="li_model">
                                        <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="img_model">
                                            <e-img :src="r.bijifengmian" pb="100"></e-img>
                                        </router-link>
                                        <div class="d_model">
                                            <div class="brand_model">
                                                <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="a_cen">
                                                    {{ $substr(r.bijimingcheng, 15) }}
                                                </router-link>
                                            </div>
                                        </div>
                                    </div>
                                </el-col>
                            </el-row>
                        </div>
                    </div>
                </div>
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
    import { useYonghuFindById, canYonghuFindById } from "@/module";

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
    const loadWebFind = (id) => {
        http.post("/api/yonghu/detailWeb", { id }).then((res) => {
            console.log(res.data);
        });
    };
    watch(() => props.id, loadWebFind, { immediate: true });

    /**
     * 获取详情页面的一行数据,当url参数id变更时，当url参数id变更时，自动更新map中的数据
     * @type {EYonghu}
     */
    const map = useYonghuFindById(props.id);
    watch(
        () => props.id,
        (id) => {
            canYonghuFindById(id).then((res) => {
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
        bijilist.value = await DB.name("biji").where("tianjiaren", "=", map.zhanghao).where("issh", "是").limit("8").order("id desc").select();
    };
    // 监听map值变化后，并重新获取数据笔记模块的数据
    watch(() => map, getbijilist, { immediate: true, deep: true });

    // 获取与用户"我在玩"相关的笔记
    const gameRelatedNotes = ref([]);
    const getGameRelatedNotes = async () => {
        if (map.wozaiwan) {
            // 解析用户"我在玩"字段，支持多种分隔符
            const gameNames = map.wozaiwan.split(/[,，;；|]/).filter(name => name.trim() !== '');

            if (gameNames.length > 0) {
                // 构建查询条件，查找与这些游戏相关的笔记
                const allNotes = [];

                // 为每个游戏查找相关笔记
                for (const gameName of gameNames) {
                    if (gameName.trim() !== '') {
                        // 先查找游戏的ID
                        const games = await DB.name("youxi")
                            .where("youximingcheng", "LIKE", `%${gameName.trim()}%`)
                            .select();

                        // 根据游戏ID查找相关笔记
                        for (const game of games) {
                            const notes = await DB.name("biji")
                                .where("guanlianyouxi", "=", game.id)
                                .where("issh", "=", "是")
                                .limit("2") // 每个游戏最多2个笔记
                                .order("id desc")
                                .select();

                            // 确保不会重复添加相同的笔记
                            for (const note of notes) {
                                if (!allNotes.some(n => n.id === note.id)) {
                                    allNotes.push(note);
                                }
                            }
                        }
                    }
                }

                // 限制总数为8个
                gameRelatedNotes.value = allNotes.slice(0, 8);
            } else {
                gameRelatedNotes.value = [];
            }
        } else {
            gameRelatedNotes.value = [];
        }
    };

    // 监听用户信息变化，更新相关游戏笔记
    watch(() => map.wozaiwan, getGameRelatedNotes, { immediate: true });
</script>

<style scoped lang="scss">
    .views-yonghu-web-detail {
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
</style>
