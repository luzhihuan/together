package com.easycom.entity.VO;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class Result {

    private int code;
    private Object data;
    private Long pageNo;
    private Long pageSize;
    private Long totalPage;
    private Long total;
    private String msg;
    private String status ;


    public static <T> Result ok(Object data, IPage<T> page) {
        return new Result(1,data,page.getCurrent(),page.getSize(),page.getPages(),page.getTotal(),"请求成功","成功");
    }
    public static Result ok(Object data) {
        return new Result(1,data,null,null,null,null,"请求成功","成功");
    }
    public static Result ok(){
        return new Result(1,"请求成功",null,null,null,null,"处理完毕！","成功");
    }

    public static Result fail(String msg){
        return new Result(0,msg,null,null,null,null,msg,"失败");
    }

    public static Result firstLogin(Object data) {
        return new Result(2,data,null,null,null,null,"第一次登录","成功");
    }
}
