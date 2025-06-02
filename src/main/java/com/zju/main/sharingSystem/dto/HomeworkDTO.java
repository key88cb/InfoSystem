package com.zju.main.sharingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HomeworkDTO {
    private int homeworkId;

    private int teacherId;

    private int sectionId;

    private String title;

    private String description;

    private BigDecimal proportion;

    private LocalDateTime deadline;

    private List<String> files;
}
