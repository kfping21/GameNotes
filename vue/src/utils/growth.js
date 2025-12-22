const STORAGE_KEY = "yqb_growth";

const LEVEL_STEPS = [
    { level: 1, min: 0, max: 60 },
    { level: 2, min: 60, max: 160 },
    { level: 3, min: 160, max: 320 },
    { level: 4, min: 320, max: 520 },
    { level: 5, min: 520, max: 800 },
    { level: 6, min: 800, max: 1200 },
];

const getKey = (username) => `${STORAGE_KEY}:${username || "guest"}`;

const getEmptyState = () => ({
    points: 0,
    lastSignIn: "",
    claimed: {},
    history: [],
});

const loadState = (username) => {
    try {
        const raw = localStorage.getItem(getKey(username));
        if (!raw) return getEmptyState();
        const parsed = JSON.parse(raw);
        return {
            ...getEmptyState(),
            ...parsed,
            claimed: parsed?.claimed || {},
            history: Array.isArray(parsed?.history) ? parsed.history : [],
        };
    } catch (error) {
        console.error(error);
        return getEmptyState();
    }
};

const saveState = (username, state) => {
    localStorage.setItem(getKey(username), JSON.stringify(state));
};

const formatDateKey = (value) => {
    const date = value ? new Date(value) : new Date();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${date.getFullYear()}-${month}-${day}`;
};

export const getLevelInfo = (points) => {
    let current = LEVEL_STEPS[LEVEL_STEPS.length - 1];
    for (const step of LEVEL_STEPS) {
        if (points >= step.min && points < step.max) {
            current = step;
            break;
        }
    }
    const next = LEVEL_STEPS.find((step) => step.level === current.level + 1);
    const max = next ? next.min : current.max;
    const progress = max > current.min ? (points - current.min) / (max - current.min) : 1;
    return {
        level: current.level,
        min: current.min,
        max,
        progress: Math.min(Math.max(progress, 0), 1),
        isMax: !next,
    };
};

export const getGrowthState = (username) => loadState(username);

export const signIn = (username, reward = 5) => {
    const state = loadState(username);
    const today = formatDateKey();
    if (state.lastSignIn === today) {
        return { ok: false, message: "今天已签到", points: state.points };
    }
    state.lastSignIn = today;
    state.points += reward;
    state.history.unshift({ type: "签到", points: reward, time: new Date().toISOString() });
    state.history = state.history.slice(0, 20);
    saveState(username, state);
    return { ok: true, message: "签到成功", points: state.points };
};

export const claimReward = (username, taskId, reward = 0) => {
    const state = loadState(username);
    if (state.claimed[taskId]) {
        return { ok: false, message: "奖励已领取", points: state.points };
    }
    state.claimed[taskId] = true;
    state.points += reward;
    state.history.unshift({ type: "任务奖励", points: reward, time: new Date().toISOString() });
    state.history = state.history.slice(0, 20);
    saveState(username, state);
    return { ok: true, message: "领取成功", points: state.points };
};
