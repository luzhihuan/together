<template>
    <div class="body">
        <span class="font-title">往期综测成绩查询</span>
        <el-divider class="divider"></el-divider>
        <div class="style-center"
          v-for="item in prePerformanceList"
          :key="item.value"
        >
          <el-container
            class="style-container"
            @click="openDetails"
          >
            <el-title class="font-title">{{ item.title }}</el-title>
          </el-container>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

//假设有以下年份的数据
const prePerformanceList = [
  {
    title: '2022-2023年度综测评定结果',
    value: '2223',
  },
  {
    title: '2023-2024年度综测评定结果',
    value: '2324',
  },
  {
    title: '2024-2025年度综测评定结果',
    value: '2425',
  },
]

//获取数据库中往年的综测数据
const getPrePerformance = async () =>{

}


</script>

<style lang="scss" scoped>
//整体样式
.body{
  overflow: auto;
  display: flex;
  flex-direction: column;
}
//居中
.style-center{
  width: 100%;

  display: flex;
  justify-content: center;
  align-items: center;
}
//分界线样式
.divider{
  border-width: 3px;
  border-color: rgb(110, 87, 2);
  box-shadow: 0 5px 5px rgba(0,0,0, 0.3);
}
//标题样式
.font-title{
  font-size: 30px;
  font-weight: bold;
  margin-top: 20px;
  margin-left: 30px;
  color: #05a1f5;
  text-shadow: 0 5px 5px rgba(0,0,0, 0.3);
}
//容器样式
.style-container{
  margin: 20px;
  height: 600px;
  box-shadow: 0 0 3px 1px rgba(0,0,0, 0.3);
  border-radius: 5px;
  background-color: rgb(241, 242, 241);
  transition: all 0.3s;

  &:hover{
    box-shadow: 0 0 6px 3px rgba(0,0,0, 0.3);
  }
}
</style>