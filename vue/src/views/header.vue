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


                <li :class="{ current: isFullPathActive({ path: '/yonghu/add' }) }">
                    <router-link :to="{ path: '/yonghu/add' }" title="用户注册"> 用户注册 </router-link>
                </li>
                <li :class="{ current: isFullPathActive('/login') }">
                    <router-link :to="'/login'" title="后台管理"> 后台管理 </router-link>
                </li>
            </ul>
            <div class="right clearfix">

                                <li v-if="$session.cx !='管理员'">

                                <e-chat-button>消息</e-chat-button>
                                </li>
                

                <template v-if="$session.username">
                    <div class="line"></div>
                    <div class="link">
                        <el-dropdown>
                            <a href="javascript:;"> {{ $session.username }}，{{ $session.cx }} </a>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="$router.push('/admin/sy')">个人中心</el-dropdown-item>
                                    <el-dropdown-item @click="logout">退出</el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </template>
                <template v-else>
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

        .logo {
            float: left;
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
            float: left;
            margin-left: 50px;

            li {
                margin-right: 40px;
                float: left;
                position: relative;

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
            float: right;
            margin-top: 35px;
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
                    padding: 5px 10px;
                    border: 1px solid var(--header-main-color);
                    border-radius: 10px;
                }
                
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
