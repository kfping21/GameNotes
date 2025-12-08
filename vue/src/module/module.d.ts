
declare interface EModuleForm<T>{
    form:T;
}

declare interface EModuleReadForm<T,R>{
    form:T;
    readMap:R;
}



declare interface EAdmins {
    // 帐号 。
    username?:string;
    // 密码 。
    pwd?:string;
        
}

declare type EAdminsForm = EModuleForm<EAdmins>;
declare interface EYonghu {
    // 账号 。
    zhanghao?:string;
    // 密码 。
    mima?:string;
    // 名称 。
    mingcheng?:string;
    // 性别 。
    // 可选项:男,女。
    xingbie?:string;
    // 出生日期 。
    chushengriqi?:string;
    // 手机 。
    shouji?:string;
    // 邮箱 。
    youxiang?:string;
    // 个性签名 。
    gexingqianming?:string;
    // 标签 。
    biaoqian?:string;
    // 我在玩 。
    wozaiwan?:string;
    // 头像 。
    touxiang?:string;
        
}

declare type EYonghuForm = EModuleForm<EYonghu>;
declare interface ELunbotu {
    // 标题 。
    title?:string;
    // 图片 。
    image?:string;
    // 连接地址 。
    url?:string;
        
}

declare type ELunbotuForm = EModuleForm<ELunbotu>;
declare interface EShoucang {
    // 用户 。
    username?:string;
    // 关联表id 。
    xwid?:number;
    // 关联表 。
    biao?:string;
    // 标题 。
    biaoti?:string;
    // 收藏时间 。
    addtime?:string;
        
}

declare type EShoucangForm = EModuleForm<EShoucang>;
declare interface EDianzan {
    // 用户 。
    username?:string;
    // 关联表id 。
    biaoid?:number;
    // 关联表 。
    biao?:string;
    // 标题 。
    biaoti?:string;
    // 点赞时间 。
    addtime?:string;
        
}

declare type EDianzanForm = EModuleForm<EDianzan>;
declare interface EBiji {
    // 笔记编号 。
    bijibianhao?:string;
    // 笔记名称 。
    bijimingcheng?:string;
    // 笔记封面 。
    bijifengmian?:string;
    // 标签 。
    biaoqian?:string;
    // 关联游戏 。
    guanlianyouxi?:number;
    // 视频 。
    shipin?:string;
    // 关注量 。
    guanzhuliang?:number;
    // 种草度 。
    zhongcaodu?:number;
    // 详情 。
    xiangqing?:string;
    // 添加人 。
    tianjiaren?:string;
    issh?:string;
        // 种草的数量。
    zhongcaoCount?:number;
    // 关注的数量。
    guanzhuCount?:number;

}

declare type EBijiForm = EModuleForm<EBiji>;
declare interface EYouxi {
    // 游戏名称 。
    youximingcheng?:string;
    // 关联笔记 。
    guanlianbiji?:number;
    // 游戏图片 。
    youxitupian?:string;
    // 装备库 。
    zhuangbeiku?:string;
    // 详情 。
    xiangqing?:string;
        
}

declare type EYouxiForm = EModuleForm<EYouxi>;
declare interface EBiaoqian {
    // 标签名称 。
    biaoqianmingcheng?:string;
        
}

declare type EBiaoqianForm = EModuleForm<EBiaoqian>;
declare interface EZhongcao {
    // 笔记id 。
    bijiid?:number;
    // 笔记编号 。
    bijibianhao?:string;
    // 笔记名称 。
    bijimingcheng?:string;
    // 标签 。
    biaoqian?:string;
    // 关联游戏 。
    guanlianyouxi?:number;
    // 添加人 。
    tianjiaren?:string;
    // 种草人 。
    zhongcaoren?:string;
    // 添加时间 。
    addtime?:string;
        
}

declare type EZhongcaoForm = EModuleReadForm<EZhongcao,EBiji>;
declare interface EPinglun {
    // 表 。
    biao?:string;
    // 表id 。
    biaoid?:number;
    // 标题 。
    biaoti?:string;
    // 评分 。
    pingfen?:number;
    // 评论内容 。
    pinglunneirong?:string;
    // 评论人 。
    pinglunren?:string;
    // 评论时间 。
    addtime?:string;
            // 评论回复的数量。
    pinglunhuifuCount?:number;

}

declare type EPinglunForm = EModuleForm<EPinglun>;
declare interface EPinglunhuifu {
    // 评论id 。
    pinglunid?:number;
    // 表 。
    biao?:string;
    // 表id 。
    biaoid?:number;
    // 标题 。
    biaoti?:string;
    // 评论内容 。
    pinglunneirong?:string;
    // 评论人 。
    pinglunren?:string;
    // 回复内容 。
    huifuneirong?:string;
    // 回复人 。
    huifuren?:string;
    // 回复时间 。
    addtime?:string;
        
}

declare type EPinglunhuifuForm = EModuleReadForm<EPinglunhuifu,EPinglun>;
declare interface ETongzhi {
    // 通知内容 。
    tongzhineirong?:string;
    // 用户 。
    yonghu?:string;
    // 添加时间 。
    addtime?:string;
    issh?:string;
    
}

declare type ETongzhiForm = EModuleForm<ETongzhi>;
declare interface EGuanzhu {
    // 笔记id 。
    bijiid?:number;
    // 笔记编号 。
    bijibianhao?:string;
    // 笔记名称 。
    bijimingcheng?:string;
    // 标签 。
    biaoqian?:string;
    // 关联游戏 。
    guanlianyouxi?:number;
    // 添加人 。
    tianjiaren?:string;
    // 关注人 。
    guanzhuren?:string;
        
}

declare type EGuanzhuForm = EModuleReadForm<EGuanzhu,EBiji>;
declare interface ESiliao {
    // 编号 。
    bianhao?:string;
    // 收信人 。
    shouxinren?:string;
    // 发信人 。
    faxinren?:string;
    // 消息最后时间 。
    addtime?:string;
            // 消息的数量。
    xiaoxiCount?:number;

}

declare type ESiliaoForm = EModuleForm<ESiliao>;
declare interface EXiaoxi {
    // 私聊id 。
    siliaoid?:number;
    // 编号 。
    bianhao?:string;
    // 内容 。
    neirong?:string;
    // 发送人 。
    fasongren?:string;
    // 发送时间 。
    fasongshijian?:string;
    // 是否查看 。
    shifouzhakan?:string;
        
}

declare type EXiaoxiForm = EModuleReadForm<EXiaoxi,ESiliao>;


declare interface EResponseData<T>{
    // 为0 表示成功，其他表示错误码
    code:number;
    // 错误信息
    msg:string;
    // 数据
    data:T;
}
