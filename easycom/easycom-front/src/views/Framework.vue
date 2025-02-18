<template>
  <div class="">
    <!--顶端-->
    <div class="header">
      <!--左边-->
      <div class="logo" @click="backToMain">
        <div class="name">
          <img src="../assets/logo.png" alt="" style="cursor: pointer;margin-right: 10px"/>
          综 测 管 理 系 统
        </div>
      </div>

      <!--右边-->
      <div class="right-panel">
        <el-dropdown>
          <div class="user-info">

            <div class="avatar">
              <img src="../assets/ic_student.png" alt=""/>
            </div>

            <div class="hello">欢迎回来！</div>

            <div class="nick-name">{{userInfo.nickName}}</div>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item @click="bindEmailFormDialog">绑定邮箱</el-dropdown-item>
              <el-dropdown-item>退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!--主体部分-->
    <div class="body">
      <!--左边菜单栏-->
      <div class="left-menu">
        <div class="">
          <div @click="jump(item)" 
          :class="['menu-item',item.code===currentMenu.code?'active':'']" 
          v-for="item in menus"
          >
          <div :class="['iconfont','icon-'+item.icon,'icon-font',item.code===currentMenu.code?'icon-active':'']"></div>
            <span class="text" style="font-size: 25px;text-align: center">{{item.name}}</span>
            <span
                :class="['iconfont',item.code===currentMenu.code?'icon-up':'icon-down','icon-font2']"
                v-if="item.children"
            ></span>
            <router-link :to="item.path"></router-link>
          </div>
        </div>
      </div>


      <div class="right-main">
        <router-view/>
      </div>
      </div>

      <Dialog
        :show = "firstTimeLogin.show"
        :title = "firstTimeLogin.title"
        :buttons = "firstTimeLogin.buttons"
        width = "500px"
        :showCancel = "false"
        @close = "firstTimeLogin.show=false"
      >
        <el-form :model="bindEmailForm" ref="bindEmailFormRef" :rules="rules">
          <el-form-item prop="email">
            <el-input
            placeholder="输入一个常用的邮箱账号"
            v-model.trim = bindEmailForm.email
            >
              <template #prefix>
                <span class="iconfont icon-user "></span>
              </template>
            </el-input>
          </el-form-item>
          <div>
            <el-form-item prop="emailCode">
              <el-input
              placeholder="输入收到的验证码"
              style="width: 75%;"
              v-model.trim = bindEmailForm.emailCode
              >
                <template #prefix>
                  <span class="iconfont icon-checkCode "></span>
                </template>
              </el-input>
              <el-button 
                type='primary'
                style="margin-left: 5px;"
                @click="sendEmailCode"
              >
                发送验证码
              </el-button>
            </el-form-item>          
          </div>
        </el-form>
      </Dialog> 
      <!-- 温馨提示弹窗 -->
      <Dialog
        :show="alert.show" 
        :title="alert.title" 
        :buttons="alert.buttons" 
        :showCancel="false"
        :showClose="false"
      >
        检测到当前账号为首次登录，为了保障账号安全，请先绑定邮箱账号
      </Dialog>

  </div>
</template>

<script setup>
import { buttonEmits } from "element-plus";
import { fa } from "element-plus/es/locale/index.mjs";
import responseData from '@/utils/Request.js'
import {ref, reactive, getCurrentInstance, nextTick, watch, onMounted} from "vue"
const { proxy } = getCurrentInstance();

import {useRouter,useRoute} from "vue-router";
import message from "../utils/Message";
const route = useRoute()
const router = useRouter()

const bindEmailForm = ref({})
const bindEmailFormRef = ref()
//绑定邮箱表单数据规则
const firstTimeLogin = reactive({
  show: false,
  title: '邮箱绑定',
  buttons:[
    {
      text: '确认绑定',
      type: 'primary',
    },
    {
      text: '取消',

      click: (e) =>{
        bindEmailFormDialog()
      }
    }
  ]
})
//温馨提示弹窗
const alert = reactive({
  show: true,
  title: '温馨提示',
  buttons:[
    {
      text: '确认',
      click: (e) =>{
        bindEmailFormDialog()
      }
    },
  ],
})

const rules = {
  email: [{required: true, message: '请输入一个常用的邮箱账号', trigger: 'blur'}],
  emailCode: [{required: true, message: '请输入收到的验证码', trigger: 'blur'}],
}

const menus = [
  {
    path:"/main",
    icon:"main",
    name:"首页",
    code:'main',
  },
  {
    path:"/message",
    icon:"messages",
    name:"信箱",
    code:'messages',
  },
  {
    path:"/setting",
    icon:"setting",
    name:"设置",
    code:'setting',
  },
]
//判断邮箱是否输入并且正确的逻辑
const sendEmailCode = async () => {
  bindEmailFormRef.value.validateField("email",(valid)=>{
    if(!valid){
      return;
    }
  })
}

const currentMenu = ref(menus[0])
const currentPath = ref()

const jump = (item)=>{
  if(!item.path||item.code===currentMenu.value.code){
    return
  }
  router.push(item.path)
}

const setMenu = (code,path) => {
  const menu = menus.find((item)=>{
    return item.code === code
  })
  currentMenu.value = menu
  currentPath.value = path
}
watch(()=>route,(newVal,oldVal)=>{
  if(newVal.meta.code){
    setMenu(newVal.meta.code,newVal.path)
  }
},{immediate:true,deep:true})

const backToMain = ()=>{
  router.push('/main')
}

const userInfo = proxy.VueCookies.get("userInfo")

//控制邮箱绑定弹窗
const bindEmailFormDialog = () =>{
  if(!firstTimeLogin.show){
    firstTimeLogin.show = true;
  }else{
    firstTimeLogin.show = false;
  }
}


//判断用户状态的逻辑段
const ifFullMember = (async) => {
  if (responseData == 1){
    alert.show = true
  }
}
//组件加载完成之后判断用户状态码
onMounted(()=>{
  ifFullMember();
})
</script>



<style lang="scss" scoped>
.header {
  /* 添加阴影效果，让头部有悬浮感，增强层次感 */
  box-shadow: 0 3px 10px 0 rgb(0 0 0 / 8%);

  /* 设置头部的高度为56像素，保持固定高度 */
  height: 80px;

  /* 为头部左侧添加24像素的内边距，避免内容紧贴边缘 */
  padding-left: 24px;

  /* 为头部右侧添加24像素的内边距，避免内容紧贴边缘 */
  padding-right: 24px;

  /* 设置头部为相对定位，允许子元素使用绝对定位，并保持在文档流中 */
  position: relative;

  /* 设置头部的堆叠顺序为200，确保它在页面中位于其他元素之上 */
  z-index: 200;

  /* 使用弹性布局，使子元素可以灵活排列 */
  display: flex;

  /* 在垂直方向上（交叉轴）将子元素居中对齐 */
  align-items: center;

  /* 在水平方向上（主轴）将子元素均匀分布，第一个子元素靠左，最后一个子元素靠右 */
  justify-content: space-between;

  .logo {
    display: flex; /* 使用弹性布局，使子元素可以灵活排列 */
    align-items: center; /* 在垂直方向上将子元素居中对齐 */


    .name {
      font-weight: bold; /* 设置字体加粗 */
      margin-left: 5px; /* 设置左侧外边距为5像素，与图标保持一定间距 */
      font-size: 45px; /* 设置字体大小为25像素 */
      color: #b20a0a; /* 设置字体颜色为浅蓝色 */
      cursor: pointer;
    }
  }

  .right-panel {
    display: flex; /* 使用弹性布局，使子元素可以灵活排列 */
    align-items: center; /* 在垂直方向上将子元素居中对齐 */

    .user-info {
      margin-right: 10px; /* 设置右侧外边距为10像素 */
      display: flex; /* 使用弹性布局，使子元素可以灵活排列 */
      align-items: center; /* 在垂直方向上将子元素居中对齐 */
      cursor: pointer; /* 设置鼠标指针为手形，表示可点击 */

      .avatar {
        display: flex; /* 使用弹性布局 */
        height: 40px; /* 设置头像的高度为40像素 */
        width: 40px; /* 设置头像的宽度为40像素 */
        border-radius: 50%; /* 设置头像为圆形 */
        overflow: hidden; /* 隐藏超出头像区域的内容 */
        img {
          width: 100%; /* 图片宽度占满整个头像区域 */
          object-fit: cover; /* 图片会覆盖整个头像区域，可能会裁剪部分内容以适应 */
        }
        margin: 0 20px 0 15px; /* 设置左右外边距，左边15像素，右边5像素 */
      }

      .hello{
        font-size: 20px;
        font-weight: bold;
      }

      .nick-name {
        color: #05a1f5; /* 设置字体颜色为浅蓝色 */
      }
    }
  }
}

.body{
  display: flex;
  overflow: hidden;
  .left-menu{
    box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
    border-right: #ffffff solid 1px;
    display: flex;
    height: calc(100vh - 80px);
    width: 350px;
    .menu-item{
      margin-top: 20px;
      text-align: center;
      font-size: 14px;
      padding: 20px 0;
      cursor: pointer;
      font-weight: bold;
      width: 300px;
      &:hover{
        background-color: #9bd8fa;
      }
    }
    .icon-font{
      text-align: center;
      font-size: 40px;
    }
    .icon-active{
      color: #05a1f5;
    }
    .icon-font2{
      margin-left: 20px;
    }
    .active{
      background-color: #eef9fe;
      .text{

        color: #05a1f5;
      }
    }
  }


  .right-main{
    background-color: #ffe490;
    width: 100%;
  }

  

}




</style>