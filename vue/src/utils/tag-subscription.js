import { session } from "@/utils/utils";

const SUBSCRIBE_PREFIX = "yqb_tag_subscribe";

const getUserKey = () => {
    return session("username") || "guest";
};

const buildKey = () => {
    return `${SUBSCRIBE_PREFIX}:${getUserKey()}`;
};

const normalizeId = (id) => String(id);

export const getSubscribedTags = () => {
    try {
        const raw = localStorage.getItem(buildKey());
        const list = raw ? JSON.parse(raw) : [];
        return Array.isArray(list) ? list.map(normalizeId) : [];
    } catch (error) {
        console.error("Failed to read subscribed tags", error);
        return [];
    }
};

export const setSubscribedTags = (ids) => {
    const normalized = Array.isArray(ids) ? ids.map(normalizeId) : [];
    localStorage.setItem(buildKey(), JSON.stringify(normalized));
    return normalized;
};

export const toggleSubscribedTag = (id) => {
    const normalizedId = normalizeId(id);
    const list = getSubscribedTags();
    const idx = list.indexOf(normalizedId);
    if (idx >= 0) {
        list.splice(idx, 1);
    } else {
        list.unshift(normalizedId);
    }
    return setSubscribedTags(list);
};

export const isSubscribedTag = (id) => {
    const normalizedId = normalizeId(id);
    return getSubscribedTags().includes(normalizedId);
};

export const clearSubscribedTags = () => {
    localStorage.removeItem(buildKey());
};
