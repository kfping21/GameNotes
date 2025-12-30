// Mock data helpers for mall admin/demo flows

const PRODUCT_STORAGE_KEY = 'mall_demo_products';
const CATEGORY_STORAGE_KEY = 'mall_demo_categories';
const ORDER_STORAGE_KEY = 'mall_demo_orders';

const baseCategories = [
    { id: 1, name: '周边', sort: 1 },
    { id: 2, name: '外设', sort: 2 },
    { id: 3, name: '服饰', sort: 3 },
    { id: 4, name: '虚拟道具', sort: 4 },
];

const baseProducts = [
    {
        id: 1,
        name: '原神派蒙手办',
        price: 299,
        stock: 100,
        categoryName: '周边',
        cover_url: 'https://images.unsplash.com/photo-1607894212119-bdf3c8d1f87b?auto=format&fit=crop&w=800&q=80',
    },
    {
        id: 2,
        name: '雷蛇游戏鼠标',
        price: 199,
        stock: 50,
        categoryName: '外设',
        cover_url: 'https://images.unsplash.com/photo-1587202372616-b43abea06c1e?auto=format&fit=crop&w=800&q=80',
    },
    {
        id: 3,
        name: '王者荣耀T恤',
        price: 99,
        stock: 200,
        categoryName: '服饰',
        cover_url: 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?auto=format&fit=crop&w=800&q=80',
    },
    {
        id: 4,
        name: 'Steam充值卡',
        price: 100,
        stock: 999,
        categoryName: '虚拟道具',
        cover_url: 'https://images.unsplash.com/photo-1522071740424-8b337d0dfa12?auto=format&fit=crop&w=800&q=80',
    },
];

const baseOrders = [
    {
        id: 'D202312300001',
        orderNo: 'D202312300001',
        username: 'user01',
        totalAmount: 299,
        status: '已完成',
        createTime: '2023-12-30 10:00:00',
        items: [
            { name: '原神派蒙手办', price: 299, quantity: 1, cover_url: baseProducts[0].cover_url },
        ],
    },
    {
        id: 'D202312300002',
        orderNo: 'D202312300002',
        username: 'user02',
        totalAmount: 199,
        status: '待发货',
        createTime: '2023-12-30 11:00:00',
        items: [
            { name: '雷蛇游戏鼠标', price: 199, quantity: 1, cover_url: baseProducts[1].cover_url },
        ],
    },
];

const load = (key, fallback) => {
    if (typeof localStorage === 'undefined') return fallback;
    try {
        const raw = localStorage.getItem(key);
        if (!raw) return fallback;
        return JSON.parse(raw);
    } catch (err) {
        console.warn('load storage failed', err);
        return fallback;
    }
};

const save = (key, value) => {
    if (typeof localStorage === 'undefined') return;
    try {
        localStorage.setItem(key, JSON.stringify(value));
    } catch (err) {
        console.warn('save storage failed', err);
    }
};

let products = load(PRODUCT_STORAGE_KEY, baseProducts);
let categories = load(CATEGORY_STORAGE_KEY, baseCategories);
let orders = load(ORDER_STORAGE_KEY, baseOrders);

const persist = () => {
    save(PRODUCT_STORAGE_KEY, products);
    save(CATEGORY_STORAGE_KEY, categories);
    save(ORDER_STORAGE_KEY, orders);
};

const nextId = (list) => Math.max(0, ...list.map((i) => Number(i.id) || 0)) + 1;

export const listCategories = () => [...categories];

export const saveCategory = (payload) => {
    if (payload.id) {
        const target = categories.find((c) => c.id === payload.id);
        if (target) {
            target.name = payload.name;
            target.sort = payload.sort;
        }
    } else {
        const id = nextId(categories);
        categories.push({ id, name: payload.name, sort: payload.sort || id });
    }
    persist();
    return listCategories();
};

export const deleteCategory = (id) => {
    categories = categories.filter((c) => c.id !== id);
    persist();
    return listCategories();
};

export const getProducts = (keyword = '') => {
    const kw = keyword.trim().toLowerCase();
    const data = kw
        ? products.filter((p) => p.name.toLowerCase().includes(kw))
        : products;
    return [...data];
};

export const saveProduct = (payload) => {
    if (payload.id) {
        const target = products.find((p) => p.id === payload.id);
        if (target) {
            Object.assign(target, payload);
        }
    } else {
        const id = nextId(products);
        products.push({ ...payload, id });
    }
    persist();
    return getProducts();
};

export const deleteProduct = (id) => {
    products = products.filter((p) => p.id !== id);
    persist();
    return getProducts();
};

export const listOrders = () => [...orders];

export const getOrderDetail = (id) => orders.find((o) => String(o.id) === String(id));

export const addMockOrder = (payload) => {
    const id = `D${Date.now()}`;
    const order = { id, orderNo: id, status: '已完成', createTime: new Date().toLocaleString(), ...payload };
    orders.unshift(order);
    persist();
    return id;
};
