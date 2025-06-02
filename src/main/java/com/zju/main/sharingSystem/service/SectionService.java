package com.zju.main.sharingSystem.service;

import com.zju.main.sharingSystem.vo.SectionVO;

import java.util.List;

public interface SectionService {

    List<SectionVO> getSectionList(int userId, boolean isStudent);

    SectionVO getSection(int sectionId);
}
