package com.zju.main.sharingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Homework {

    private int homeworkId;

    private int sectionId;

    private String title;

    private String description;

    private BigDecimal proportion;

    private LocalDateTime deadline;

    private boolean committed;

    private List<String> files;
}
