import DB from "@/utils/db";
import { selectGuanzhuren, selectTianjiaren } from "@/module/guanzhu";
import { getHistory } from "@/utils/history";

const HISTORY_TYPES = ["biji", "topic", "youxi", "mall"];

const safeSelect = async (query) => {
    try {
        const list = await query.select();
        return Array.isArray(list) ? list : [];
    } catch (error) {
        console.error(error);
        return [];
    }
};

const toMonthKey = (value) => {
    if (!value) return "";
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return "";
    const month = String(date.getMonth() + 1).padStart(2, "0");
    return `${date.getFullYear()}-${month}`;
};

export const buildMonthLabels = (months = 6) => {
    const labels = [];
    const now = new Date();
    for (let i = months - 1; i >= 0; i -= 1) {
        const date = new Date(now.getFullYear(), now.getMonth() - i, 1);
        const month = String(date.getMonth() + 1).padStart(2, "0");
        labels.push(`${date.getFullYear()}-${month}`);
    }
    return labels;
};

export const countByMonth = (list, labels, field = "addtime") => {
    const map = {};
    list.forEach((item) => {
        const key = toMonthKey(item?.[field]);
        if (!key) return;
        map[key] = (map[key] || 0) + 1;
    });
    return labels.map((label) => map[label] || 0);
};

export const fetchUserStats = async (username) => {
    if (!username) {
        return {
            counts: {
                biji: 0,
                pinglun: 0,
                shoucang: 0,
                dianzan: 0,
                follow: 0,
                fans: 0,
                history: 0,
            },
            lists: {
                biji: [],
                pinglun: [],
                shoucang: [],
                dianzan: [],
            },
        };
    }

    const [biji, pinglun, shoucang, dianzan] = await Promise.all([
        safeSelect(DB.name("biji").where("tianjiaren", username)),
        safeSelect(DB.name("pinglun").where("pinglunren", username)),
        safeSelect(DB.name("shoucang").where("username", username)),
        safeSelect(DB.name("dianzan").where("username", username)),
    ]);

    let follow = 0;
    let fans = 0;
    try {
        const [followRes, fansRes] = await Promise.all([
            selectGuanzhuren({ username, page: 1, size: 1 }),
            selectTianjiaren({ username, page: 1, size: 1 }),
        ]);
        follow = followRes?.data?.lists?.total || 0;
        fans = fansRes?.data?.lists?.total || 0;
    } catch (error) {
        console.error(error);
    }

    const history = HISTORY_TYPES.reduce((sum, type) => sum + getHistory(type).length, 0);

    return {
        counts: {
            biji: biji.length,
            pinglun: pinglun.length,
            shoucang: shoucang.length,
            dianzan: dianzan.length,
            follow,
            fans,
            history,
        },
        lists: {
            biji,
            pinglun,
            shoucang,
            dianzan,
        },
    };
};
