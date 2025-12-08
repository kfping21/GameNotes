<template>
    <div class="views-youxi-add">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加游戏 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item
                        label="游戏名称 "
                        prop="youximingcheng"
                        required
                        :rules="[
                            { required: true, message: '请填写游戏名称' },
                            { validator: rule.checkRemote, message: '内容重复了', checktype: 'insert', module: 'youxi', col: 'youximingcheng', trigger: 'blur' },
                        ]"
                    >
                        <el-input type="text" placeholder="输入游戏名称" style="width: 450px" v-model="form.youximingcheng" />
                    </el-form-item>

                    <el-form-item label="关联笔记 " prop="guanlianbiji">
                        <el-select v-model="form.guanlianbiji"><e-select-option type="option" module="biji" value="id" label="bijimingcheng"></e-select-option></el-select>
                    </el-form-item>

                    <el-form-item label="游戏图片 " prop="youxitupian" required :rules="[{ required: true, message: '请填写游戏图片' }]"> <e-upload-image v-model="form.youxitupian" is-paste></e-upload-image> </el-form-item>

                    <el-form-item label="装备库 " prop="zhuangbeiku"> <e-editor v-model="form.zhuangbeiku" @getContent="getzhuangbeikuContent"></e-editor> </el-form-item>

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

    import { ref, reactive, computed } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useYouxiCreateForm, canYouxiInsert } from "@/module";
    import { extend } from "vue-design-plugin";

    const route = useRoute();
    const props = defineProps({
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
    const { form } = useYouxiCreateForm();
    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);
    var submit = () => {
        return new Promise((resolve, reject) => {
            formModel.value
                .validate()
                .then((res) => {
                    if (loading.value) return;
                    loading.value = true;
                    canYouxiInsert(form).then(
                        (res) => {
                            loading.value = false;
                            if (res.code == 0) {
                                emit("success", res.data);
                                if (props.isHouxu) {
                                    ElMessage.success("添加成功");
                                    formModel.value.resetFields();
                                }
                            } else {
                                ElMessageBox.alert(res.msg);
                            }
                            resolve(res);
                        },
                        (err) => {
                            loading.value = false;
                            ElMessageBox.alert(err.message);
                            reject(err);
                        }
                    );
                })
                .catch((err) => {
                    reject(err);
                });
        });
    };

    const getzhuangbeikuContent = (v) => {
        form.zhuangbeiku = v;
    };

    const getxiangqingContent = (v) => {
        form.xiangqing = v;
    };
</script>

<style scoped lang="scss">
    .views-youxi-add {
    }
</style>
