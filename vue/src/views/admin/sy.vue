<template>
    <div class="v-admin-sy">
        <div class="v-admin-sy-bg">
            <div class="sy-title">欢迎使用 {{ config.title }}</div>
            <div class="sy-session">
                <div class="">
                    {{ $session.username }}
                    ({{ $session.cx }})
                </div>
            </div>
        </div>
    </div>
</template>
<style type="text/scss" scoped lang="scss">
    .v-admin-sy {
        display: flex;
        align-items: center;
        justify-content: center;
        height: calc(100vh - 100px);
        .v-admin-sy-bg {
            text-align: center;
            color: var(--theme-primary-color);
            background: #ffffff;
            width: 100%;
            padding: 30px;
            border-radius: 10px;
            .sy-title {
                font-size: 24px;
            }
            .sy-session {
                color: #1d1d1d;
                margin-top: 20px;
            }
        }
    }
</style>
<script setup>
    import config from "@/config";
</script>
