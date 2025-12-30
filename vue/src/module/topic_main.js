import http from "@/utils/ajax/http";
import { reactive } from "vue";
import { extend, isArray } from "@/utils/extend";
import event from "@/utils/event";

export const canTopicMainSelect = (filter, result) => {
    http.post("/api/topic_main/selectPages", filter).then((res) => {
        if (res.code == 0) {
            extend(result, res.data);
        }
    });
};

export const useTopicMainSelect = (filter) => {
    const result = reactive({ lists: [], total: 0, page: 1, pagesize: 10 });
    canTopicMainSelect(filter, result);
    return result;
};

export const canTopicMainInsert = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/topic_main/insert", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("topic_main_insert", res.data);
                        event.emit("topic_main_change", res.data);
                    }
                },
                (err) => reject(err)
            );
    });
};

export const canTopicMainUpdate = (data) => {
    return new Promise((resolve, reject) => {
        http.post("/api/topic_main/update", data)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("topic_main_update", res.data);
                        event.emit("topic_main_change", res.data);
                    }
                },
                (err) => reject(err)
            );
    });
};

export const canTopicMainDelete = (id) => {
    const ids = isArray(id) ? id : [id];
    return new Promise((resolve, reject) => {
        http.post("/api/topic_main/delete", ids)
            .json()
            .then(
                (res) => {
                    resolve(res);
                    if (res.code == 0) {
                        event.emit("topic_main_delete", res.data);
                        event.emit("topic_main_change", res.data);
                    }
                },
                (err) => reject(err)
            );
    });
};
