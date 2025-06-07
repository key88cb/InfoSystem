package com.zju.main.sharingSystem.mapper;

import com.zju.main.sharingSystem.entity.AttendanceFinalGrade;
import com.zju.main.sharingSystem.entity.HomeworkFinalGrade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FinalCommitMapper {

    void insertHomeworkGrade(List<HomeworkFinalGrade> grades, String title);

    void insertAttendanceGrade(List<AttendanceFinalGrade> grades);

    @Select("SELECT COUNT(*) FROM grade WHERE takes_id = #{takesId} AND type = 'attending'")
    int selectAttendanceGrade(int takesId);
}
