package com.zju.main.section.dto;

import lombok.Data;

/**
 * 添加教室请求DTO
 */
@Data
public class AddClassroomRequest {
    private String campus;
    private Integer capacity;
    private String building;
    private Integer roomNumber;
    private String type;
}