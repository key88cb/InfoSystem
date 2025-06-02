package com.zju.main.sharingSystem.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherHomeworkVO {

    private int studentId;

    private String studentName;

    private String description;

    private List<String> files;

    private Integer score;

    private String feedback;
}
