import {ElMessageBox} from "element-plus";

const confirm = (message,okFun)=>{
    ElMessageBox.confirm(message,'提示',{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"info",
        customStyle: {
            top:"20px",
            left: '40%', // 水平居中
            position:"absolute"
        },
    }).then(()=>{
        okFun()
    }).catch(()=>{})
}

export default confirm
