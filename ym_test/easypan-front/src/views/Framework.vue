<template>
  <div class="framework">
    <div class="header">
      <div class="logo">
        <span class="iconfont icon-pan"></span>
        <div class="name">Easy云盘</div>
      </div>
      <div class="right-panel">
        <el-popover
          :width="800"
          trigger="click"
          v-model:visible="showUploader"
          :offset="20"
          transition="none"
          :hide-after="0"
          :popper-style="{padding:'0px'}"
        >
          <template #reference>
            <span class="iconfont icon-transfer"></span>
          </template>
          <template #default>
            <Uploader ref="uploaderRef"
                      @uploadCallback="uploadCallbackHandler"
            ></Uploader>
          </template>
        </el-popover>
        <el-dropdown>
          <div class="user-info">
            <div class="avatar">
              <Avatar
                :userId="userInfo.userId"
                :avatar="userInfo.avatar"
                :timestamp="timestamp"
                :width="46"
              ></Avatar>
            </div>
            <span class="nick-name">{{userInfo.nickName}}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="updateAvatar">修改头像</el-dropdown-item>
              <el-dropdown-item @click="updatePassword">修改密码</el-dropdown-item>
              <el-dropdown-item @click="loginOut">退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>


    <div class="body">
      <div class="left-side">
        <div class="menu-list">
          <div
              @click="jump(item)"
              :class="['menu-item',item.menuCode===currentMenu.menuCode?'active':'']"
              v-for="item in menus"
          >
            <div :class="['iconfont','icon-'+item.icon]"></div>
            <div class="text">{{ item.name }}</div>
          </div>
        </div>

        <div class="menu-sub-list">
          <div :class="['menu-item-sub',currentPath===sub.path?'active':'']" v-for="sub in currentMenu.children" @click="jump(sub)">
            <span :class="['iconfont','icon-'+sub.icon]" v-if="sub.icon"></span>
            <span class="text">{{sub.name}}</span>
          </div>
          <div class="tips" v-if="currentMenu&&currentMenu.tips">{{currentMenu.tips}}</div>
          <div class="space-info">
            <div class="">空间使用</div>
            <div class="percent">
              <el-progress
                  :percentage="(userSpace.useSpace/userSpace.totalSpace)*100"
              >
                <template #default>
                  <div style="font-size: 15px; color: #000000;">{{ proxy.Utils.sizeToStr(userSpace.useSpace) }}/{{ proxy.Utils.sizeToStr(userSpace.totalSpace) }}</div>
                </template>
              </el-progress>
            </div>
          </div>
        </div>

      </div>
      <div class="body-content">
        <router-view v-slot="{Component}">
          <component :is="Component" @addFile="addFile"></component>
        </router-view>
      </div>
    </div>
    <UpdateAvatar ref="updateAvatarRef" @updateAvatar="reloadAvatar"></UpdateAvatar>
    <UpdatePassword ref="updatePasswordRef"></UpdatePassword>
  </div>
</template>

<script setup>
import {ref, reactive, getCurrentInstance, nextTick, watch} from 'vue';

const {proxy} = getCurrentInstance()

import UpdatePassword from "@/views/updatePassword.vue"
import Avatar from "@/components/Avatar.vue";
import UpdateAvatar from "@/views/UpdateAvatar.vue";
import Uploader from "@/views/main/Uploader.vue";



import {useRouter, useRoute} from "vue-router";
const router = useRouter()
const route = useRoute()

const timestamp = ref(0)
const userInfo = ref(proxy.VueCookie.get("userInfo"))

//获取用户空间
const userSpace = ref({})
const getUserSpace = async () => {
  let result = await proxy.Request({
    url:proxy.Api.getUserSpace,
    errorCallback:(res)=>{
      proxy.Message.error(res.msg)
    }
  })
  if(!result){
    return;
  }
  userSpace.value = result.data
}
getUserSpace()

const showUploader = ref(false)
//添加文件
const uploaderRef = ref()
const addFile = (data) => {
  const {file,filePid} = data
  showUploader.value = true
  uploaderRef.value.addFile(file,filePid)
}

const menus = [
  {
    icon:"cloude",
    name:"首页",
    menuCode:"main",
    path:"/main/all",
    allShow:true,
    children:[
      {
        icon:"all",
        name:"全部",
        category:"all",
        path:"/main/all"
      },
      {
        icon:"video",
        name:"视频",
        category:"video",
        path:"/main/video"
      },
      {
        icon:"music",
        name:"音频",
        category:"music",
        path:"/main/music"
      },
      {
        icon:"image",
        name:"图片",
        category:"image",
        path:"/main/image"
      },
      {
        icon:"doc",
        name:"全部",
        category:"doc",
        path:"/main/doc"
      },
      {
        icon:"more",
        name:"其他",
        category:"other",
        path:"/main/other"
      },
    ]
  },
  {
    path:"/myshare",
    icon:"share",
    name:"分享",
    menuCode: "share",
    allShow:true,
    children:[
      {
        name:"分享文件",
        path:"/myshare",
      },
    ]
  },
  {
    path:"/recycle",
    icon:"del",
    name:"回收站",
    menuCode: "recycle",
    tips:"回收站为你保存10内的删除文件",
    allShow:true,
    children:[
      {
        name:"删除的文件",
        path:"/recycle",
      },
    ]
  },
  {
    path:"/admin/fileList",
    icon:"settings",
    name:"设置",
    menuCode: "settings",
    allShow:false,
    children:[
      {
        name:"用户文件",
        path:"/admin/fileList",
      },
      {
        name:"用户管理",
        path:"/admin/userList",
      },
      {
        path:"/admin/sysSetting",
        name:"系统设置",
      },
    ]
  },
]

const currentMenu = ref({})
const currentPath = ref()

//上传文件回调
const uploadCallbackHandler = () => {
  nextTick(()=>{
    //TODO 更新用户空间
  })
}

const jump = (data)=>{
  if(!data.path||data.menuCode==currentMenu.value.menuCode){
    return
  }
  router.push(data.path);

}

const setMenu = (menuCode,path)=>{
  const menu = menus.find(item=>{
    return item.menuCode===menuCode;
  })
  currentMenu.value = menu
  currentPath.value = path
}
watch(()=> route,(newVal,oldVal)=>{
  if(newVal.meta.menuCode){
    setMenu(newVal.meta.menuCode,newVal.path)
  }
},{immediate:true,deep:true})

//修改头像
const updateAvatarRef = ref()
const updateAvatar = () => {
  updateAvatarRef.value.show(userInfo.value)
}

const reloadAvatar = ()=>{
  userInfo.value = proxy.VueCookie.get("userInfo")
  timestamp.value = new Date().getTime()

}

//修改密码
const updatePasswordRef = ref()
const updatePassword = ()=>{
  updatePasswordRef.value.show()
}

//退出
const loginOut = ()=>{
  proxy.Confirm(`确定退出登录？`,async ()=>{
    let result = await proxy.Request({
      url:proxy.Api.loginOut,

      errorCallback:(res)=>{
        proxy.Message.error(res.msg)
      }
    })
    if(!result){
      return;
    }
    proxy.VueCookie.remove("userInfo")
    await router.push("/login")
  })
}

console.log(userInfo.value)

</script>




<style scoped lang="scss">
.header {
  box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);
  height: 56px;
  padding-left: 24px;
  padding-right: 24px;
  position: relative;
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .logo {
    display: flex;
    align-items: center;

    .icon-pan {
      font-size: 40px;
      color: #1296db;
    }

    .name {
      font-weight: bold;
      margin-left: 5px;
      font-size: 25px;
      color: #05a1f5;
    }
  }

  .right-panel {
    display: flex;
    align-items: center;
    .icon-transfer {
      cursor: pointer;
    }
    .user-info {
      margin-right: 10px;
      display: flex;
      align-items: center;
      cursor: pointer;

      .avatar {
        margin: 0 5px 0 15px;
      }

      .nick-name {
        color: #05a1f5;
      }
    }
  }

}

.body{
  display: flex;
  .left-side{
    border-right: 1px solid #f1f2f4;
    display: flex;
    .menu-list{
      height: calc(100vh - 56px);
      width: 80px;
      box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);
      border-right: 1px solid #f1f2f4;
      .menu-item{
        text-align: center;
        font-size: 14px;
        font-weight: bold;
        padding: 20px 0px;
        cursor: pointer;
        &:hover{
          background: #f3f3f3;
        }
        .iconfont{
          font-weight: normal;
          font-size: 28px;
        }
      }
      .active{
        background: #eef9fe;
        .iconfont{
          color: #05a1f5;
        }
        .text{
          color: #05a1f5;
        }
      }
    }
    .menu-sub-list{
      width: 200px;
      padding: 20px 10px 0;
      position: relative;
      .menu-item-sub{
        text-align: center;
        line-height: 40px;
        border-radius: 5px;
        cursor: pointer;
        &:hover{
          background: #f3f3f3;
        }
        .iconfont{
          font-size: 14px;
          margin-right: 20px;
        }
        .text{
          font-size: 13px;
        }
      }
      .active{
        background: #eef9fe;
        .iconfont{
          color: #05a1f5;
        }
        .text{
          color: #05a1f5;
        }
      }

      .tips{
        margin-top: 10px;
        color: #888888;
        font-size: 13px;
      }

      .space-info{
        position: absolute;
        bottom: 10px;
        width: 100%;
        padding: 0 5px;
        .percent{
          padding-right: 10px;
        }
        .space-use{
          margin-top: 5px;
          color: #7e7e7e;
          display: flex;
          justify-content: space-around;
          .use{
            flex: 1;
          }
          .iconfont{
            cursor: pointer;
            margin-right: 20px;
            color: #05a1f5;
          }
        }
      }
    }
  }
  .body-content{
    flex: 1;
    width: 0;
    padding-left: 20px;
  }
}


</style>
