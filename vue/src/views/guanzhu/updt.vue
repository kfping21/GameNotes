<template>
    <div class="views-guanzhu-updt">
        <div>
            <el-card class="box-card">
                <template #header>
                    <div class="clearfix">
                        <span class="title"> 添加关注 </span>
                    </div>
                </template>

                <el-form :model="form" ref="formModel" :label-width="labelWidth" status-icon validate-on-rule-change>
                    <el-form-item v-if="isRead" label="笔记编号 " prop="bijibianhao"> {{ form.bijibianhao }} </el-form-item>

                    <el-form-item v-if="isRead" label="笔记名称 " prop="bijimingcheng"> {{ form.bijimingcheng }} </el-form-item>

                    <el-form-item v-if="isRead" label="标签 " prop="biaoqian"> <e-select-view module="biaoqian" :value="form.biaoqian" select="id" show="biaoqianmingcheng"></e-select-view> </el-form-item>

                    <el-form-item v-if="isRead" label="关联游戏 " prop="guanlianyouxi"> <e-select-view module="youxi" :value="form.guanlianyouxi" select="id" show="youximingcheng"></e-select-view> </el-form-item>

                    <el-form-item v-if="isRead" label="添加人 " prop="tianjiaren"> {{ form.tianjiaren }} </el-form-item>

                    <el-form-item label="关注人 " prop="guanzhuren"> <el-input v-model="form.guanzhuren" readonly style="width: 250px"></el-input> </el-form-item>

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

    import { ref, reactive, computed, watch } from "vue";
    import { useRoute } from "vue-router";
    import { session } from "@/utils/utils";
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useGuanzhuFindById, canGuanzhuFindById, canGuanzhuUpdate, canBijiFindById } from "@/module";
    import { extend } from "@/utils/extend";

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
    const form = useGuanzhuFindById(props.id);
    const emit = defineEmits(["success"]);
    const formModel = ref();
    const loading = ref(false);

    const submit = () => {
        formModel.value.validate().then((res) => {
            if (loading.value) return;
            loading.value = true;
            canGuanzhuUpdate(form).then(
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

    const readMap = reactive({});
    watch(
        () => form.bijiid,
        (id) => {
            canBijiFindById(id).then((res) => {
                extend(readMap, res);
            });
        }
    );
</script>

<style scoped lang="scss">
    .views-guanzhu-updt {
    }
</style>
