package com.zju.main.sharingSystem.service.impl;

import com.zju.main.sharingSystem.dto.HomeworkCorrectionDTO;
import com.zju.main.sharingSystem.dto.HomeworkDTO;
import com.zju.main.sharingSystem.dto.HomeworkRecordDTO;
import com.zju.main.sharingSystem.entity.Homework;
import com.zju.main.sharingSystem.entity.HomeworkFinalGrade;
import com.zju.main.sharingSystem.mapper.FinalCommitMapper;
import com.zju.main.sharingSystem.mapper.HomeworkCorrectionMapper;
import com.zju.main.sharingSystem.mapper.HomeworkInfoMapper;
import com.zju.main.sharingSystem.mapper.HomeworkRecordMapper;
import com.zju.main.sharingSystem.service.HomeworkService;
import com.zju.main.sharingSystem.vo.StudentHomeworkVO;
import com.zju.main.sharingSystem.vo.TeacherHomeworkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HomeworkServiceImpl implements HomeworkService {
    @Autowired
    HomeworkRecordMapper homeworkRecordMapper;

    @Autowired
    HomeworkInfoMapper homeworkInfoMapper;

    @Autowired
    HomeworkCorrectionMapper homeworkCorrectionMapper;

    @Autowired
    FinalCommitMapper finalCommitMapper;

    @Override
    public List<StudentHomeworkVO> getStudentHomework(int sectionId, int studentId) {
        return homeworkInfoMapper.selectHomeworkWithDetail(sectionId, studentId);
    }

    @Override
    public void commitHomework(HomeworkRecordDTO homeworkRecordDTO) {
        homeworkRecordMapper.insertHomeworkRecord(homeworkRecordDTO);
    }

    @Override
    public List<Homework> getHomework(int sectionId) {
        return homeworkInfoMapper.selectHomeworkInfo(sectionId);
    }

    @Override
    public void assignHomework(HomeworkDTO homeworkDTO) {
        homeworkInfoMapper.insertHomework(homeworkDTO);
    }

    @Override
    public List<TeacherHomeworkVO> getTeacherHomework(int sectionId, int homeworkId) {
        return homeworkCorrectionMapper.selectHomeworkDetail(sectionId, homeworkId);
    }

    @Override
    public void correctHomework(HomeworkCorrectionDTO homeworkCorrectionDTO) {
        homeworkCorrectionMapper.insertCorrection(homeworkCorrectionDTO);
    }

    @Override
    public boolean modifyCorrection(HomeworkCorrectionDTO homeworkCorrectionDTO) {
        return homeworkCorrectionMapper.updateCorrection(homeworkCorrectionDTO) != 0;
    }

    @Transactional
    @Override
    public int finalCommit(int sectionId, int homeworkId) {
        Homework homework = homeworkInfoMapper.selectHomeworkById(homeworkId);
        if (homework == null) {
            return -2;
        }
        if (homework.isCommitted()) {
            return -1;
        } else {
            List<HomeworkFinalGrade> list = homeworkCorrectionMapper.selectHomeworkCorrection(sectionId, homeworkId);
            for (HomeworkFinalGrade homeworkFinalGrade : list) {
                if (homeworkFinalGrade.getScore() == null) {
                    return 0;
                }
                homeworkFinalGrade.setProportion(homework.getProportion());
            }
            finalCommitMapper.insertHomeworkGrade(list, homework.getTitle());
            homeworkInfoMapper.updateHomework(homeworkId);
            return 1;
        }
    }
}
