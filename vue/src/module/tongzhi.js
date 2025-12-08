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
 * @return {ETongzhi}
 */

export const TongzhiCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        tongzhineirong: "",
        yonghu: $session.username,
        issh: "否",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<ETongzhiForm>}
 */
export const canTongzhiCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = TongzhiCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取通知 模块的表单字段数据
 * @return {ETongzhiForm}
 */
export const useTongzhiCreateForm = () => {
    const form = TongzhiCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canTongzhiSelect = (filter, result) => {
    http.post("/api/tongzhi/selectPages").then((res) => {
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
export const useTongzhiSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canTongzhiSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<ETongzhi>}
 */
export const canTongzhiFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/tongzhi/findById", { id }).then((res) => {
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
 * @return {ETongzhi}
 */
export const useTongzhiFindById = (id) => {
    var form = reactive({});

    canTongzhiFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {ETongzhi} data
 * @return {Promise<EResponseData<ETongzhi>>}
 */
export const canTongzhiInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/tongzhi/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("tongzhi_insert", res.data);
                        event.emit("tongzhi_change", res.data);
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
 * @param {ETongzhi} data
 * @return {Promise<EResponseData<ETongzhi>>}
 */
export const canTongzhiUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/tongzhi/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("tongzhi_update", res.data);
                        event.emit("tongzhi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 审核是否上架
 * @param {ETongzhi} data
 * @return {Promise<EResponseData<ETongzhi>>}
 */
export const canTongzhiCheckIssh = (row) => {
    return new Promise((resolve, reject) => {
        var id = row.id;
        var value = row.issh === "是" ? "否" : "是";
        http.get("/api/tongzhi/checkIssh", { id, value })
            .json()
            .then(
                (res) => {
                    if (res.code == 0) {
                        row.issh = value;
                    }
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("tongzhi_update", row);
                        event.emit("tongzhi_change", row);
                    }
                },
                (err) => {
                    ElMessageBox.alert(err.message);
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
export const canTongzhiDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/tongzhi/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("tongzhi_delete", res.data);
                        event.emit("tongzhi_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};
