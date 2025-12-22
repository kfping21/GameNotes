<template>
    <el-dialog
        :model-value="modelValue"
        title="举报反馈"
        width="520px"
        @close="handleClose"
    >
        <div class="report-target" v-if="target">
            <div class="target-title">{{ targetTitle }}</div>
            <div class="target-meta">
                <span>类型：{{ target.typeLabel }}</span>
                <span v-if="target.id">ID：{{ target.id }}</span>
            </div>
        </div>

        <el-form :model="form" label-width="90px">
            <el-form-item label="举报原因">
                <el-select v-model="form.reason" placeholder="请选择原因" style="width: 100%">
                    <el-option v-for="item in reasonOptions" :key="item" :label="item" :value="item" />
                </el-select>
            </el-form-item>
            <el-form-item label="补充说明">
                <el-input
                    v-model="form.detail"
                    type="textarea"
                    :rows="3"
                    placeholder="可填写补充说明（选填）"
                />
            </el-form-item>
        </el-form>

        <template #footer>
            <el-button @click="handleClose">取消</el-button>
            <el-button type="primary" :loading="submitting" @click="submitReport">提交举报</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { computed, reactive, watch, ref } from "vue";
import { ElMessage } from "element-plus";
import { canTongzhiInsert } from "@/module";
import { session } from "@/utils/utils";

const props = defineProps({
    modelValue: Boolean,
    target: {
        type: Object,
        default: () => ({}),
    },
});

const emit = defineEmits(["update:modelValue", "submitted"]);

const reasonOptions = [
    "垃圾广告",
    "恶意攻击",
    "虚假信息",
    "不当内容",
    "其他原因",
];

const form = reactive({
    reason: "",
    detail: "",
});

const submitting = ref(false);

const targetTitle = computed(() => {
    return props.target?.title || props.target?.content || "未知内容";
});

const handleClose = () => {
    emit("update:modelValue", false);
};

const resetForm = () => {
    form.reason = "";
    form.detail = "";
};

const buildReportContent = (reporter) => {
    const lines = [
        `举报类型：${props.target?.typeLabel || "未知"}`,
        props.target?.id ? `目标ID：${props.target.id}` : "",
        props.target?.title ? `标题：${props.target.title}` : "",
        props.target?.content ? `内容：${props.target.content}` : "",
        props.target?.url ? `链接：${props.target.url}` : "",
        `原因：${form.reason}`,
        `补充：${form.detail || "无"}`,
        `举报人：${reporter}`,
        `时间：${new Date().toLocaleString()}`,
    ];
    return lines.filter(Boolean).join("\n");
};

const submitReport = async () => {
    if (!form.reason) {
        ElMessage.warning("请选择举报原因");
        return;
    }
    const reporter = session("username");
    if (!reporter) {
        ElMessage.warning("请先登录");
        return;
    }

    submitting.value = true;
    try {
        const payload = {
            tongzhineirong: buildReportContent(reporter),
            yonghu: reporter,
            issh: "否",
        };
        const res = await canTongzhiInsert(payload);
        if (res.code === 0) {
            ElMessage.success("举报已提交");
            emit("submitted", res.data);
            handleClose();
            resetForm();
        } else {
            ElMessage.error(res.msg || "提交失败");
        }
    } catch (error) {
        ElMessage.error(error.message || "提交失败");
    } finally {
        submitting.value = false;
    }
};

watch(
    () => props.modelValue,
    (val) => {
        if (val) {
            resetForm();
        }
    }
);
</script>

<style scoped lang="scss">
.report-target {
    padding: 12px;
    background: var(--theme-surface-muted);
    border-radius: 8px;
    margin-bottom: 16px;
    border: 1px solid var(--theme-border-color);

    .target-title {
        font-weight: 600;
        color: #303133;
        margin-bottom: 6px;
    }

    .target-meta {
        display: flex;
        gap: 16px;
        font-size: 12px;
        color: #909399;
    }
}
</style>
