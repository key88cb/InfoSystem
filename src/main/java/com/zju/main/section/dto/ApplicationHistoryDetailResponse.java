package com.zju.main.section.dto;

import lombok.Data;

@Data
public class ApplicationHistoryDetailResponse {
    private Integer appId;
    private Integer adminId;
    private Integer secId;
    private String reason;
    private Integer teacherId;
    private String suggestion;
    private Boolean finalDecision;
    private String courseTitle;        // 课程名称
    private String semester;           // 学期
    private Integer year;              // 学年
    private String teacherName;        // 教师名称
    private String classroomLocation;  // 教室位置
}
