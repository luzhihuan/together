import {createRouter,createWebHistory} from "vue-router";

import VueCookies from "vue-cookies"

const router =createRouter({
    history:createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path:'/login',
            name:'login',
            component:()=>import("@/views/Login.vue")
        },
        {
            path:'/',
            name:'Framework',
            component:()=>import("@/views/Framework.vue"),
            children:[
                {
                    path:"/",
                    redirect:"/main"
                },
                {
                    path:"/main",
                    name:"首页",
                    meta:{
                        needLogin:true,
                        code:'main'
                    },
                    component:()=>import("@/views/main/main.vue"),
                },
                {
                    path:"/message",
                    name:"收件箱",
                    meta:{
                        needLogin:true,
                        code:'messages'
                    },
                    component:()=>import("@/views/message/message.vue"),

                },
                {
                    path:"/setting",
                    name:"设置",
                    meta:{
                        needLogin:true,
                        code:'setting'
                    },
                    component:()=>import("@/views/setting/setting.vue"),
                },
                
            ]
        },
        {
            path:'/passwordReset',
            name:'passwordReset',
            component:()=>import("@/views/passwordReset.vue")
        },
        {
            path: "/main/fileUpLoad",
            name: "文件上传",
            meta: {
                needLogin: true,
                code: 'file',
            },
            component:()=>import("@/views/main/fileUpLoad.vue"),
        },
        {
            path: "/main/prePerformance",
            name: "往期综测成绩",
            meta: {
                needLogin: true,
                code: 'prePerformance',
            },
            component:()=>import("@/views/main/prePerformance.vue"),
        },
        {
            path: "/main/evaluationResult",
            name: "评定结果",
            meta: {
                needLogin: true,
                code: 'evaluationResult',
            },
            component:()=>import("@/views/main/evaluationResult.vue"),
        },
        {
            path: "/message/inbox",
            name: "信箱",
            meta: {
                needLogin: true,
                code: 'inbox',
            },
            component:()=>import("@/views/message/inbox.vue"),
        },
        {
            path: "/main/unprocessedTask",
            name: "未处理任务",
            meta: {
                needLogin: true,
                code: 'unprocessedTask',
            },
            component:()=>import("@/views/main/unprocessedTask.vue"),
        },
        {
            path: "/main/unprocessedTask/id",
            name: "单个任务",
            meta: {
                needLogin: true,
                code: 'singleTask',
            },
            component:()=>import("@/views/main/singleTask.vue"),
        },
    ]
})
router.beforeEach((to,from,next,)=>{
    const userInfo = VueCookies.get("userInfo")
    if(to.meta.needLogin!=null&&to.meta.needLogin&&userInfo==null){
        router.push("/login")
    }
    next();
})

export default router







