<template>
  <Dialog 
    :show="show"
    :title="title"
    :buttons="buttons"
    :width="width"
    :show-cancel="showCancel"
    @close=""
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
        <el-input clearable placeholder="请输入原邮箱" v-model.trim="formData.email">
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
          <el-button type="primary">发送验证码</el-button>
        </div>
      </el-form-item>

    </el-form>
  </Dialog>
  
  <Dialog
  
  
</template>

<script setup>
import Dialog from "@/components/Dialog.vue";
import { ref, reactive, getCurrentInstance, nextTick } from "vue"
const { proxy } = getCurrentInstance();

const formData = ref({});
const formDataRef = ref();

const checkRePassword = (rule,value,callback) =>{
  if(value !== formData.value.password){
    callback(new Error(rule.message))
  }else {
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
    {validator:checkRePassword,message: '两次密码不一致！'}
  ]
};


const props = defineProps({
  show:{
    type:Boolean,
    default:false,
  },
  title:{
    type:String,
    default:"绑定邮箱并修改密码"
  },
  width:{
    type:String,
    default:'500px'
  },
  showCancel:{
    type:Boolean,
    default:true
  },
  changeType:{
    type:Number,
    default:1
  },
  buttons:{
    type:Array,
  }
})


const 



</script>

<style lang="scss" scoped>

.sendEmail{
  display: flex;
}

</style>