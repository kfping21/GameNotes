<template>
    <div class="views-my-activity">
        <e-container>
            <div class="activity-card">
                <div class="header">
                    <div class="title">我的收藏与点赞</div>
                </div>

                <el-tabs v-model="activeTab">
                    <el-tab-pane label="我的收藏" name="favorites">
                        <el-table :data="favorites.list" v-loading="favorites.loading" style="width: 100%">
                            <el-table-column prop="biaoti" label="标题" min-width="240" />
                            <el-table-column prop="addtime" label="时间" width="180">
                                <template #default="{ row }">{{ formatTime(row.addtime) }}</template>
                            </el-table-column>
                            <el-table-column label="操作" width="120">
                                <template #default="{ row }">
                                    <el-button type="primary" link @click="goToItem(row, 'favorites')">查看</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <el-empty v-if="!favorites.loading && favorites.list.length === 0" description="暂无收藏" />
                        <div class="pager">
                            <el-pagination
                                @current-change="(page) => loadFavorites(page)"
                                :page-sizes="[12, 24, 36, 48]"
                                v-model:current-page="favorites.page"
                                v-model:page-size="favorites.pagesize"
                                @size-change="(size) => onFavoriteSizeChange(size)"
                                layout="total, sizes, prev, pager, next"
                                :total="favorites.total"
                            />
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="我的点赞" name="likes">
                        <el-table :data="likes.list" v-loading="likes.loading" style="width: 100%">
                            <el-table-column prop="biaoti" label="标题" min-width="240" />
                            <el-table-column prop="addtime" label="时间" width="180">
                                <template #default="{ row }">{{ formatTime(row.addtime) }}</template>
                            </el-table-column>
                            <el-table-column label="操作" width="120">
                                <template #default="{ row }">
                                    <el-button type="primary" link @click="goToItem(row, 'likes')">查看</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <el-empty v-if="!likes.loading && likes.list.length === 0" description="暂无点赞" />
                        <div class="pager">
                            <el-pagination
                                @current-change="(page) => loadLikes(page)"
                                :page-sizes="[12, 24, 36, 48]"
                                v-model:current-page="likes.page"
                                v-model:page-size="likes.pagesize"
                                @size-change="(size) => onLikeSizeChange(size)"
                                layout="total, sizes, prev, pager, next"
                                :total="likes.total"
                            />
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import http from "@/utils/ajax/http";
import { session } from "@/utils/utils";
import { ElMessage } from "element-plus";

const router = useRouter();
const activeTab = ref("favorites");

const favorites = reactive({
    list: [],
    loading: false,
    page: 1,
    pagesize: 12,
    total: 0,
});

const likes = reactive({
    list: [],
    loading: false,
    page: 1,
    pagesize: 12,
    total: 0,
});

const formatTime = (value) => {
    if (!value) return "";
    return String(value).substring(0, 19);
};

const buildQuery = (page, pagesize) => ({
    username: session("username"),
    page,
    pagesize,
    orderby: "id",
    sort: "desc",
});

const loadFavorites = (page = favorites.page) => {
    if (favorites.loading) return;
    favorites.loading = true;
    favorites.page = page;
    http.post("/api/shoucang/selectUsername", buildQuery(page, favorites.pagesize)).then(
        (res) => {
            favorites.loading = false;
            if (res.code === 0) {
                favorites.list = res.data?.lists?.records || [];
                favorites.total = res.data?.lists?.total || 0;
            } else {
                ElMessage.error(res.msg || "获取收藏失败");
            }
        },
        (err) => {
            favorites.loading = false;
            ElMessage.error(err.message || "获取收藏失败");
        }
    );
};

const loadLikes = (page = likes.page) => {
    if (likes.loading) return;
    likes.loading = true;
    likes.page = page;
    http.post("/api/dianzan/selectUsername", buildQuery(page, likes.pagesize)).then(
        (res) => {
            likes.loading = false;
            if (res.code === 0) {
                likes.list = res.data?.lists?.records || [];
                likes.total = res.data?.lists?.total || 0;
            } else {
                ElMessage.error(res.msg || "获取点赞失败");
            }
        },
        (err) => {
            likes.loading = false;
            ElMessage.error(err.message || "获取点赞失败");
        }
    );
};

const onFavoriteSizeChange = (size) => {
    favorites.pagesize = size;
    loadFavorites(1);
};

const onLikeSizeChange = (size) => {
    likes.pagesize = size;
    loadLikes(1);
};

const goToItem = (row, type) => {
    const biao = row.biao || (type === "favorites" ? "biji" : "biji");
    const id = row.biaoid || row.xwid || row.id;
    if (biao && id) {
        router.push({ path: `/${biao}/detail`, query: { id } });
    }
};

onMounted(() => {
    loadFavorites(1);
    loadLikes(1);
});
</script>

<style scoped lang="scss">
.views-my-activity {
    padding: 24px 0 40px;
}

.activity-card {
    background: var(--theme-surface-color);
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.header {
    display: flex;
    align-items: center;
    margin-bottom: 16px;

    .title {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
    }
}

.pager {
    margin-top: 16px;
    display: flex;
    justify-content: center;
}
</style>
