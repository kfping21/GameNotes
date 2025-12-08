<script setup>
import { RouterLink, RouterView, useRouter } from "vue-router";
import { useUserStore } from "@/stores";
let userStore = useUserStore();
userStore.tokenLogin();
import NotifyPanel from '@/components/notify/notify-panel.vue'
</script>

<template>
    <RouterView />
    <e-chat-dialog/>
    <NotifyPanel />
</template>

<style>
html,
body,
#app {
    height: 100%;
}
</style>
