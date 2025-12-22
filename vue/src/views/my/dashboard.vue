<template>
    <div class="views-my-dashboard">
        <e-container>
            <div class="dashboard-card">
                <div class="header">
                    <div>
                        <div class="title">数据看板</div>
                        <div class="subtitle">快速了解你的发布与互动情况</div>
                    </div>
                    <el-button type="primary" plain @click="reload">刷新数据</el-button>
                </div>

                <div class="stat-grid">
                    <div v-for="item in statCards" :key="item.label" class="stat-item">
                        <div class="stat-icon">
                            <i :class="item.icon"></i>
                        </div>
                        <div class="stat-value">{{ item.value }}</div>
                        <div class="stat-label">{{ item.label }}</div>
                    </div>
                </div>

                <div class="chart-grid">
                    <div class="chart-card">
                        <div class="chart-title">近半年活跃趋势</div>
                        <div v-if="loading" class="chart-skeleton">
                            <el-skeleton :rows="4" animated />
                        </div>
                        <div v-else ref="trendChartRef" class="chart"></div>
                    </div>
                    <div class="chart-card">
                        <div class="chart-title">互动构成</div>
                        <div v-if="loading" class="chart-skeleton">
                            <el-skeleton :rows="4" animated />
                        </div>
                        <div v-else ref="donutChartRef" class="chart"></div>
                    </div>
                </div>
            </div>
        </e-container>
    </div>
</template>

<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, reactive, ref } from "vue";
import * as echarts from "echarts";
import { session } from "@/utils/utils";
import { buildMonthLabels, countByMonth, fetchUserStats } from "@/utils/user-stats";

const loading = ref(true);
const trendChartRef = ref(null);
const donutChartRef = ref(null);
let trendChart;
let donutChart;

const counts = reactive({
    biji: 0,
    pinglun: 0,
    shoucang: 0,
    dianzan: 0,
    follow: 0,
    fans: 0,
    history: 0,
});

const trendSeries = reactive({
    labels: [],
    biji: [],
    pinglun: [],
    shoucang: [],
    dianzan: [],
});

const statCards = computed(() => [
    { label: "笔记", value: counts.biji, icon: "fa fa-book" },
    { label: "评论", value: counts.pinglun, icon: "fa fa-comment" },
    { label: "收藏", value: counts.shoucang, icon: "fa fa-star" },
    { label: "点赞", value: counts.dianzan, icon: "fa fa-thumbs-up" },
    { label: "关注", value: counts.follow, icon: "fa fa-user-plus" },
    { label: "粉丝", value: counts.fans, icon: "fa fa-users" },
]);

const buildTrendOption = () => ({
    tooltip: { trigger: "axis" },
    legend: { data: ["笔记", "评论", "收藏", "点赞"] },
    grid: { left: "3%", right: "4%", bottom: "3%", containLabel: true },
    xAxis: { type: "category", data: trendSeries.labels },
    yAxis: { type: "value" },
    series: [
        { name: "笔记", type: "line", smooth: true, data: trendSeries.biji },
        { name: "评论", type: "line", smooth: true, data: trendSeries.pinglun },
        { name: "收藏", type: "line", smooth: true, data: trendSeries.shoucang },
        { name: "点赞", type: "line", smooth: true, data: trendSeries.dianzan },
    ],
});

const buildDonutOption = () => ({
    tooltip: { trigger: "item" },
    legend: { orient: "vertical", left: "left" },
    series: [
        {
            name: "互动构成",
            type: "pie",
            radius: ["45%", "70%"],
            avoidLabelOverlap: false,
            label: { show: false, position: "center" },
            emphasis: { label: { show: true, fontSize: 16, fontWeight: "bold" } },
            labelLine: { show: false },
            data: [
                { value: counts.pinglun, name: "评论" },
                { value: counts.shoucang, name: "收藏" },
                { value: counts.dianzan, name: "点赞" },
            ],
        },
    ],
});

const renderCharts = () => {
    if (trendChartRef.value) {
        if (!trendChart) {
            trendChart = echarts.init(trendChartRef.value);
        }
        trendChart.setOption(buildTrendOption(), true);
    }

    if (donutChartRef.value) {
        if (!donutChart) {
            donutChart = echarts.init(donutChartRef.value);
        }
        donutChart.setOption(buildDonutOption(), true);
    }
};

const loadDashboard = async () => {
    loading.value = true;
    const username = session("username");
    const { counts: nextCounts, lists } = await fetchUserStats(username);
    Object.assign(counts, nextCounts);

    const labels = buildMonthLabels(6);
    trendSeries.labels = labels;
    trendSeries.biji = countByMonth(lists.biji, labels, "addtime");
    trendSeries.pinglun = countByMonth(lists.pinglun, labels, "addtime");
    trendSeries.shoucang = countByMonth(lists.shoucang, labels, "addtime");
    trendSeries.dianzan = countByMonth(lists.dianzan, labels, "addtime");

    loading.value = false;
    await nextTick();
    renderCharts();
};

const reload = () => {
    loadDashboard();
};

const handleResize = () => {
    trendChart?.resize();
    donutChart?.resize();
};

onMounted(() => {
    loadDashboard();
    window.addEventListener("resize", handleResize);
});

onBeforeUnmount(() => {
    window.removeEventListener("resize", handleResize);
    trendChart?.dispose();
    donutChart?.dispose();
});
</script>

<style scoped lang="scss">
.views-my-dashboard {
    padding: 24px 0 40px;
}

.dashboard-card {
    background: var(--theme-surface-color);
    border-radius: 12px;
    padding: 24px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .title {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
    }

    .subtitle {
        color: #909399;
        font-size: 13px;
        margin-top: 4px;
    }
}

.stat-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
    gap: 16px;
    margin-bottom: 24px;
}

.stat-item {
    background: #ffffff;
    border-radius: 12px;
    padding: 16px;
    border: 1px solid var(--theme-border-color);
    display: flex;
    flex-direction: column;
    gap: 6px;
    align-items: flex-start;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);

    .stat-icon {
        width: 32px;
        height: 32px;
        border-radius: 10px;
        background: var(--theme-primary-soft-color);
        color: var(--theme-primary-color);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 16px;
    }

    .stat-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
    }

    .stat-label {
        color: #909399;
        font-size: 12px;
    }
}

.chart-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
    gap: 18px;
}

.chart-card {
    background: #ffffff;
    border-radius: 12px;
    padding: 16px;
    border: 1px solid var(--theme-border-color);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chart-title {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 12px;
}

.chart {
    width: 100%;
    height: 280px;
}

.chart-skeleton {
    padding: 10px 0;
}
</style>
