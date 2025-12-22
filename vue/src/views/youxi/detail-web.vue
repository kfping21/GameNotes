<template>
    <div class="views-youxi-web-detail">
        <div>
            <e-container>
                <div class="nav-bar" style="margin-bottom: 15px; padding-top: 20px;">
                    <div class="back-link" @click="$router.push('/youxi/category')" style="cursor: pointer; font-size: 14px; color: #606266; display: inline-flex; align-items: center;">
                        <el-icon style="margin-right: 4px;"><ArrowLeft /></el-icon> 返回游戏列表
                    </div>
                </div>
                <div class="title-modelbox-widget1">
                    <h3 class="section-title">详情</h3>
                    <div class="sidebar-widget-body">
                        <div class="">
                            <div class="goods-info clearfix">
                                <div class="gallery-list">
                                    <e-shangpinban :images="map.youxitupian"></e-shangpinban>
                                </div>
                                <div class="goods-right-content">
                                    <h3 class="title" v-text="map.youximingcheng"></h3>
                                    <div class="descount">
                                        <div>
                                            <span class="name"> 简介： </span>
                                            <span class="val" v-html="gameIntro"></span>
                                        </div>
                                    </div>
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
                        <el-tab-pane label="简介">
                            <div class="content-section">
                                <el-tabs v-model="infoTab" class="detail-sub-tabs">
                                    <el-tab-pane label="游戏简介" name="intro">
                                        <section class="detail-block">
                                            <h4>游戏简介</h4>
                                            <div v-if="map.xiangqing" v-html="map.xiangqing"></div>
                                            <el-empty v-else description="暂无游戏介绍" />
                                        </section>
                                    </el-tab-pane>
                                    <el-tab-pane label="装备库" name="equipment">
                                        <section class="detail-block">
                                            <h4>装备库</h4>
                                            <el-empty v-if="!equipment.length" description="暂无装备" />
                                            <el-row v-else :gutter="16">
                                                <el-col v-for="eq in equipment" :key="eq.id" :xl="6" :lg="8" :md="12" :sm="12" :xs="24">
                                                    <el-card class="info-card" shadow="hover" @click="showDetail('equipment', eq)">
                                                        <div class="card-thumb">
                                                            <e-img :src="eq.cover_url" class="info-card-cover" />
                                                            <div class="card-title-overlay">{{ eq.name }}</div>
                                                        </div>
                                                        <div class="card-body">
                                                            <div class="card-meta">
                                                                <span v-if="eq.type">{{ eq.type }}</span>
                                                                <span v-if="eq.rarity">{{ eq.rarity }}</span>
                                                            </div>
                                                            <p class="card-desc">{{ eq.summary }}</p>
                                                        </div>
                                                    </el-card>
                                                </el-col>
                                            </el-row>
                                        </section>
                                    </el-tab-pane>
                                    <el-tab-pane label="人物库" name="characters">
                                        <section class="detail-block">
                                            <h4>人物库</h4>
                                            <el-empty v-if="!characters.length" description="暂无人物" />
                                            <el-row v-else :gutter="16">
                                                <el-col v-for="ch in characters" :key="ch.id" :xl="6" :lg="8" :md="12" :sm="12" :xs="24">
                                                    <el-card class="info-card" shadow="hover" @click="showDetail('characters', ch)">
                                                        <div class="card-thumb">
                                                            <e-img :src="ch.avatar_url" class="info-card-cover" />
                                                            <div class="card-title-overlay">{{ ch.name }}</div>
                                                        </div>
                                                        <div class="card-body">
                                                            <div class="card-meta">
                                                                <span v-if="ch.role">{{ ch.role }}</span>
                                                                <span v-if="ch.position">{{ ch.position }}</span>
                                                            </div>
                                                            <p class="card-desc">{{ ch.summary }}</p>
                                                        </div>
                                                    </el-card>
                                                </el-col>
                                            </el-row>
                                        </section>
                                    </el-tab-pane>
                                </el-tabs>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="相关笔记">
                            <div class="note-list">
                                <el-empty v-if="!notes.length" description="暂无关联笔记" />
                                <div v-else class="note-items">
                                    <div v-for="note in notes" :key="note.id" class="note-item" @click="goNote(note)">
                                        <div class="note-info">
                                            <h4 class="note-title">{{ note.bijimingcheng }}</h4>
                                            <div class="note-desc">{{ getNoteSummary(note) }}</div>
                                        </div>
                                        <div class="note-thumb" v-if="note.bijifengmian">
                                            <e-img :src="note.bijifengmian" class="note-img" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="周边">
                            <div class="content-section">
                                <div style="margin-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
                                    <h4 style="margin: 0;">相关周边</h4>
                                    <el-button v-if="peripheralsUrl" type="primary" size="small" @click="goPeripherals">查看更多</el-button>
                                </div>
                                
                                <el-empty v-if="!products.length" description="暂无相关周边" />
                                <el-row v-else :gutter="16">
                                    <el-col v-for="prod in products" :key="prod.id" :xl="6" :lg="8" :md="12" :sm="12" :xs="24">
                                        <el-card class="info-card" shadow="hover" @click="$router.push({ path: '/mall/detail', query: { id: prod.id } })">
                                            <div class="card-thumb">
                                                <e-img :src="prod.cover_url" class="info-card-cover" />
                                                <div class="card-title-overlay">{{ prod.name }}</div>
                                            </div>
                                            <div class="card-body">
                                                <div class="card-meta">
                                                    <span style="color: #f56c6c; font-weight: bold;">￥{{ prod.price }}</span>
                                                </div>
                                                <p class="card-desc" style="margin-top: 5px;">库存: {{ prod.stock }}</p>
                                            </div>
                                        </el-card>
                                    </el-col>
                                </el-row>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="游戏评论">
                            <e-comments module="youxi" :lists="comments"
                                        :biaoti="map.youximingcheng"
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
        <el-dialog v-model="detailDrawer.visible" :title="detailDrawer.title" width="800px" align-center @close="closeDetail">
            <div class="dialog-body">
                <div class="dialog-left">
                    <e-img v-if="detailDrawer.image" :src="detailDrawer.image" class="dialog-cover" />
                </div>
                <div class="dialog-right">
                    <p class="dialog-desc">{{ detailDrawer.description }}</p>
                    <div class="dialog-meta" v-if="detailDrawer.meta.length">
                        <p v-for="meta in detailDrawer.meta" :key="meta.label">
                            <span class="meta-label">{{ meta.label }}：</span>{{ meta.value }}
                        </p>
                    </div>
                    <div class="dialog-stats" v-if="detailDrawer.pairs.length">
                        <h5>详细信息</h5>
                        <ul class="stat-list">
                            <li v-for="(pair, idx) in detailDrawer.pairs" :key="idx">
                                <span class="stat-label">{{ pair.label }}</span>
                                <span class="stat-value">{{ pair.value }}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import EShangpinban from "@/components/shangpin/shangpinban.vue";
    import EComments from '@/components/comments/comment';

    import { ref, reactive, watch, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { pushHistory } from "@/utils/history";
    import { extend } from "@/utils/extend";
    import router from "@/router";
    import { ElMessage } from "element-plus";
    import { ArrowLeft } from '@element-plus/icons-vue';

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

    const map = reactive({
        intro: "",
        youximingcheng: "",
        youxitupian: "",
        xiangqing: "",
    });
    const notes = ref([]);
    const equipment = ref([]);
    const characters = ref([]);
    const products = ref([]);
    const peripheralsUrl = ref("");
    const infoTab = ref("intro");
    const detailDrawer = reactive({
        visible: false,
        section: "",
        title: "",
        image: "",
        description: "",
        meta: [],
        pairs: [],
    });

    const getSummary = (html) => {
        if (!html) return "";
        return String(html).replace(/<[^>]+>/g, "").trim().slice(0, 80);
    };

    const gameIntro = computed(() => {
        return map.intro || map.jianjie || map.summary || "暂无简介";
    });

    const safeParseJson = (payload, fallback = []) => {
        if (!payload) return fallback;
        try {
            const parsed = typeof payload === "string" ? JSON.parse(payload) : payload;
            return parsed ?? fallback;
        } catch (error) {
            console.warn("JSON 解析失败", error, payload);
            return fallback;
        }
    };

    const toPairs = (raw, labelPrefix = "属性") => {
        const parsed = safeParseJson(raw, []);
        if (Array.isArray(parsed)) {
            return parsed.map((item, index) => {
                if (typeof item === "string" || typeof item === "number") {
                    return { label: `${labelPrefix}${index + 1}`, value: item };
                }
                if (item && typeof item === "object") {
                    const label = item.label || item.name;
                    let value = item.value ?? item.power ?? item.content ?? item.desc ?? item.description;
                    
                    if (label && (value === undefined || value === null || value === "")) {
                         const otherKeys = Object.keys(item).filter(k => k !== 'label' && k !== 'name' && k !== 'id');
                         if (otherKeys.length > 0) {
                             value = item[otherKeys[0]];
                         }
                    }

                    if (label) {
                        if (value !== undefined && value !== null && value !== "") {
                            return { label, value };
                        }
                        return { label: `${labelPrefix}${index + 1}`, value: label };
                    }

                    const entries = Object.entries(item);
                    if (entries.length > 0) {
                        const [firstKey, firstValue] = entries[0];
                        return {
                            label: firstKey || `${labelPrefix}${index + 1}`,
                            value: firstValue,
                        };
                    }
                }
                return { label: `${labelPrefix}${index + 1}`, value: item };
            });
        }
        if (parsed && typeof parsed === "object") {
            return Object.keys(parsed).map((key) => ({ label: key, value: parsed[key] }));
        }
        return [];
    };

    const pickValue = (payload = {}, keys = [], fallback = "") => {
        for (const key of keys) {
            const value = payload?.[key];
            if (value !== undefined && value !== null && value !== "") {
                return value;
            }
        }
        return fallback;
    };

    const normalizeEquipment = (item = {}) => {
        const description = pickValue(item, ["intro", "description", "jianjie", "summary", "jieshao", "miaoshu", "neirong"], "暂无简介");
        return {
            ...item,
            cover_url: pickValue(item, ["cover_url", "cover", "image", "fengmian", "tupian", "logo"]),
            name: pickValue(item, ["name", "mingcheng", "title", "biaoti"], "未命名装备"),
            type: pickValue(item, ["type", "leixing", "category", "fenlei"]),
            rarity: pickValue(item, ["rarity", "pinji", "dengji", "pingji"]),
            source: pickValue(item, ["source", "laiyuan", "huoqu"], ""),
            description,
            summary: description,
            statsPairs: toPairs(item.stats_json ?? item.stats ?? [], "属性"),
        };
    };

    const normalizeCharacter = (item = {}) => {
        const summary = pickValue(item, ["intro", "tagline", "biaoyu", "jianjie", "summary", "desc"], "暂无简介");
        const bio = pickValue(item, ["bio", "description", "jieshao", "story", "content", "intro"], summary);
        return {
            ...item,
            avatar_url: pickValue(item, ["avatar_url", "avatar", "image", "cover", "touxiang"], item.cover_url),
            name: pickValue(item, ["name", "mingcheng", "title", "nickname"], "未命名角色"),
            role: pickValue(item, ["role", "dingwei", "zhiye"], ""),
            position: pickValue(item, ["position", "zhiye", "type", "fenlei"], ""),
            tagline: summary,
            bio,
            summary,
            skillsPairs: toPairs(item.skills_json ?? item.skills ?? [], "技能"),
            statsPairs: toPairs(item.stats_json ?? item.stats ?? [], "属性"),
        };
    };

    const loadDetail = async (id) => {
        if (!id) return;
        const res = await http.get("/api/youxi/findById", { id });
        if (res.code === 0) {
            const data = res.data || {};
            console.log("游戏详情数据:", data);
            extend(map, data.youxi || {});
            // 强制更新 intro，防止 extend 浅拷贝或属性丢失问题
            if (data.youxi && data.youxi.intro) {
                map.intro = data.youxi.intro;
            }
            notes.value = data.notes || [];
            equipment.value = (data.equipment || []).map(normalizeEquipment);
            characters.value = (data.characters || []).map(normalizeCharacter);
            peripheralsUrl.value = data.peripheralsUrl || "";

            pushHistory("youxi", {
                id: map.id || id,
                title: map.youximingcheng || data.youxi?.youximingcheng,
                summary: getSummary(map.intro || map.xiangqing),
                cover: map.youxitupian || data.youxi?.youxitupian,
                url: `/youxi/detail?id=${id}`,
            });

            // 加载周边商品
            loadProducts(id);
        } else {
            ElMessage.error(res.msg || "加载游戏详情失败");
        }
    };

    const loadProducts = async (gameId) => {
        try {
            // 调用商品列表接口，传入 gameId
            const res = await http.get("/api/mall/products", { gameId, page: 1, size: 4 });
            if (res.code === 0) {
                products.value = res.data.records || [];
            }
        } catch (e) {
            console.error("加载周边失败", e);
        }
    };
    watch(() => props.id, (id) => loadDetail(id), { immediate: true });

    const goNote = (row) => {
        const id = row.id;
        router.push({ path: '/biji/detail', query: { id } });
    };
    const goPeripherals = () => {
        // 跳转到周边商城列表页（显示所有周边）
        router.push('/mall/products');
    };

    const buildMeta = (section, entity) => {
        const meta = section === "equipment"
            ? [
                { label: "类型", value: entity.type },
                { label: "稀有度", value: entity.rarity },
                { label: "来源", value: entity.source },
            ]
            : [
                { label: "定位", value: entity.role || entity.position },
                { label: "阵营", value: entity.camp },
                { label: "特长", value: entity.specialty },
            ];
        return meta.filter((item) => item.value);
    };

    const showDetail = (section, entity) => {
        if (!entity) return;
        detailDrawer.visible = true;
        detailDrawer.section = section;
        detailDrawer.title = entity.name || (section === "equipment" ? "装备详情" : "人物详情");
        detailDrawer.image = section === "equipment" ? entity.cover_url : entity.avatar_url;
        detailDrawer.description = entity.description || entity.bio || entity.summary || entity.content || entity.tagline || "暂无简介";
        detailDrawer.meta = buildMeta(section, entity);
        
        let pairs = [];
        if (section === "equipment") {
            pairs = entity.statsPairs || [];
        } else {
            pairs = [...(entity.statsPairs || []), ...(entity.skillsPairs || [])];
        }
        
        // Deduplicate pairs based on label
        const uniquePairs = [];
        const seenLabels = new Set();
        for (const pair of pairs) {
            if (!seenLabels.has(pair.label)) {
                seenLabels.add(pair.label);
                uniquePairs.push(pair);
            }
        }
        detailDrawer.pairs = uniquePairs;
    };

    const closeDetail = () => {
        detailDrawer.visible = false;
        detailDrawer.section = "";
        detailDrawer.title = "";
        detailDrawer.meta = [];
        detailDrawer.pairs = [];
        detailDrawer.description = "";
        detailDrawer.image = "";
    };

    const getNoteSummary = (note) => {
        if (note.jianjie) return note.jianjie;
        const content = note.neirong || note.xiangqing || note.content || '';
        const text = content.replace(/<[^>]+>/g, '').replace(/\s+/g, ' ').trim();
        return text ? (text.substring(0, 100) + (text.length > 100 ? '...' : '')) : '暂无简介';
    };

    // 获取评论信息
    const isCommit = ref(true);
    const commentCallback = (commit) => {
        // 评论提交的时候添加内容
    };
    const comments = ref([]);
    const getcommentsFunc = async () => {
        if (props.id) {
            comments.value = await DB.name("pinglun").alias('pl')
                .joinLeft("yonghu u", "u.zhanghao=pl.pinglunren")
                .field('pl.*')
                .field('u.touxiang as headimg')
                .field('u.mingcheng as nickname')
                .field('u.zhanghao as username')
                .where('pl.biao', 'youxi')
                .where('pl.biaoid', props.id)
                .order("pl.id desc")
                .select();
        }
    };
    watch(() => props.id, getcommentsFunc, { immediate: true });
</script>

<style scoped lang="scss">
    .views-youxi-web-detail {
        .content-section {
            min-height: 300px;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 4px;
            
            :deep(img) {
                max-width: 100%;
                height: auto;
            }
        }

        .detail-stack {
            display: flex;
            flex-direction: column;
            gap: 24px;
        }

        .detail-block {
            border: 1px solid #ebeef5;
            border-radius: 8px;
            padding: 16px;
            background: #fff;

            h4 {
                font-size: 16px;
                font-weight: 600;
                margin: 0 0 12px;
            }
        }
    }

    .stat-list {
        list-style: none;
        margin: 12px 0 0;
        padding: 0;
        font-size: 13px;
        color: #606266;
    }

    .stat-list li {
        display: flex;
        justify-content: space-between;
        border-bottom: 1px solid #ebeef5;
        padding: 6px 0;
    }

    .stat-label {
        font-weight: 500;
    }

    .stat-value {
        color: #303133;
    }

    .detail-sub-tabs {
        .el-tabs__header {
            margin-bottom: 16px;
        }
    }

    .info-card {
        cursor: pointer;
        transition: transform 0.2s ease;
        display: flex;
        flex-direction: column;

        &:hover {
            transform: translateY(-4px);
        }
    }

    .card-thumb {
        position: relative;
    }

    .info-card-cover {
        width: 100%;
        height: 220px;
        object-fit: cover;
        border-radius: 6px;
    }

    .card-title-overlay {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        padding: 10px 12px;
        background: linear-gradient(180deg, rgba(0,0,0,0.6) 0%, rgba(0,0,0,0) 100%);
        color: #fff;
        font-weight: 600;
        font-size: 15px;
        border-radius: 6px 6px 0 0;
        box-sizing: border-box;
    }

    .card-body {
        flex: 1;
        display: flex;
        flex-direction: column;
        padding: 12px 2px 4px;
    }

    .card-title {
        font-weight: 600;
        font-size: 15px;
        margin-bottom: 4px;
    }

    .card-meta {
        margin-top: 4px;
        color: #909399;
        display: flex;
        gap: 8px;
        font-size: 13px;
        flex-wrap: wrap;
    }

    .card-desc {
        margin-top: 12px;
        font-size: 13px;
        color: #606266;
        flex: 1;
        line-height: 1.5;
        overflow: hidden;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
    }

    .drawer-body {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }

    .drawer-cover {
        width: 100%;
        max-height: 260px;
        object-fit: cover;
        border-radius: 8px;
    }

    .drawer-desc {
        color: #606266;
        line-height: 1.6;
        margin: 0;
    }

    .drawer-meta {
        border-top: 1px solid #ebeef5;
        padding-top: 8px;
        margin-top: 4px;

        p {
            margin: 0 0 6px;
            color: #303133;
        }
    }

    .drawer-stats {
        margin-top: 8px;

        h5 {
            margin: 0 0 6px;
            font-size: 14px;
            color: #303133;
        }
    }

    .meta-label {
        color: #909399;
    }

    .dialog-body {
        display: flex;
        gap: 24px;
        
        @media (max-width: 768px) {
            flex-direction: column;
        }
    }

    .dialog-left {
        flex: 0 0 300px;
        
        @media (max-width: 768px) {
            flex: auto;
        }
    }

    .dialog-right {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 16px;
    }

    .dialog-cover {
        width: 100%;
        border-radius: 8px;
        object-fit: cover;
    }

    .dialog-desc {
        color: #606266;
        line-height: 1.6;
        margin: 0;
        font-size: 14px;
    }

    .dialog-meta {
        border-top: 1px solid #ebeef5;
        padding-top: 12px;
        
        p {
            margin: 0 0 8px;
            color: #303133;
            font-size: 14px;
        }
    }

    .dialog-stats {
        h5 {
            margin: 0 0 8px;
            font-size: 15px;
            color: #303133;
            font-weight: 600;
        }
    }

    .note-items {
        display: flex;
        flex-direction: column;
        gap: 16px;
    }

    .note-item {
        display: flex;
        justify-content: space-between;
        gap: 16px;
        padding: 16px;
        background: #fff;
        border: 1px solid #ebeef5;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            transform: translateY(-2px);
        }
    }

    .note-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 8px;
        overflow: hidden;
    }

    .note-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .note-desc {
        font-size: 14px;
        color: #606266;
        line-height: 1.5;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .note-thumb {
        flex: 0 0 160px;
        height: 100px;
        border-radius: 4px;
        overflow: hidden;
    }

    .note-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

</style>
