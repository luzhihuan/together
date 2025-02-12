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
        }
    ]
})
router.beforeEach((to,from,next,)=>{
    const userInfo = VueCookies.get("userInfo")
    // if(to.meta.needLogin!=null&&to.meta.needLogin&&userInfo==null){
    //     router.push("/login")
    // }
    next();
})

export default router







