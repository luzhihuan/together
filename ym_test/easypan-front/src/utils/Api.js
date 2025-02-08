const api = {

    //获取验证码
    checkCode: "/account/checkCode",
    //发送邮箱验证码
    sendEmailCode:"/account/sendEmailCode",
    //注册
    register:"/account/register",
    //登录
    login:"account/login",
    //重置密码
    resetPassword:"/account/resetPassword",
    //获取用户头像
    avatarUrl:'/account/getAvatar/',
    //更新用户头像
    updateUserAvatar:'/account/updateUserAvatar',
    //修改密码
    updatePassword:'/account/updatePassword',
    //退出登录
    loginOut:'/account/loginOut',
    //获取用户空间
    getUserSpace:'/account/getUseSpace',

    //获取文件列表
    loadFileList:'/file/loadFileList',
    //重命名
    rename:"/file/rename",
    //新建文件夹
    newFolder:"/file/newFolder",
    //获取文件夹信息
    getFolderInfo:"/file/getFolderInfo",
    //删除文件
    delFile:"/file/delFile",
    //移动文件
    changeFileFolder:"/file/changeFileFolder",
    //创建下载链接
    createDownloadUrl:"/file/createDownloadUrl",
    //下载
    download:"/file/download",
    //文件前面的小图标
    imageUrl:"/api/file/getImage/",
    //上传文件
    uploadFile:"/file/uploadFile"

}

export default api;
