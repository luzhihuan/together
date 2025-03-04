<template>
  <Dialog
      :show="show"
      :title="title"
      :buttons="buttons"
      :width="width"
      :show-cancel="showCancel"
      @close="$emit('close')"
  >
    <el-form
        :model="formData"
        :rules="rules"
        ref="formDataRef"
        label-width="80px"
        @submit.prevent
    >
      <!--input输入-->
      <el-form-item v-if="changeType!==3" prop="password" size="large" label="新密码">
        <el-input clearable placeholder="请输入新密码" v-model.trim="formData.password">
          <template #prefix>
            <span class="iconfont icon-password"></span>
          </template>
        </el-input>
      </el-form-item>

      <!--input输入-->
      <el-form-item v-if="changeType!==3" prop="rePassword" size="large" label="确认密码">
        <el-input clearable placeholder="请再次输入密码" v-model.trim="formData.rePassword">
          <template #prefix>
            <span class="iconfont icon-password"></span>
          </template>
        </el-input>
      </el-form-item>

      <!--input输入-->
      <el-form-item v-if="changeType===3" prop="email" size="large" label="原邮箱">
        <el-input clearable placeholder="请输入原邮箱" v-model.trim="formData.oldEmail">
          <template #prefix>
            <span class="iconfont icon-messages"></span>
          </template>
        </el-input>
      </el-form-item>

      <!--input输入-->
      <el-form-item v-if="changeType!==2" prop="email" size="large" label="新邮箱">
        <el-input clearable placeholder="请输入邮箱" v-model.trim="formData.email">
          <template #prefix>
            <span class="iconfont icon-messages"></span>
          </template>
        </el-input>
      </el-form-item>

      <!--input输入-->
      <el-form-item v-if="changeType!==2" prop="emailCode" size="large" label="验证码">
        <div class="sendEmail">
          <el-input clearable placeholder="请输入邮箱验证码" v-model.trim="formData.emailCode">
            <template #prefix>
              <span class="iconfont icon-checkCode"></span>
            </template>
          </el-input>
          <el-button class="sendButton" type="primary" @click="showEmailDialog()">发送验证码</el-button>
        </div>
      </el-form-item>

    </el-form>
  </Dialog>
  
  <!-- 邮箱弹窗 -->
  <Dialog
      :show="emailConfirmDialog.show"
      :title="emailConfirmDialog.title"
      :buttons="emailConfirmDialog.buttons"
      width="500px"
      :showCancel="false"
      @close="emailConfirmDialog.show=false"
  >
    <el-form
        :model="formDataSendMailCode"
        :rules="rules"
        ref="formDataSendMailCodeRef"
        label-width="80px"
        @submit.prevent
    >
      <el-form-item label="邮箱:">
        {{ formData.email }}
      </el-form-item>

      <el-form-item prop="checkCode">
        <div class="form_base">
          <el-input
              size="large"
              placeholder="请输入图片验证码"
              v-model.trim="formDataSendMailCode.checkCode"
              class="form_checkCode"
          >
            <template #prefix>
              <span class="iconfont icon-checkCode "></span>
            </template>
          </el-input>
          <img class="form_checkCode" :src="checkCodeEmailUrl" alt="" @click="getEmailCheckCode()"/>
        </div>
      </el-form-item>

    </el-form>
  </Dialog>


</template>

<script setup>

import Dialog from "@/components/Dialog.vue";
import {ref, reactive, getCurrentInstance, nextTick} from "vue"
import router from "@/router/index.js";

const {proxy} = getCurrentInstance();
const formData = ref({});
const formDataRef = ref();

//检查两次密码是否一致
const checkRePassword = (rule, value, callback) => {
  if (value !== formData.value.password) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}

const rules = {
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {validator: proxy.Verify.password, message: "密码只能是数字，字母，特殊字符8-18位"}
  ],
  checkCode: [{required: true, message: '请输入正确的图片验证码', trigger: 'blur'}],
  email: [
    {required: true, message: '请输入正确的邮箱', trigger: 'blur'},
    {validator: proxy.Verify.email, message: '请输入正确的邮箱'}
  ],
  emailCode: [
    {required: true, message: '请输入收到的邮箱验证码', trigger: 'blur'},
  ],
  rePassword: [
    {required: true, message: '请确认新密码', trigger: 'blur'},
    {validator: checkRePassword, message: '两次密码不一致！'}
  ]
};

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  title: {
    type: String,
    default: "绑定邮箱并修改密码"
  },
  width: {
    type: String,
    default: '600px'
  },
  showCancel: {
    type: Boolean,
    default: true
  },
  changeType: {
    type: Number,
    default: 1
  },
})

//提交完善信息
const submitFormData = () => {
  formDataRef.value.validate(async (valid)=>{
    if(!valid){
      return
    }
    const params = {}
    //操作类型，1第一次登录，2修改密码，3更换邮箱
    if(props.changeType === 1){
      params.password = formData.value.password
      params.email = formData.value.email
      params.emailCode = formData.value.emailCode
    }else if(props.changeType === 2){
      params.password = formData.value.password
    }else {
      params.oldEmail = formData.value.oldEmail
      params.email = formData.value.email
      params.emailCode = formData.value.emailCode
    }
    let result = await proxy.Request({
       // url:proxy.Api.login,
       params:params,
      errorCallback: (res) => {
         proxy.Message.error(res.msg)
      }
    })
    if(!result){
      return;
    }
    proxy.Message.success("更新成功，请重新登录")
    setTimeout(()=>{  
      window.location.reload()
    },1000)
    proxy.VueCookies.remove("userInfo")
    proxy.VueCookies.remove("token")
    await router.push("/login")
  })
}


const buttons =reactive([
  {text:"确定",type:"primary",click:(e) =>{submitFormData()} },
])

    
//获取需要发送邮箱验证码时的验证码
const checkCodeEmailUrl = ref()
const getEmailCheckCode = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getCheckCode,
    params: {
      type: 1
    },
    errorCallback: (res) => {
      proxy.Message.error(res.msg)
    }
  })
  if (!result) {
    return;
  }
  checkCodeEmailUrl.value = result.data.checkCode
  console.log(checkCodeEmailUrl)
  localStorage.setItem("checkCodeEmailKey", result.data.checkCodeKey)
}


//邮箱弹窗设置
const emailConfirmDialog = reactive({
  show: false,
  title: "发送邮箱验证码",
  buttons: [{
    type: "primary",
    text: "发送验证码",
    click: (e) => {
      sendEmailCode();
    },
  }]
})

//显示邮箱弹窗
const showEmailDialog = () => {
  formDataRef.value.validateField("email",(valid)=>{
    if(!valid){
      return
    }
    emailConfirmDialog.show=true
    getEmailCheckCode()
  })
}

//发送邮箱验证码所需的参数
const formDataSendMailCode = ref({});
const formDataSendMailCodeRef = ref();

//获取邮箱验证码请求
const sendEmailCode = async () => {
  formDataSendMailCodeRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let result = await proxy.Request({
      url:proxy.Api.sendEmailCode,
      params:{
        email:formDataSendMailCode.value.email,
        checkCodeKey:localStorage.getItem("checkCodeEmailKey"),
        checkCode:formDataSendMailCode.value.checkCode,
      },
      errorCallback: (res) => {
        proxy.Message.error(res.msg)
      }
    })
    if(!result){
      return;
    }
    proxy.Message.success("邮件发送成功！请登录邮箱查询！")
    emailConfirmDialog.value = false
  });
};

</script>

<style lang="scss" scoped>

.sendEmail {
  display: flex;
  align-items: center; 
  .sendButton{
    margin-left: 8px;
    
  }
}

.form_base {
  width: 100%;
  display: flex;

  .form_checkCode {
    margin-left: 5px;
    cursor: pointer;
  }
}

</style>