package com.zju.main.sharingSystem.service.impl;

import com.zju.main.sharingSystem.entity.AttendanceFinalGrade;
import com.zju.main.sharingSystem.mapper.FinalCommitMapper;
import com.zju.main.sharingSystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    FinalCommitMapper finalCommitMapper;

    @Override
    @Transactional
    public int finalCommit(List<AttendanceFinalGrade> grades) {
        for (AttendanceFinalGrade attendanceFinalGrade : grades) {
            if (attendanceFinalGrade.getScore() == null) {
                return -1;
            }
        }
        if (finalCommitMapper.selectAttendanceGrade(grades.get(0).getTakesId()) > 0) {
            return -2;
        }
        finalCommitMapper.insertAttendanceGrade(grades);
        return 1;
    }
}
