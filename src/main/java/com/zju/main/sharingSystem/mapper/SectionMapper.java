package com.zju.main.sharingSystem.mapper;

import com.zju.main.sharingSystem.entity.TimeSlot;
import com.zju.main.sharingSystem.vo.SectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SectionMapper {

    List<SectionVO> selectSectionsByStudent(int studentId);

    List<SectionVO> selectSectionsByTeacher(int teacherId);

    SectionVO selectSectionById(int sectionId);

    @Select("SELECT * FROM time_slot")
    List<TimeSlot> selectTimeSlots();
}
