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
 * @return {EYouxi}
 */

export const YouxiCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        youximingcheng: "",
        guanlianbiji: "",
        youxitupian: "",
        zhuangbeiku: "",
        xiangqing: "",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EYouxiForm>}
 */
export const canYouxiCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = YouxiCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取游戏 模块的表单字段数据
 * @return {EYouxiForm}
 */
export const useYouxiCreateForm = () => {
    const form = YouxiCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canYouxiSelect = (filter, result) => {
    http.post("/api/youxi/selectPages").then((res) => {
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
export const useYouxiSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canYouxiSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EYouxi>}
 */
export const canYouxiFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/youxi/findById", { id }).then((res) => {
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
 * @return {EYouxi}
 */
export const useYouxiFindById = (id) => {
    var form = reactive({});

    canYouxiFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EYouxi} data
 * @return {Promise<EResponseData<EYouxi>>}
 */
export const canYouxiInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/youxi/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("youxi_insert", res.data);
                        event.emit("youxi_change", res.data);
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
 * @param {EYouxi} data
 * @return {Promise<EResponseData<EYouxi>>}
 */
export const canYouxiUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/youxi/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("youxi_update", res.data);
                        event.emit("youxi_change", res.data);
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
export const canYouxiDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/youxi/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("youxi_delete", res.data);
                        event.emit("youxi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
