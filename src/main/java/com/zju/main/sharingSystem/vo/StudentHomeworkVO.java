package com.zju.main.sharingSystem.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentHomeworkVO {

    private int homeworkId;

    private String title;

    private String description;

    private LocalDateTime deadline;

    private BigDecimal proportion;

    private Integer score;

    private String feedback;

    private String commitDescription;

    private List<String> files;

    private List<String> commitFiles;
}
