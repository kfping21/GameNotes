<template>
    <div class="aa-cy-header-main">
        <header class="aa-header clearfix" style="">
            <a href="/" class="logo logo1" :title="config.title">
                {{ config.title }}
                <!--            <img src="images/logo.png" alt="专业宠物医院">-->
            </a>
            <ul class="nav">
                <li :class="{ current: isFullPathActive('/index') }">
                    <router-link :to="'/index'" title="首页"> 首页 </router-link>
                </li>
                <li :class="{ current: isFullPathActive({ path: '/biji' }) }">
                    <router-link :to="{ path: '/biji' }" title="笔记信息"> 笔记信息 </router-link>
                </li>
                <li :class="{ current: isFullPathActive({ path: '/topic' }) }">
                    <router-link :to="{ path: '/topic' }" title="话题广场"> 话题广场 </router-link>
                </li>
                <li :class="{ current: isFullPathActive({ path: '/youxi/category' }) }">
                    <router-link :to="{ path: '/youxi/category' }" title="游戏详情"> 游戏详情 </router-link>
                </li>
                <li :class="{ current: isFullPathActive({ path: '/mall/products' }) }">
                    <router-link :to="{ path: '/mall/products' }" title="周边商城"> 周边商城 </router-link>
                </li>


            </ul>
            <div class="right clearfix">
                <div class="link message-link" v-if="$session.cx !='管理员'">
                    <e-chat-button>消息</e-chat-button>
                </div>

                <template v-if="$session.username">
                    <div class="line"></div>
                    <template v-if="isAdmin">
                        <div class="link user-link">
                            <span class="action-btn user-trigger"> {{ $session.mingcheng || $session.username }}，你好 </span>
                        </div>
                        <div class="line"></div>
                        <div class="link header-shortcut">
                            <router-link class="action-btn" to="/admin/sy">管理后台</router-link>
                        </div>
                        <div class="line"></div>
                        <div class="link header-shortcut">
                            <a class="action-btn" href="javascript:;" @click="logout">退出</a>
                        </div>
                    </template>
                    <template v-else>
                        <div class="link header-shortcut">
                            <router-link class="action-btn" to="/history">浏览历史</router-link>
                        </div>
                        <div class="link header-shortcut">
                            <router-link class="action-btn" to="/my/dashboard">数据看板</router-link>
                        </div>
                        <div class="link header-shortcut">
                            <router-link class="action-btn" to="/my/growth">成长中心</router-link>
                        </div>
                        <div class="line"></div>
                        <div class="link user-link">
                            <el-dropdown>
                                <a href="javascript:;" class="action-btn user-trigger"> {{ $session.mingcheng || $session.username }}，你好 </a>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item @click="$router.push('/my/profile')">我的主页</el-dropdown-item>
                                        <el-dropdown-item @click="logout">退出</el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                        </div>
                    </template>
                </template>
                <template v-else>
                    <div class="line"></div>
                    <div class="link">
                        <router-link to="/login">管理员登录</router-link>
                    </div>
                    <div class="line"></div>
                    <div class="link">
                        <a href="javascript:;" @click="showReg"> <i class="fa fa-user-plus"></i> 注册 </a>
                        <div class="login-model" @click.stop :class="{ 'show-model': isShowReg }">
                            <div class="login-box">
                                <h3 class="login-title">用户注册</h3>
                                <form action="javascript:;" @submit.prevent="onReg">
                                    <div class="login-input input-username">
                                        <input type="text" class="input" v-model="regForm.zhanghao" placeholder="输入账号" required />
                                        <span class="input-title">账号：</span>
                                    </div>
                                    <div class="login-input input-password">
                                        <input type="password" class="input" v-model="regForm.mima" placeholder="输入密码" required />
                                        <span class="input-title">密码：</span>
                                    </div>
                                    <div class="login-input input-username">
                                        <input type="text" class="input" v-model="regForm.mingcheng" placeholder="输入昵称" required />
                                        <span class="input-title">昵称：</span>
                                    </div>
                                    <div class="login-input input-cx">
                                        <select class="input" v-model="regForm.xingbie">
                                            <option value="男">男</option>
                                            <option value="女">女</option>
                                        </select>
                                        <span class="input-title">性别：</span>
                                    </div>
                                    <div class="login-btn">
                                        <button class="input-btn-bottom" type="submit">注册</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="line"></div>
                    <div class="link">
                        <a href="javascript:;" @click="showLogin"> <i class="fa fa-user-o"></i> {{ isShowLogin ? "关闭" : "登录" }} </a>
                        <div class="login-model" @click.stop :class="{ 'show-model': isShowLogin }">
                            <div class="login-box" ref="loginBoxRef">
                                <h3 class="login-title">登录系统</h3>
                                <form action="javascript:;" @submit.prevent="onLogin">
                                    <div class="login-input input-username">
                                        <input type="text" class="input" v-model="loginForm.username" name="username" placeholder="输入用户名" />
                                        <span class="input-title">账号：</span>
                                    </div>
                                    <div class="login-input input-password">
                                        <input type="password" class="input" v-model="loginForm.pwd" name="pwd" placeholder="输入密码" />
                                        <span class="input-title">密码：</span>
                                    </div>
                                    <div class="login-input input-captch">
                                        <input type="text" class="input" v-model="loginForm.pagerandom" name="pagerandom" placeholder="输入验证码" />
                                        <span class="input-title">验证码：</span>
                                        <img :src="captchaUrl" @click="loadCaptcha" class="captch" />
                                    </div>
                                    <div class="login-input input-cx" v-if="rules.length > 1">
                                        <select class="input" name="cx" @change="loginForm.cx = $event.target.value">
                                            <option :value="o" v-for="o in rules" :key="o">{{ o }}</option>
                                        </select>
                                    </div>
                                    <div class="login-btn">
                                        <button class="input-btn-bottom" type="submit">登录</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </template>
            </div>
        </header>
    </div>
</template>
<script setup>
    import { useRouter, useRoute } from "vue-router";
    import { computed, ref, reactive, watch, onMounted, onBeforeUnmount } from "vue";
    import { logout, session, useEvent } from "@/utils";
    import { canYonghuInsert } from "@/module";
    import config from "@/config";
    import DB from "@/utils/db";
    import { Search } from "@element-plus/icons-vue";
    import { isObject } from "@/utils/extend";
    import { isFullPathActive, isPathActive } from "@/router/router-utils";
    import { useUserStore } from "@/stores";
    import domEvent from "@/utils/dom-event";

    import { captch } from "@/utils/utils";
    import { canLogin } from "@/stores";
    import { ElMessage } from "element-plus";

    const isShowLogin = ref(false);
    const isShowReg = ref(false);

    const isAdmin = computed(() => {
        const cx = (session("cx") || "").toString().trim();
        const lower = cx.toLowerCase();
        return cx === "管理员" || lower === "admin" || lower === "administrator" || lower === "superadmin" || cx.indexOf("管理员") !== -1;
    });

    const loginBoxRef = ref(null);
    const captchaUrl = ref("");
    const rules = ["用户"];
    const loginForm = reactive({
        username: "",
        pwd: "",
        cx: rules[0],
        /* 验证码段 */
        pagerandom: "",
        a: "a",
        /* 验证码段 */
    });

    const regForm = reactive({
        zhanghao: "",
        mima: "",
        mingcheng: "",
        xingbie: "男",
    });

    const showReg = (e) => {
        if (e) {
            e.stopPropagation();
            e.preventDefault();
        }
        isShowLogin.value = false;
        if (!isShowReg.value) {
            regForm.zhanghao = "";
            regForm.mima = "";
            regForm.mingcheng = "";
            regForm.xingbie = "男";
            domEvent.once(document, "click", () => {
                isShowReg.value = false;
            });
        }
        isShowReg.value = !isShowReg.value;
    };

    const onReg = async () => {
        if (!regForm.zhanghao || !regForm.mima || !regForm.mingcheng) {
            ElMessage.error("请填写完整信息");
            return;
        }
        const res = await canYonghuInsert(regForm);
        if (res.code === 0) {
            ElMessage.success("注册成功，请登录");
            isShowReg.value = false;
            isShowLogin.value = true;
        } else {
            ElMessage.error(res.msg);
        }
    };
    const loadCaptcha = () => {
        captch().then((res) => {
            loginForm.captchToken = res.token;
            captchaUrl.value = res.url;
        });
    };

    const onLogin = async () => {
        const res = await canLogin(loginForm);
        if (res.code === 0) {
            ElMessage.success("登录成功");
            isShowLogin.value = false;
        } else {
            if (res.code === 20) {
                loadCaptcha();
            }
            ElMessage.success(res.msg);
        }
    };

    const showLogin = (e) => {
        e.stopPropagation();
        e.preventDefault();
        isShowReg.value = false;
        if (!isShowLogin.value) {
            loginForm.username = "";
            loginForm.pwd = "";
            loginForm.pagerandom = "";
            loginForm.cx = rules[0];
            loadCaptcha();
            domEvent.once(document, "click", () => {
                isShowLogin.value = false;
            });
        }
        isShowLogin.value = !isShowLogin.value;
    };

    const router = useRouter();
    const userState = useUserStore();

    const isHover = ref(false);

    const loadListMenu = async (module, target) => {
        target.value = await DB.name(module).order("id desc").select();
    };

    const route = useRoute();
</script>

<style lang="scss" type="text/scss" scoped>
    .aa-cy-header-main {
        height: 90px;
    }

    .aa-header {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 90px;
        padding: 0 3%;
        background: #ffffff;
        z-index: 999;
        border-bottom: 1px solid #eeeeee;
        --header-main-color: var(--theme-primary-color, #1cb19c);
        box-sizing: border-box;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .logo {
            
        }

        .logo1 {
            height: 100%;
            display: flex;
            align-items: center;
            font-size: 30px;
            color: var(--header-main-color);
        }

        .logo2 {
            display: none;
        }

        .nav {
            margin-left: 50px;
            flex: 1;
            display: flex;
            align-items: center;

            li {
                margin-right: 40px;
                position: relative;
                flex-shrink: 0;

                > a {
                    display: block;
                    line-height: 90px;
                    height: 90px;
                    color: #777777;
                    position: relative;

                    &:after {
                        content: "";
                        position: absolute;
                        bottom: 0px;
                        left: 0;
                        width: 0;
                        height: 3px;
                        background: var(--header-main-color);
                        border-radius: 2px;
                        -webkit-transition: all ease 0.4s;
                        -moz-transition: all ease 0.4s;
                        -ms-transition: all ease 0.4s;
                        -o-transition: all ease 0.4s;
                        transition: all ease 0.4s;
                    }
                }

                div {
                    display: none;
                    position: absolute;
                    top: 90px;
                    left: 50%;
                    margin-left: -55px;
                    width: 110px;
                    background: rgba(0, 0, 0, 0.3);
                    text-align: center;

                    a {
                        display: block;
                        line-height: 40px;
                        height: 40px;
                        color: #ffffff;
                    }
                }

                &.current > a,
                &:hover > a {
                    color: var(--header-main-color);
                }

                &.current > a:after {
                    width: 100%;
                }

                &:hover {
                    > a:after {
                        width: 100%;
                    }

                    div {
                        display: block;
                    }
                }

                div a:hover {
                    background: rgba(0, 0, 0, 0.3);
                }
            }
        }

        .right {
            display: flex;
            align-items: center;

            .tel {
                float: left;
                font-size: 20px;
                color: var(--header-main-color);
                display: flex;
                align-items: center;

                i {
                    margin-right: 10px;
                }
            }

            .line {
                float: left;
                width: 1px;
                height: 15px;
                background: #e5e5e5;
                margin: 0 20px;
            }

                .link {
                float: left;
                position: relative;
                display: flex;
                align-items: center;

                i {
                    margin-right: 5px;
                }

                a {
                    font-size: 12px;
                    color: #777777;
                    margin: 0 6px;
                    position: relative;
                    padding: 3px 8px;
                    border: 1px solid var(--header-main-color);
                    border-radius: 10px;
                }

                .action-btn {
                    display: inline-flex;
                    align-items: center;
                    font-size: 13px;
                    padding: 4px 10px;
                    border-radius: 14px;
                    font-weight: 500;
                    color: var(--header-main-color);
                    background: #fff;
                    transition: all 0.3s;
                    &:hover {
                        background: var(--header-main-color);
                        color: #fff;
                    }
                }
            }

            .message-link :deep(.el-button) {
                height: 34px;
                padding: 0 16px;
                border-radius: 16px;
                font-size: 13px;
                border-color: var(--header-main-color);
                color: var(--header-main-color);
            }

            .user-link .user-trigger {
                font-size: 13px;
                padding: 5px 12px;
                border-radius: 15px;
                color: #ffffff;
                border: none;
                background: linear-gradient(120deg, var(--header-main-color) 0%, #7aa7ff 100%);
                box-shadow: 0 4px 10px rgba(71, 104, 230, 0.25);
            }

            .user-link .user-trigger:hover {
                filter: brightness(1.03);
            }

            .search-box {
                width: 180px;
                position: relative;

                .in {
                    width: 100%;
                    background: none;
                    padding: 8px 28px 8px 20px;
                    border-radius: 16px;
                    background: rgba(255, 255, 255, 0.3);
                    border: 1px solid #dedede;
                    box-sizing: border-box;
                    transition: all ease 0.4s;
                }

                .in:focus {
                    box-shadow: 0 0 5px var(--header-main-color);
                }

                .bt {
                    position: absolute;
                    top: 0;
                    right: 8px;
                    width: 20px;
                    height: 100%;
                    display: flex;
                    align-items: center;
                    border: none;
                    background: transparent;
                }
            }
        }

        .login-model {
            position: absolute;
            top: 25px;
            width: 350px;
            background: #ffffff;
            right: 0px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            z-index: 999;
            transform: rotateX(-90deg);
            transform-origin: 0 0;
            visibility: hidden;
            transition: all 0.3s;

            .login-box {
                padding: 20px;

                .login-title {
                    color: var(--header-main-color);
                    font-size: 18px;
                    margin: 0;
                    margin-bottom: 10px;
                    padding-bottom: 5px;
                    border-bottom: 1px solid var(--header-main-color);
                }

                .login-input {
                    margin-bottom: 15px;
                    position: relative;

                    .input-title {
                        position: absolute;
                        height: 100%;
                        top: 0;
                        left: 0;
                        display: flex;
                        align-items: center;
                        padding-left: 10px;
                    }

                    .input {
                        width: 100%;
                        height: 40px;
                        padding-left: 65px;
                        line-height: 40px;
                        border: 1px solid #eeeeee;
                        border-radius: 5px;
                        outline: none;
                    }

                    .input:focus {
                        box-shadow: 0 0 5px var(--header-main-color);
                    }

                    .input:focus + .input-title {
                        color: var(--header-main-color);
                    }
                }

                .input-captch {
                    position: relative;

                    .captch {
                        position: absolute;
                        right: 0;
                        top: 0;
                    }
                }

                .login-btn {
                    .input-btn-bottom {
                        color: var(--header-main-color);
                        display: block;
                        width: 100%;
                        padding: 15px 10px;
                        background: #ffffff;
                        cursor: pointer;
                        border-radius: 30px;
                        border: 1px solid var(--header-main-color);
                    }
                }
            }
        }

        .login-model.show-model {
            visibility: visible;
            transform: rotateX(0);
        }
    }
</style>
