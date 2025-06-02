package com.zju.main.sharingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeworkRecordDTO {

    private int sectionId;

    private int studentId;

    private int homeworkId;

    private String description;

    private List<String> files;
}
