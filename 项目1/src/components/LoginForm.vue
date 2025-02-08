

<template>
    <div class="login-form">
      <el-form :model="form" :rules="rules" ref="loginForm" label-width="80px">
       
        <el-form-item label="账号" prop="user_id">
          <el-input v-model="form.user_id" placeholder="请输入账号" @focus="clearValidation('user_id')"/>
        </el-form-item>
        
        <el-form-item label="密码" prop="password" >
          <el-input v-model="form.password" placeholder="请输入密码" show-password @focus="clearValidation('password')" />
        </el-form-item>

        
        <el-form-item>
          
          <el-button type="primary" @click="handleLogin">登录</el-button>
          <el-button type="text" @click="$emit('forgot-password')">忘记密码？</el-button>
        </el-form-item>
      
      </el-form>
    </div>
  </template>
  
  <script>
  import api from '../services/api';
  import { ref } from 'vue';
  import { ElMessage } from 'element-plus';
//  import { useRouter } from 'vue-router';
  
  export default {
    name: 'LoginForm',
    emits: ['login-success', 'forgot-password'],
    setup({ emit }) {
        const loginForm = ref(null);
        const form = ref ({
          user_id: '',
          password: '',
        });
        const rules =  {
          user_id: [{ required: true, message: '请输入账号', trigger: 'blur' }],
          password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        };
      
        const handleLogin = async () => {
          try {
            const valid = await loginForm.value.validate();
            if (valid) {
              const response = await api.post('/login', form.value);
              console.log('请求返回：',response);         //打印完整返回信息    #用于开发过程中，开发完成删除
            
              if(response && response.token){ 
                ElMessage.success("登陆成功!");
                localStorage.setItem("token",response.token); //保存token
                emit('login-success',response)
              }
              else{
                ElMessage.error('登录失败!服务器未返回token');     // #用于开发过程中，开发完成删除
              }
            }
          } catch (error) {
            console.error('登录失败:', error);
            ElMessage.error(error.response?.message || "登录失败!");
          }
        };

        const clearValidation = (field) => {     //重新聚焦就清除提示信息
          loginForm.value.clearValidate(field);
        };

        return{
          loginForm,
          form,
          rules,
          handleLogin,
          clearValidation,
        };
    },
  };
  </script>
  
  <style scoped>
    /* 强制密码框的小眼睛始终占位，避免界面跳动 */
    :deep(.el-input__suffix-inner) {
      width: 10px !important;
      visibility: visible !important;
      display: flex !important;
    }

    .login-form {
      max-width: 400px;
      margin: 0 auto;
    }
  </style>
  