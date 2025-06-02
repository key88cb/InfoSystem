package com.zju.main.sharingSystem.common;

import lombok.Data;

@Data
public class ApiResult {
    private Integer code; // 返回码: 1成功，否则失败
    private String message; // 错误信息
    private Object data; // 数据

    public static ApiResult success() {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(1);
        return apiResult;
    }

    public static ApiResult success(Object data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(1);
        apiResult.setData(data);
        return apiResult;
    }

    public static ApiResult error(String message) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(0);
        apiResult.setMessage(message);
        return apiResult;
    }
}
