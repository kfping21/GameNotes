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
                             <e-select-view module="youxi" :value="map.guanlianyouxi" select="id"
                                            show="youximingcheng"></e-select-view>                        </span>
                                        </div>
                                        <div>
                        <span class="name">
                            关注量：
                        </span>
                                            <span class="val">
                            {{ map.guanzhuliang }}                        </span>
                                        </div>
                                        <div>
                        <span class="name">
                            种草度：
                        </span>
                                            <span class="val">
                            {{ map.zhongcaodu }}                        </span>
                                        </div>
                                        <div>
                        <span class="name">
                            添加人：
                        </span>
                                            <span class="val">
                            {{ map.tianjiaren }}                        </span>
                                        </div>
                                    </div>

                                    <template v-if="$session.cx == '用户'">

                                        <el-button v-if="$session.username != map.tianjiaren" type="primary"
                                                   @click="$router.push('/zhongcao/add?id='+map.id)">种草
                                        </el-button>




                                        <e-chat-check  v-if="map.tianjiaren != $session.zhanghao " type="success" :rid="map.tianjiaren">私聊</e-chat-check>



                                        <el-button v-if="$session.username != map.tianjiaren && !map.guanzhuCount" type="primary"
                                                   @click="followNote">
                                            <i class="fa fa-plus-circle"></i> 关注
                                        </el-button>
                                        <el-button v-if="$session.username != map.tianjiaren && map.guanzhuCount" type="success" plain
                                                   @click="unfollowNote">
                                            <i class="fa fa-check-circle"></i> 已关注
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
                        <el-tab-pane label="关联游戏">


                            <div class="list-table">
                                <table width="100%" border="1" class="table table-list table-bordered table-hover">
                                    <thead>
                                    <tr align="center">

                                        <th> 游戏名称</th>
                                        <th> 关联笔记</th>

                                        <th width="80" align="center">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>

                                    <tr v-for="(r,i) in youxilist">
                                        <td> {{ r.youximingcheng }}</td>
                                        <td>
                                            <e-select-view module="biji" :value="r.guanlianbiji" select="id"
                                                           show="bijimingcheng"></e-select-view>
                                        </td>
                                        <td align="center">
                                            <el-button @click="$goto('/youxi/detail?id='+r.id)">
                                                详细
                                            </el-button>
                                        </td>
                                    </tr>

                                    </tbody>
                                </table>
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

import {ref, reactive, watch, computed, unref} from 'vue';
import {useRoute} from 'vue-router';
import {session} from '@/utils/utils';
import {extend} from '@/utils/extend';
import {useBijiFindById, canBijiFindById, canZhongcaoCreateForm, canZhongcaoInsert} from '@/module';
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
watch(() => props.id, async (id) => {
    const res = await canBijiFindById(id);
    extend(map, res);

    // 检查当前用户是否已关注此笔记
    if (session('username')) {
        const guanzhuRecords = await DB.name('guanzhu')
            .where('bijiid', '=', map.id)
            .where('guanzhuren', '=', session('username'))
            .select();
        map.guanzhuCount = guanzhuRecords.length > 0 ? 1 : 0;
    } else {
        map.guanzhuCount = 0;
    }
});
// end 获取详情页面的一行数据

const zhongcaoForm = ref({});
const readMap = ref({});
const zhongcaoFormElement = ref(null);

watch(() => map, async () => {
    if (!map.id || !session('username')) return;

    var s = await canZhongcaoCreateForm(map.id, map);
    zhongcaoForm.value = s.form;
    readMap.value = s.readMap;
}, {deep: true});

const onAddCard = async () => {
    if (!session('username')) {
        var r = await ElMessageBox.confirm('请先登录');
        router.push({path: '/login', query: {redirect: route.fullPath}});
        return;
    }
    zhongcaoFormElement.value.validate().then(async () => {
        const loadingInstance = ElLoading.service({
            fullscreen: true,
            lock: true,
            text: '正在提交...',
        });
        try {
            const res = await canZhongcaoInsert(zhongcaoForm.value);
            loadingInstance.close();
            if (res.code == 0) {
                ElMessage.success('添加种草成功');
            } else {
                ElMessage.error(res.msg);
            }
        } catch (e) {
            ElMessage.error(e.message);
            loadingInstance.close();
        }
    });
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

// 关注/取消关注笔记功能
const followNote = async () => {
    if (!session('username')) {
        var r = await ElMessageBox.confirm('请先登录');
        router.push({path: '/login', query: {redirect: route.fullPath}});
        return;
    }

    if (session('username') === map.tianjiaren) {
        ElMessage.error('不能关注自己的笔记');
        return;
    }

    try {
        const form = {
            bijiid: map.id,
            bijibianhao: map.bijibianhao,
            bijimingcheng: map.bijimingcheng,
            biaoqian: map.biaoqian,
            guanlianyouxi: map.guanlianyouxi,
            tianjiaren: map.tianjiaren,
            guanzhuren: session('username')
        };

        const res = await http.post('/api/guanzhu/insert', form);
        if (res.code === 0) {
            ElMessage.success('关注成功');
            // 更新关注量
            map.guanzhuliang = (map.guanzhuliang || 0) + 1;
            // 设置已关注状态
            map.guanzhuCount = 1;
        } else {
            ElMessage.error(res.msg || '关注失败');
        }
    } catch (error) {
        ElMessage.error(error.message || '关注失败');
    }
};

const unfollowNote = async () => {
    if (!session('username')) {
        var r = await ElMessageBox.confirm('请先登录');
        router.push({path: '/login', query: {redirect: route.fullPath}});
        return;
    }

    try {
        // 先查询是否存在关注记录
        const guanzhuRecords = await DB.name('guanzhu')
            .where('bijiid', '=', map.id)
            .where('guanzhuren', '=', session('username'))
            .select();

        if (guanzhuRecords.length > 0) {
            // 删除关注记录
            const res = await http.post('/api/guanzhu/delete', [guanzhuRecords[0].id]);
            if (res.code === 0) {
                ElMessage.success('取消关注成功');
                // 更新关注量
                map.guanzhuliang = Math.max(0, (map.guanzhuliang || 0) - 1);
                // 设置未关注状态
                map.guanzhuCount = 0;
            } else {
                ElMessage.error(res.msg || '取消关注失败');
            }
        }
    } catch (error) {
        ElMessage.error(error.message || '取消关注失败');
    }
};
</script>


<style scoped lang="scss">
.views-biji-web-detail {

}

</style>