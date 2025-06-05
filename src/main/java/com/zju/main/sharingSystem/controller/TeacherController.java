package com.zju.main.sharingSystem.controller;

import com.zju.main.sharingSystem.common.ApiResult;
import com.zju.main.sharingSystem.dto.HomeworkCorrectionDTO;
import com.zju.main.sharingSystem.dto.HomeworkDTO;
import com.zju.main.sharingSystem.entity.AttendanceFinalGrade;
import com.zju.main.sharingSystem.entity.Homework;
import com.zju.main.sharingSystem.service.AttendanceService;
import com.zju.main.sharingSystem.service.HomeworkService;
import com.zju.main.sharingSystem.service.StudentService;
import com.zju.main.sharingSystem.vo.StudentAttendanceVO;
import com.zju.main.sharingSystem.vo.TeacherHomeworkVO;
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
public class TeacherController {

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    StudentService studentService;

    @Autowired
    AttendanceService attendanceService;

    @GetMapping("/teacher/homework")
    public ApiResult getHomework(int sectionId) {
        // 获取作业列表
        List<Homework> homeworkList = homeworkService.getHomework(sectionId);

        return ApiResult.success(homeworkList);
    }

    @PostMapping("/teacher/homework")
    public ApiResult assignHomework(@RequestBody HomeworkDTO homeworkDTO) {
        // 检测文件
        String userDir = "teacher";
        Path sourceBase = Paths.get(BASE_PATH, userDir, String.valueOf(homeworkDTO.getTeacherId()));
        for (String fileName : homeworkDTO.getFiles()) {
            Path sourceFile = sourceBase.resolve(fileName);
            if (!Files.exists(sourceFile)) {
                return ApiResult.error("文件不存在!");
            }
        }
        // 布置作业
        homeworkService.assignHomework(homeworkDTO);
        // 复制文件
        try {
            Path targetBase = Paths.get(BASE_PATH, "homework", String.valueOf(homeworkDTO.getHomeworkId()));
            if (!Files.exists(targetBase)) {
                Files.createDirectories(targetBase);
            }
            for (String fileName : homeworkDTO.getFiles()) {
                Path sourceFile = sourceBase.resolve(fileName);
                Path targetFile = targetBase.resolve(fileName);
                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ApiResult.success();
    }

    // TODO: 更新查询语句
    @GetMapping("/teacher/homework-detail")
    public ApiResult getHomeworkDetail(int sectionId, int homeworkId) {
        // 获取该作业所有学生及其提交批改情况
        List<TeacherHomeworkVO> homeworkDetail = homeworkService.getTeacherHomework(sectionId, homeworkId);

        return ApiResult.success(homeworkDetail);
    }

    @PostMapping("/teacher/homework/correction")
    public ApiResult correctHomework(@RequestBody HomeworkCorrectionDTO homeworkCorrectionDTO) {
        // 批改作业
        homeworkService.correctHomework(homeworkCorrectionDTO);

        return ApiResult.success();
    }

    @PutMapping("/teacher/homework/correction")
    public ApiResult modifyCorrection(@RequestBody HomeworkCorrectionDTO homeworkCorrectionDTO) {
        // 修改成绩
        homeworkService.modifyCorrection(homeworkCorrectionDTO);

        return ApiResult.success();
    }

    // TODO: 更新提交语句
    @PostMapping("/teacher/homework/commit")
    public ApiResult commitHomework(int sectionId, int homeworkId) {
        // 最终提交
        int code = homeworkService.finalCommit(sectionId, homeworkId);
        return switch (code) {
            case -2 -> ApiResult.error("作业不存在!");
            case -1 -> ApiResult.error("不能重复提交！");
            case 0 -> ApiResult.error("有作业未批改！");
            default -> ApiResult.success();
        };
    }

    // TODO: 更新查询语句
    @GetMapping("/teacher/attendance")
    public ApiResult getAttendanceList(int sectionId) {
        // 获取考勤列表，即学生列表
        List<StudentAttendanceVO> studentList = studentService.getStudentList(sectionId);

        return ApiResult.success(studentList);
    }

    // TODO: 更新查询语句
    @PostMapping("/teacher/attendance")
    public ApiResult commitAttendance(@RequestBody List<AttendanceFinalGrade> grades) {
        // 录入考勤成绩
        int code = attendanceService.finalCommit(grades);
        return switch (code) {
            case -2 -> ApiResult.error("考勤成绩已录入，不能重复录入！");
            case -1 -> ApiResult.error("成绩录入不能有空！");
            default -> ApiResult.success();
        };
    }
}
