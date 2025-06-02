package com.zju.main.sharingSystem.mapper;

import com.zju.main.sharingSystem.dto.HomeworkCorrectionDTO;
import com.zju.main.sharingSystem.entity.HomeworkFinalGrade;
import com.zju.main.sharingSystem.util.typehandler.StringToListTypeHandler;
import com.zju.main.sharingSystem.vo.TeacherHomeworkVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeworkCorrectionMapper {

    @Insert("INSERT INTO homework_correction (student_id, homework_id, score, feedback) VALUES (#{studentId}, #{homeworkId}, #{score}, #{feedback})")
    void insertCorrection(HomeworkCorrectionDTO homeworkCorrectionDTO);

    @Update("""
    UPDATE homework_correction
    SET score = #{score}, feedback = #{feedback}
    WHERE homework_id = #{homeworkId} AND student_id = #{studentId}
    """)
    int updateCorrection(HomeworkCorrectionDTO homeworkCorrectionDTO);

    @Select("""
    SELECT I.student_id, P.name AS student_name, R.description, R.files, C.score, C.feedback
    FROM (
        SELECT student_id FROM takes WHERE sec_id = #{sectionId}
    ) I
    LEFT JOIN user U ON I.student_id = U.user_id
    LEFT JOIN personal_information P ON P.personal_infor_ID = U.personal_infor_ID
    LEFT JOIN homework_record R ON R.student_id = I.student_id AND R.homework_id = #{homeworkId}
    LEFT JOIN homework_correction C ON C.student_id = I.student_id AND C.homework_id = #{homeworkId}
    """)
    @Result(column = "files", property = "files", typeHandler = StringToListTypeHandler.class)
    List<TeacherHomeworkVO> selectHomeworkDetail(int sectionId, int homeworkId);

    @Select("""
    SELECT I.takes_id, C.score
    FROM (
        SELECT takes_id, student_id FROM takes WHERE sec_id = #{sectionId}
    ) I
    LEFT JOIN homework_correction C ON C.student_id = I.student_id AND C.homework_id = #{homeworkId}
    """)
    List<HomeworkFinalGrade> selectHomeworkCorrection(int sectionId, int homeworkId);
}
