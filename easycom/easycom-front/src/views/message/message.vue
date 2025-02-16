<template>
  <div class="message-body"> 
    <!-- <div class="oneThirdPart">
       <el-container class="button-container"> 
        <el-button class="style-button" >
          <template #icon>
            <el-icon><MessageBox /></el-icon>
            
          </template>
          <span class="style-font">收件箱</span>
        </el-button>
      
      </el-container>     
    </div>
    <div class="oneThirdPart">
       <el-container class="button-container"> 
        <el-button class="style-button" >
        <template #icon>
          <el-icon><Message /></el-icon>
        </template>
          <span class="style-font">写信件</span>       
        </el-button>
      </el-container>
    </div>
    <div class="oneThirdPart">
      <el-container class="button-container">
       
        <el-button class="style-button" >
        <template #icon>
          <el-icon><Edit /></el-icon>
        </template>
        <span class="style-font">草稿箱</span>
        </el-button>
      </el-container>
    </div> -->

    
  </div>
</template>

<script setup>

</script>

<style lang="scss" scoped>

.message-body{
  display: flex;
  overflow: auto;

  //每个小部分的样式
  .oneThirdPart{
    flex: 1;
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0;
    padding: 0;
  }

  //按钮容器及按钮样式
   .button-container{
     display: flex;
     justify-content: center;
     align-items: center;
     height: 100%;
     flex-direction: row;
   }

  .style-button{
    display: flex;
    border: 0px;
    font-size: 100px;
    width: 250px;
    height: 400px;
    color: #05a1f5;
    box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
    
    flex-direction: column;
    transition: transform 0.3s ease;
    
  }
  .style-button:hover{
  transform: scale(1.2);
  }

  .style-font{
   
    margin-top: 10px;
    font-size: 30px;
    
  }

  
}
</style>