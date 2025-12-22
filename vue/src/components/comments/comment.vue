<template>
    <div class="">
        <div class="" v-if="isComment">
            <el-form action="javascript:;" ref="form" :model="comment" method="post" status-icon validate-on-rule-change label-width="100px">
                <el-form-item label="评分" prop="pingfen">
                    <el-rate v-model="comment.pingfen"></el-rate>
                </el-form-item>
                <el-form-item label="评论内容" prop="pinglunneirong" required :rules="[{ required: true, message: '请输入评论内容' }]">
                    <el-input type="textarea" v-model="comment.pinglunneirong" placeholder="请输入评论内容"></el-input>
                </el-form-item>
                <el-form-item>

                    <template v-if="$session.cx == '用户'">



                    <el-button type="primary" @click="submitComment">发布评论</el-button>
                    </template>
                </el-form-item>
            </el-form>
        </div>
        <div v-else>
            <slot name="notCommit"></slot>
        </div>
        <div style="margin-top: 10px">
            <div class="comment" v-for="v in lists">
                <div class="common-avatar J_User">
                    <e-img v-if="v[headimg]" :src="v[headimg]" width="100%" height="100%"></e-img>
                    <img :src="defaultHeadimg" v-else width="100%" height="100%" />
                </div>
                <div class="comment-block">
                    <p class="comment-user">
                        <span class="comment-username J_User"> {{ v[name] }} </span>
                        <span>
                            <el-rate v-model="v.pingfen" disabled show-score text-color="#ff9900" score-template="{value}"> </el-rate>
                        </span>
                        <span class="comment-time">{{ v.addtime.substr(0, 10) }}</span>
                        <router-link v-if="isReply" :to="buildReplyLink(v)">回复</router-link>
                        <router-link v-if="isReply" class="comment-quote-link" :to="buildQuoteLink(v)">引用回复</router-link>
                        <el-button class="comment-report-btn" link @click="openReport(v)">举报</el-button>
                    </p>
                    <div class="comment-content J_CommentContent">{{ v.pinglunneirong }}</div>

                    <div class="" style="display: flex;justify-content: flex-start;align-items: center">


                        <e-collect2 module="dianzan" :form="{biaoid:'biaoid',biao:'biao',biaoti:'biaoti'}" :biaoid="v.id" biao="pinglun" :biaoti="v.pinglunneirong">
                            <template #default="{isCollect,count}">
                                <div class="collect" :class="{isCollect:isCollect}" style="display: flex;align-items: center">
                                    <i style="margin-right: 5px" :class="isCollect ? 'fa fa-thumbs-up' : 'fa fa-thumbs-o-up'"></i>
                                    <span>
                                        {{ isCollect ? '已点赞' : '赞' }}({{ count }})
                                    </span>
                                </div>
                            </template>
                        </e-collect2>


                    </div>
                    <reply-list v-if="isReply" :commitid="v.id"></reply-list>
                </div>
            </div>
        </div>
    <report-dialog v-model="reportVisible" :target="reportTarget" />
    </div>
</template>
<style type="text/scss" scoped lang="scss">
    .comment {
        position: relative;
        margin-top: 32px;
        padding: 0px 0px 32px 68px;
        border-bottom: 1px solid rgb(240, 240, 240);
        .common-avatar {
            display: block;
            position: absolute;
            left: 0px;
            top: 8px;
            z-index: 1;
            width: 50px;
            height: 50px;
            font-size: 0px;
            background: url("./asset/default.gif") no-repeat;
            border-radius: 50%;
            background-size: cover;
            overflow: hidden;
        }
        .common-avatar img {
            width: 100%;
            height: 100%;
        }
    .comment-user {
        height: 32px;
        line-height: 32px;
        font-size: 12px;
        > span {
            display: inline-block;
        }
        .comment-username {
            font-weight: 600;
            color: rgb(55, 155, 233);
        }
        .comment-time {
            color: rgb(153, 153, 153);
            display: inline-block;
            margin-left: 12px;
        }
    }
    .comment-report-btn {
        font-size: 12px;
        margin-left: 8px;
    }
    .comment-quote-link {
        font-size: 12px;
        margin-left: 8px;
        color: var(--theme-primary-color);
    }
    }
    .comment-content {
        font-size: 14px;
        color: rgb(75, 75, 75);
        line-height: 24px;
        margin-top: 10px;
        overflow-wrap: break-word;
    }
    .comment-short .comment-content {
        margin-top: 2px;
    }
</style>
<script>
    import { extend, isFunction } from "@/utils/extend";
    import ReplyList from "@/components/comments/replyList";
    import ReportDialog from "@/components/report/ReportDialog.vue";
    import defaultHeadimg from "./asset/default.gif";
    import { canPinglunInsert } from "@/module";

    export default {
        name: "e-comments",
        components: { ReplyList, ReportDialog },
        data() {
            return {
                comment: {
                    pingfen: 5,
                    pinglunneirong: "",
                    biao: "",
                    biaoid: 0,
                    biaoti: "",
                },
                isLoading: false,
                defaultHeadimg: defaultHeadimg,
                reportVisible: false,
                reportTarget: {},
            };
        },
        props: {
            lists: Array,
            module: String,
            biaoti: String,
            headimg: String,
            name: String,
            isReply: Boolean,
            commitCallback: Function,
            isComment: {
                type: Boolean,
                default: true,
            },
        },
        watch: {},
        computed: {},
        methods: {
            buildReplyLink(row) {
                return { path: "/pinglunhuifu/add", query: { id: row.id } };
            },
            buildQuoteLink(row) {
                const raw = row?.pinglunneirong ? String(row.pinglunneirong) : "";
                const quote = raw.replace(/\s+/g, " ").trim().slice(0, 60);
                const replyTo = row?.[this.name] || row?.pinglunren || "";
                return {
                    path: "/pinglunhuifu/add",
                    query: {
                        id: row.id,
                        quote,
                        replyTo,
                    },
                };
            },
            openReport(row) {
                this.reportTarget = {
                    type: "comment",
                    typeLabel: "评论",
                    id: row.id,
                    title: row.biaoti,
                    content: row.pinglunneirong,
                    url: window.location.href,
                };
                this.reportVisible = true;
            },
            submitComment() {
                this.$refs.form.validate().then(() => {
                    var comment = extend(true, {}, this.comment);
                    comment.biao = this.module;
                    comment.biaoti = this.biaoti;
                    comment.pinglunren = this.$session.username;
                    comment.biaoid = this.$route.query.id;
                    if (isFunction(this.commitCallback)) {
                        this.commitCallback(comment);
                    }

                    this.isLoading = true;
                    canPinglunInsert(comment)
                        .then((res) => {
                            this.isLoading = false;
                            if (res.code == 0) {
                                comment[this.name] = this.$session[this.name];
                                comment[this.headimg] = this.$session[this.headimg];
                                comment.addtime = "刚刚";

                                this.$emit("success");
                                //this.lists.unshift(extend(true, { id: res.data.id }, comment));
                                this.$message.success("评论成功");
                            } else {
                                this.$message.error(res.msg);
                            }
                        })
                        .catch((err) => {
                            this.isLoading = false;
                            this.$message.error(err.message);
                        });
                });
            },
        },
        created() {},
        mounted() {},
        destroyed() {},
    };
</script>
