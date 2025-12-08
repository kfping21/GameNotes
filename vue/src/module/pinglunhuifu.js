import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

import { canPinglunFindById } from "./pinglun";

/**
 * 响应式的对象数据
 * @return {EPinglunhuifu}
 */

export const PinglunhuifuCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        biaoti: "",
        pinglunneirong: "",
        pinglunren: $session.username,
        huifuneirong: "",
        huifuren: $session.username,
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["pinglunid", "biao", "biaoid", "biaoti", "pinglunneirong", "pinglunren"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EPinglunhuifuForm>}
 */
export const canPinglunhuifuCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = PinglunhuifuCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canPinglunFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.pinglunid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取评论回复 模块的表单字段数据
 * @return {EPinglunhuifuForm}
 */
export const usePinglunhuifuCreateForm = (id) => {
    const form = PinglunhuifuCreateForm();
    const formReactive = reactive(form);

    const readMap = reactive({});
    canPinglunFindById(id).then(
        (map) => {
            exportForm(formReactive, map);
            extend(readMap, map);
            formReactive.pinglunid = map.id;
        },
        (err) => {
            ElMessageBox.alert(err.message);
        }
    );
    return { form: formReactive, readMap };
};

export const canPinglunhuifuSelect = (filter, result) => {
    http.post("/api/pinglunhuifu/selectPages").then((res) => {
        if (res.code == 0) {
            extend(result, res.data);
        } else {
            ElMessageBox.alert(res.msg);
        }
    });
};

/**
 * 获取分页数据
 * @param filter
 */
export const usePinglunhuifuSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canPinglunhuifuSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EPinglunhuifu>}
 */
export const canPinglunhuifuFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/pinglunhuifu/findById", { id }).then((res) => {
            if (res.code == 0) {
                resolve(res.data);
            } else {
                reject(new Error(res.msg));
            }
        }, reject);
    });
};

/**
 * 根据id 获取一行数据
 * @param id
 * @return {EPinglunhuifu}
 */
export const usePinglunhuifuFindById = (id) => {
    var form = reactive({});

    canPinglunhuifuFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EPinglunhuifu} data
 * @return {Promise<EResponseData<EPinglunhuifu>>}
 */
export const canPinglunhuifuInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/pinglunhuifu/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("pinglunhuifu_insert", res.data);
                        event.emit("pinglunhuifu_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 根据数据更新数据库
 * @param {EPinglunhuifu} data
 * @return {Promise<EResponseData<EPinglunhuifu>>}
 */
export const canPinglunhuifuUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/pinglunhuifu/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("pinglunhuifu_update", res.data);
                        event.emit("pinglunhuifu_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 根据id 或者列表id
 * @param {number|number[]} id
 * @return {Promise<EResponseData<string>>}
 */
export const canPinglunhuifuDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/pinglunhuifu/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("pinglunhuifu_delete", res.data);
                        event.emit("pinglunhuifu_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
