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
                    },
                    component:()=>import("@/views/main/main.vue"),
                },
                {
                    path:"/message",
                    name:"收件箱",
                    meta:{
                        needLogin:true,
                    },
                    component:()=>import("@/views/message/message.vue"),

                },
                {
                    path:"/setting",
                    name:"设置",
                    meta:{
                        needLogin:true,
                    },
                    component:()=>import("@/views/setting/setting.vue"),
                },
            ]
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







