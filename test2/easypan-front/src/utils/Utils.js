export default {
    sizeToStr: (limit) => {
        var size = ""
        if (limit < 1 * 1024) {   //小于0.1KB，转化为B
            size = limit.toFixed(2) + "B"
        } else if (limit < 1 * 1024 * 1024) {   //小于0.1MB，转化为KB
            size = (limit / 1024).toFixed(2) + "KB"
        } else if (limit < 1 * 1024 * 1024 * 1024) {   //小于0.1GB，转化为MB
            size = (limit / (1024 * 1024)).toFixed(2) + "MB"
} else {                                                  //其他转化为GB
            size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB"
        }
        var sizeStr = size + ""                 //转化为字符串
        var index = sizeStr.indexOf(".")        //获取小数点处的索引
        var dou = sizeStr.substr(index+1,2) //获取小数点后两位的值
        if(dou=="00"){                                        //判断后两位是否为零，如果是0则删除00
            return sizeStr.substring(0,index)+sizeStr.substr(index+3,2)
        }
        return size
    }
}
