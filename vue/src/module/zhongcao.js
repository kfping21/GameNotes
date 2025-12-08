import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

import { canBijiFindById } from "./biji";

/**
 * 响应式的对象数据
 * @return {EZhongcao}
 */

export const ZhongcaoCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        bijibianhao: "",
        bijimingcheng: "",
        biaoqian: "",
        guanlianyouxi: "",
        tianjiaren: $session.username,
        zhongcaoren: $session.username,
    };

    return form;
};

function exportForm(form, readMap) {
    var autoText = ["bijiid", "bijibianhao", "bijimingcheng", "biaoqian", "guanlianyouxi", "tianjiaren"];
    for (var txt of autoText) {
        form[txt] = readMap[txt];
    }
}

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EZhongcaoForm>}
 */
export const canZhongcaoCreateForm = (id, readMap) => {
    return new Promise(async (resolve, reject) => {
        var form = ZhongcaoCreateForm();
        if (!readMap || !readMap.id) {
            readMap = await canBijiFindById(id).catch(reject);
        }
        exportForm(form, readMap);
        form.bijiid = readMap.id;
        resolve({ form, readMap });
    });
};

/**
 * 响应式获取种草 模块的表单字段数据
 * @return {EZhongcaoForm}
 */
export const useZhongcaoCreateForm = (id) => {
    const form = ZhongcaoCreateForm();
    const formReactive = reactive(form);

    const readMap = reactive({});
    canBijiFindById(id).then(
        (map) => {
            exportForm(formReactive, map);
            extend(readMap, map);
            formReactive.bijiid = map.id;
        },
        (err) => {
            ElMessageBox.alert(err.message);
        }
    );
    return { form: formReactive, readMap };
};

export const canZhongcaoSelect = (filter, result) => {
    http.post("/api/zhongcao/selectPages").then((res) => {
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
export const useZhongcaoSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canZhongcaoSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EZhongcao>}
 */
export const canZhongcaoFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/zhongcao/findById", { id }).then((res) => {
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
 * @return {EZhongcao}
 */
export const useZhongcaoFindById = (id) => {
    var form = reactive({});

    canZhongcaoFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EZhongcao} data
 * @return {Promise<EResponseData<EZhongcao>>}
 */
export const canZhongcaoInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/zhongcao/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("zhongcao_insert", res.data);
                        event.emit("zhongcao_change", res.data);
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
 * @param {EZhongcao} data
 * @return {Promise<EResponseData<EZhongcao>>}
 */
export const canZhongcaoUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/zhongcao/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("zhongcao_update", res.data);
                        event.emit("zhongcao_change", res.data);
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
export const canZhongcaoDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/zhongcao/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("zhongcao_delete", res.data);
                        event.emit("zhongcao_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
