<template>
    <div class="views-biji-updt">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加笔记 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item label="笔记编号 " prop="bijibianhao" :rules="[{ required: true, message: '请填写笔记编号' }]"> <el-input type="text" placeholder="输入笔记编号" style="width: 450px" v-model="form.bijibianhao" /> </el-form-item>

                    <el-form-item
                        label="笔记名称 "
                        prop="bijimingcheng"
                        required
                        :rules="[
                            { required: true, message: '请填写笔记名称' },
                            { validator: rule.checkRemote, message: '内容重复了', checktype: 'update', module: 'biji', col: 'bijimingcheng', id: form.id, trigger: 'blur' },
                        ]"
                    >
                        <el-input type="text" placeholder="输入笔记名称" style="width: 450px" v-model="form.bijimingcheng" />
                    </el-form-item>

                    <el-form-item label="笔记封面 " prop="bijifengmian" required :rules="[{ required: true, message: '请填写笔记封面' }]"> <e-upload-images v-model="form.bijifengmian" is-paste></e-upload-images> </el-form-item>

                    <el-form-item label="标签 " prop="biaoqian" required :rules="[{ required: true, message: '请填写标签' }]">
                        <el-select v-model="biaoqian" multiple="multiple"><e-select-option type="option" module="biaoqian" value="id" label="biaoqianmingcheng"></e-select-option></el-select>
                    </el-form-item>

                    <el-form-item label="关联游戏 " prop="guanlianyouxi" required :rules="[{ required: true, message: '请填写关联游戏' }]">
                        <el-select v-model="form.guanlianyouxi"><e-select-option type="option" module="youxi" value="id" label="youximingcheng"></e-select-option></el-select>
                    </el-form-item>

                    <el-form-item label="话题" prop="topicIds">
                        <el-select v-model="form.topicIds" multiple placeholder="选择话题" style="width: 450px">
                            <el-option
                                v-for="t in topics"
                                :key="t.id"
                                :label="t.title"
                                :value="t.id"
                            />
                        </el-select>
                    </el-form-item>

                    <el-form-item label="视频 " prop="shipin"> <e-upload-file v-model="form.shipin"></e-upload-file> </el-form-item>

                    <el-form-item label="详情 " prop="xiangqing"> <e-editor v-model="form.xiangqing" @getContent="getxiangqingContent"></e-editor> </el-form-item>

                    <el-form-item v-if="btnText">
                        <el-button type="primary" @click="submit">{{ btnText }}</el-button>
                    </el-form-item>
                </el-form></el-card
            >
        </div>
    </div>
</template>

<script setup>
    import http from "@/utils/ajax/http";
    import DB from "@/utils/db";
    import rule from "@/utils/rule";
    import router from "@/router";
    import EEditor from "@/components/EEditor.vue";

    import { ref, reactive, computed, watch } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useBijiFindById, canBijiFindById, canBijiUpdate } from "@/module";
    import { getTopicCategories } from "@/module/topic";

    const route = useRoute();
    const props = defineProps({
        id: [String, Number],
        btnText: {
            type: String,
            default: "保存",
        },
        isRead: {
            type: Boolean,
            default: true,
        },
        isHouxu: {
            type: Boolean,
            default: true,
        },
        labelWidth: {
            type: String,
            default: "140px",
        },
    });
    const form = useBijiFindById(props.id);
    
    const topics = ref([]);
    const loadTopics = async () => {
        const res = await getTopicCategories({ page: 1, pagesize: 100 });
        if (res.code === 0) {
            topics.value = res.data.lists?.records || res.data.lists || [];
        }
    };
    loadTopics();

    // Watch for form.topics to populate form.topicIds
    watch(() => form.topics, (newTopics) => {
        if (newTopics && Array.isArray(newTopics)) {
            form.topicIds = newTopics.map(t => t.id);
        } else if (!form.topicIds) {
            form.topicIds = [];
        }
    }, { immediate: true });

    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);

    const submit = () => {
        formModel.value.validate().then((res) => {
            if (loading.value) return;
            loading.value = true;
            canBijiUpdate(form).then(
                (res) => {
                    loading.value = false;
                    if (res.code == 0) {
                        emit("success", res.data);
                        if (props.isHouxu) {
                            ElMessage.success("更新成功");
                            router.go(-1);
                        }
                    } else {
                        ElMessageBox.alert(res.msg);
                    }
                },
                (err) => {
                    loading.value = false;
                    ElMessageBox.alert(err.message);
                }
            );
        });
    };

    const biaoqian = computed({
        get() {
            if (form.biaoqian) {
                return form.biaoqian.split(",").filter((s) => s).map(s => isNaN(s) ? s : Number(s));
            }
            return [];
        },
        set(val) {
            form.biaoqian = val ? val.join(",") : "";
        },
    });
    const getxiangqingContent = (v) => {
        form.xiangqing = v;
    };
</script>

<style scoped lang="scss">
    .views-biji-updt {
    }
</style>
