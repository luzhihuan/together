<template>
    <div class="style-body">
        <el-divider
            direction="vertical"
            class="style-divider"
        />
        <el-card
            class="style-container"
        >
            <el-card
                class="style-taskCard"
                v-for="item in taskList"
                :key="item.id"
                @click="openSingleTask(item.id)"
            >   
                <div>
                    <span class="font-name">{{ item.name }}</span> 
                    <span class="font-id">{{ item.id }}</span>
                </div>
                <p class="font-content">{{ item.content }}</p>
            </el-card>
        </el-card>
        <el-divider
            direction="vertical"
            class="style-divider"
        />
        
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()

//后续逻辑转化为获取数据库中的审核资料数据
const taskList = ref([
    {
        name: '姓名1',
        id: 'id1',
        content: '内容1',
    },
    {
        name: '姓名2',
        id: 'id2',
        content: '内容2',
    },
    {
        name: '姓名3',
        id: 'id3',
        content: '内容3',
    },
    {
        name: '姓名4',
        id: 'id4',
        content: '内容4',
    },
    {
        name: '姓名5',
        id: 'id5',
        content: '内容5',
    },
    {
        name: '姓名6',
        id: 'id6',
        content: '内容6',
    },
    {
        name: '姓名7',
        id: 'id7',
        content: '内容7',
    },
])

const openSingleTask = (item) =>{
    const route = router.resolve({name:"单个任务"})
    window.open(route.href, '_blank')
}

</script>

<style lang="scss" scoped>

.style-body{
    display: flex;
    flex-direction: row;
    overflow: hidden;
    justify-content: center;
    height: 100vh;
}

.style-divider{
    border-width: 5px;
    color: lightblue;
    // margin: 20px;
    margin-left: 40px;
    margin-right: 40px;
    height: 100vh;
}

.style-container{
    display: flex;
    flex-direction: column;
    overflow: auto;
    // align-items: center;
    // height: 100vh;
    width: 75vw;
    margin: 20px;
    background-color: rgb(248, 248, 248);
    transition: all 0.3s;
    border-radius: 8px;
    &:hover{
        box-shadow: 0 0 5px 5px rgba($color: #706e6e, $alpha: 0.3); 
    }
}

.style-taskCard{
    display: flex;
    width: 90%;
    height: 100px;
    background-color: rgb(252, 252, 252);
    margin-top: 20px;
    max-width: none;
    min-width: 0;
    margin:auto;
    margin-top: 20px;
    border-radius: 8px;
    transition: all 0.5s;

    &:hover{
        transform: scale(1.04);
        box-shadow: 0 0 5px 5px rgba($color: #d4d3d3, $alpha: 0.4);
    }
}

.font-name{
    font-size: 20px;
    margin: 0;
}

.font-id{
    margin-left: 30px;
}

.font-content{
    font-size: 14px; 
}
</style>