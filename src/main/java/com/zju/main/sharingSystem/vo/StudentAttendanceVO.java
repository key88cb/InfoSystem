package com.zju.main.sharingSystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentAttendanceVO {

    private int studentId;

    private int takesId;

    private Integer score;

    private String studentName;
}
