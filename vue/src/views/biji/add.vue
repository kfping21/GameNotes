<template>
    <div class="views-biji-add">
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
                            { validator: rule.checkRemote, message: '内容重复了', checktype: 'insert', module: 'biji', col: 'bijimingcheng', trigger: 'blur' },
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

                    <el-form-item label="视频 " prop="shipin"> <e-upload-file v-model="form.shipin"></e-upload-file> </el-form-item>

                    <el-form-item label="关注量 " prop="guanzhuliang" :rules="[{ validator: rule.checkNumber, message: '输入一个有效数字' }]"> <el-input type="number" placeholder="输入关注量" style="width: 450px" v-model.number="form.guanzhuliang" /> </el-form-item>

                    <el-form-item label="种草度 " prop="zhongcaodu" :rules="[{ validator: rule.checkNumber, message: '输入一个有效数字' }]"> <el-input type="number" placeholder="输入种草度" style="width: 450px" v-model.number="form.zhongcaodu" /> </el-form-item>

                    <el-form-item label="详情 " prop="xiangqing"> <e-editor v-model="form.xiangqing" @getContent="getxiangqingContent"></e-editor> </el-form-item>

                    <el-form-item label="添加人 " prop="tianjiaren"> <el-input v-model="form.tianjiaren" readonly style="width: 250px"></el-input> </el-form-item>

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
    import { useBijiCreateForm, canBijiInsert } from "@/module";
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
    const { form } = useBijiCreateForm();
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
                    canBijiInsert(form).then(
                        (res) => {
                            loading.value = false;
                            if (res.code == 0) {
                                emit("success", res.data);
                                if (props.isHouxu) {
                                    ElMessage.success("添加成功");
                                    formModel.value.resetFields();
                                    form.bijibianhao = rule.getID();
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

    const biaoqian = computed({
        get() {
            if (form.biaoqian) {
                return form.biaoqian.split(",").filter((s) => s);
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
    .views-biji-add {
    }
</style>
