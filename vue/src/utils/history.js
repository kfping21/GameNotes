import { session } from "@/utils/utils";

const HISTORY_PREFIX = "yqb_history";

const getUserKey = () => {
    return session("username") || "guest";
};

const buildKey = (type) => {
    return `${HISTORY_PREFIX}:${getUserKey()}:${type}`;
};

const readHistory = (type) => {
    try {
        const raw = localStorage.getItem(buildKey(type));
        const list = raw ? JSON.parse(raw) : [];
        return Array.isArray(list) ? list : [];
    } catch (error) {
        console.error("Failed to parse history", error);
        return [];
    }
};

const writeHistory = (type, list) => {
    localStorage.setItem(buildKey(type), JSON.stringify(list));
};

export const pushHistory = (type, item, maxSize = 50) => {
    if (!type || !item) return;
    const list = readHistory(type);
    const key = item.id ?? item.url;
    const nextItem = {
        ...item,
        type,
        id: item.id ?? key,
        visitedAt: item.visitedAt ?? Date.now(),
    };
    const filtered = list.filter((entry) => String(entry.id) !== String(nextItem.id));
    filtered.unshift(nextItem);
    if (maxSize && filtered.length > maxSize) {
        filtered.length = maxSize;
    }
    writeHistory(type, filtered);
};

export const getHistory = (type, maxSize = 50) => {
    const list = readHistory(type);
    if (!maxSize) return list;
    return list.slice(0, maxSize);
};

export const clearHistory = (type) => {
    if (type) {
        localStorage.removeItem(buildKey(type));
        return;
    }
    const prefix = `${HISTORY_PREFIX}:${getUserKey()}:`;
    Object.keys(localStorage).forEach((key) => {
        if (key.startsWith(prefix)) {
            localStorage.removeItem(key);
        }
    });
};
