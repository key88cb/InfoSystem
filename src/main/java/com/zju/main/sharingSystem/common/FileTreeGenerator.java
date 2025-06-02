package com.zju.main.sharingSystem.common;

import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class FileTreeGenerator {

    private long idGenerator = 1;

    public List<FileNode> getFileTreeBySection(int sectionId, String base) {
        // 替换成你的文件根路径
        String basePath = base + "/section/" + sectionId;
        File baseDir = new File(basePath);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            return new ArrayList<>();
        }

        return listFilesRecursive(baseDir);
    }

    private List<FileNode> listFilesRecursive(File dir) {
        List<FileNode> result = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files == null) return result;

        for (File file : files) {
            FileNode node = new FileNode();
            node.setId(idGenerator++);
            node.setLabel(file.getName());
            if (file.isDirectory()) {
                node.setType("directory");
                node.setChildren(listFilesRecursive(file)); // 递归
            } else {
                node.setType("file");
                node.setChildren(new ArrayList<>()); // 文件无 children
            }
            result.add(node);
        }

        return result;
    }
}
