package com.zju.main.sharingSystem.service.impl;

import com.zju.main.sharingSystem.entity.TimeSlot;
import com.zju.main.sharingSystem.mapper.SectionMapper;
import com.zju.main.sharingSystem.service.SectionService;
import com.zju.main.sharingSystem.vo.SectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    SectionMapper sectionMapper;

    @Override
    public List<SectionVO> getSectionList(int userId, boolean isStudent) {
        // 获取所有时间段信息
        List<TimeSlot> timeSlots = sectionMapper.selectTimeSlots();
        Map<Integer, TimeSlot> timeSlotMap = new HashMap<>();
        for (TimeSlot timeSlot : timeSlots) {
            timeSlotMap.put(timeSlot.getTimeSlotId(), timeSlot);
        }
        // TODO: 根据学生还是老师选择不同的查询语句
        List<SectionVO> sectionList;
        if (!isStudent) {
            sectionList =  sectionMapper.selectSectionsByTeacher(userId);
        } else {
            sectionList =  sectionMapper.selectSectionsByStudent(userId);
        }
        // 添加时间信息
        for (SectionVO section : sectionList) {
            section.setTime(section.getTimeSlotIds().stream().map(timeSlotMap::get).map(TimeSlot::getString).collect(Collectors.joining("，")));
        }
        return sectionList;
    }

    @Override
    public SectionVO getSection(int sectionId) {
        // 获取所有时间段信息
        List<TimeSlot> timeSlots = sectionMapper.selectTimeSlots();
        Map<Integer, TimeSlot> timeSlotMap = new HashMap<>();
        for (TimeSlot timeSlot : timeSlots) {
            timeSlotMap.put(timeSlot.getTimeSlotId(), timeSlot);
        }
        SectionVO section = sectionMapper.selectSectionById(sectionId);
        section.setTime(section.getTimeSlotIds().stream().map(timeSlotMap::get).map(TimeSlot::getString).collect(Collectors.joining("，")));
        return section;
    }
}
