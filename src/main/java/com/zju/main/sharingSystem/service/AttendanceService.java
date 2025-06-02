package com.zju.main.sharingSystem.service;

import com.zju.main.sharingSystem.entity.AttendanceFinalGrade;

import java.util.List;

public interface AttendanceService {
    int finalCommit(List<AttendanceFinalGrade> grades);
}
