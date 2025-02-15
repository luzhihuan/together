<template>
  <div class="marginTop">
    <span class="font-title">
      综测服务
    </span>
    <el-divider class="divider marginTop"></el-divider>
    <div class="style-row">
      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="fileUpLoad"
        >
          <el-icon >
            <Management />
          </el-icon>
        </el-button>
        <span class="button-title">佐证资料上传</span>
      </el-container>
      
      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="fileUpLoad"
        >
        <el-icon>
          <TrendCharts />
        </el-icon>
        </el-button>
        <span class="button-title">往期综测成绩</span>
      </el-container> 

      <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="fileUpLoad"
        >
        <el-icon>
          <Checked />
        </el-icon>
        </el-button>
        <span class="button-title">综测成绩查询</span>
      </el-container>
    </div>
    <div class="marginTop">
    <span class="font-title">
      敬请期待
    </span>
    <el-divider class="divider"></el-divider>
    <el-container class="button-container">
        <el-button 
          class="style-button"
          @click="fileUpLoad"
        >
        <el-icon>
          <Cherry /> 
        </el-icon>
        </el-button>
        <span class="button-title">敬请期待</span>
      </el-container>
    </div>
  </div>
</template>

<script setup>

</script>

<style lang="scss" scoped>

//分界线样式
.divider{
  border-width: 3px;
  border-color: rgb(110, 87, 2);
  box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
}
//上下间距
.marginTop{
  margin-top: 30px;
}
//标题样式
.font-title{
    font-size: 30px;
    font-weight: bold;
    margin-top: 20px;
    color: #05a1f5;
    text-shadow: 0 5px 5px rgba(0,0,0, 0.3);
    margin-left: 30px;
}
//图标按钮样式
.style-button{
  border: 0px;
  font-size: 80px;
  width: 100px;
  height: 100px;
  color: #05a1f5;
  box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
  margin-bottom: 10px;
  //background-color: ;
  transition: transform 0.3s ease;
}
.style-button:hover{
  transform: scale(1.2);
}
.button-container{
  display: flex;
  flex-direction: column;
  margin-left: 40px;
  position: relative;
  height: 100vh;
  max-height: 140px;
  max-width: 140px;
  align-items: center;
}
.button-title{
  margin-top: 10px;
  text-align: center;
}

.style-row{
  display: flex;
  flex-direction: row;
}

</style>