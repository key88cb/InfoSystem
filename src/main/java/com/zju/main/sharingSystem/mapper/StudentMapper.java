package com.zju.main.sharingSystem.mapper;

import com.zju.main.sharingSystem.vo.StudentAttendanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Select("""
    SELECT I.student_id, I.takes_id, P.name AS student_name
    FROM (
        SELECT student_id, takes_id FROM takes WHERE sec_id = #{sectionId}
    ) I
    LEFT JOIN user U ON student_id = U.user_id
    LEFT JOIN personal_information P ON P.personal_infor_ID = U.personal_infor_ID
    """)
    List<StudentAttendanceVO> selectStudentBySection(int sectionId);
}
