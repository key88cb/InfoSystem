package com.zju.main.sharingSystem.service;

import com.zju.main.sharingSystem.dto.HomeworkCorrectionDTO;
import com.zju.main.sharingSystem.dto.HomeworkDTO;
import com.zju.main.sharingSystem.dto.HomeworkRecordDTO;
import com.zju.main.sharingSystem.entity.Homework;
import com.zju.main.sharingSystem.vo.StudentHomeworkVO;
import com.zju.main.sharingSystem.vo.TeacherHomeworkVO;

import java.util.List;

public interface HomeworkService {

    /**
     * 获取学生的作业列表数据
     * @return 返回该section所有作业的信息及提交、批改情况
     */
    List<StudentHomeworkVO> getStudentHomework(int sectionId, int studentId);

    void commitHomework(HomeworkRecordDTO homeworkRecordDTO);

    /**
     * 教师获取作业列表数据
     * @return 返回该section所有作业的信息
     */
    List<Homework> getHomework(int sectionId);

    void assignHomework(HomeworkDTO homeworkDTO);

    /**
     * 教师获取某个作业的详细情况
     * @return 返回该作业下所有学生的信息、提交、批改情况
     */
    List<TeacherHomeworkVO> getTeacherHomework(int sectionId, int homeworkId);

    void correctHomework(HomeworkCorrectionDTO homeworkCorrectionDTO);

    boolean modifyCorrection(HomeworkCorrectionDTO homeworkCorrectionDTO);

    int finalCommit(int sectionId, int homeworkId);
}
