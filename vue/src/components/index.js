import { vueGlobalInstall } from "./vue-global";
import Elink from "@/components/ELink.vue";
import ECollect from "./collect.vue";
import ECollect2 from "./collect2.vue";

import EUser from "./EUserInfo.vue";
const modules = import.meta.globEager("./**/index.js");

export function install(app) {
    vueGlobalInstall(app);
    app.component("ELink", Elink);
    app.component("ECollect", ECollect);
    app.component("ECollect2", ECollect2);

    app.component("e-user-info", EUser);

    for (var module in modules) {
        app.use(modules[module].default);
    }
}

export default {
    install,
};
