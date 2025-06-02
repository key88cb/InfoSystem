package com.zju.main.sharingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

    private int sectionId;

    private int userId;

    private boolean student;

    private String relativePath;

    private List<String> files;

    private String folder;

    private String createType;
}
