<template>
  <div class="login-body">
    <div class="bg"></div>
    <div class="login-panel">

      <!--      登录界面的各个输入框-->
      <el-form
          class="login-register"
          :model="formData"
          :rules="rules"
          ref="formDataRef"
          @submit.prevent
      >
        <!--        标题-->
        <div class="login-title">Easy云盘</div>

        <!--        输入邮箱-->
        <el-form-item prop="email">
          <el-input
              size="large"
              clearable
              placeholder="请输入邮箱"
              v-model.trim="formData.email"
              maxlength="150"
          >
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>

        <!--        发送邮箱验证码-->
        <div v-if="opType===0||opType===2">
          <el-form-item prop="emailCode">
            <div class="send-email-panel">
              <el-input
                  size="large"
                  clearable
                  placeholder="请输入邮箱验证码"
                  v-model.trim="formData.emailCode"
                  maxlength="5"
              >
                <template #prefix>
                  <span class="iconfont icon-checkcode"></span>
                </template>
              </el-input>
              <el-button
                  class="send-email-btn"
                  type="primary"
                  size="large"
                  @click="getEmailCode"
              >
                获取验证码
              </el-button>
            </div>
            <el-popover placement="left" :width="500" trigger="click">
              <div>
                <p>1、检查垃圾箱中是否有邮箱验证码</p>
                <p>2、在邮箱中，设置邮件地址白名单</p>
                <p>3、将找到的邮箱添加到白名单</p>
              </div>
              <template #reference>
                <span class="a-link" :style="{ 'font-size': '14px' }">
                  未收到邮箱验证码？
                </span>
              </template>
            </el-popover>
          </el-form-item>
        </div>

        <!--        输入昵称-->
        <div v-if="opType===0">
          <el-form-item prop="nickName">
            <el-input
                size="large"
                placeholder="请输入昵称"
                v-model.trim="formData.nickName"
                maxlength="30"
            >
              <template #prefix>
                <span class="iconfont icon-account"></span>
              </template>
            </el-input>
          </el-form-item>
        </div>

        <!--        密码-->
        <el-form-item prop="password">
          <el-input
              size="large"
              placeholder="请输入密码"
              v-model.trim="formData.password"
              maxlength="20"
              show-password
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>

        <!--        再次密码-->
        <div v-if="opType===0||opType===2">
          <el-form-item prop="rePassword">
            <el-input
                size="large"
                placeholder="请再次输入密码"
                v-model.trim="formData.rePassword"
                maxlength="20"
                show-password
            >
              <template #prefix>
                <span class="iconfont icon-password"></span>
              </template>
            </el-input>
          </el-form-item>
        </div>

        <!--        验证码-->
        <el-form-item prop="checkCode">
          <div class="check-code-panel">
            <el-input
                size="large"
                placeholder="请输入验证码"
                v-model.trim="formData.checkCode"
                maxlength="5"
                @keyup.enter="doSubmit"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img :src="checkCodeUrl" class="check-code" alt="" @click="changeCheckCode(0)"/>
          </div>
        </el-form-item>

        <!--        记住我？-->
        <el-form-item>
          <div class="rememberMe-panel">
            <el-checkbox v-if="opType===1" v-model="formData.rememberMe">记住我</el-checkbox>
          </div>

          <!--          其他功能-->
          <div class="no-account">
            <a href="javascript:void(0)" class="a-link" @click="showPanel(2)" v-if="opType===1">忘记密码？</a>
            <a href="javascript:void(0)" class="a-link" @click="showPanel(1)" v-if="opType===0||opType===2">返回登录</a>
            <a href="javascript:void(0)" class="a-link" @click="showPanel(0)" v-if="opType===1">没有账号？</a>
          </div>
        </el-form-item>

        <!--        切换操作时，底部按钮也跟随变化-->
        <el-form-item>
          <el-button class="op-btn" type="primary" @click="doSubmit">
            <span v-if="opType===0">注册</span>
            <span v-if="opType===1">登录</span>
            <span v-if="opType===2">重置密码</span>
          </el-button>
        </el-form-item>

        <div class="login-btn-qq" v-if="opType===1">
          <img src="../assets/qq.png" alt="?" @click="qqLogin">
          <span style="cursor: pointer" @click="qqLogin">QQ快捷登录</span>
        </div>
      </el-form>
    </div>

    <!--    弹窗设置-->
    <Dialog
        :show="dialogConfigSendMailCode.show"
        :title="dialogConfigSendMailCode.title"
        :buttons="dialogConfigSendMailCode.buttons"
        width="500px"
        :showCancel="false"
        @close="dialogConfigSendMailCode.show=false">
      <el-form
          :model="formDataSendMailCode"
          :rules="rules"
          ref="formDataSendMailCodeRef"
          label-width="80px"
          @submit.prevent
      >
        <el-form-item label="邮箱">
          {{ formData.email }}
        </el-form-item>

        <el-form-item label="验证码" prop="checkCode">
          <div class="check-code-panel">
            <el-input
                size="large"
                placeholder="请输入验证码"
                v-model.trim="formDataSendMailCode.checkCode"
                maxlength="5"
            >
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img :src="checkCodeEmailUrl" class="check-code" alt="" @click="changeCheckCode(1)"/>
          </div>
        </el-form-item>

      </el-form>
    </Dialog>
  </div>


</template>

<script setup>
//定义响应式对象，获取当前实例等
import {ref, reactive, getCurrentInstance, nextTick, onMounted} from 'vue';

const {proxy} = getCurrentInstance()

import {md5} from "js-md5";
import {useRouter, useRoute} from "vue-router";

const router = useRouter()
const route = useRoute()


//登陆或注册表单数据
const formData = ref({});
const formDataRef = ref();
const checkRePassword = (rule, value, callback) => {
  if (value !== formData.value.password) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}

//定义校验规则
const rules = {
  email: [
    {required: true, message: "请输入正确邮箱"},
    {validator: proxy.Verify.email, message: "请输入正确邮箱"},
  ],
  emailCode: [
    {required: true, message: "请输入邮箱验证码"},
  ],
  nickName: [
    {required: true, message: "请输入昵称"},
  ],
  password: [
    {required: true, message: "请输入密码"},
    {validator: proxy.Verify.password, message: "密码只能是数字，字母，特殊字符8-18位"},
  ],
  rePassword: [
    {required: true, message: "请再次输入密码"},
    {validator: checkRePassword, message: "两次输入的密码不一致"},
  ],
  checkCode: [
    {required: true, message: "请输入图片验证码"},
  ],
};


//获取验证码
const checkCodeUrl = ref()
const checkCodeEmailUrl = ref()
const changeCheckCode = async (type) => {
  let result = await proxy.Request({
    url: proxy.Api.checkCode,
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
    localStorage.setItem("checkCodeEmailKey", result.data.checkCodeKey)
  }
}
changeCheckCode(0)


//操作类型 0：注册 1：登录 2：重置密码
const opType = ref(1)
const showPanel = (type) => {
  opType.value = type
  restForm()
}

onMounted(()=>{
  showPanel(1)
})

//发送邮箱验证码
const formDataSendMailCode = ref({});
const formDataSendMailCodeRef = ref();

//弹出窗口的设置
const dialogConfigSendMailCode = reactive({
  show: false,
  title: "发送邮箱验证码",
  buttons: [
    {
      type: "primary",
      text: "发送验证码",
      click: (e) => {
        sendEmailCode();
      },
    },
  ]
})

//显示邮箱验证码获取的弹出窗口
const getEmailCode = () => {
  formDataRef.value.validateField("email", (valid) => {
    if (!valid) {
      return;
    }
    dialogConfigSendMailCode.show = true;
    nextTick(() => {
      changeCheckCode(1)
      formDataSendMailCodeRef.value.resetFields();
      formDataSendMailCode.value = {
        email: formData.value.email
      };
    })
  });
}

//发送邮箱验证码
const sendEmailCode = async () => {
  formDataSendMailCodeRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
  })
  let result = await proxy.Request({
    url: proxy.Api.sendEmailCode,
    params: {
      checkCodeKey: localStorage.getItem("checkCodeEmailKey"),
      email: formDataSendMailCode.value.email,
      checkCode: formDataSendMailCode.value.checkCode,
      type: opType.value === 0 ? 0 : 1
    },
    errorCallback: (res) => {
      changeCheckCode(1)
      proxy.Message.error(res.msg)
    }
  })
  if (!result) {
    return;
  }
  proxy.Message.success("发送成功！请登录邮箱查询！")
  dialogConfigSendMailCode.show = false;
}


//重置表单
const restForm = () => {
  changeCheckCode(0)
  formDataRef.value.resetFields()
  formData.value = {}
  //登录
  if (opType.value === 1) {
    const cookieLoginInfo = proxy.VueCookie.get("loginInfo")
    if(cookieLoginInfo){
      formData.value = cookieLoginInfo
    }
  }

}

//登录？注册？重置？提交信息按钮
const doSubmit = async () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return;
    }
  });
  let params = {}
  Object.assign(params, formData.value)
  params.checkCodeKey = localStorage.getItem("checkCodeKey")
  if(opType.value === 1){
    let cookieLoginInfo = proxy.VueCookie.get("loginInfo");
    let cookiePassword = cookieLoginInfo==null?null:cookieLoginInfo.password
    if(params.password!==cookiePassword){
      params.password = md5(params.password)
    }
  }

  let url = null
  //0 注册  1 登录  2 重置密码
  if (opType.value == 0) {
    url = proxy.Api.register
  } else if (opType.value == 1) {
    url = proxy.Api.login
  } else if (opType.value == 2) {
    url = proxy.Api.resetPassword
  }
  let result = await proxy.Request({
    url: url,
    params: params,
    errorCallback: (res) => {
      proxy.Message.error(res.msg)
      changeCheckCode(0)
    }
  })
  if (!result) {
    proxy.Message.error(result.msg)
    return;
  }

  if (opType.value == 0) {
    proxy.Message.success("注册成功！")
    showPanel(1)
  } else if (opType.value == 1) {
    if (params.rememberMe) {
      const loginInfo = {
        email: params.email,
        password: params.password,
        rememberMe: params.rememberMe
      };
      proxy.VueCookie.set("loginInfo", loginInfo, "3d")
    } else {
      proxy.VueCookie.remove("loginInfo")
    }
    proxy.Message.success("登录成功！")

    //存储cookies
    proxy.VueCookie.set("userInfo", result.data, 0)
    proxy.VueCookie.set("token",result.data.token,0)

    //重定向到原始页面
    const redirectUrl = route.query.redirectUrl || "/"
    await router.push(redirectUrl)

  } else if (opType.value === 2) {
    proxy.Message.success("重置成功！")
    showPanel(1)

  }
}

</script>







<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;
  background: #28ccd8;
  display: flex;

  .bg {
    flex: 1;
    background-size: cover;
    background-position: center;
    background-size: 500px;
    background-repeat: no-repeat;
    background-image: url("../assets/bg_icon.png");
  }

  .login-panel {
    width: 430px;
    margin-right: 15%;
    margin-top: calc((100vh - 500px) / 2);

    .login-register {
      padding: 25px;
      background: #fff;
      border-radius: 5px;

      .login-title {
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 20px;
      }

      .send-email-panel {
        display: flex;
        width: 100%;
        justify-content: space-between;

        .send-email-btn {
          margin-left: 5px;
        }
      }

      .rememberMe-panel {
        width: 100%;
      }

      .no-account {
        width: 100%;
        display: flex;
        justify-content: space-between;
      }

      .op-btn {
        width: 100%;
      }
    }
  }

  .check-code-panel {
    width: 100%;
    display: flex;

    .check-code {
      margin-left: 5px;
      cursor: pointer;
    }
  }

  .login-btn-qq {
    margin-top: 20px;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;

    img {
      cursor: pointer;
      margin-left: 10px;
      width: 20px;
    }
  }
}

</style>
