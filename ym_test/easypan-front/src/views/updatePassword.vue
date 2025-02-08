<template>
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
      <el-form-item size="large" label="新密码" prop="password" >
        <el-input show-password clearable placeholder="请输入新密码" v-model.trim="formData.password">
          <template #prefix>
            <span class="iconfont icon-password"></span>
          </template>
        </el-input>

      </el-form-item>

      <el-form-item  size="large" label="确认密码" prop="rePassword" >
        <el-input show-password clearable placeholder="请再次输入密码" v-model.trim="formData.rePassword">
          <template #prefix>
            <span class="iconfont icon-password"></span>
          </template>
        </el-input>
      </el-form-item>

    </el-form>
  </Dialog>
</template>

<script setup>
import { ref,reactive,getCurrentInstance,nextTick } from 'vue';
import router from "@/router/index.js";
const {proxy} = getCurrentInstance()



const formData = ref({});
const formDataRef = ref();
const checkRePassword = (rule, value, callback) => {
  if (value !== formData.value.password) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}
const rules = {
  password: [
    {required: true, message: "请输入密码"},
    {validator: proxy.Verify.password, message: "密码只能是数字，字母，特殊字符8-18位"},
  ],
  rePassword: [
    {required: true, message: "请再次输入密码"},
    {validator: checkRePassword, message: "两次输入的密码不一致"},
  ],
};

const show = () => {
  dialogConfig.value.show=true
  nextTick(()=>{
    formDataRef.value.resetFields()
    formData.value={}
  })
}
defineExpose({show})

const dialogConfig = ref({
  show:false,
  title:"修改密码",
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

const submitForm = async () =>{
  formDataRef.value.validate(async (valid)=>{
    if(!valid){
      return
    }
    let result = await proxy.Request({
      url:proxy.Api.updatePassword,
      params:{
        password:formData.value.password
      },
      errorCallback:(res)=>{
        proxy.Message.error(res.msg)
      }
    })
    if(!result){
      return;
    }
    dialogConfig.value.show = false
    await proxy.Request({
      url:proxy.Api.loginOut,
      errorCallback:(res)=>{
        proxy.Message.error(res.msg)
      }
    })
    proxy.VueCookie.remove("userInfo")
    await router.push("/login")
    proxy.Message.success("密码修改成功，请重新登录")
    setTimeout(function () {
      window.location.reload();
    }, 2000);
  })
}


</script>

<style lang="scss" scoped>


</style>
