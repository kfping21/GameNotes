<template>
    <div class="views-biji-web-detail">
        <div>
            <e-container>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">
                        笔记详情 </h3>
                    <div class="sidebar-widget-body">

                        <div class="">
                            <div class="goods-info clearfix">
                                <div class="gallery-list">
                                    <e-shangpinban :images="map.bijifengmian"></e-shangpinban>
                                </div>
                                <div class="goods-right-content">
                                    <h3 class="title" v-text="map.bijimingcheng"></h3>
                                    <div class="descount">
                                        <div>
                        <span class="name">
                            笔记编号：
                        </span>
                                            <span class="val">
                            {{ map.bijibianhao }}                        </span>
                                        </div>
                                        <div>
                        <span class="name">
                            标签：
                        </span>
                                            <span class="val">
                             <e-select-view module="biaoqian" :value="map.biaoqian" select="id"
                                            show="biaoqianmingcheng"></e-select-view>                        </span>
                                        </div>
                                        <div>
                        <span class="name">
                            关联游戏：
                        </span>
                                            <span class="val">
                             <router-link v-if="map.guanlianyouxi" :to="'/youxi/detail?id=' + map.guanlianyouxi" class="author-link" style="font-weight: normal;">
                                 <e-select-view module="youxi" :value="map.guanlianyouxi" select="id"
                                                show="youximingcheng"></e-select-view>
                             </router-link>
                             <span v-else>暂无</span>
                        </span>
                                        </div>
                                        <div>
                        <span class="name">
                            添加人：
                        </span>
                                            <span class="val">
                            <span v-if="authorInfo.id" @click="goToUserDetail(authorInfo.id)" class="author-link" style="cursor: pointer; display: inline-flex; align-items: center;">
                                <e-img :src="authorInfo.touxiang" style="width: 24px; height: 24px; border-radius: 50%; vertical-align: middle; margin-right: 5px; display: inline-block;"></e-img>
                                {{ authorInfo.mingcheng || map.tianjiaren }}
                            </span>
                            <span v-else @click="ElMessage.warning('用户信息未找到')" style="cursor: pointer">{{ map.tianjiaren }}</span>
                        </span>
                                        </div>
                                    </div>

                                    <template v-if="$session.cx == '用户'">
                                        <div class="action-buttons">
                                            <el-badge :value="counts.zhongcao" style="margin-right: 10px" v-if="$session.username != map.tianjiaren">
                                                <el-button :type="isZhongcao ? 'success' : 'primary'" @click="handleZhongcao">
                                                    {{ isZhongcao ? '已种草' : '种草' }}
                                                </el-button>
                                            </el-badge>

                                            <e-chat-check  v-if="map.tianjiaren != $session.zhanghao " type="success" :rid="map.tianjiaren">私聊</e-chat-check>

                                            <el-button v-if="$session.username != map.tianjiaren && !isFollowed" type="primary"
                                                       @click="followUser">
                                                <i class="fa fa-plus-circle"></i> 关注作者
                                            </el-button>
                                            <el-button v-if="$session.username != map.tianjiaren && isFollowed" type="success" plain
                                                       @click="unfollowUser">
                                                <i class="fa fa-check-circle"></i> 已关注作者
                                            </el-button>

                                            <e-collect module="shoucang"
                                                       :form="{biaoid:'xwid',biao:'biao',biaoti:'biaoti'}"
                                                       :biaoid="$route.query.id"
                                                       biao="biji"
                                                       :biaoti="map.bijimingcheng"
                                            >
                                                <template #icon="{isCollect}">
                                                    <i class="fa" :class="isCollect?'fa-star' : 'fa-star-o'"></i>
                                                </template>
                                                <template #default="{isCollect}">
                                                    {{ isCollect ? '取消收藏' : '收藏' }}
                                                </template>

                                            </e-collect>

                                            <e-collect module="dianzan"
                                                       :form="{biaoid:'biaoid',biao:'biao',biaoti:'biaoti'}"
                                                       :biaoid="$route.query.id"
                                                       biao="biji"
                                                       :biaoti="map.bijimingcheng"
                                            >
                                                <template #icon="{isCollect}">
                                                    <i :class="is_dianzan ? 'fa fa-thumbs-up' : 'fa fa-thumbs-o-up'"></i>
                                                </template>
                                                <template #default="{isCollect}">
                                                    {{ isCollect ? '已点赞' : '赞' }}
                                                </template>

                                            </e-collect>
                                        </div>
                                    </template>


                                </div>
                            </div>


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
                        <el-tab-pane label="视频">
                            <div class="youku-aiqiyi">
                                <div class="clearfix">
                                    <div class="viedo">
                                        <div class="flash_box">
                                            <vue3VideoPlay
                                                width="100%"
                                                v-if="map.shipin"
                                                :src="$formatImageSrc(map.shipin)"
                                                poster=""
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div class="message">
                                    <div class="detail_left">
                                        <div class="player_title title_score">


                                        </div>
                                        <div class="player_intro">
                                            <div class="intro_mn">
                                                <div class="intro_mnc">
                                                    <div class="player_tag">


                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="评论区">

                            <e-comments module="biji" :lists="comments"
                                        :biaoti="map.bijimingcheng"
                                        :is-comment="isCommit"
                                        :commit-callback="commentCallback"
                                        @success="getcommentsFunc"
                                        user="username"
                                        headimg="headimg"
                                        :is-reply="true"
                                        name="nickname"
                            >
                            </e-comments>
                        </el-tab-pane>
                    </el-tabs>
                </e-container>


            </e-container>
        </div>
    </div>
</template>

<script setup>
import http from '@/utils/ajax/http';
import DB from '@/utils/db';
import router from '@/router';
import rule from '@/utils/rule';
import EShangpinban from '@/components/shangpin/shangpinban.vue';
import 'vue3-video-play/dist/style.css';
import vue3VideoPlay from 'vue3-video-play';
import EComments from '@/components/comments/comment';
import { canGuanzhuInsert, canGuanzhuDelete, checkIsFollow } from "@/module/guanzhu";

import {ref, reactive, watch, computed, unref} from 'vue';
import {useRoute} from 'vue-router';
import {session} from '@/utils/utils';
import {extend} from '@/utils/extend';
import {useBijiFindById, canBijiFindById, canZhongcaoCreateForm, canZhongcaoInsert, canZhongcaoDelete} from '@/module';
import {ElLoading, ElMessage, ElMessageBox} from 'element-plus';

const route = useRoute();
const props = defineProps({
    id: {
        type: [Number, String],
    },
    isShowBtn: {
        type: Boolean,
        default: true
    }
});


/**
 * 获取详情页面的一行数据,当url参数id变更时，当url参数id变更时，自动更新map中的数据
 * @type {EBiji}
 */
const map = useBijiFindById(props.id);
const counts = reactive({
    zhongcao: 0,
    dianzan: 0,
    shoucang: 0
});

const loadCounts = async (id) => {
    if (!id) return;
    try {
        counts.zhongcao = await DB.name('zhongcao').where('bijiid', id).count();
        counts.dianzan = await DB.name('dianzan').where('biao', 'biji').where('biaoid', id).count();
        counts.shoucang = await DB.name('shoucang').where('biao', 'biji').where('xwid', id).count();
    } catch (e) {
        console.error('Failed to load counts', e);
    }
};

watch(() => props.id, async (id) => {
    const res = await canBijiFindById(id);
    extend(map, res);
    loadCounts(id);
}, { immediate: true });

// 获取作者信息
const authorInfo = ref({});
watch(() => map.tianjiaren, async (val) => {
    if (val) {
        const user = await DB.name('yonghu').where('zhanghao', val).find();
        if (user) {
            authorInfo.value = user;
        }
    }
}, { immediate: true });
// end 获取详情页面的一行数据

const zhongcaoForm = ref({});
const readMap = ref({});
const isZhongcao = ref(false);
const zhongcaoId = ref(null);

// Check if user has already planted grass
const checkZhongcaoStatus = async () => {
    if (map.id && session('username')) {
        // Use select() instead of find() to avoid backend error on empty result
        const res = await DB.name('zhongcao')
            .where('bijiid', map.id)
            .where('zhongcaoren', session('username'))
            .select();
            
        if (res && res.length > 0) {
            isZhongcao.value = true;
            zhongcaoId.value = res[0].id;
        } else {
            isZhongcao.value = false;
            zhongcaoId.value = null;
        }
    }
};

watch(() => map, async () => {
    if (!map.id || !session('username')) return;

    var s = await canZhongcaoCreateForm(map.id, map);
    zhongcaoForm.value = s.form;
    readMap.value = s.readMap;
    
    checkZhongcaoStatus();
}, {deep: true});

const handleZhongcao = async () => {
    if (!session('username')) {
        var r = await ElMessageBox.confirm('请先登录');
        router.push({path: '/login', query: {redirect: route.fullPath}});
        return;
    }
    
    const loadingInstance = ElLoading.service({
        fullscreen: true,
        lock: true,
        text: '正在提交...',
    });

    try {
        if (isZhongcao.value) {
            // Cancel Zhongcao
            if (zhongcaoId.value) {
                const res = await canZhongcaoDelete(zhongcaoId.value);
                if (res.code == 0) {
                    ElMessage.success('取消种草成功');
                    isZhongcao.value = false;
                    zhongcaoId.value = null;
                    map.zhongcaodu = Math.max(0, (map.zhongcaodu || 0) - 1);
                    loadCounts(map.id); // Refresh counts
                } else {
                    ElMessage.error(res.msg || '取消种草失败');
                }
            }
        } else {
            // Add Zhongcao
            const res = await canZhongcaoInsert(zhongcaoForm.value);
            if (res.code == 0) {
                ElMessage.success('种草成功');
                isZhongcao.value = true;
                zhongcaoId.value = res.data.id || res.data; // Adjust based on return value
                map.zhongcaodu = (map.zhongcaodu || 0) + 1;
                loadCounts(map.id); // Refresh counts
                // Re-fetch to get ID if needed, or assume res.data contains it
                if (!zhongcaoId.value || typeof zhongcaoId.value !== 'number') {
                     checkZhongcaoStatus();
                }
            } else {
                ElMessage.error(res.msg);
            }
        }
    } catch (e) {
        ElMessage.error(e.message);
    } finally {
        loadingInstance.close();
    }
};

/**
 * 定义响应式变量youxilist
 * @type {UnwrapNestedRefs<EYouxi[]>}
 */
const youxilist = ref([]);
const getyouxilist = async () => {
    // 获取游戏数据,并赋值给youxilist变量
    // 将笔记的关联游戏ID与游戏表的ID进行匹配
    if (map.guanlianyouxi) {
        youxilist.value = await DB.name('youxi').where('id', '=', map.guanlianyouxi).limit("4").order("id desc").select();
    } else {
        youxilist.value = [];
    }
}
// 监听map值变化后，并重新获取数据游戏模块的数据
watch(() => map, getyouxilist, {immediate: true, deep: true});

// 获取评论信息
const isCommit = ref(true);
const commentCallback = (commit) => {
    // 评论提交的时候添加内容
};
const comments = ref([]);
const getcommentsFunc = async () => {
    comments.value = await DB.name("pinglun").alias('pl')
        .joinLeft("yonghu u", "u.zhanghao=pl.pinglunren")
        .field('pl.*')
        .field('u.touxiang as headimg')
        .field('u.mingcheng as nickname')
        .field('u.zhanghao as username')
        .where('pl.biao', 'biji')
        .where('pl.biaoid', route.query.id)
        .order("pl.id desc")
        .select();
};
getcommentsFunc();
// end 获取评论信息

// 关注/取消关注用户功能
const isFollowed = ref(false);

const checkFollowStatus = async () => {
    if (session('username') && map.tianjiaren) {
        isFollowed.value = await checkIsFollow(map.tianjiaren);
    }
};

// Watch map change to check follow status
watch(() => map.tianjiaren, checkFollowStatus);

const followUser = async () => {
    if (!session('username')) {
        var r = await ElMessageBox.confirm('请先登录');
        router.push({path: '/login', query: {redirect: route.fullPath}});
        return;
    }

    if (session('username') === map.tianjiaren) {
        ElMessage.error('不能关注自己');
        return;
    }

    try {
        const res = await canGuanzhuInsert(map.tianjiaren);
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
        const res = await canGuanzhuDelete(map.tianjiaren);
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

const goToUserDetail = (id) => {
    console.log('Navigating to user detail with ID:', id);
    if (id) {
        router.push({ path: '/yonghu/detail', query: { id } }).catch(err => {
            console.error('Navigation failed:', err);
            ElMessage.error('跳转失败: ' + err.message);
        });
    } else {
        ElMessage.warning('用户ID无效');
    }
};
</script>


<style scoped lang="scss">
.views-biji-web-detail {
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
    width: 400px;
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

.author-link {
    color: #409EFF;
    text-decoration: none;
    font-weight: 600;
    display: inline-flex;
    align-items: center;
    transition: all 0.3s;
    
    &:hover {
        color: #66b1ff;
        transform: translateX(2px);
    }
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

/* Tabs styling */
:deep(.el-tabs__item) {
    font-size: 16px;
    font-weight: 500;
}

:deep(.el-tabs--border-card) {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
    border: none;
    border-radius: 8px;
}

:deep(.el-tabs--border-card>.el-tabs__header) {
    background-color: #f5f7fa;
    border-bottom: 1px solid #ebeef5;
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
}
</style>