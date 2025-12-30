<template>
    <span class="e-select-view">
        {{ content }}
    </span>
</template>
<style type="text/scss" lang="scss"></style>
<script>
    import DB from "@/utils/db";

    export default {
        name: "e-select-view",
        data() {
            return {
                content: "",
            };
        },
        props: {
            value: [String, Number, Array],
            module: {
                type: String,
                required: true,
            },
            select: {
                type: [String, Number],
                required: true,
            },
            show: {
                type: [String, Number],
                required: true,
            },
        },
        watch: {
            value() {
                this.getValue();
            },
        },
        computed: {},
        methods: {
            getValue() {
                if (this.value) {
                    var val = this.value;
                    if (typeof val == "string") {
                        if (val.indexOf(",") != -1) {
                            val = val.split(",");
                        } else {
                            val = [val];
                        }
                    } else if (typeof val == "number") {
                        val = [val];
                    }
                    
                    // 尝试转换为数字，以防后端对类型敏感
                    if (Array.isArray(val)) {
                        val = val.map(v => {
                            const n = Number(v);
                            return isNaN(n) ? v : n;
                        });
                    }

                    console.log(`e-select-view querying: module=${this.module}, field=${this.select}, values=`, val);

                    DB.name(this.module)
                        .where(this.select, "in", val)
                        .select()
                        .then((res) => {
                            console.log(`e-select-view result:`, res);
                            if (res && Array.isArray(res)) {
                                var list = res.map((r) => r[this.show]);
                                this.content = list.join(" ");
                            }
                        })
                        .catch(err => {
                            console.error("e-select-view error:", err);
                        });
                } else {
                    this.content = "";
                }
            },
        },
        created() {
            this.getValue();
        },
        mounted() {},
        destroyed() {},
    };
</script>
