package com.zju.main.sharingSystem.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileNode {

    private long id;

    private String label;

    private String type; // "directory" or "file"

    private List<FileNode> children;
}
