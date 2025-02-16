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
                  <el-form :model="formData" :rules="rules" ref="formDataRef">
                      <el-form-item prop = "username" class="form_base">
                          <el-input 
                          v-model="formData.username" 
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
                          v-mode="formData.password" 
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
                      
                      <el-form-item prop="checkcode" class="form_base form_checkcode" >
                          <el-input 
                          v-model="formData.checkcode" 
                          placeholder="请输入图片验证码" 
                          v-model.trim="formData.checkcode"
                          @focus="clearValidation('checkcode')"
                          >
                              <template #prefix>
                                <span class="iconfont icon-checkCode"></span>
                              </template>
                          </el-input>
                          <img :src="checkCodeUrl"  alt="" @click="changeCheckCode(0)"/>
                      </el-form-item>

                      <div class="form_base">
                          <el-checkbox class="rememberMe" v-model="formData.rememberMe">记住我</el-checkbox>
                      </div>
                      <div class="form_base">
                          <el-button type="text" class="fogotPassword" @click="reSetCancel">忘记密码?</el-button>
                          <el-button type="text" class="fogotPassword" @click="">没有账号?</el-button>
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
                <el-form :model="resetForm" ref="resetFormRef" :rules="rules">
                  <el-form-item prop="email" class="form_base">
                    <el-input
                      v-model="emailConfirmDialog.email"
                      placeholder="请输入邮箱"
                      v-model-trim = "emailConfirmDialog.email"
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
                        v-model="emailConfirmDialog.emailCode"
                        placeholder="请输入收到的验证码"
                        v-model-trim = "emailConfirmDialog.emailCode"
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
                        v-mode="resetForm.firstPassword" 
                        placeholder="请输入新密码" 
                        v-model.trim="resetForm.firstPassword"
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
                      v-mode="resetForm.rePassword" 
                      placeholder="请确认新密码" 
                      v-model.trim="resetForm.rePassword"
                      show-password
                      @focus="clearValidation('rePassword')"
                      >
                        <template #prefix>
                            <span class="iconfont icon-password"></span>
                        </template>
                      </el-input>
                  </el-form-item>

                  <el-form-item prop="reSetFormCheckcode">
                    <el-input
                      v-model="emailConfirmDialog.checkcode"
                      placeholder="请输入图片验证码"
                      v-model-trim = "emailConfirmDialog.checkcode"
                      @focus="clearValidation('checkcode')"
                      class="form_checkcode"
                    >
                    <template #prefix>
                      <span class="iconfont icon-checkCode "></span>
                    </template>
                    </el-input>
                  </el-form-item>

                  <el-form-item class="form_base button">
                    <el-button type="primary" @click="reSetPassword">重置</el-button>
                    <el-button class="cancel" @click="reSetCancel">取消</el-button>
                  </el-form-item> 
                                 
                </el-form>
              </el-card>
            </div>
          </transition>

        </div>
        <!-- 弹窗 -->
        <Dialog
          :show = "emailConfirmDialog.show"
          :title = "emailConfirmDialog.title"
          :buttons = "emailConfirmDialog.buttons"
          width = "500px"
          :showCancel = "false"
          @close = "emailConfirmDialog.show=false"
        >
          <el-form :model="emailConfirmDialog" :rules="rules" ref="emailConfirmDialogRef">
            <el-form-item prop="email">
              <el-input
                v-model="emailConfirmDialog.email"
                placeholder="请输入邮箱"
                v-model-trim = "emailConfirmDialog.email"
                @focus="clearValidation('email')"
              >
              <template #prefix>
                <span class="iconfont icon-user "></span>
              </template>
              </el-input>
              <img :src="checkCodeEmailUrl"  alt="" @click="changeCheckCode(1)"/>
            </el-form-item>  
          
            <el-form-item prop="checkcode">
              <el-input
                v-model="emailConfirmDialog.checkcode"
                placeholder="请输入图片验证码"
                v-model-trim = "emailConfirmDialog.checkcode"
                @focus="clearValidation('checkcode')"
                class="form_checkcode"
              >
              <template #prefix>
                <span class="iconfont icon-checkCode "></span>
              </template>
              </el-input>
            </el-form-item>

          </el-form>
        </Dialog>
                    

    </div>
  </div>


</template>


<script setup>
import message from '../utils/Message';
import { ref, reactive, getCurrentInstance, nextTick, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute, useRouter } from 'vue-router';
import { md5 } from "js-md5";
// import FrameworkVue from './Framework.vue';
  //路由初始化
  const route = useRoute()
  const router = useRouter()
  //重置变量初始化
  const emailConfirmDialog = reactive({
    show: false,
    title: "人机验证",
    buttons: [{
      type: "primary",
      text: "发送验证码",
      click: (e) =>{
        sendEmailCode();
      },
    },
    {
      type: "primary",
      text: "取消",
      class: "checkcodeButton",
      click: (e) =>{
        emailConfirmDialog.show = false
      },
    }
    ]
  })
  const emailConfirmDialogRef = ref();
  const resetForm = ref({});
  const resetFormRef = ref();
  //弹窗
  const popUp = async() =>{
    emailConfirmDialog.show = true;
  }
  //界面切换动画初始化
  const currentCard = ref(1)
  const reSetCancel  = () =>{
    currentCard.value = currentCard.value === 1 ? 2 : 1;
  }
  //重置密码表单验证
  const reSetPassword = async () =>{
    resetFormRef.value.validate (async (valid) =>{
      if(!valid){
        return;
      }
    })
  };
  

  const { proxy } = getCurrentInstance()

  const rules = {
      username:[{required: true, message:'请输入账号', trigger:'blur'}],
      password:[{required: true, message:'请输入密码', trigger:'blur'}],
      checkcode:[{required: true, message:'请输入图片验证码', trigger:'blur'}],
      email:[{required: true, message:'请输入邮箱', trigger:'blur'}],
      emailCode:[{required: true, message:'请输入收到的邮箱验证码', trigger:'blur'}],
      firstPassword:[{required: true, message:'请输入新密码', trigger:'blur'}],
      rePassword:[{required: true, message:'请确认新密码', trigger:'blur'}],
      reSetFormCheckcode:[{required: true, message:'请输入图片验证码', trigger:'blur'}]
  };

  const formData = ref({});
  const formDataRef = ref(); 

  let url = null; //url初始化

  //获取验证码
  // const checkCodeUrl = ref()
  // const checkCodeEmailUrl = ref()
  // const changeCheckCode = async (type) => {
  //   let result = await proxy.Request({
  //     url: Proxy.Api.checkCode,
  //     params:{
  //       type: type
  //     },
  //     errorCallback: (res) =>{
  //       proxy.Message.error(res.msg)
  //     }
  //   })
  //   if(!result){
  //     return;
  //   }
  //   if(type === 0){
  //     checkCodeUrl.value = result.data.checkCode
  //     localStorage.setItem("checkCodeKey", result.data.checkCodeKey)
  //   }else{
  //     checkCodeUrl.value = result.data.checkCode
  //     localStorage.setItem("checkCodeEmailKey", result.data.checkCodeKey)
  //   }
  // }
  // changeCheckCode(0)

    //验证邮箱验证码发送表单
    const sendEmailCode = async() =>{
      emailConfirmDialogRef.value.validate(async (valid) => {
        if(!valid){
        return;
      }
      });
    };

    //重置表单  后续需要修改为只重置验证码保留用户输入的内容
    const restForm = () =>{
      changeCheckCode(0)
      formDataRef.value.resetFields()
    }
   
    //登录 失败则返回 验证登录表单
    const loginAction = async() => {
      formDataRef.value.validate(async (valid) => {
        if(!valid){
          return;
        }
      });

      let params = {}
      Object.assign(params,formData.value)
      //是否记住我
      if(params.rememberMe){
        const loginInfo = {
          username: params.username,
          password: params.password,
        }
        proxy.VueCookie.set("loginInfo", loginInfo, "7d")
      }else{
        proxy.VueCookie.remove("loginInfo")
      }
      
      //使用cookie
      // const cookieLoginInfo = proxy.VueCookie.get("loginInfo")
      // if(cookieLoginInfo){
      //   formData.value = cookieLoginInfo;
      // }
        //params.checkCodeKey = localStorage.getItem("checkCodeKey") //从本地存储中获取checkCode密钥添加到请求参数中
        
        //存储cookies
        //proxy.VueCookie.set("UserInfo", result.data,0)
        //proxy.VueCookie.set("token",result.data.token,0)

        await router.push({name:'首页'})
    }


    const clearValidation = (field) =>{  //重新聚焦输入框就清除提示信息
      formDataRef.value.clearValidate(field);
      emailConfirmDialogRef.value.clearValidate(field);
    }

</script>



<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;

  display: flex;
  .bg{
    background-image: url("../assets/bg.png");
    //background-size: 800px;
    background-repeat: no-repeat;
    flex: 1;
    display: flex;
    background-size: cover;
    opacity: 0.8;
  }

  .left-part{
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .right-part{
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .login-card{
    width: 500px;
    height: 550px;
    min-width: 300px;
    max-width: 800px;
    min-height: 400px;
    max-height: 800px;
    padding :10px;
    border-radius: 7px;
    justify-content: center;
    align-items: center;
    background-color: aquamarine;
    box-shadow: 0 4px 20px rgba(19, 179, 77, 0.9);
  }

  .login-title{
    text-align: center;
    font-size: 40px;
    font-weight: bold;
    margin-top: 20px;
    color: #333;
    text-shadow: 0 5px 5px rgba(0,0,0, 0.4);
  }

  .form_base{
    margin-top: 30px;
  }

  .form_checkcode{
    width: 300px;
    
  }
  
  .input-button-group{
    display: flex;
    align-items: center;
  }

  .checkcodeButton{
    margin-left: 8px;
    margin-right: 8px;
  }

  .rememberMe{
    justify-content: center;
  }

  .logIn{
    
    width: 100%;
  }

  .fogotPassword{
    margin-right: 20px;
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