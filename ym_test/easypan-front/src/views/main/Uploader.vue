<template>
  <div class="uploader-panel">

    <div class="uploader-title">
      <span>上传任务</span>
      <span class="tips">（仅展示本次上传任务）</span>
    </div>

    <!--    遍历每一个加入的文件-->
    <div class="file-list">
      <div v-for="(item,index) in fileList" class="file-item">
        <div class="upload-panel">

          <!--          文件名-->
          <div class="file-name">
            {{ item.fileName }}
          </div>

          <!--          进度条-->
          <div class="progress">
            <el-progress
                :percentage="item.uploadProgress"
                v-if="item.status===STATUS.uploading.value ||
                    item.status===STATUS.upload_seconds.value ||
                    item.status===STATUS.upload_finish.value
              "
            ></el-progress>
          </div>
          <!--          进度条下的状态图标和字段-->
          <div class="upload-status">
            <span :class="['iconfont','icon-'+STATUS[item.status].icon]"
                  :style="{color:STATUS[item.status].color}"></span>
            <!--            状态描述-->
            <span class="status" :style="{color:STATUS[item.status].color}">
              {{ item.status === "fail" ? item.errorMsg : STATUS[item.status].desc }}
            </span>
            <!--            上传中显示的大小-->
            <span class="upload-info" v-if="item.status===STATUS.uploading.value">
              {{ proxy.Utils.sizeToStr(item.uploadSize) }}/{{ proxy.Utils.sizeToStr(item.totalSize) }}
            </span>
          </div>


        </div>

        <!--        暂停，继续等操作按钮-->
        <div class="op">
          <!--          MD5-->
          <el-progress
              type="circle"
              :width="60"
              :percentage="item.md5Progress"
              v-if="item.status===STATUS.init.value"
          >
            <template #default>
              <div style="font-size: 15px; color: #000000;">{{ item.md5Progress }}%</div>
            </template>
          </el-progress>

          <div class="op-btn">
            <span v-if="item.status===STATUS.uploading.value">
              <Icon
                  :width="28"
                  class="btn-item"
                  iconName="upload"
                  v-if="item.pause"
                  title="上传"
                  @click="startUpload(item.uid)"
              ></Icon>
              <Icon
                  v-else
                  :width="28"
                  class="btn-item"
                  iconName="pause"
                  title="暂停"
                  @click="pauseUpload(item.uid)"
              ></Icon>
            </span>
            <Icon
                :width="28"
                class="del btn-item"
                iconName="del"
                title="删除"
                @click="delUpload(item.uid,index)"
                v-if="item.status!==STATUS.init.value &&
                      item.status!==STATUS.upload_finish.value &&
                      item.status!==STATUS.upload_seconds.value"
            ></Icon>
            <Icon
                :width="28"
                class="clean btn-item"
                iconName="clean"
                title="清除"
                @click="delUpload(item.uid,index)"
                v-if="item.status===STATUS.upload_finish.value &&
                      item.status===STATUS.upload_seconds.value"
            ></Icon>
          </div>
        </div>

      </div>

      <div v-if="fileList.length===0">
        <NoData msg="暂无上传任务!"></NoData>
      </div>

    </div>


  </div>

</template>

<script setup>
import {ref, reactive, getCurrentInstance, nextTick} from 'vue';

const {proxy} = getCurrentInstance()

import SparkMD5 from "spark-md5"

const STATUS = {
  emptyFile: {
    value: "emptyFile",
    desc: "文件为空",
    color: "#F75000",
    icon: "close",
  },
  fail: {
    value: "fail",
    desc: "上传失败",
    color: "#F75000",
    icon: "close"
  },
  init: {
    value: "init",
    desc: "解析中",
    color: "#e6a23c",
    icon: "clock"
  },
  uploading: {
    value: "uploading",
    desc: "上传中",
    color: "#409eff",
    icon: "upload"
  },
  upload_finish: {
    value: "upload_finish",
    desc: "上传完成",
    color: "#67c23a",
    icon: "ok"
  },
  upload_seconds: {
    value: "upload_seconds",
    desc: "秒传",
    color: "#67c23a",
    icon: "ok"
  },
}
const fileList = ref([])

const delList = ref([])

const addFile = async (file, filePid) => {
  const fileItem = {
    //文件：包含文件大小，文件流，文件名
    file: file,
    //文件UID
    uid: file.uid,
    //MD5进度条
    md5Progress: 0,
    //MD5值
    md5: null,
    //文件名
    fileName: file.name,
    //上传状态
    status: STATUS.init.value,
    //已上传大小
    uploadSize: 0,
    //文件总大小
    totalSize: file.size,
    //上传进度
    uploadProgress: 0,
    //暂停
    pause: false,
    //当前分片
    chunkIndex: 0,
    //文件父级ID
    filePid: filePid,
    //错误信息
    errorMsg: null,
  }
  fileList.value.unshift(fileItem)
  if (fileItem.totalSize === 0) {
    fileItem.status = STATUS.emptyFile.value
    return
  }
  let md5FileUid = await computeMd5(fileItem)
  if (md5FileUid == null) {
    return
  }
  uploadFile(md5FileUid)
}

defineExpose({addFile})

const emit = defineEmits(["uploadCallback"])
//5MB
const chunkSize = 1024 * 1024 * 5
//md5操作
const computeMd5 = (fileItem) => {
  let file = fileItem.file
  let blogSlice = File.prototype.slice ||
      File.prototype.mozSlice ||
      File.prototype.webkitSlice
  let chunks = Math.ceil(file.size / chunkSize)
  let currentChunk = 0
  let spark = new SparkMD5.ArrayBuffer()
  let fileReader = new FileReader()
  let loadNext = () => {
    let start = currentChunk * chunkSize
    let end = start + chunkSize >= file.size ? file.size : start + chunkSize
    fileReader.readAsArrayBuffer(blogSlice.call(file, start, end))
  }
  loadNext()

  return new Promise((resolve, reject) => {
    let resultFile = getFileByUid(file.uid)
    fileReader.onload = (e) => {
      spark.append(e.target.result)
      currentChunk++
      if (currentChunk < chunks) {
        console.log(`${file.name}，第${currentChunk}分片解析完成，开始第${currentChunk + 1}个分片`)
        let percent = Math.floor((currentChunk / chunks) * 100)
        resultFile.md5Progress = percent
        loadNext()
      } else {
        let md5 = spark.end()
        // 释放内存
        spark.destroy()
        resultFile.md5Progress = 100
        resultFile.status = STATUS.uploading.value
        resultFile.md5 = md5
        resolve(fileItem.uid)
      }
    }
    fileReader.onerror = () => {
      resultFile.md5Progress = -1
      resultFile.status = STATUS.fail.value
      resolve(fileItem.uid)
    }
  }).catch(error => {
    return null
  })
}

//获取文件
const getFileByUid = (uid) => {
  let file = fileList.value.find(item => {
    return item.file.uid === uid
  })
  return file
}

const uploadFile = async (uid, chunkIndex) => {
  chunkIndex = chunkIndex ? chunkIndex : 0
  //分片上传
  let currentFile = getFileByUid(uid)
  const file = currentFile.file
  const fileSize = currentFile.totalSize
  const chunks = Math.ceil(fileSize / chunkSize)
  for (let i = chunkIndex; i < chunks; i++) {
    let delIndex = delList.value.indexOf(uid)
    if (delIndex !== -1) {
      delList.value.splice(delIndex, 1)
      break
    }
    currentFile = getFileByUid(uid)
    if (currentFile.pause) {
      break
    }
    let start = i * chunkSize
    let end = start + chunkSize >= fileSize ? fileSize : start + chunkSize
    let chunkFile = file.slice(start, end)
    let updateResult = await proxy.Request({
      url: proxy.Api.uploadFile,
      showLoading: false,
      dataType: "file",
      params: {
        file: chunkFile,
        fileName: file.name,
        fileMd5: currentFile.md5,
        chunkIndex: i,
        chunks: chunks,
        fileId: currentFile.fileId,
        filePid: currentFile.filePid
      },
      showError: false,
      errorCallback: (res) => {
        proxy.Message.error(res.msg)
        currentFile.status = STATUS.fail.value
        currentFile.errorMsg = res.msg
      },
      uploadProgressCallback: (event) => {
        let loaded = event.loaded
        if (loaded > fileSize) {
          loaded = fileSize
        }
        currentFile.uploadSize = i * chunkSize + loaded
        currentFile.uploadProgress = Math.floor((currentFile.uploadSize / fileSize) * 100)
      }
    })
    if (!updateResult) {
      break;
    }
    currentFile.fileId = updateResult.data.fileId
    currentFile.status = STATUS[updateResult.data.status].value
    currentFile.chunkIndex = i
    if(updateResult.data.status === STATUS.upload_seconds.value||
        updateResult.data.status ===STATUS.upload_finish.value){
      currentFile.uploadProgress = 100
      emit("uploadCallback")
      break
    }
  }
}
</script>

<style lang="scss" scoped>
.uploader-panel {
  .uploader-title {
    border-bottom: 1px solid #ddd;
    line-height: 40px;
    padding: 0 10px;
    font-size: 15px;

    .tips {
      font-size: 13px;
      color: rgb(169, 169, 169);
    }
  }

  .file-list {
    overflow: auto;
    padding: 10px 0;
    min-height: calc(100vh / 2);
    max-height: calc(100vh - 120px);

    .file-item {
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 3px 10px;
      background-color: #fff;
      border-bottom: 1px solid #ddd;
    }

    .file-item:nth-child(even) {
      background-color: #fcf8f4;
    }

    .upload-panel {
      flex: 1;

      .file-name {
        color: rgb(64, 62, 62);
      }

      .upload-status {
        display: flex;
        align-items: center;
        margin-top: 5px;

        .iconfont {
          margin-right: 3px;
        }

        .status {
          color: red;
          font-size: 13px;
        }

        .upload-info {
          margin-left: 5px;
          font-size: 12px;
          color: rgb(112, 111, 111);
        }
      }

      .progress {
        height: 10px;
      }
    }

    .op {
      width: 100px;
      display: flex;
      align-items: center;
      justify-content: flex-end;

      .op-btn {
        .btn-item {
          cursor: pointer;
        }

        .del,
        .clean {
          margin-left: 5px;
        }
      }
    }
  }
}
</style>
