<template>
    <div class="footer mt10">
        <div class="copyrightnr">
        </div>
    </div>
</template>

<style type="text/scss" scoped lang="scss">
    .footer {
        padding: 15px;
        color: #767676;
        text-align: center;
        .link {
            a:after {
                content: " | ";
            }
            a:last-child:after {
                content: "";
            }
        }
    }
</style>
<script>
    import DB from "@/utils/db";
    import config from "@/config";
    export default {
        data() {
            return {
                $order: config,
                likeList: [],
            };
        },
        methods: {},
        created() {},
    };
</script>
