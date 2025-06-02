package com.zju.main.sharingSystem.mapper;

import com.zju.main.sharingSystem.dto.HomeworkDTO;
import com.zju.main.sharingSystem.entity.Homework;
import com.zju.main.sharingSystem.util.typehandler.StringToListTypeHandler;
import com.zju.main.sharingSystem.vo.StudentHomeworkVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeworkInfoMapper {
    @Select("""
    SELECT
        h.homework_id,
        h.title,
        h.description,
        h.deadline,
        h.proportion,
        h.files AS homework_files,
        c.score,
        c.feedback,
        r.description AS commit_description,
        r.files AS commit_files
    FROM homework_information h
    LEFT JOIN homework_record r ON h.homework_id = r.homework_id AND r.student_id = #{studentId}
    LEFT JOIN homework_correction c ON h.homework_id = c.homework_id AND c.student_id = #{studentId}
    WHERE h.sec_id = #{sectionId}
    """)
    @Results({
            @Result(column = "homework_files", property = "files", typeHandler = StringToListTypeHandler.class),
            @Result(column = "commit_files", property = "commitFiles", typeHandler = StringToListTypeHandler.class)
    })
    List<StudentHomeworkVO> selectHomeworkWithDetail(int sectionId, int studentId);

    @Select("SELECT * FROM homework_information WHERE sec_id = #{sectionId}")
    @Result(column = "files", property = "files", typeHandler = StringToListTypeHandler.class)
    List<Homework> selectHomeworkInfo(int sectionId);

    @Insert("""
    INSERT INTO homework_information (sec_id, title, description, deadline, proportion, files)
    VALUES (#{sectionId}, #{title}, #{description}, #{deadline}, #{proportion}, #{files})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "homeworkId")
    void insertHomework(HomeworkDTO homeworkDTO);

    @Select("SELECT * FROM homework_information WHERE homework_id = #{homeworkId}")
    Homework selectHomeworkById(int homeworkId);

    @Update("UPDATE homework_information SET committed = 1 WHERE homework_id = #{homeworkId}")
    void updateHomework(int homeworkId);
}
