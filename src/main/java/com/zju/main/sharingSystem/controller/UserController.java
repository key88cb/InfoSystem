package com.zju.main.sharingSystem.controller;

import com.zju.main.sharingSystem.common.ApiResult;
import com.zju.main.sharingSystem.common.FileInfo;
import com.zju.main.sharingSystem.common.FileTreeGenerator;
import com.zju.main.sharingSystem.dto.FileDTO;
import com.zju.main.sharingSystem.service.SectionService;
import com.zju.main.sharingSystem.vo.SectionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.zju.main.sharingSystem.constant.FilePathConstant.BASE_PATH;

@RestController
@Slf4j
@RequestMapping("/sharing-system")
public class UserController {

    @Autowired
    SectionService sectionService;

    // TODO: 更新查询语句
    @GetMapping("/section-list")
    public ApiResult getSectionList(int userId, boolean isStudent) {
        // 获取课程列表
        List<SectionVO> sectionList = sectionService.getSectionList(userId, isStudent);

        return ApiResult.success(sectionList);
    }
    // TODO: 更新查询语句
    @GetMapping("/section")
    public ApiResult getSection(int sectionId) {
        // 获取课程详细信息
        SectionVO sectionVO = sectionService.getSection(sectionId);

        return ApiResult.success(sectionVO);
    }

    @GetMapping("/api/file")
    public ResponseEntity<Resource> downloadSharedFile(String filePath) {
        // 返回文件
        try {
            File file = new File(BASE_PATH + filePath);
            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            FileSystemResource resource = new FileSystemResource(file);

            // 处理中文文件名下载问题
            String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/shared-file")
    public ApiResult getSharedFileTree(int sectionId) {
        FileTreeGenerator tree = new FileTreeGenerator();
        return ApiResult.success(tree.getFileTreeBySection(sectionId, BASE_PATH));
    }

    @PostMapping("/shared-file")
    public ApiResult uploadSharedFile(@RequestBody FileDTO fileDTO) {
        // 上传文件
        try {
            String userDir = fileDTO.isStudent() ? "student" : "teacher";
            Path sourceBase = Paths.get(BASE_PATH, userDir, String.valueOf(fileDTO.getUserId()));
            Path targetBase = Paths.get(BASE_PATH, "section", String.valueOf(fileDTO.getSectionId()), fileDTO.getRelativePath());
            // 不存在则先创建
            if (!Files.exists(targetBase)) {
                Files.createDirectories(targetBase);
            }

            if (fileDTO.getCreateType().equals("folder")) {
                Path folderPath = targetBase.resolve(fileDTO.getFolder());
                Files.createDirectories(folderPath);
            } else if (fileDTO.getCreateType().equals("file")) {
                for (String fileName : fileDTO.getFiles()) {
                    Path sourceFile = sourceBase.resolve(fileName);
                    Path targetFile = targetBase.resolve(fileName);
                    Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ApiResult.success();
    }

    @DeleteMapping("/shared-file")
    public ApiResult deleteResourceFile(String filePath) {
        // 删除文件
        File file = new File(BASE_PATH + filePath);
        if (!file.exists()) {
            return ApiResult.error("删除对象不存在！");
        }
        boolean deleted;
        if (file.isDirectory()) {
            deleted = deleteDirectoryRecursively(file);
        } else {
            deleted = file.delete();
        }
        if (deleted) {
            return ApiResult.success();
        } else {
            return ApiResult.error("删除失败");
        }
    }

    @PostMapping("/api/files")
    public ApiResult uploadFile(int userId, boolean isStudent, MultipartFile file) {
        // 上传文件

        String originalFilename = file.getOriginalFilename();
        String folderPath = BASE_PATH + (isStudent ? "/student/" : "/teacher/") + userId + "/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // 拆分文件名和后缀
        String name = originalFilename;
        String ext = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex != -1) {
            name = originalFilename.substring(0, dotIndex);
            ext = originalFilename.substring(dotIndex);
        }

        // 目标文件
        File dest = new File(folderPath + originalFilename);
        int count = 1;

        // 如果文件存在，循环重命名
        while (dest.exists()) {
            String newFilename = name + "(" + count + ")" + ext;
            dest = new File(folderPath + newFilename);
            count++;
        }

        // 保存文件
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ApiResult.success(dest.getName());
    }

    @GetMapping("/api/files")
    public ApiResult getFiles(int userId, boolean isStudent) {
        String folderPath = BASE_PATH + (isStudent ? "/student/" : "/teacher/") + userId + "/";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            // 目录不存在，返回空列表
            return ApiResult.success(Collections.emptyList());
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return ApiResult.success(new ArrayList<>());
        }

        List<FileInfo> fileInfoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();
                String size = formatFileSize(file.length());
                String lastModified = sdf.format(new Date(file.lastModified()));

                fileInfoList.add(new FileInfo(name, size, lastModified));
            }
        }

        return ApiResult.success(fileInfoList);
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        int z = (63 - Long.numberOfLeadingZeros(size)) / 10;
        return String.format("%.1f %sB", (double)size / (1L << (z*10)), " KMGTPE".charAt(z));
    }

    private boolean deleteDirectoryRecursively(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                boolean result = file.isDirectory() ? deleteDirectoryRecursively(file) : file.delete();
                if (!result) return false;
            }
        }
        return dir.delete();
    }

}
