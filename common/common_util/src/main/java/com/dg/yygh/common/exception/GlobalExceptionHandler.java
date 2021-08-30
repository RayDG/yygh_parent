package com.dg.yygh.common.exception;

import com.dg.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: DG
 * @Date: 2021/8/29 15:36
 * @Description:
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

//    try {
//        int a = 1/0;
//    } catch (Exception e) {
    //自定义异常类，需手动抛出
//        throw new YyghException("失败", 201);
//    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody
    public Result error(YyghException e) {
        e.printStackTrace();
        return Result.fail();
    }
}
