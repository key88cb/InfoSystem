package com.zju.main.sharingSystem.service.impl;

import com.zju.main.sharingSystem.mapper.StudentMapper;
import com.zju.main.sharingSystem.service.StudentService;
import com.zju.main.sharingSystem.vo.StudentAttendanceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public List<StudentAttendanceVO> getStudentList(int sectionId) {
        return studentMapper.selectStudentBySection(sectionId);
    }
}
