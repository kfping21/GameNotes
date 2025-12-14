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
 * 关注用户
 * @param {object|string} data 包含 followee 的对象 或 followee 用户名字符串
 * @return {Promise}
 */
export const canGuanzhuInsert = (data) => {
    let followee = "";
    if (typeof data === "string") {
        followee = data;
    } else if (data && data.followee) {
        followee = data.followee;
    } else if (data && data.tianjiaren) {
        // 兼容旧代码，如果传了 tianjiaren，视为 followee
        followee = data.tianjiaren;
    }
    
    return new Promise((resolve, reject) => {
        http.post("/api/guanzhu/insert", { followee })
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("guanzhu_insert", res.data);
                        event.emit("guanzhu_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 取消关注
 * @param {string|number[]|object} data 目标用户名 或 ID列表 或 包含 followee 的对象
 * @return {Promise}
 */
export const canGuanzhuDelete = (data) => {
    let payload = {};
    if (Array.isArray(data)) {
        payload = { id: data };
    } else if (typeof data === "string") {
        payload = { followee: data };
    } else if (data && data.followee) {
        payload = { followee: data.followee };
    } else if (data && data.tianjiaren) {
        payload = { followee: data.tianjiaren };
    } else {
        // Fallback for ID list in object
        payload = data;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/guanzhu/delete", payload)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("guanzhu_delete", res.data);
                        event.emit("guanzhu_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

/**
 * 查询是否已关注
 * @param {string} followee 目标用户名
 * @returns {Promise<boolean>}
 */
export const checkIsFollow = (followee) => {
    return new Promise((resolve, reject) => {
        http.get("/api/guanzhu/isFollow", { followee }).then(res => {
            if (res.code === 0) {
                resolve(res.data.isFollow);
            } else {
                resolve(false);
            }
        }).catch(err => {
            resolve(false);
        });
    });
};

/**
 * 查询我关注的人
 * @param {object} params { page, size, username }
 */
export const selectGuanzhuren = (params) => {
    return http.post("/api/guanzhu/selectGuanzhuren", params);
};

/**
 * 查询粉丝
 * @param {object} params { page, size, username }
 */
export const selectTianjiaren = (params) => {
    return http.post("/api/guanzhu/selectTianjiaren", params);
};

// 保持一些旧的导出以防报错，但功能可能不再适用
export const GuanzhuCreateForm = () => {
    return {};
};

export const useGuanzhuCreateForm = (id) => {
    return { form: reactive({}), readMap: reactive({}) };
};

export const canGuanzhuSelect = (filter, result) => {
    // 适配新的查询接口? 或者暂时留空
    // 如果是查询关注列表，应该用 selectGuanzhuren
    selectGuanzhuren({ page: 1, size: 10 }).then(res => {
        if (res.code == 0) {
            extend(result, res.data);
        }
    });
};

export const useGuanzhuSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canGuanzhuSelect(filter, result);
    return result;
};

export const canGuanzhuFindById = (id) => {
    return http.get("/api/guanzhu/findById", { id });
};

export const useGuanzhuFindById = (id) => {
    var form = reactive({});
    canGuanzhuFindById(id).then((res) => {
        extend(form, res.data);
    });
    return form;
};

export const canGuanzhuUpdate = (data) => {
    // 关注通常没有更新操作，只有增删
    return Promise.resolve({ code: 0 });
};
