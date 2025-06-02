package com.zju.main.sharingSystem.service;

import com.zju.main.sharingSystem.vo.StudentAttendanceVO;

import java.util.List;

public interface StudentService {

    List<StudentAttendanceVO> getStudentList(int sectionId);
}
