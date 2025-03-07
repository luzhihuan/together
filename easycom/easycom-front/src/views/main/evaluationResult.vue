<template>
    <div class="body">
        <span class="font-title">各项目评定结果</span>
        <el-divider class="divider"></el-divider>
        <div class="style-center"
          v-for="item in evaluationResultList"
          :key="item.value"
        >
          <el-container
            class="style-container"
          >
            <el-title class="font-title">{{ item.title }}</el-title>
          </el-container>
          <el-container
            class="style-container-secondary"
          >
            <el-title class="font-title">审核员评语</el-title>
          </el-container>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

//假设有以下项目的评定数据
const evaluationResultList = [
  {
    title: '德育成绩评定结果',
    value: '1',
  },
  {
    title: '智育成绩评定结果',
    value: '2',
  },
  {
    title: '体育成绩评定结果',
    value: '3',
  },
  {
    title: '美育成绩评定结果',
    value: '4',
  },
  {
    title: '劳育成绩评定结果',
    value: '5',
  },
]

//获取数据库中评定的数据
const getEvaluationResult = async () =>{

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
  box-shadow: 0 5px 15px rgba(0,0,0, 0.3);
  border-radius: 5px;
  background-color: rgb(241, 242, 241);
}
.style-container-secondary{
  height: 500px;
  margin: 20px;
  box-shadow: 0 5px 15px rgba(0,0,0, 0.3);
  border-radius: 5px;
  background-color: rgb(241, 242, 241);
}
</style>