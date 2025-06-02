package com.zju.main.sharingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    private int sectionId;

    private String title;

    private String departmentName;

    private String semester;

    private String teacherName;

    private String time;

    private String address;
}
