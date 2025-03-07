<template>
    <div class="style-body">
        <el-container class="style-container">
            <h2 class="font-title">德育资料审核</h2>
            <div class="demo-image__custom-toolbar">
                <el-image
                    style="width: 150px; height: 150px; margin-left: 20px;"
                    :src="url"
                    :preview-src-list="imageList"
                    fit="cover"
                    v-for="item in imageList"
                    :key="item"
                >
                    <template #toolbar="{ actions, reset, activeIndex }">
                        <el-icon @click="actions('zoomOut')"><ZoomOut /></el-icon>
                        <el-icon
                            @click="actions('zoomIn', { enableTransition: false, zoomRate: 2 })"
                        >
                        <ZoomIn />
                        </el-icon>
                        <el-icon
                            @click="
                                actions('clockwise', { rotateDeg: 180, enableTransition: false })
                            "
                        >
                            <RefreshRight />
                        </el-icon>
                        <el-icon @click="actions('anticlockwise')"><RefreshLeft /></el-icon>
                        <el-icon @click="reset"><Refresh /></el-icon>
                        <el-icon @click="download(activeIndex)"><Download /></el-icon>
                    </template>
                </el-image>
            </div>
            <div>
                <el-link
                    type="primary"
                    v-for="item in fileList"
                    :key="item"
                    class="style-fileLink"
                    @click="downloadFile(item.url, item.name + '.' + item.type)"
                >
                    {{ item.name + '.' + item.type }} 
                </el-link>
            </div>
            <el-radio-group v-model="radio">
                <el-radio :value="0">审核通过</el-radio>
                <el-radio :value="1">审核不通过</el-radio>
            </el-radio-group>
            <el-form class="style-form">
                <el-input 
                    class="style-input"
                    maxlength="400"
                    show-word-limit
                    placeholder="请输入评语或修改建议"
                    type="textarea"
                    :rows="5"
                >
                </el-input>
                <!-- <el-button></el-button> -->
            </el-form>
            <el-divider class="style-divider"/>
        </el-container>
        <el-container class="style-container">
            <h2 class="font-title">智育资料审核</h2>
            <el-divider class="style-divider"/>
        </el-container>
        <el-container class="style-container">
            <h2 class="font-title">体育资料审核</h2>
            <el-divider class="style-divider"/>
        </el-container>
        <el-container class="style-container">
            <h2 class="font-title">美育资料审核</h2>
            <el-divider class="style-divider"/>
        </el-container>
        <el-container class="style-container">
            <h2 class="font-title">劳育资料审核</h2>
            <el-divider class="style-divider"/>
        </el-container>

        <el-affix position="bottom">
            <el-container class="style-informationContainer">
                <div 
                    style=
                    "height: 100%; 
                    display: flex;
                    align-items: center;
                    margin-left: 10px;"             
                >
                    <el-avatar  :size="100">
                        <el-icon :size="75" style="margin: auto;">
                            <Avatar />
                        </el-icon>
                    </el-avatar>
                </div>
                <div style="margin-left: 30px;">
                    <h3>姓名</h3>
                    <h3>学号</h3>
                    <h3>所属学院</h3>
                </div>
            </el-container>
        </el-affix>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import imageUrl from '@/assets/bg.png';

const url = imageUrl;

const imageList = [
    imageUrl,
    imageUrl,
    imageUrl,
    imageUrl,
]

const radio = ref(2)

const fileList = ref([
    {
        name:'综测项目开发方案',
        type:'docx',
        url:'@/assets/综测项目开发方案.docx',
    },
    {
        name:'奖学金名单',
        type:'xlsx',
        url:'@/assets/奖学金名单.xlsx',
    }
])

//为链接提供下载接口
const downloadFile = (url, name) => {
    if (!url) {
    console.error("文件 URL 为空");
    return;
    }

    const link = document.createElement("a");
    link.href = url;
    link.download = name || "download"; // 防止 name 为空
    link.style.display = "none";

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
};

//为图片提供下载接口
const download = (index) => {
    const url = imageList[index]
    const suffix = url.slice(url.lastIndexOf('.'))
    const filename = Date.now() + suffix

    fetch(url)
    .then((response) => response.blob())
    .then((blob) => {
        const blobUrl = URL.createObjectURL(new Blob([blob]))
        const link = document.createElement('a')
        link.href = blobUrl
        link.download = filename
        document.body.appendChild(link)
        link.click()
        URL.revokeObjectURL(blobUrl)
        link.remove()
    })
}
</script>

<style lang="scss" scoped>

.style-body{
    display: flex;
    overflow: auto;
    width: 100%;
    height: 100%;
    margin: none;
    border: none;
    flex-direction: column;
}

.style-container{
    display: flex;
    width: 100%;
    height: 60vh;
    // background-color: lightpink;
    position: relative;
    flex-direction: column;
    align-items: center;
    margin: 0;
}

.style-divider{
    width: 90%;
    border-width: 5px;
    color: lightblue;
    position: absolute;
    bottom: 0px;
}

.style-informationContainer{
    display: flex;
    flex-direction: row;
    width: 100%;
    height: 15vh;
    background-color: rgb(240,240,240);
    border-radius: 8px;
    transition: all 0.3s;
    opacity: 0.6;
    &:hover{
        box-shadow: 0 0 4px 4px rgba(0,0,0, $alpha: 0.1);
    }
}

.font-title{
    margin-top: 0px;
    // writing-mode: vertical-rl;
}

.style-form{
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 30%;
    position: absolute;
    bottom: 30px;

}

.style-input{
    width: 60%;
    height: 100%;
}

.style-fileLink{
    font-size: 20px;
    margin: 20px;
}

</style>