// import { createApp } from 'vue'

// import App from './App.vue'
// import router from './router'
// import ElementPlus from 'element-plus'
// import 'element-plus/dist/index.css'
// import zhCn from 'element-plus/es/locale/lang/zh-cn'
// import "@/assets/icon/iconfont.css"
// import '@/assets/base.scss'
// import '@/assets/icon/iconfont.css'
// import VueCookie from 'vue-cookies'


// const app = createApp(App)
// app.use(ElementPlus)
// app.use(router)
// app.use(ElementPlus, { locale: zhCn }) 
// app.mount('#app')

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import '@/assets/icon/iconfont.css'
import '@/assets/base.scss'

import VueCookie from 'vue-cookies'

//全局属性引入
//import Verify from '@/utils/Verify.js'
import Api from '@/utils/Api.js'
import Message from '@/utils/Message.js'
import Request from '@/utils/Request.js'
//import Confirm from '@/utils/confirm.js'
//import Utils from "@/utils/Utils.js";

//自定义组件
import Dialog from '@/components/Dialog.vue'
//import Avatar from "@/components/Avatar.vue"
//import Table from "@/components/Table.vue"
//import Icon from '@/components/Icon.vue'
//import NoData from "@/components/NoData.vue";


const app = createApp(App)

app.use(ElementPlus)
app.use(router)

app.component("Dialog",Dialog)
//app.component("Avatar",Avatar)
//app.component("Table",Table)
//app.component("Icon",Icon)
//app.component("NoData",NoData)

//配置全局组件
//app.config.globalProperties.Verify = Verify
app.config.globalProperties.Api = Api
app.config.globalProperties.Message = Message
//app.config.globalProperties.Utils = Utils
app.config.globalProperties.Request = Request
app.config.globalProperties.VueCookie = VueCookie
//app.config.globalProperties.Confirm = Confirm
app.config.globalProperties.globalInfo = {
    avatarUrl:Api.avatarUrl,
    imageUrl:Api.imageUrl
}

app.mount('#app')
