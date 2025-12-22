<template>
    <div class="views-zhuye">
        <!-- 轮播图：展示游戏 -->
        <div class="carousel-section">
            <el-carousel indicator-position="outside" height="400px">
                <el-carousel-item v-for="item in youxiList" :key="item.id">
                    <router-link :to="{ path: '/youxi/detail', query: { id: item.id } }">
                        <div class="carousel-img" :style="{ 'background-image': 'url(' + $formatImageSrc(item.youxitupian) + ')' }">
                            <div class="carousel-title">{{ item.youximingcheng }}</div>
                        </div>
                    </router-link>
                </el-carousel-item>
            </el-carousel>
        </div>

        <e-container>
            <el-row :gutter="20">
                <!-- 左侧：笔记列表 (可切换) -->
                <el-col :md="18" :sm="24">
                    <div class="main-content">
                        <el-tabs v-model="activeTab" class="note-tabs">
                            <el-tab-pane label="推荐" name="recommend">
                                <div class="note-list">
                                    <div v-for="r in bijilist" :key="r.id" class="note-item">
                                        <div class="note-cover">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }">
                                                <e-img :src="r.bijifengmian" pb="100"></e-img>
                                            </router-link>
                                        </div>
                                        <div class="note-info">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="note-title" v-html="$substr(r.bijimingcheng, 30)"></router-link>
                                            <div class="note-desc" v-html="$substr(r.xiangqing, 60)"></div>
                                            <div class="note-meta">
                                                <span class="author"><i class="fa fa-user"></i> {{ r.authorName || r.tianjiaren }}</span>
                                                <span class="time">{{ r.addtime }}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <el-empty v-if="bijilist.length === 0" description="暂无推荐笔记"></el-empty>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="关注" name="following">
                                <div class="note-list">
                                    <div v-if="bijilist1.length === 0" class="empty-tip">
                                        {{ $session.username ? '暂无关注用户的动态' : '登录后查看关注动态' }}
                                    </div>
                                    <div v-else v-for="r in bijilist1" :key="r.id" class="note-item">
                                        <div class="note-cover">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }">
                                                <e-img :src="r.bijifengmian" pb="100"></e-img>
                                            </router-link>
                                        </div>
                                        <div class="note-info">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="note-title" v-html="$substr(r.bijimingcheng, 30)"></router-link>
                                            <div class="note-desc" v-html="$substr(r.xiangqing, 60)"></div>
                                            <div class="note-meta">
                                                <span class="author"><i class="fa fa-user"></i> {{ r.authorName || r.tianjiaren }}</span>
                                                <span class="time">{{ r.addtime }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="热门" name="hot">
                                <div class="note-list">
                                    <div v-for="r in bijilist2" :key="r.id" class="note-item">
                                        <div class="note-cover">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }">
                                                <e-img :src="r.bijifengmian" pb="100"></e-img>
                                            </router-link>
                                        </div>
                                        <div class="note-info">
                                            <router-link :to="{ path: '/biji/detail', query: { id: r.id } }" class="note-title" v-html="$substr(r.bijimingcheng, 30)"></router-link>
                                            <div class="note-desc" v-html="$substr(r.xiangqing, 60)"></div>
                                            <div class="note-meta">
                                                <span class="likes"><i class="fa fa-heart"></i> {{ r.zhongcaoCount || 0 }}</span>
                                                <span class="author"><i class="fa fa-user"></i> {{ r.authorName || r.tianjiaren }}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <el-empty v-if="bijilist2.length === 0" description="暂无热门笔记"></el-empty>
                                </div>
                            </el-tab-pane>
                        </el-tabs>
                    </div>
                </el-col>

                <!-- 右侧：侧边栏 (话题、游戏、周边) -->
                <el-col :md="6" :sm="24">
                    <div class="sidebar">
                        <!-- 热门话题 -->
                        <div class="sidebar-widget">
                            <h3 class="widget-title">
                                热门话题
                                <router-link to="/topic" class="more">更多</router-link>
                            </h3>
                            <ul class="widget-list">
                                <li v-for="item in topicList" :key="item.id">
                                    <router-link :to="{ path: '/topic/detail', query: { id: item.id } }">
                                        <span class="hash">#</span> {{ item.title }}
                                    </router-link>
                                </li>
                                <li v-if="topicList.length === 0" class="empty">暂无话题</li>
                            </ul>
                        </div>

                        <!-- 热门游戏 -->
                        <div class="sidebar-widget">
                            <h3 class="widget-title">
                                热门游戏
                                <router-link to="/youxi/category" class="more">更多</router-link>
                            </h3>
                            <div class="game-grid">
                                <div v-for="item in youxiList" :key="item.id" class="game-item">
                                    <router-link :to="{ path: '/youxi/detail', query: { id: item.id } }">
                                        <e-img :src="item.youxitupian" class="game-img" pb="100"></e-img>
                                        <div class="game-name">{{ item.youximingcheng }}</div>
                                    </router-link>
                                </div>
                            </div>
                        </div>

                        <!-- 周边推荐 -->
                        <div class="sidebar-widget">
                            <h3 class="widget-title">
                                周边推荐
                                <router-link to="/mall/products" class="more">更多</router-link>
                            </h3>
                            <div class="product-list">
                                <div v-for="item in productList" :key="item.id" class="product-item">
                                    <router-link :to="{ path: '/mall/detail', query: { id: item.id } }" class="product-link">
                                        <div class="product-img">
                                            <e-img :src="getProductImage(item)" pb="100"></e-img>
                                        </div>
                                        <div class="product-info">
                                            <div class="name">{{ item.name }}</div>
                                            <div class="price">￥{{ item.price }}</div>
                                        </div>
                                    </router-link>
                                </div>
                                <div v-if="productList.length === 0" class="empty">暂无周边</div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </e-container>

    </div>
</template>

<script setup>
import http from "@/utils/ajax/http";
import DB from "@/utils/db";
import { selectGuanzhuren } from "@/module/guanzhu";
import { getTopicCategories } from "@/module/topic";

import { ref, reactive, onMounted } from "vue";
import { useRoute } from "vue-router";
import { session } from "@/utils/utils";

const route = useRoute();
const activeTab = ref('recommend');

// 获取游戏列表 (用于轮播图和侧边栏)
const youxiList = DB.name("youxi").order("id desc").limit(5).selectRef();

// 获取话题列表
const topicList = ref([]);
const loadTopics = async () => {
    try {
        const res = await getTopicCategories({ page: 1, pagesize: 5 });
        if (res.code === 0) {
            topicList.value = res.data.lists.records || res.data.lists || [];
        }
    } catch (e) {
        console.error("加载话题失败", e);
    }
};

// 获取周边列表
const productList = ref([]);
const loadProducts = async () => {
    try {
        const res = await http.get("/api/mall/products", { page: 1, pagesize: 5 });
        if (res.code === 0) {
            let records = res.data.records || [];
            
            // 尝试手动获取图片（如果接口没返回 images）
            try {
                const ids = records.map(r => r.id);
                if (ids.length > 0) {
                    // 尝试从 product_image 表获取图片
                    const images = await DB.name("product_image")
                        .where("product_id", "in", ids)
                        .order("sort asc")
                        .select();
                    
                    if (images && Array.isArray(images)) {
                        records.forEach(r => {
                            // 将图片关联到商品
                            const productImages = images.filter(img => img.product_id == r.id);
                            if (productImages.length > 0) {
                                r.images = productImages;
                            }
                        });
                    }
                }
            } catch (err) {
                console.warn("尝试加载 product_images 失败，可能表不存在", err);
            }
            
            productList.value = records;
        }
    } catch (e) {
        console.error("加载周边失败", e);
    }
};

const getProductImage = (item) => {
    // 优先使用非默认命名的图片（假设默认命名包含 product_，而上传的图片是哈希名）
    const isPreferred = (url) => {
        if (!url) return false;
        const s = String(url);
        return s.indexOf('product_') === -1 && s.indexOf('/upload/') !== -1;
    };

    // 1. 尝试从 images 数组中找
    if (item.images && Array.isArray(item.images)) {
        const found = item.images.find(img => isPreferred(img.url || img));
        if (found) return found.url || found;
    }

    // 2. 检查 cover_url
    if (isPreferred(item.cover_url)) return item.cover_url;

    // 3. 再次尝试 images 数组的第一个
    if (item.images && Array.isArray(item.images) && item.images.length > 0) {
        return item.images[0].url || item.images[0];
    }

    // 4. 兜底
    return item.cover_url || item.image || item.tupian || item.fengmian || item.img || "";
};

// 获取用户个性化推荐的笔记列表
const bijilist = ref([]);

const fetchAuthorNames = async (list) => {
    if (!list || list.length === 0) return;
    const ids = [...new Set(list.map(item => item.tianjiaren).filter(id => id))];
    if (ids.length === 0) return;
    
    // Fetch users
    const users = await DB.name("yonghu").where("zhanghao", "in", ids).select();
    const userMap = {};
    users.forEach(u => {
        userMap[u.zhanghao] = u.mingcheng;
    });
    
    list.forEach(item => {
        if (userMap[item.tianjiaren]) {
            item.authorName = userMap[item.tianjiaren];
        }
    });
};

const fetchZhongcaoCounts = async (list) => {
    if (!list || list.length === 0) return;
    const ids = list.map(item => item.id);
    if (ids.length === 0) return;

    try {
        // Fetch zhongcao counts from zhongcao table
        const counts = await DB.name("zhongcao")
            .field("bijiid, count(*) as count")
            .where("bijiid", "in", ids)
            .group("bijiid")
            .select();
        
        const countMap = {};
        counts.forEach(c => {
            countMap[c.bijiid] = c.count;
        });

        list.forEach(item => {
            item.zhongcaoCount = countMap[item.id] || 0;
        });
    } catch (e) {
        console.error("Failed to fetch zhongcao counts", e);
    }
};

const fetchLikeCounts = async (list) => {
    if (!list || list.length === 0) return;
    const ids = list.map(item => item.id);
    if (ids.length === 0) return;

    try {
        // Fetch like counts from dianzan table
        const counts = await DB.name("dianzan")
            .field("biaoid, count(*) as count")
            .where("biao", "biji")
            .where("biaoid", "in", ids)
            .group("biaoid")
            .select();
        
        const countMap = {};
        counts.forEach(c => {
            countMap[c.biaoid] = c.count;
        });

        list.forEach(item => {
            item.likeCount = countMap[item.id] || 0;
        });
    } catch (e) {
        console.error("Failed to fetch like counts", e);
    }
};

// 调用后端推荐接口获取个性化推荐笔记
const getRecommendData = async () => {
    try {
        const res = await http.get('/api/biji/recommendByUserTagsAndGames');
        if (res.code === 0) {
            bijilist.value = res.data.list || [];
            await fetchAuthorNames(bijilist.value);
            await fetchLikeCounts(bijilist.value);
        } else {
            // 如果推荐获取失败，回退到默认的笔记列表
            const data = await DB.name("biji")
                .alias("a")
                .joinLeft("yonghu b", "a.tianjiaren=b.zhanghao")
                .field("a.*,b.mingcheng as authorName")
                .where("a.issh", "是")
                .order("a.id desc")
                .select();
            bijilist.value = data;
            await fetchLikeCounts(bijilist.value);
        }
    } catch (error) {
        // 如果调用失败，回退到默认的笔记列表
        const data = await DB.name("biji")
            .alias("a")
            .joinLeft("yonghu b", "a.tianjiaren=b.zhanghao")
            .field("a.*,b.mingcheng as authorName")
            .where("a.issh", "是")
            .order("a.id desc")
            .select();
        bijilist.value = data;
        await fetchLikeCounts(bijilist.value);
    }
};

/**
 * 定义响应式变量bijilist1,并获取数据笔记模块的数据
 * @type {UnwrapNestedRefs<EBiji[]>}
 */
const bijilist1 = ref([]);

const getFollowData = async () => {
    const username = session('username');
    if (username) {
        try {
            // 获取关注的用户列表
            const res = await selectGuanzhuren({ page: 1, size: 100, username });
            let followList = [];
            if (res.code == 0) {
                if (Array.isArray(res.data.lists)) {
                    followList = res.data.lists;
                } else if (res.data.lists && Array.isArray(res.data.lists.records)) {
                    followList = res.data.lists.records;
                }
            }

            if (followList.length > 0) {
                const followees = followList.map(r => r.followee);
                // 获取这些用户的笔记
                const data = await DB.name("biji")
                    .alias("a")
                    .joinLeft("yonghu b", "a.tianjiaren=b.zhanghao")
                    .field("a.*,b.mingcheng as authorName")
                    .where("a.issh", "是")
                    .where("a.tianjiaren", "in", followees)
                    .order("a.id desc")
                    .select();
                bijilist1.value = data;
                await fetchLikeCounts(bijilist1.value);
            } else {
                bijilist1.value = [];
            }
        } catch (e) {
            console.error(e);
            bijilist1.value = [];
        }
    } else {
        bijilist1.value = [];
    }
};

/**
 * 定义响应式变量bijilist2,并获取数据笔记模块的数据
 * @type {UnwrapNestedRefs<EBiji[]>}
 */
const bijilist2 = ref([]);
const loadHotNotes = async () => {
    const data = await DB.name("biji")
        .alias("a")
        .joinLeft("yonghu b", "a.tianjiaren=b.zhanghao")
        .field("a.*,b.mingcheng as authorName")
        .where("a.issh", "是")
        .order("a.zhongcaodu desc")
        .select();
    bijilist2.value = data;
    await fetchZhongcaoCounts(bijilist2.value);
    // Re-sort based on real-time counts to ensure display matches sorting
    bijilist2.value.sort((a, b) => (b.zhongcaoCount || 0) - (a.zhongcaoCount || 0));
};

onMounted(() => {
    getRecommendData();
    getFollowData();
    loadHotNotes();
    loadTopics();
    loadProducts();
});
</script>

<style scoped lang="scss">
.views-zhuye {
    padding-bottom: 40px;
}

.carousel-section {
    margin-bottom: 20px;
    .carousel-img {
        width: 100%;
        height: 400px;
        background-size: cover;
        background-position: center;
        position: relative;
        
        .carousel-title {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            padding: 20px;
            background: linear-gradient(to top, rgba(0,0,0,0.7), transparent);
            color: #fff;
            font-size: 24px;
            font-weight: bold;
        }
    }
}

.main-content {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.note-list {
    .note-item {
        display: flex;
        gap: 15px;
        padding: 15px 0;
        border-bottom: 1px solid #f0f0f0;
        
        &:last-child {
            border-bottom: none;
        }
        
        .note-cover {
            width: 160px;
            height: 100px;
            flex-shrink: 0;
            border-radius: 4px;
            overflow: hidden;
        }
        
        .note-info {
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            
            .note-title {
                font-size: 16px;
                font-weight: bold;
                color: #333;
                margin-bottom: 8px;
                display: block;
                
                &:hover {
                    color: #409eff;
                }
            }
            
            .note-desc {
                font-size: 14px;
                color: #666;
                line-height: 1.5;
                margin-bottom: 8px;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
            }
            
            .note-meta {
                font-size: 12px;
                color: #999;
                display: flex;
                gap: 15px;
                
                i {
                    margin-right: 4px;
                }
            }
        }
    }
    
    .empty-tip {
        text-align: center;
        padding: 40px;
        color: #999;
    }
}

.sidebar {
    .sidebar-widget {
        background: #fff;
        padding: 15px;
        border-radius: 8px;
        box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
        margin-bottom: 20px;
        
        .widget-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
            display: flex;
            justify-content: space-between;
            align-items: center;
            
            .more {
                font-size: 12px;
                color: #999;
                font-weight: normal;
                
                &:hover {
                    color: #409eff;
                }
            }
        }
        
        .widget-list {
            list-style: none;
            padding: 0;
            margin: 0;
            
            li {
                margin-bottom: 10px;
                
                a {
                    color: #333;
                    display: block;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    
                    &:hover {
                        color: #409eff;
                    }
                    
                    .hash {
                        color: #409eff;
                        margin-right: 4px;
                    }
                }
            }
            
            .empty {
                color: #999;
                text-align: center;
                padding: 10px;
            }
        }
        
        .game-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 10px;
            
            .game-item {
                .game-img {
                    border-radius: 4px;
                    overflow: hidden;
                    margin-bottom: 5px;
                }
                .game-name {
                    font-size: 12px;
                    text-align: center;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    color: #333;
                }
            }
        }
        
        .product-list {
            .product-item {
                margin-bottom: 10px;
                
                .product-link {
                    display: flex;
                    gap: 10px;
                    align-items: center;
                    
                    .product-img {
                        width: 50px;
                        height: 50px;
                        flex-shrink: 0;
                        border-radius: 4px;
                        overflow: hidden;
                    }
                    
                    .product-info {
                        flex: 1;
                        overflow: hidden;
                        
                        .name {
                            font-size: 14px;
                            color: #333;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                            margin-bottom: 4px;
                        }
                        
                        .price {
                            color: #f56c6c;
                            font-weight: bold;
                            font-size: 12px;
                        }
                    }
                }
            }
            
            .empty {
                color: #999;
                text-align: center;
                padding: 10px;
            }
        }
    }
}

</style>
