package com.zju.main.sharingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceFinalGrade {

    private int takesId;

    private Integer score;

    private BigDecimal proportion;
}
