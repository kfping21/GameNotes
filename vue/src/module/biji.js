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
 * @return {EBiji}
 */

export const BijiCreateForm = () => {
    var route = unref(router.currentRoute);
    const userStore = useUserStore();
    const $session = userStore.session;
    if (!route.query) {
        route = useRoute();
    }
    const form = {
        bijibianhao: rule.getID(),
        bijimingcheng: "",
        bijifengmian: "",
        biaoqian: "",
        guanlianyouxi: "",
        shipin: "",
        guanzhuliang: "",
        zhongcaodu: "",
        xiangqing: "",
        tianjiaren: $session.username,
        issh: "否",
    };

    return form;
};

/**
 * 异步模式获取数据
 * @param id
 * @param readMap
 * @return {Promise<EBijiForm>}
 */
export const canBijiCreateForm = () => {
    return new Promise(async (resolve, reject) => {
        var form = BijiCreateForm();
        resolve({ form });
    });
};

/**
 * 响应式获取笔记 模块的表单字段数据
 * @return {EBijiForm}
 */
export const useBijiCreateForm = () => {
    const form = BijiCreateForm();
    const formReactive = reactive(form);

    return { form: formReactive };
};

export const canBijiSelect = (filter, result) => {
    http.post("/api/biji/selectPages").then((res) => {
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
export const useBijiSelect = (filter) => {
    const result = reactive({
        lists: [],
        total: {},
    });
    canBijiSelect(filter, result);
    return result;
};

/**
 * 根据
 * @param id
 * @return {Promise<EBiji>}
 */
export const canBijiFindById = (id) => {
    return new Promise((resolve, reject) => {
        // 读取后台数据
        http.get("/api/biji/findById", { id }).then((res) => {
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
 * @return {EBiji}
 */
export const useBijiFindById = (id) => {
    var form = reactive({});

    canBijiFindById(id).then((res) => {
        extend(form, res);
    });
    return form;
};

/**
 * 根据数据,插入到数据库中
 * @param {EBiji} data
 * @return {Promise<EResponseData<EBiji>>}
 */
export const canBijiInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/biji/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("biji_insert", res.data);
                        event.emit("biji_change", res.data);
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
 * @param {EBiji} data
 * @return {Promise<EResponseData<EBiji>>}
 */
export const canBijiUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/biji/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("biji_update", res.data);
                        event.emit("biji_change", res.data);
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
 * @param {EBiji} data
 * @return {Promise<EResponseData<EBiji>>}
 */
export const canBijiCheckIssh = (row) => {
    return new Promise((resolve, reject) => {
        var id = row.id;
        var value = row.issh === "是" ? "否" : "是";
        http.get("/api/biji/checkIssh", { id, value })
            .json()
            .then(
                (res) => {
                    if (res.code == 0) {
                        row.issh = value;
                    }
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("biji_update", row);
                        event.emit("biji_change", row);
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
export const canBijiDelete = (id) => {
    var res = [];
    if (!isArray(id)) {
        res.push(id);
    } else {
        res = id;
    }

    return new Promise((resolve, reject) => {
        http.post("/api/biji/delete", res)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("biji_delete", res.data);
                        event.emit("biji_change", res.data);
                    }
                },
                (err) => {
                    reject(err);
                }
            );
    });
};

export const useBijishoucang = (id, iss, count) => {
    const is_shoucang = iss ? iss : ref(false);
    const shoucangCount = count ? count : ref(0);

    http.get("/api/biji/getshoucang", { id }).then((res) => {
        if (res.code == 0) {
            is_shoucang.value = res.data.is_shoucang;
            shoucangCount.value = res.data.shoucangCount;
        }
    });

    return { is_shoucang, shoucangCount };
};

export const useBijidianzan = (id, iss, count) => {
    const is_dianzan = iss ? iss : ref(false);
    const dianzanCount = count ? count : ref(0);

    http.get("/api/biji/getdianzan", { id }).then((res) => {
        if (res.code == 0) {
            is_dianzan.value = res.data.is_dianzan;
            dianzanCount.value = res.data.dianzanCount;
        }
    });

    return { is_dianzan, dianzanCount };
};
