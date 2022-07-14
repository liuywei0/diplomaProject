package com.liuywei.housekeeping.config;

import com.liuywei.housekeeping.utils.CustomException;
import com.liuywei.housekeeping.utils.Message;
import com.liuywei.housekeeping.utils.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类，对控制器层抛出来的异常进行统一处理
 **/
@RestControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public <E> Message handler(Exception exception) {
        exception.printStackTrace();
//        if (exception instanceof PermissionException) {
//            return Result.forbidden("权限不足");
//        }
        if (exception instanceof CustomException) {
            return Result.error(exception.getMessage());
        } else if (exception instanceof DataIntegrityViolationException) {
            return Result.error("数据库异常！");
        }
        return Result.error("后台接口异常！");
    }

}
