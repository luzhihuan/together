<template>
  <div class="">
<!--    顶端-->
    <div class="header">
<!--      左边-->
      <div class="logo" @click="backToMain">
        <div class="name">
          <img src="../assets/logo.png" alt="" style="cursor: pointer;margin-right: 10px"/>
          综 测 管 理 系 统
        </div>
      </div>

<!--      右边-->
      <div class="right-panel">
        <el-dropdown>
          <div class="user-info">

            <div class="avatar">
              <img src="../assets/ic_student.png" alt=""/>
            </div>

            <div class="hello">欢迎回来！</div>

            <div class="nick-name">luzhihuan</div>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item>退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

<!--    主体部分-->
    <div class="body">
<!--      左边菜单栏-->
      <div class="left-menu">
        <div class="">
          <div
              @click="jump(item)"
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

  </div>
</template>

<script setup>
import {ref, reactive, getCurrentInstance, nextTick, watch} from "vue"
const { proxy } = getCurrentInstance();

import {useRouter,useRoute} from "vue-router";
const route = useRoute()
const router = useRouter()

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
    name:"收件箱",
    code:'messages',
  },
  {
    path:"/setting",
    icon:"setting",
    name:"设置",
    code:'setting',
  },
]

const currentMenu = ref({})
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


</script>



<style lang="scss" scoped>
.header {
  /* 添加阴影效果，让头部有悬浮感，增强层次感 */
  box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);

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
    border-right: #ffffff solid 1px;
    display: flex;
    height: calc(100vh - 70px);
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