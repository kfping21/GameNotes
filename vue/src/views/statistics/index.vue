<template>
    <div class="statistics-container">
        <el-row :gutter="20">
            <el-col :span="6">
                <el-card shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span>用户总数</span>
                        </div>
                    </template>
                    <div class="card-value">{{ userCount }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span>笔记总数</span>
                        </div>
                    </template>
                    <div class="card-value">{{ noteCount }}</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span>今日活跃</span>
                        </div>
                    </template>
                    <div class="card-value">123</div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card shadow="hover">
                    <template #header>
                        <div class="card-header">
                            <span>总交易额</span>
                        </div>
                    </template>
                    <div class="card-value">¥ 88,888</div>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
                <el-card shadow="hover">
                    <div id="userChart" style="height: 400px;"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover">
                    <div id="contentChart" style="height: 400px;"></div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts';
import http from "@/utils/ajax/http";

const userCount = ref(0);
const noteCount = ref(0);

onMounted(() => {
    initData();
    initUserChart();
    initContentChart();
});

const initData = () => {
    // 获取用户总数
    http.post('/api/yonghu/selectPages', { page: 1, pagesize: 1 }).then(res => {
        if (res.code === 1) {
            userCount.value = res.data.total;
        }
    });
    // 获取笔记总数
    http.post('/api/biji/selectPages', { page: 1, pagesize: 1 }).then(res => {
        if (res.code === 1) {
            noteCount.value = res.data.total;
        }
    });
};

const initUserChart = () => {
    const chartDom = document.getElementById('userChart');
    const myChart = echarts.init(chartDom);
    const option = {
        title: {
            text: '用户增长趋势'
        },
        tooltip: {
            trigger: 'axis'
        },
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: [150, 230, 224, 218, 135, 147, 260],
                type: 'line'
            }
        ]
    };
    myChart.setOption(option);
};

const initContentChart = async () => {
    const chartDom = document.getElementById('contentChart');
    const myChart = echarts.init(chartDom);
    
    // 定义要统计的标签
    const tags = ['攻略', '心得', '资讯', '测评', '其他'];
    const data = [];
    
    // 并发请求获取各标签数量
    for (const tag of tags) {
        const res = await http.post('/api/biji/selectPages', { biaoqian: tag, page: 1, pagesize: 1 });
        if (res.code === 1) {
            data.push({ value: res.data.total, name: tag });
        } else {
            data.push({ value: 0, name: tag });
        }
    }

    const option = {
        title: {
            text: '内容分类统计'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            top: '5%',
            left: 'center'
        },
        series: [
            {
                name: 'Access From',
                type: 'pie',
                radius: ['40%', '70%'],
                avoidLabelOverlap: false,
                itemStyle: {
                    borderRadius: 10,
                    borderColor: '#fff',
                    borderWidth: 2
                },
                label: {
                    show: false,
                    position: 'center'
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: 40,
                        fontWeight: 'bold'
                    }
                },
                labelLine: {
                    show: false
                },
                data: data
            }
        ]
    };
    myChart.setOption(option);
};
</script>

<style scoped>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.card-value {
    font-size: 24px;
    font-weight: bold;
    color: #409EFF;
}
</style>
