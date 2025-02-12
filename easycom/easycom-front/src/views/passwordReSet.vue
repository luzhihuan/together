<template>
    <div class="bg">
        <el-card class="login-card">
            <el-form>
                <el-form-item>
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
            </el-form>
        </el-card>
    </div>
</template>


<style lang="scss" scoped>
.login-body {
  height: calc(100vh);
  background-size: cover;

  display: flex;
  .bg{
    background:linear-gradient(to right, #4ad124, #036920);
    flex: 1;
    display: flex;
    background-size: cover;
    opacity: 0.8;
  }


  .login-card{
    width: 500px;
    height: 700px;
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

  .rememberMe{
    justify-content: center;
  }

  .logIn{
    margin-left: 80px;
  }

  .fogotPassword{
    margin-left: 20px;
  }

}
</style>