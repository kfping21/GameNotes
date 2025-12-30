<template>
    <div class="reply J_ReplyBlock" v-if="replyList.length > 0">
        <div class="reply-block" v-for="hf in visibleReplies" :key="hf.id">
            <div class="reply-content">
                <span class="reply-user">
                    <b class="reply-user-nick J_User">{{ hf.huifuren_nickname || hf.huifuren }}</b> :
                </span>
                {{ hf.huifuneirong }}
            </div>
            <div class="reply-operate reply-operate--hot">
                <i class="reply-dot">·</i>
                <span>{{ hf.addtime }}</span>
            </div>
        </div>
        <div class="reply-toggle" v-if="replyList.length > maxVisible">
            <el-button link type="primary" size="small" @click="toggleExpand">
                {{ expanded ? "收起回复" : `展开全部(${replyList.length})` }}
            </el-button>
        </div>
    </div>
</template>

<script>
    import DB from "@/utils/db";

    export default {
        name: "replyList",
        data() {
            return {
                replyList: [],
                expanded: false,
                maxVisible: 2,
            };
        },
        props: {
            commitid: [String, Number],
        },
        watch: {
            commitid() {
                this.loadReply();
            },
        },
        computed: {
            visibleReplies() {
                if (this.expanded) {
                    return this.replyList;
                }
                return this.replyList.slice(0, this.maxVisible);
            },
        },
        methods: {
            toggleExpand() {
                this.expanded = !this.expanded;
            },
            async loadReply() {
                if (this.commitid) {
                    this.replyList = await DB.name("pinglunhuifu")
                        .alias('ph')
                        .joinLeft("yonghu u", "u.zhanghao=ph.huifuren")
                        .field("ph.*, u.mingcheng as huifuren_nickname")
                        .order("ph.id asc")
                        .where("ph.pinglunid", this.commitid)
                        .select();
                    if (this.replyList.length <= this.maxVisible) {
                        this.expanded = false;
                    }
                }
            },
        },
        created() {
            this.loadReply();
        },
    };
</script>

<style scoped lang="scss">
    .reply {
        background-color: #f7f7f7;
        padding: 8px 16px 24px;
        margin-top: 10px;
    }
    .reply-block {
        position: relative;
    }
    .reply-content {
        font-size: 14px;
        color: #4b4b4b;
        line-height: 24px;
        margin-top: 10px;
    }
    .reply-user {
        color: #4b4b4b;
    }
    .center-click .my-avatar,
    .user-click .comment-short .common-avatar,
    .user-click .comment-username,
    .user-click .reply-user-nick {
        cursor: pointer;
    }

    .reply-user-nick {
        font-weight: 600;
    }
    .reply-operate--hot {
        padding-left: 20px;
        background: url(./asset/news-icon.png) 0 -104px no-repeat;
    }
    .reply-operate {
        height: 14px;
        line-height: 14px;
        font-size: 12px;
        margin-top: 6px;
        color: #adadad;
    }
    .reply-operate span {
        color: #adadad;
    }

    .reply-operate-item {
        cursor: pointer;
    }
    .reply-dot {
        display: inline-block;
        margin: 0 2px;
    }
    .reply-operate span {
        color: #adadad;
    }
    .reply-operate .reply-operate-report,
    .reply-operate .reply-operate-report-dot {
        display: none;
    }
    .reply-toggle {
        text-align: center;
        margin-top: 10px;
    }
</style>
