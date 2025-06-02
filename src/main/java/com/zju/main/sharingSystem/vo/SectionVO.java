package com.zju.main.sharingSystem.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionVO {

    private int sectionId;

    private String title;

    private String departmentName;

    private String semester;

    private Year year;

    private String teacherName;

    private List<Integer> timeSlotIds;

    private String time;
}
