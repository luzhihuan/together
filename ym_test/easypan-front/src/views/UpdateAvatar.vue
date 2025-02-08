<template>
  <div>
    <Dialog
        :show="dialogConfig.show"
        :title="dialogConfig.title"
        :buttons="dialogConfig.buttons"
        width="500px"
        :showCancel="false"
        @close="dialogConfig.show=false"
    >
      <el-form
          :model="formData"
          :rules="rules"
          ref="formDataRef"
          label-width="80px"
          @submit.prevent
      >
        <!--昵称-->
        <el-form-item label="昵称">
          {{formData.nickName}}
        </el-form-item>

        <!--上传头像-->
        <el-form-item label="头像" >
          <AvatarUpload
            v-model="formData.avatar"
          ></AvatarUpload>
        </el-form-item>
      </el-form>
    </Dialog>

  </div>
</template>

<script setup>
import { ref,reactive,getCurrentInstance,nextTick } from 'vue';
const {proxy} = getCurrentInstance()

import {useRouter,useRoute} from "vue-router";
import AvatarUpload from "@/components/avatarUpload.vue";

const router = useRouter()
const route = useRoute()


const formData = ref({});
const formDataRef = ref();


const show = (data) => {
  formData.value = Object.assign({},data)
  formData.value.avatar = {userId:data.userId,qqAvatar:data.avatar}
  dialogConfig.value.show=true
}

defineExpose({show})

const dialogConfig = ref({
  show:false,
  title:"修改头像",
  buttons:[
    {
      type:"primary",
      text:"确定",
      click:(e)=>{
        submitForm();
      },
    },
  ]
})


const emit = defineEmits()

const submitForm = async () =>{
  if(!(formData.value.avatar instanceof File)){
    dialogConfig.value.show = false
    return
  }
  let result = await proxy.Request({
    url:proxy.Api.updateUserAvatar,
    params:{
      avatar:formData.value.avatar
    },
    errorCallback:(res)=>{
      proxy.Message.error(res.msg)
    }
  })
  if(!result){
    return;
  }
  dialogConfig.value.show = false
  const cookieUserInfo = proxy.VueCookie.get("userInfo")
  delete cookieUserInfo.avatar
  proxy.VueCookie.set("userInfo",cookieUserInfo,0)
  emit("updateAvatar")
  proxy.Message.success("修改成功")
}


</script>

<style lang="scss" scoped>
</style>
