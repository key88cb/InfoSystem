package com.zju.main.section.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zju.main.section.entity.Application;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
      /**
     * 根据课程章节ID查询所有申请（按申请ID倒序）
     * @param secId 课程章节ID
     * @return 申请列表
     */
    List<Application> findBySecIdOrderByAppIdDesc(Integer secId);
      /**
     * 根据教师ID查询历史申请（分页）
     * @param teacherId 教师ID
     * @param pageable 分页参数
     * @return 申请列表
     */
    Page<Application> findByTeacherIdOrderByAppIdDesc(Integer teacherId, Pageable pageable);
      /**
     * 根据教师ID查询历史申请列表
     * @param teacherId 教师ID
     * @return 申请列表
     */
    List<Application> findByTeacherIdOrderByAppIdDesc(Integer teacherId);
    
    /**
     * 根据管理员ID查询申请列表
     * @param adminId 管理员ID
     * @return 申请列表
     */
    List<Application> findByAdminIdOrderByAppIdDesc(Integer adminId);
    
    /**
     * 根据管理员ID查询申请列表（分页）
     * @param adminId 管理员ID
     * @param pageable 分页参数
     * @return 申请列表
     */
    Page<Application> findByAdminIdOrderByAppIdDesc(Integer adminId, Pageable pageable);      /**
     * 联表查询教师的历史申请，包含课程名和上课地点
     */
    @org.springframework.data.jpa.repository.Query(value = """
        SELECT 
            a.app_id,
            a.admin_id,
            a.sec_id,
            a.reason,
            a.teacher_id,
            a.suggestion,
            a.final,
            c.title as courseTitle,
            s.semester as semester,
            s.year as year,
            pi.name as teacherName,
            CONCAT(cr.campus, '-', cr.building, '-', cr.room_number) as classroomLocation
        FROM application a
        LEFT JOIN section s ON a.sec_id = s.sec_id
        LEFT JOIN course c ON s.course_id = c.course_id
        LEFT JOIN classroom cr ON s.classroom_id = cr.classroom_id
        LEFT JOIN teacher t ON a.teacher_id = t.user_id
        LEFT JOIN user u ON t.user_id = u.user_id
        LEFT JOIN personal_information pi ON u.personal_infor_id = pi.personal_infor_id
        WHERE a.teacher_id = :teacherId
        ORDER BY a.app_id DESC
        """, nativeQuery = true)
    List<java.util.Map<String, Object>> findHistoryWithDetailByTeacherId(@org.springframework.data.repository.query.Param("teacherId") Integer teacherId);
    
    /**
     * 联表查询所有申请记录的详细信息，包含教师名称、课程名称、课程学期、教室信息
     */
    @org.springframework.data.jpa.repository.Query(value = """
        SELECT 
            a.app_id,
            a.admin_id,
            a.sec_id,
            a.reason,
            a.teacher_id,
            a.suggestion,
            a.final,
            c.title as courseTitle,
            s.semester as semester,
            s.year as year,
            pi.name as teacherName,
            CONCAT(cr.campus, '-', cr.building, '-', cr.room_number) as classroomLocation
        FROM application a
        LEFT JOIN section s ON a.sec_id = s.sec_id
        LEFT JOIN course c ON s.course_id = c.course_id
        LEFT JOIN classroom cr ON s.classroom_id = cr.classroom_id
        LEFT JOIN teacher t ON a.teacher_id = t.user_id
        LEFT JOIN user u ON t.user_id = u.user_id
        LEFT JOIN personal_information pi ON u.personal_infor_id = pi.personal_infor_id
        ORDER BY a.app_id DESC
        """, nativeQuery = true)
    List<java.util.Map<String, Object>> findAllWithDetails();
    
    /**
     * 根据管理员ID联表查询申请记录详细信息
     */
    @org.springframework.data.jpa.repository.Query(value = """
        SELECT 
            a.app_id,
            a.admin_id,
            a.sec_id,
            a.reason,
            a.teacher_id,
            a.suggestion,
            a.final,
            c.title as courseTitle,
            s.semester as semester,
            s.year as year,
            pi.name as teacherName,
            CONCAT(cr.campus, '-', cr.building, '-', cr.room_number) as classroomLocation
        FROM application a
        LEFT JOIN section s ON a.sec_id = s.sec_id
        LEFT JOIN course c ON s.course_id = c.course_id
        LEFT JOIN classroom cr ON s.classroom_id = cr.classroom_id
        LEFT JOIN teacher t ON a.teacher_id = t.user_id
        LEFT JOIN user u ON t.user_id = u.user_id
        LEFT JOIN personal_information pi ON u.personal_infor_id = pi.personal_infor_id
        WHERE a.admin_id = :adminId
        ORDER BY a.app_id DESC
        """, nativeQuery = true)
    List<java.util.Map<String, Object>> findDetailsByAdminId(@org.springframework.data.repository.query.Param("adminId") Integer adminId);
    
    /**
     * 根据申请ID联表查询申请记录详细信息
     */
    @org.springframework.data.jpa.repository.Query(value = """
        SELECT 
            a.app_id,
            a.admin_id,
            a.sec_id,
            a.reason,
            a.teacher_id,
            a.suggestion,
            a.final,
            c.title as courseTitle,
            s.semester as semester,
            s.year as year,
            pi.name as teacherName,
            CONCAT(cr.campus, '-', cr.building, '-', cr.room_number) as classroomLocation
        FROM application a
        LEFT JOIN section s ON a.sec_id = s.sec_id
        LEFT JOIN course c ON s.course_id = c.course_id
        LEFT JOIN classroom cr ON s.classroom_id = cr.classroom_id
        LEFT JOIN teacher t ON a.teacher_id = t.user_id
        LEFT JOIN user u ON t.user_id = u.user_id
        LEFT JOIN personal_information pi ON u.personal_infor_id = pi.personal_infor_id
        WHERE a.app_id = :appId
        """, nativeQuery = true)
    java.util.Map<String, Object> findDetailsByAppId(@org.springframework.data.repository.query.Param("appId") Integer appId);
    
    // @org.springframework.data.jpa.repository.Query(value = """
    //     SELECT 
    //         a.*, 
    //         c.title as courseTitle,
    //         CONCAT(cr.campus, '-', cr.building, '-', cr.room_number) as classroomLocation
    //     FROM application a
    //     LEFT JOIN section s ON a.sec_id = s.sec_id
    //     LEFT JOIN course c ON s.course_id = c.course_id
    //     LEFT JOIN classroom cr ON s.classroom_id = cr.classroom_id
    //     WHERE a.teacher_id = ?1
    //     ORDER BY a.sec_id DESC
    //     LIMIT ?2 OFFSET ?3
    //     """, nativeQuery = true)
    // List<java.util.Map<String, Object>> findHistoryWithDetailByTeacherIdPaged(Integer teacherId, int limit, int offset);
}