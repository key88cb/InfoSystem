package com.zju.main.sharingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HomeworkCorrectionDTO {

    private int sectionId;

    private int homeworkId;

    private int studentId;

    private int score;

    private String feedback;
}
