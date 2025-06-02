package com.zju.main.sharingSystem.handler;

import com.zju.main.sharingSystem.common.ApiResult;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class GlobalSQLExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResult handleDuplicateKeyException(DuplicateKeyException e) {
        return ApiResult.error("数据已存在，请勿重复操作！");
    }

    @ExceptionHandler(SQLException.class)
    public ApiResult handleSQLException(SQLException e) {
        return ApiResult.error("数据库错误！");
    }

    @ExceptionHandler(Exception.class)
    public ApiResult handleException(Exception e) {
        e.printStackTrace();
        return ApiResult.error("未知错误！");
    }
}
