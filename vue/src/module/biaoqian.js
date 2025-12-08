import http from "@/utils/ajax/http";
import { useRoute } from "vue-router";
import { useUserStore } from "@/stores";
import { reactive, ref, unref } from "vue";
import rule from "@/utils/rule";
import { extend, isArray } from "@/utils/extend";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import event from "@/utils/event";

/**
 * 响应式的对象数据
 * @return {EBiaoqian}
 */

export const BiaoqianCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        biaoqianmingcheng: "",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EBiaoqianForm>}
 */
export const canBiaoqianCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = BiaoqianCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取标签 模块的表单字段数据
 * @return {EBiaoqianForm}
 */
export const useBiaoqianCreateForm = () => {
    const form = BiaoqianCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canBiaoqianSelect = (filter, result) => {
    http.post("/api/biaoqian/selectPages").then((res) => {
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
export const useBiaoqianSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canBiaoqianSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EBiaoqian>}
 */
export const canBiaoqianFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/biaoqian/findById", { id }).then((res) => {
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
 * @return {EBiaoqian}
 */
export const useBiaoqianFindById = (id) => {
    var form = reactive({});

    canBiaoqianFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EBiaoqian} data
 * @return {Promise<EResponseData<EBiaoqian>>}
 */
export const canBiaoqianInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/biaoqian/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("biaoqian_insert", res.data);
                        event.emit("biaoqian_change", res.data);
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
 * @param {EBiaoqian} data
 * @return {Promise<EResponseData<EBiaoqian>>}
 */
export const canBiaoqianUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/biaoqian/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("biaoqian_update", res.data);
                        event.emit("biaoqian_change", res.data);
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
export const canBiaoqianDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/biaoqian/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("biaoqian_delete", res.data);
                        event.emit("biaoqian_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
