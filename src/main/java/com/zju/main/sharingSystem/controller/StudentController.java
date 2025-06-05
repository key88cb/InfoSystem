package com.zju.main.sharingSystem.controller;

import com.zju.main.sharingSystem.common.ApiResult;
import com.zju.main.sharingSystem.dto.HomeworkRecordDTO;
import com.zju.main.sharingSystem.service.HomeworkService;
import com.zju.main.sharingSystem.vo.StudentHomeworkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.zju.main.sharingSystem.constant.FilePathConstant.BASE_PATH;

@RestController
@Slf4j
@RequestMapping("/sharing-system")
public class StudentController {

    @Autowired
    HomeworkService homeworkService;

    @GetMapping("/student/homework")
    public ApiResult getHomework(int sectionId, int studentId) {
        // 获取作业列表
        List<StudentHomeworkVO> homeworkList = homeworkService.getStudentHomework(sectionId, studentId);

        return ApiResult.success(homeworkList);
    }

    @PostMapping("/student/homework")
    public ApiResult commitHomework(@RequestBody HomeworkRecordDTO homeworkRecordDTO) {
        // 提交作业
        try {
            String userDir = "student";
            Path sourceBase = Paths.get(BASE_PATH, userDir, String.valueOf(homeworkRecordDTO.getStudentId()));
            Path targetBase = Paths.get(BASE_PATH, "homeworkRecord", String.valueOf(homeworkRecordDTO.getHomeworkId()), String.valueOf(homeworkRecordDTO.getStudentId()));
            if (!Files.exists(targetBase)) {
                Files.createDirectories(targetBase);
            }
            for (String fileName : homeworkRecordDTO.getFiles()) {
                Path sourceFile = sourceBase.resolve(fileName);
                Path targetFile = targetBase.resolve(fileName);
                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        homeworkService.commitHomework(homeworkRecordDTO);
        return ApiResult.success();
    }


}
