package com.zju.main.sharingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeworkRecord {

    private int studentId;

    private int homeworkId;

    private String description;

    private String files;
}
