package com.zju.main.sharingSystem.mapper;

import com.zju.main.sharingSystem.dto.HomeworkRecordDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HomeworkRecordMapper {

    @Insert("""
    INSERT INTO homework_record (student_id, homework_id, description, files)
        VALUES (#{studentId}, #{homeworkId}, #{description}, #{files})
    """)
    void insertHomeworkRecord(HomeworkRecordDTO homeworkRecordDTO);
}
