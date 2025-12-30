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
                                    <div style="width: 100%; height: 0; padding-bottom: 100%; position: relative; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.1);">
                                        <img :src="avatarUrl" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; object-fit: cover;" alt="头像">
                                    </div>
                                </div>
                                <div class="goods-right-content">
                                    <h3 class="title" v-text="map.mingcheng || map.zhanghao"></h3>
                                    <div class="descount">
                                        <div>
                                            <span class="name"> 账号： </span>
                                            <span class="val"> {{ map.zhanghao }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 性别： </span>
                                            <span class="val"> {{ map.xingbie || '暂无' }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 出生日期： </span>
                                            <span class="val"> {{ map.chushengriqi || '暂无' }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 手机： </span>
                                            <span class="val"> {{ map.shouji || '暂无' }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 邮箱： </span>
                                            <span class="val"> {{ map.youxiang || '暂无' }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 粉丝数： </span>
                                            <span class="val"> {{ fansCount }} </span>
                                        </div>
                                        <div>
                                            <span class="name"> 关注数： </span>
                                            <span class="val"> {{ followCount }} </span>
                                        </div>
                                        <div v-if="map.wozaiwan">
                                            <span class="name"> 我在玩： </span>
                                            <span class="val"> 
                                                <!-- Debug: {{ map.wozaiwan }} -->
                                                <e-select-view module="youxi" :value="map.wozaiwan" select="id" show="youximingcheng"></e-select-view> 
                                            </span>
                                        </div>


                                        <div v-if="map.gexingqianming">
                                            <span class="name"> 个性签名： </span>
                                            <span class="val"> <div v-html="map.gexingqianming"></div> </span>
                                        </div>


                                        <template v-if="$session.zhanghao != map.zhanghao">
                                            <div class="action-buttons">
                                                <e-chat-check v-if="map.zhanghao != $session.zhanghao" type="success" :rid="map.zhanghao">私信</e-chat-check>

                                                <el-button v-if="!isFollowed" type="primary" @click="followUser">
                                                    <i class="fa fa-plus-circle"></i> 关注
                                                </el-button>
                                                <el-button v-else type="success" plain @click="unfollowUser">
                                                    <i class="fa fa-check-circle"></i> 已关注
                                                </el-button>
                                            </div>
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
                        <div class="note-list">
                            <div class="note-item" v-for="r in bijilist" :key="r.id" @click="$router.push({ path: '/biji/detail', query: { id: r.id } })">
                                <div class="note-content">
                                    <div class="note-title">{{ r.bijimingcheng }}</div>
                                    <div class="note-desc">{{ $substr(r.xiangqing, 60) }}</div>
                                    <div class="note-meta">
                                        <span class="meta-item" title="种草数"><i class="fa fa-leaf"></i> {{ r.zhongcaoshu || 0 }}</span>
                                        <span class="meta-item" title="点赞数"><i class="fa fa-thumbs-up"></i> {{ r.dianzanshu || 0 }}</span>
                                        <span class="meta-item" title="收藏数"><i class="fa fa-star"></i> {{ r.shoucangshu || 0 }}</span>
                                        <span class="meta-item">{{ r.addtime }}</span>
                                    </div>
                                </div>
                                <div class="note-img-box">
                                    <e-img :src="r.bijifengmian" class="note-img" style="width: 100%; height: 100%; object-fit: cover; border-radius: 4px;"></e-img>
                                </div>
                            </div>
                            <div v-if="bijilist.length === 0" style="text-align: center; padding: 20px; color: #999;">
                                暂无笔记
                            </div>
                        </div>
                    </div>
                    <!-- /.sidebar-widget-body -->
                </div>
            </e-container>
        </div>

        <!-- 用户"我在玩"的游戏相关的笔记 -->
        <!-- <div v-if="map.wozaiwan">
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
        </div> -->
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import EShangpinban from "@/components/shangpin/shangpinban.vue";
    import { canGuanzhuInsert, canGuanzhuDelete, checkIsFollow, selectGuanzhuren, selectTianjiaren } from "@/module/guanzhu";
    import { ElMessage, ElMessageBox } from 'element-plus';
    import router from "@/router";
    import config from "@/config";

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

    // Avatar URL handling
    const avatarUrl = computed(() => {
        const url = map.touxiang;
        if (!url) return ''; 
        if (url.startsWith('http')) return url;
        return config.service_url + (url.startsWith('/') ? '' : '/') + url;
    });

    // Fans and Follow counts
    const fansCount = ref(0);
    const followCount = ref(0);

    const loadFollowCounts = async (username) => {
        if (!username) return;
        try {
            // Fans (people who follow me -> tianjiaren)
            const fansRes = await selectTianjiaren({ username, page: 1, size: 1 });
            if (fansRes.code === 0 && fansRes.data && fansRes.data.lists) {
                fansCount.value = fansRes.data.lists.total || 0;
            }
            
            // Following (people I follow -> guanzhuren)
            const followRes = await selectGuanzhuren({ username, page: 1, size: 1 });
            if (followRes.code === 0 && followRes.data && followRes.data.lists) {
                followCount.value = followRes.data.lists.total || 0;
            }
        } catch (e) {
            console.error(e);
        }
    };

    watch(() => map.zhanghao, (val) => {
        if (val) {
            loadFollowCounts(val);
        }
    });

    /**
     * 定义响应式变量bijilist
     * @type {UnwrapNestedRefs<EBiji[]>}
     */
    const bijilist = ref([]);
    const getbijilist = async () => {
        if (!map.zhanghao) return;
        // 获取笔记数据,并赋值给bijilist变量
        const list = await DB.name("biji").where("tianjiaren", "=", map.zhanghao).where("issh", "是").limit("8").order("id desc").select();
        
        // Fetch counts for each note
        if (list && list.length > 0) {
            const promises = list.map(async (note) => {
                // Like count
                const likeCount = await DB.name('dianzan').where('biao', 'biji').where('biaoid', note.id).count();
                note.dianzanshu = likeCount;
                
                // Collect count
                const collectCount = await DB.name('shoucang').where('biao', 'biji').where('xwid', note.id).count();
                note.shoucangshu = collectCount;

                // Grass count
                const grassCount = await DB.name('zhongcao').where('bijiid', note.id).count();
                note.zhongcaoshu = grassCount;
                
                return note;
            });
            
            bijilist.value = await Promise.all(promises);
        } else {
            bijilist.value = [];
        }
    };
    // 监听map值变化后，并重新获取数据笔记模块的数据
    watch(() => map, (val) => {
        console.log('User Map Updated:', val);
        getbijilist();
    }, { immediate: true, deep: true });

    // 关注/取消关注用户功能
    const isFollowed = ref(false);

    const checkFollowStatus = async () => {
        if (session('username') && map.zhanghao) {
            isFollowed.value = await checkIsFollow(map.zhanghao);
        }
    };

    // Watch map change to check follow status
    watch(() => map.zhanghao, checkFollowStatus);

    const followUser = async () => {
        if (!session('username')) {
            var r = await ElMessageBox.confirm('请先登录');
            router.push({path: '/login', query: {redirect: route.fullPath}});
            return;
        }

        if (session('username') === map.zhanghao) {
            ElMessage.error('不能关注自己');
            return;
        }

        try {
            const res = await canGuanzhuInsert(map.zhanghao);
            if (res.code === 0) {
                ElMessage.success('关注成功');
                isFollowed.value = true;
            } else {
                ElMessage.error(res.msg || '关注失败');
            }
        } catch (error) {
            ElMessage.error(error.message || '关注失败');
        }
    };

    const unfollowUser = async () => {
        if (!session('username')) {
            var r = await ElMessageBox.confirm('请先登录');
            router.push({path: '/login', query: {redirect: route.fullPath}});
            return;
        }

        try {
            const res = await canGuanzhuDelete(map.zhanghao);
            if (res.code === 0) {
                ElMessage.success('取消关注成功');
                isFollowed.value = false;
            } else {
                ElMessage.error(res.msg || '取消关注失败');
            }
        } catch (error) {
            ElMessage.error(error.message || '取消关注失败');
        }
    };

    // 获取与用户"我在玩"相关的笔记
    const gameRelatedNotes = ref([]);
    const getGameRelatedNotes = async () => {
        if (map.wozaiwan) {
            // 解析用户"我在玩"字段，支持多种分隔符
            let gameIds = [];
            const val = map.wozaiwan;
            if (typeof val === 'string') {
                gameIds = val.split(/[,，;；|]/).filter(id => id.trim() !== '');
            } else if (typeof val === 'number') {
                gameIds = [val];
            } else if (Array.isArray(val)) {
                gameIds = val;
            }

            if (gameIds.length > 0) {
                // 构建查询条件，查找与这些游戏相关的笔记
                const allNotes = [];

                // 查找对应的游戏信息
                const games = await DB.name("youxi")
                    .where("id", "in", gameIds)
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
    background-color: #f5f7fa;
    min-height: 100vh;
    padding-top: 20px;
}

.title-modelbox-widget1 {
    background: #fff;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
    margin-bottom: 20px;
}

.section-title {
    font-size: 22px;
    font-weight: 600;
    color: #2c3e50;
    border-bottom: 1px solid #ebeef5;
    padding-bottom: 15px;
    margin-bottom: 25px;
    position: relative;
    
    &::after {
        content: '';
        position: absolute;
        bottom: -1px;
        left: 0;
        width: 80px;
        height: 3px;
        background-color: #409EFF;
    }
}

.goods-info {
    display: flex;
    flex-wrap: wrap;
    gap: 30px;
}

.gallery-list {
    width: 300px;
    flex-shrink: 0;
    
    :deep(.el-image) {
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }
}

.goods-right-content {
    flex: 1;
    min-width: 300px;
}

.goods-right-content .title {
    font-size: 26px;
    color: #303133;
    margin-top: 0;
    margin-bottom: 20px;
    font-weight: bold;
    line-height: 1.4;
}

.descount {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 25px;
    border: 1px solid #ebeef5;
}

.descount > div {
    margin-bottom: 12px;
    line-height: 1.8;
    font-size: 15px;
    display: flex;
    align-items: center;
    
    &:last-child {
        margin-bottom: 0;
    }
}

.descount .name {
    color: #909399;
    width: 90px;
    flex-shrink: 0;
    text-align: right;
    margin-right: 15px;
    font-weight: 500;
}

.descount .val {
    color: #606266;
    font-weight: 500;
}

.action-buttons {
    margin-top: 30px;
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
}

.el-button {
    margin: 0 !important;
    padding: 10px 20px;
    font-weight: 500;
    
    i {
        margin-right: 5px;
    }
}

.note-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
}
.note-item {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 15px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.05);
    cursor: pointer;
    transition: all 0.3s;
}
.note-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0,0,0,0.1);
}
.note-content {
    flex: 1;
    margin-right: 20px;
}
.note-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 8px;
}
.note-desc {
    font-size: 14px;
    color: #666;
    line-height: 1.5;
    margin-bottom: 10px;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
.note-meta {
    font-size: 12px;
    color: #999;
}
.meta-item {
    margin-right: 15px;
}
.note-img-box {
    width: 120px;
    height: 90px;
    flex-shrink: 0;
}

/* Note List Styling */
.paoche {
    margin-top: 20px;
}

.li_model {
    margin-bottom: 20px;
    transition: transform 0.3s;
    
    &:hover {
        transform: translateY(-5px);
    }
}

.img_model {
    display: block;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
    margin-bottom: 10px;
}

.brand_model {
    text-align: center;
    padding: 5px 0;
}

.a_cen {
    color: #303133;
    text-decoration: none;
    font-weight: 500;
    font-size: 15px;
    
    &:hover {
        color: #409EFF;
    }
}
</style>
