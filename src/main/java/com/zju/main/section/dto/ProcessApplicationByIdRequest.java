package com.zju.main.section.dto;

import lombok.Data;

/**
 * 通过申请ID处理申请请求DTO
 */
@Data
public class ProcessApplicationByIdRequest {
    private Integer appId;
    private String suggestion;
    private Boolean finalDecision;
}
