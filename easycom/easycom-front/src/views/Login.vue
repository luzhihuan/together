<template>
  <div class="login-body">
    <div class="bg">
      <div class="left-part">
        <!-- 左半部分占位 -->
      </div>
      <div class="right-part">
        
        <!-- 登录面板及动画 -->
        <transition name="slide">
          <div v-if="currentCard === 1">
            <el-card class="login-card">
              <div class="login-title">用户登录</div>
              <el-form :model="formData" :rules="rules" ref="formDataRef" @submit.prevent>
                <el-form-item prop="username" class="form_base">
                  <el-input
                      size="large"
                      placeholder="请使用学号或邮箱登录"
                      v-model.trim="formData.username"
                      @focus="clearValidation('username')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-user"></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="password" class="form_base">
                  <el-input
                      size="large"
                      placeholder="请输入密码"
                      v-model.trim="formData.password"
                      show-password
                      @focus="clearValidation('password')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-password"></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="checkCode">
                  <div class="form_base">
                    <el-input
                        size="large"
                        placeholder="请输入图片验证码"
                        v-model.trim="formData.checkCode"
                        @focus="clearValidation('checkCode')"
                        @keyup.enter="loginAction"
                    >
                      <template #prefix>
                        <span class="iconfont icon-checkCode"></span>
                      </template>
                    </el-input>
                    <img class="form_checkcode" :src="checkCodeUrl" alt="" @click="changeCheckCode(0)"/>

                  </div>
                </el-form-item>

                <div class="form_base">
                  <el-checkbox class="rememberMe" v-model="formData.rememberMe">记住我</el-checkbox>
                </div>
                <div class="form_base">
                  <el-button type="text" class="fogotPassword" @click="reSetCancel(2)">忘记密码?</el-button>
                  <el-button type="text" class="regiseter" @click="reSetCancel(3)">没有账号?</el-button>
                </div>
                <div class="form_base">
                  <el-button type="primary" class="logIn" @click="loginAction">登录</el-button>
                </div>
              </el-form>
            </el-card>
          </div>
        </transition>

        <!-- 重置密码面板及动画 -->
        <transition name="slide">
          <div v-if="currentCard === 2">
            <el-card class="login-card">
              <div class="login-title">密码重置</div>
              <el-form :model="formData" ref="formDataRef" :rules="rules">
                <el-form-item prop="email" class="form_base">
                  <el-input
                      size="large"
                      placeholder="请输入邮箱"
                      v-model.trim="formData.email"
                      @focus="clearValidation('email')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-user "></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="emailCode" class="form_base ">
                  <div class="input-button-group">
                    <el-input
                        size="large"
                        v-model.trim="formData.emailCode"
                        placeholder="请输入收到的验证码"
                        @focus="clearValidation('emailCode')"
                    >
                      <template #prefix>
                        <span class="iconfont icon-checkCode "></span>
                      </template>
                    </el-input>
                    <el-button type="primary" class="checkcodeButton" @click="popUp">发送验证码</el-button>
                  </div>
                  <el-button type="text">没收到验证码？</el-button>
                </el-form-item>

                <el-form-item prop="firstPassword" class="form_base">
                  <el-input
                      size="large"
                      v-model.trim="formData.password"
                      placeholder="请输入新密码"
                      show-password
                      @focus="clearValidation('firstPassword')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-password"></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="rePassword" class="form_base">
                  <el-input
                      size="large"
                      v-model.trim="formData.rePassword"
                      placeholder="请确认新密码"
                      show-password
                      @focus="clearValidation('rePassword')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-password"></span>
                    </template>
                  </el-input>
                </el-form-item>
                <el-form-item prop="checkcode">
                  <div class="form_base">
                    <el-input
                        size="large"
                        v-model.trim="formData.checkCode"
                        placeholder="请输入图片验证码"
                        @focus="clearValidation('checkCode')"
                    >
                      <template #prefix>
                        <span class="iconfont icon-checkCode"></span>
                      </template>
                    </el-input>
                    <img class="form_checkcode" :src="checkCodeUrl" alt="" @click="changeCheckCode(0)"/>
                  </div>
                </el-form-item>

                <el-form-item class="form_base button">
                  <el-button type="primary" @click="reSetPassword">重置</el-button>
                  <el-button class="cancel" @click="reSetCancel(1)">取消</el-button>
                </el-form-item>

              </el-form>
            </el-card>
          </div>
        </transition>

        
        <!-- 注册面板及动画 -->
        <transition name="slide">
          <div v-if="currentCard === 3">
            <el-card class="login-card">
              <div class="login-title">用户注册</div>
              <el-form :model="formData" ref="formDataRef" :rules="rules">
                <el-form-item prop="email" class="form_base">
                  <el-input
                      size="large"
                      placeholder="请输入邮箱"
                      v-model.trim="formData.email"
                      @focus="clearValidation('email')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-user "></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="nickName" class="form_base">
                  <el-input
                      size="large"
                      placeholder="请输入昵称"
                      v-model.trim="formData.nickName"
                      @focus="clearValidation('nickName')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-user "></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="emailCode" class="form_base ">
                  <div class="input-button-group">
                    <el-input
                        size="large"
                        placeholder="请输入收到的验证码"
                        v-model.trim="formData.emailCode"
                        @focus="clearValidation('emailCode')"
                    >
                      <template #prefix>
                        <span class="iconfont icon-checkCode "></span>
                      </template>
                    </el-input>
                    <el-button type="primary" class="checkcodeButton" @click="popUp">发送验证码</el-button>
                  </div>
                  <el-button type="text">没收到验证码？</el-button>
                </el-form-item>

                <el-form-item prop="password" class="form_base">
                  <el-input
                      size="large"
                      v-model.trim="formData.password"
                      placeholder="请输入密码"
                      show-password
                      @focus="clearValidation('password')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-password"></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="rePassword" class="form_base">
                  <el-input
                      size="large"
                      placeholder="请确认密码"
                      v-model.trim="formData.rePassword"
                      show-password
                      @focus="clearValidation('rePassword')"
                  >
                    <template #prefix>
                      <span class="iconfont icon-password"></span>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item prop="checkCode">
                  <div class="form_base">
                    <el-input
                        size="large"
                        placeholder="请输入图片验证码"
                        v-model.trim="formData.checkCode"
                        @focus="clearValidation('checkCode')"
                    >
                      <template #prefix>
                        <span class="iconfont icon-checkCode"></span>
                      </template>
                    </el-input>
                    <img class="form_checkcode" :src="checkCodeUrl" alt="" @click="changeCheckCode(0)"/>

                  </div>
                </el-form-item>

                <el-form-item class="form_base button" >
                  <el-button type="primary" @click="submitRegister">注册</el-button>
                  <el-button class="cancel" @click="reSetCancel(1)">取消</el-button>
                </el-form-item>

              </el-form>
            </el-card>
          </div>
        </transition>

      </div>
      
      
      <!-- 弹窗 -->
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
            {{formData.email}}
          </el-form-item>

          <el-form-item prop="checkCode">
            <div class="form_base">
              <el-input
                  size="large"
                  placeholder="请输入图片验证码"
                  v-model.trim="formDataSendMailCode.checkCode"
                  @focus="clearValidation('checkCode')"
                  class="form_checkcode"
              >
                <template #prefix>
                  <span class="iconfont icon-checkCode "></span>
                </template>
              </el-input>
              <img class="form_checkcode" :src="checkCodeEmailUrl" alt="" @click="changeCheckCode(1)"/>
            </div>
          </el-form-item>

        </el-form>
      </Dialog>


    </div>
  </div>


</template>


<script setup>
import {ref, reactive, getCurrentInstance, nextTick, onMounted} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {md5} from "js-md5";

const {proxy} = getCurrentInstance()

//路由初始化
const route = useRoute()
const router = useRouter()


const formData = ref({});
const formDataRef = ref();




//界面切换动画初始化
const currentCard = ref(1)
const reSetCancel = (type) => {
  currentCard.value = type
  formData.value = {}
  restForm()
}


const checkRePassword = (rule,value,callback) =>{
  if(value !== formData.value.password){
    callback(new Error(rule.message))
  }else {
    callback()
  }
}

const rules = {
  username: [{required: true, message: '请输入学号或邮箱', trigger: 'blur'}],
  nickname: [{required: true, message: '请输入昵称', trigger: 'blur'}],
  password: [
      {required: true, message: '请输入密码', trigger: 'blur'},
      {validator: proxy.Verify.password, message: "密码只能是数字，字母，特殊字符8-18位"}
  ],
  checkcode: [{required: true, message: '请输入正确的图片验证码', trigger: 'blur'}],
  email: [
    {required: true, message: '请输入正确的邮箱', trigger: 'blur'},
    {validator: proxy.Verify.email, message: '请输入正确的邮箱'}
  ],
  emailCode: [
      {required: true, message: '请输入收到的邮箱验证码', trigger: 'blur'},
  ],
  rePassword: [
      {required: true, message: '请确认新密码', trigger: 'blur'},
      {validator:checkRePassword,message: '两次密码不一致！'}
  ]
};


// 获取验证码
const checkCodeUrl = ref()
const checkCodeEmailUrl = ref()
const changeCheckCode = async (type) => {
  let result = await proxy.Request({
    url: proxy.Api.getCheckCode,
    params: {
      type: type
    },
    errorCallback: (res) => {
      proxy.Message.error(res.msg)
    }
  })
  if (!result) {
    return;
  }
  if (type === 0) {
    checkCodeUrl.value = result.data.checkCode
    localStorage.setItem("checkCodeKey", result.data.checkCodeKey)
  } else {
    checkCodeEmailUrl.value = result.data.checkCode
    console.log(checkCodeEmailUrl)
    localStorage.setItem("checkCodeEmailKey", result.data.checkCodeKey)
  }
}




//重置表单 且如果存在则或者去用户的登录信息
const restForm = () => {
  changeCheckCode(0)
  formDataRef.value.resetFields()
  formData.value = {}
  //登录
  if (currentCard.value === 1) {
    const cookieLoginInfo = proxy.VueCookies.get("loginInfo")
    if (cookieLoginInfo) {
      formData.value = cookieLoginInfo
    }
  }
}

onMounted(() => {
  reSetCancel(1)
})

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
//发送邮箱验证码
const formDataSendMailCode = ref({});
const formDataSendMailCodeRef = ref();

const popUp = () => {
  formDataRef.value.validateField("email",(valid)=>{
    if(!valid){
      return;
    }
    emailConfirmDialog.show = true;
    nextTick(()=>{
      changeCheckCode(1)
      formDataSendMailCodeRef.value.resetFields()
      formDataSendMailCode.value = {
        email:formData.value.email
      }
    })
  })
}

//验证邮箱验证码发送表单
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
         type:currentCard.value===3?0:1
       },
      errorCallback: (res) => {
         changeCheckCode(1)
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


//登录 
const loginAction = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
    let params = {}
    //将表单的数据复制过去
    Object.assign(params, formData.value)
    params.checkCodeKey = localStorage.getItem("checkCodeKey")
    let cookieLoginInfo = proxy.VueCookies.get("loginInfo")
    let cookiePassword = cookieLoginInfo == null ? null : cookieLoginInfo.password
    if(params.password !== cookiePassword){
      params.password = md5(params.password)
    }
    
    let result = await proxy.Request({
       url:proxy.Api.login,
       params:params,
      errorCallback: (res) => {
         proxy.Message.error(res.msg)
         changeCheckCode(0) 
      }
    })
    if(!result){
      return;
    }
    if (params.rememberMe) {
      const loginInfo = {
        username: params.username,
        password: params.password,
        rememberMe: params.rememberMe
      }
      proxy.VueCookies.set("loginInfo", loginInfo, "3d")
    }
    proxy.Message.success("登录成功！")
    //存储cookies
    proxy.VueCookies.set("userInfo", result.data, 0)
    proxy.VueCookies.set("token",result.data.token,0)
    //重定向到原始页面
    const redirectUrl = route.query.redirectUrl || "/main"
    await router.push(redirectUrl)
  })
  
}


const clearValidation = (field) => {  //重新聚焦输入框就清除提示信息
  formDataRef.value.clearValidate(field);
}

//注册功能
const submitRegister = async () => {
  formDataRef.value.validate(async (valid)=>{
    if(!valid){
      return
    }
    let result = await proxy.Request({
      url:proxy.Api.register,
      params:{
        email:formData.value.email,
        nickName:formData.value.nickName,
        emailCode:formData.value.emailCode,
        password:formData.value.password,
        checkCode:formData.value.checkCode,
        checkCodeKey:localStorage.getItem("checkCodeKey"),
      },
      errorCallback: (res) => {
        changeCheckCode(0)
        proxy.Message.error(res.msg)
      }
    })
    if(!result){
      return;
    }
    proxy.Message.success("注册成功！")
    reSetCancel(1)
  })
}


</script>


<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;

  display: flex;

  .bg {
    background-image: url("../assets/bg.png");
    //background-size: 800px;
    background-repeat: no-repeat;
    flex: 1;
    display: flex;
    background-size: cover;
    opacity: 0.8;
  }

  .left-part {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .right-part {
    border-radius: 5px;
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .login-card {
    width: 500px;
    height: 550px;
    min-width: 300px;
    max-width: 800px;
    min-height: 400px;
    max-height: 800px;
    padding: 10px;
    border-radius: 7px;
    justify-content: center;
    align-items: center;
    background: rgba(255, 255, 255, 0.82);
    box-shadow: 0 4px 20px rgba(19, 179, 77, 0.9);
  }

  .login-title {
    text-align: center;
    font-size: 40px;
    font-weight: bold;
    color: #333;
    text-shadow: 0 5px 5px rgba(0, 0, 0, 0.4);
  }

  .form_base {
    width: 100%;
    display: flex;

    .form_checkcode {
      margin-left: 5px;
      cursor: pointer;
    }
  }


  .input-button-group {
    display: flex;
    align-items: center;
  }

  .checkcodeButton {
    margin-left: 8px;
    margin-right: 8px;
  }

  .rememberMe {
    justify-content: center;
  }

  .logIn {

    width: 100%;
  }

  .fogotPassword {
    margin-right: 20px;
  }

  .regiseter {
    margin-left: 270px;
  }

  /* 进入动画：卡片从右侧滑入 */
  .slide-enter-active {
    transition: transform 0.5s ease, opacity 0.5s ease;
  }

  .slide-enter-from {
    transform: translateX(100%);
    opacity: 0;
  }

  .slide-enter-to {
    transform: translateX(0);
    opacity: 1;
  }

  /* 离开动画：卡片向左侧滑出 */
  .slide-leave-active {
    transition: transform 0.5s ease, opacity 0.5s ease;
  }

  .slide-leave-from {
    transform: translateX(0);
    opacity: 1;
  }

  .slide-leave-to {
    transform: translateX(-100%);
    opacity: 0;
  }


}
</style>