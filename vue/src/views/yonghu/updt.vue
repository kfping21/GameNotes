<template>
    <div class="views-yonghu-updt">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加用户 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item
                        label="账号 "
                        prop="zhanghao"
                        required
                        :rules="[
                            { required: true, message: '请填写账号' },
                            { validator: rule.checkRemote, message: '内容重复了', checktype: 'update', module: 'yonghu', col: 'zhanghao', id: form.id, trigger: 'blur' },
                        ]"
                    >
                        <el-input type="text" placeholder="输入账号" style="width: 450px" v-model="form.zhanghao" />
                    </el-form-item>

                    <el-form-item label="名称 " prop="mingcheng" required :rules="[{ required: true, message: '请填写名称' }]"> <el-input type="text" placeholder="输入名称" style="width: 450px" v-model="form.mingcheng" /> </el-form-item>

                    <el-form-item label="性别 " prop="xingbie">
                        <el-select v-model="form.xingbie"
                            ><el-option label="男" value="男"></el-option>
                            <el-option label="女" value="女"></el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="出生日期 " prop="chushengriqi"> <el-date-picker v-model="form.chushengriqi" type="date" value-format="YYYY-MM-DD" placeholder="选择日期"> </el-date-picker> </el-form-item>

                    <el-form-item label="手机 " prop="shouji" :rules="[{ validator: rule.checkPhone, message: '请输入正确手机号码' }]"> <el-input type="text" placeholder="输入手机" style="width: 450px" v-model="form.shouji" /> </el-form-item>

                    <el-form-item label="邮箱 " prop="youxiang" :rules="[{ type: 'email', message: '请输入正确邮箱地址' }]"> <el-input type="text" placeholder="输入邮箱" style="width: 450px" v-model="form.youxiang" /> </el-form-item>

                    <el-form-item label="个性签名 " prop="gexingqianming"> <e-editor v-model="form.gexingqianming" @getContent="getgexingqianmingContent"></e-editor> </el-form-item>

                    <el-form-item label="标签 " prop="biaoqian">
                        <el-select v-model="biaoqian" multiple="multiple"><e-select-option type="option" module="biaoqian" value="id" label="biaoqianmingcheng"></e-select-option></el-select>
                    </el-form-item>

                    <el-form-item label="我在玩 " prop="wozaiwan">
                        <el-select v-model="wozaiwan" multiple="multiple"><e-select-option type="option" module="youxi" value="id" label="youximingcheng"></e-select-option></el-select>
                    </el-form-item>

                    <el-form-item label="头像 " prop="touxiang"> <e-upload-image v-model="form.touxiang" is-paste></e-upload-image> </el-form-item>

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
    import { useYonghuFindById, canYonghuFindById, canYonghuUpdate } from "@/module";

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
    const form = useYonghuFindById(props.id);
    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);

    const submit = () => {
        formModel.value.validate().then((res) => {
            if (loading.value) return;
            loading.value = true;
            canYonghuUpdate(form).then(
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

    const getgexingqianmingContent = (v) => {
        form.gexingqianming = v;
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
    const wozaiwan = computed({
        get() {
            if (form.wozaiwan) {
                return form.wozaiwan.split(",").filter((s) => s);
            }
            return [];
        },
        set(val) {
            form.wozaiwan = val ? val.join(",") : "";
        },
    });
</script>

<style scoped lang="scss">
    .views-yonghu-updt {
    }
</style>
