drop database inforsystem;

create database inforsystem;

USE inforsystem;
-- 先关闭外键检查，避免因依赖顺序报错
SET FOREIGN_KEY_CHECKS = 0;

-- 按照命名列出所有表，批量删除
DROP TABLE IF EXISTS
    application,
    grade_change,
    grade,
    takes,
    section,
    classroom,
    time_slot,
    course,
    student,
    teacher,
    administrator,
    personal_information,
    `user`,
    department;

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `department` (
    `dept_name` VARCHAR(255) NOT NULL UNIQUE,
    `campus` VARCHAR(255) NOT NULL,
    PRIMARY KEY(`dept_name`)
);

CREATE TABLE `user` (
    `user_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `account_number` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `personal_infor_id` INTEGER NOT NULL,
    `type` ENUM('student', 'teacher', 'administrator') NOT NULL,
    PRIMARY KEY(`user_id`)
);

CREATE TABLE `personal_information` (
    `personal_infor_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `name` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `picture` VARCHAR(255) NOT NULL,
    PRIMARY KEY(`personal_infor_id`)
);

CREATE TABLE `student` (
    `user_id` INTEGER NOT NULL,
    `dept_name` VARCHAR(255) NOT NULL,
    `tot_cred` INTEGER NOT NULL,
    PRIMARY KEY(`user_id`),
    FOREIGN KEY (dept_name) REFERENCES department(dept_name)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE `teacher` (
    `user_id` INTEGER NOT NULL,
    `dept_name` VARCHAR(255) NOT NULL,
    `salary` INTEGER NOT NULL,
    PRIMARY KEY(`user_id`),
    FOREIGN KEY (dept_name) REFERENCES department(dept_name)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE `administrator` (
    `user_id` INTEGER NOT NULL,
    PRIMARY KEY(`user_id`),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE `course` (
    `course_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `title` VARCHAR(255) NOT NULL,
    `dept_name` VARCHAR(255) NOT NULL,
    `credits` INTEGER NOT NULL,
    `course_introduction` VARCHAR(255) NOT NULL,
    `capacity` INTEGER NOT NULL,
    `required_room_type` VARCHAR(255) NOT NULL,
    `grade_year` INT NOT NULL DEFAULT 1,
    `period` INT NOT NULL DEFAULT 1,
    PRIMARY KEY(`course_id`),
    FOREIGN KEY(`dept_name`) REFERENCES `department`(`dept_name`)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE `classroom` (
    `campus` VARCHAR(255) NOT NULL,
    `room_number` INTEGER NOT NULL,
    `capacity` INTEGER NOT NULL,
    `classroom_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `building` VARCHAR(255),
    `type` VARCHAR(255),
    PRIMARY KEY(`classroom_id`)
);

CREATE TABLE `time_slot` (
    `time_slot_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `day` INTEGER NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    PRIMARY KEY(`time_slot_id`)
);

CREATE TABLE `section` (
    `sec_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `course_id` INTEGER NOT NULL,
    `semester` VARCHAR(255) NOT NULL,
    `year` YEAR NOT NULL,
    `classroom_id` INTEGER,
    `time_slot_ids` JSON,
    `teacher_id` INTEGER NOT NULL,
    PRIMARY KEY(`sec_id`),
    FOREIGN KEY(`course_id`) REFERENCES `course`(`course_id`)
        ON DELETE RESTRICT  -- 有学生选课时阻止删除课程
        ON UPDATE CASCADE,
    FOREIGN KEY(`classroom_id`) REFERENCES `classroom`(`classroom_id`)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    FOREIGN KEY(`teacher_id`) REFERENCES `teacher`(`user_id`)
        ON DELETE RESTRICT  -- 有授课关系时阻止删除教师
        ON UPDATE CASCADE
);

CREATE TABLE `takes` (
    `student_id` INTEGER NOT NULL,
    `takes_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `sec_id` INTEGER NOT NULL,
    PRIMARY KEY(`takes_id`),
    FOREIGN KEY(`student_id`) REFERENCES `student`(`user_id`)
        ON DELETE RESTRICT  -- 有选课记录时阻止删除学生
        ON UPDATE CASCADE,
    FOREIGN KEY(`sec_id`) REFERENCES `section`(`sec_id`)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE `grade` (
    `takes_id` INTEGER NOT NULL,
    `grade` INTEGER NOT NULL,
    `proportion` FLOAT NOT NULL,
    `type` ENUM('attending', 'homework', 'test') NOT NULL,
    `grade_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    PRIMARY KEY(`grade_id`),
    FOREIGN KEY(`takes_id`) REFERENCES `takes`(`takes_id`)
        ON DELETE CASCADE  -- 成绩随选课记录删除
        ON UPDATE CASCADE
);

CREATE TABLE `grade_change` (
    `takes_id` INTEGER NOT NULL,
    `teacher_id` INTEGER NOT NULL,
    `result` BOOLEAN,
    `reason` VARCHAR(255) NOT NULL,
    `new_grade` INTEGER NOT NULL,
    `apply_time` DATETIME NOT NULL,
    `check_time` DATETIME,
    `grade_type` VARCHAR(255) NOT NULL,
    `grade_change_id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `grade_id` INTEGER NOT NULL ,
    PRIMARY KEY(`grade_change_id`),
    FOREIGN KEY(`takes_id`) REFERENCES `takes`(`takes_id`)
        ON DELETE CASCADE  -- 申请随选课记录删除
        ON UPDATE CASCADE,
    FOREIGN KEY(`teacher_id`) REFERENCES `teacher`(`user_id`)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    FOREIGN KEY(`grade_id`) REFERENCES `grade`(`grade_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE `application` (
    `sec_id` INTEGER NOT NULL,
    `reason` VARCHAR(256) NOT NULL,
    `teacher` VARCHAR(256) NOT NULL,
    `suggestion` VARCHAR(256),
    `final` BOOLEAN NOT NULL,
    PRIMARY KEY(`sec_id`),
    FOREIGN KEY(`sec_id`) REFERENCES `section`(`sec_id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

ALTER TABLE grade
ADD CONSTRAINT chk_grade_range 
CHECK (grade BETWEEN 0 AND 100);

ALTER TABLE grade
MODIFY COLUMN proportion DECIMAL(3,2),
ADD CONSTRAINT chk_proportion_range 
CHECK (proportion BETWEEN 0 AND 1);

CREATE INDEX idx_takes_student ON takes(student_id);
CREATE INDEX idx_section_course ON section(course_id);
CREATE INDEX idx_grade_take ON grade(takes_id);

CREATE INDEX idx_section_search ON section(semester, year);
CREATE INDEX idx_grade_change_search ON grade_change(takes_id, check_time);
CREATE INDEX idx_application_section ON application(sec_id);

-- 插入部门数据
INSERT INTO department (dept_name, campus) VALUES
('计算机科学', '邯郸校区'),
('数学', '江湾校区'),
('物理', '张江校区'),
('化学', '枫林校区'),
('生物', '邯郸校区'),
('历史', '邯郸校区'),
('文学', '江湾校区'),
('经济', '邯郸校区'),
('管理', '枫林校区'),
('工程', '张江校区'),
('环境科学', '江湾校区'),
('心理学', '邯郸校区');

-- 插入个人信息
INSERT INTO personal_information (name, phone_number, picture) VALUES
('张三', '13800138001', 'zhangsan.jpg'),
('李四', '13800138002', 'lisi.jpg'),
('王五', '13800138003', 'wangwu.jpg'),
('赵六', '13800138004', 'zhaoliu.jpg'),
('钱七', '13800138005', 'qianqi.jpg'),
('孙八', '13800138006', 'sunba.jpg'),
('周九', '13800138007', 'zhoujiu.jpg'),
('吴十', '13800138008', 'wushi.jpg'),
('郑十一', '13800138009', 'zhengshiyi.jpg'),
('王十二', '13800138010', 'wangshier.jpg'),
('张教授', '13900139001', 'prof_zhang.jpg'),
('李教授', '13900139002', 'prof_li.jpg'),
('王教授', '13900139003', 'prof_wang.jpg'),
('赵教授', '13900139004', 'prof_zhao.jpg'),
('刘教授', '13900139005', 'prof_liu.jpg'),
('陈教授', '13900139006', 'prof_chen.jpg'),
('杨教授', '13900139007', 'prof_yang.jpg'),
('周教授', '13900139008', 'prof_zhou.jpg'),
('吴教授', '13900139009', 'prof_wu.jpg'),
('孙教授', '13900139010', 'prof_sun.jpg'),
('管理员1', '13700137001', 'admin1.jpg'),
('管理员2', '13700137002', 'admin2.jpg');

-- 插入用户数据
INSERT INTO `user` (account_number, password, personal_infor_id, `type`) VALUES
('10001', 'pass123', 1, 'student'),
('10002', 'pass123', 2, 'student'),
('10003', 'pass123', 3, 'student'),
('10004', 'pass123', 4, 'student'),
('10005', 'pass123', 5, 'student'),
('10006', 'pass123', 6, 'student'),
('10007', 'pass123', 7, 'student'),
('10008', 'pass123', 8, 'student'),
('10009', 'pass123', 9, 'student'),
('10010', 'pass123', 10, 'student'),
('20001', 'teach456', 11, 'teacher'),
('20002', 'teach456', 12, 'teacher'),
('20003', 'teach456', 13, 'teacher'),
('20004', 'teach456', 14, 'teacher'),
('20005', 'teach456', 15, 'teacher'),
('20006', 'teach456', 16, 'teacher'),
('20007', 'teach456', 17, 'teacher'),
('20008', 'teach456', 18, 'teacher'),
('20009', 'teach456', 19, 'teacher'),
('20010', 'teach456', 20, 'teacher'),
('30001', 'admin789', 21, 'administrator'),
('30002', 'admin789', 22, 'administrator');

-- 插入学生数据
INSERT INTO student (user_id, dept_name, tot_cred) VALUES
(1, '计算机科学', 120),
(2, '数学', 110),
(3, '物理', 100),
(4, '化学', 90),
(5, '生物', 115),
(6, '历史', 95),
(7, '文学', 105),
(8, '经济', 125),
(9, '管理', 85),
(10, '工程', 130),
(11, '计算机科学', 75);

-- 插入教师数据
INSERT INTO teacher (user_id, dept_name, salary) VALUES
(11, '计算机科学', 80000),
(12, '数学', 75000),
(13, '物理', 78000),
(14, '化学', 72000),
(15, '生物', 76000),
(16, '历史', 70000),
(17, '文学', 71000),
(18, '经济', 82000),
(19, '管理', 79000),
(20, '工程', 83000);

-- 插入管理员数据
INSERT INTO administrator (user_id) VALUES
(21),
(22);

-- 插入教室数据
INSERT INTO classroom (campus, room_number, capacity, building, `type`) VALUES
('邯郸校区', 101, 60, '逸夫楼', '多媒体教室'),
('邯郸校区', 201, 40, '二教', '普通教室'),
('江湾校区', 301, 100, '法学楼', '阶梯教室'),
('张江校区', 102, 50, '计算机楼', '实验室'),
('枫林校区', 202, 45, '医学楼', '普通教室'),
('邯郸校区', 302, 70, '三教', '多媒体教室'),
('江湾校区', 103, 55, '物理楼', '实验室'),
('张江校区', 203, 65, '软件楼', '多媒体教室'),
('枫林校区', 303, 80, '护理学院', '阶梯教室'),
('邯郸校区', 104, 30, '四教', '研讨室'),
('江湾校区', 204, 90, '环境学院', '多媒体教室');

-- 插入时间段数据
INSERT INTO time_slot (`day`, start_time, end_time) VALUES
(1, '08:00:00', '09:40:00'),  -- 周一 1-2节
(1, '10:00:00', '11:40:00'),  -- 周一 3-4节
(2, '08:00:00', '09:40:00'),  -- 周二 1-2节
(2, '10:00:00', '11:40:00'),  -- 周二 3-4节
(3, '08:00:00', '09:40:00'),  -- 周三 1-2节
(3, '14:00:00', '15:40:00'),  -- 周三 5-6节
(4, '10:00:00', '11:40:00'),  -- 周四 3-4节
(4, '13:00:00', '14:40:00'),  -- 周四 5-6节
(5, '08:00:00', '09:40:00'),  -- 周五 1-2节
(5, '10:00:00', '11:40:00');  -- 周五 3-4节

-- 插入课程数据
INSERT INTO course (title, dept_name, credits, course_introduction, capacity, required_room_type, grade_year, period) VALUES
('数据结构', '计算机科学', 4, '基本数据结构与算法', 60, '多媒体教室', 2, 2),
('高等数学', '数学', 6, '微积分与线性代数', 100, '阶梯教室', 1, 2),
('量子力学', '物理', 4, '量子理论基础', 40, '普通教室', 3, 2),
('有机化学', '化学', 4, '有机分子结构与反应', 45, '普通教室', 2, 2),
('分子生物学', '生物', 3, '生物分子结构与功能', 50, '实验室', 3, 1),
('中国近代史', '历史', 2, '1840-1949年中国历史', 70, '多媒体教室', 1, 1),
('现代文学', '文学', 3, '20世纪中国文学', 55, '普通教室', 2, 1),
('宏观经济学', '经济', 4, '国民经济总体分析', 65, '多媒体教室', 2, 2),
('项目管理', '管理', 3, '项目管理理论与实务', 30, '研讨室', 3, 1),
('软件工程', '计算机科学', 4, '软件开发流程与方法', 50, '多媒体教室', 3, 2),
('数据库系统', '计算机科学', 4, '数据库原理与应用', 60, '实验室', 3, 2);

-- 插入教学班数据
INSERT INTO section (course_id, semester, `year`, classroom_id, time_slot_ids, teacher_id, time) VALUES
(1, '秋季', 2023, 1, '[1, 3]', 11, 2),   -- 周一1-2节+周二1-2节
(2, '秋季', 2023, 3, '[2, 4]', 12, 2),   -- 周一3-4节+周二3-4节
(3, '秋季', 2023, 2, '[5]', 13, 2),      -- 周三1-2节
(4, '秋季', 2023, 5, '[7]', 14, 2),      -- 周四3-4节
(5, '秋季', 2023, 4, '[8]', 15, 2),      -- 周四5-6节
(6, '春季', 2024, 6, '[6]', 16, 2),      -- 周三5-6节
(7, '春季', 2024, 7, '[9]', 17, 2),      -- 周五1-2节
(8, '春季', 2024, 8, '[10]', 18, 2),     -- 周五3-4节
(9, '春季', 2024, 10, '[3]', 19, 2),     -- 周二1-2节
(10, '秋季', 2023, 1, '[1, 5]', 11, 2),  -- 周一1-2节+周三1-2节
(11, '春季', 2024, 4, '[4, 9]', 11, 2);  -- 周二3-4节+周五1-2节

-- 插入选课记录
INSERT INTO takes (student_id, sec_id) VALUES
(1, 1), (1, 2), (1, 3),
(2, 2), (2, 4), (2, 6),
(3, 3), (3, 5), (3, 7),
(4, 4), (4, 8), (4, 10),
(5, 5), (5, 9), (5, 11),
(6, 6), (6, 1), (6, 3),
(7, 7), (7, 2), (7, 4),
(8, 8), (8, 5), (8, 6),
(9, 9), (9, 7), (9, 8),
(10, 10), (10, 9), (10, 11),
(1, 11), (2, 10), (3, 9);

-- 插入成绩数据
INSERT INTO grade (takes_id, grade, proportion, `type`) VALUES
(1, 85, 0.30, 'attending'),
(1, 90, 0.20, 'homework'),
(1, 88, 0.50, 'test'),
(2, 78, 0.30, 'attending'),
(2, 82, 0.20, 'homework'),
(2, 85, 0.50, 'test'),
(4, 92, 0.30, 'attending'),
(4, 88, 0.20, 'homework'),
(4, 90, 0.50, 'test'),
(7, 76, 0.30, 'attending'),
(7, 80, 0.20, 'homework'),
(7, 82, 0.50, 'test'),
(10, 89, 0.30, 'attending'),
(10, 91, 0.20, 'homework'),
(10, 87, 0.50, 'test'),
(13, 93, 0.30, 'attending'),
(13, 95, 0.20, 'homework'),
(13, 92, 0.50, 'test'),
(16, 81, 0.30, 'attending'),
(16, 79, 0.20, 'homework'),
(16, 83, 0.50, 'test'),
(19, 85, 0.30, 'attending'),
(19, 88, 0.20, 'homework'),
(19, 86, 0.50, 'test'),
(22, 90, 0.30, 'attending'),
(22, 92, 0.20, 'homework'),
(22, 91, 0.50, 'test'),
(25, 77, 0.30, 'attending'),
(25, 75, 0.20, 'homework'),
(25, 80, 0.50, 'test'),
(28, 84, 0.30, 'attending'),
(28, 86, 0.20, 'homework'),
(28, 88, 0.50, 'test');

-- 插入成绩变更申请
INSERT INTO grade_change (takes_id, teacher_id, result, new_grade, apply_time, check_time, grade_type, reason, grade_id) VALUES
(1, 11, NULL, 90, '2023-12-01 10:00:00', NULL, 'test', '试卷批改错误', 3),       -- 关联test成绩(grade_id=3)
(4, 12, 1, 95, '2023-12-02 11:00:00', '2023-12-03 14:00:00', 'homework', '补交作业', 8),  -- 关联homework成绩(grade_id=8)
(7, 13, 0, 85, '2023-12-03 09:30:00', '2023-12-05 10:00:00', 'test', '申请额外加分', 12), -- 关联test成绩(grade_id=12)
(10, 14, 1, 90, '2023-12-04 13:45:00', '2023-12-06 16:00:00', 'attending', '补交假条', 13), -- 关联attending成绩(grade_id=13)
(13, 15, NULL, 95, '2023-12-05 15:20:00', NULL, 'test', '答案争议', 18),    -- 关联test成绩(grade_id=18)
(16, 16, 1, 85, '2023-12-06 10:10:00', '2023-12-08 11:30:00', 'homework', '重新评分', 20), -- 关联homework成绩(grade_id=20)
(19, 17, 0, 90, '2023-12-07 14:00:00', '2023-12-10 09:00:00', 'test', '申请复查', 24),   -- 关联test成绩(grade_id=24)
(22, 18, 1, 93, '2023-12-08 11:00:00', '2023-12-09 14:00:00', 'attending', '补交材料', 25), -- 关联attending成绩(grade_id=25)
(25, 19, NULL, 82, '2023-12-09 16:30:00', NULL, 'homework', '迟交作业', 29),  -- 关联homework成绩(grade_id=29)
(28, 11, 1, 90, '2023-12-10 09:45:00', '2023-12-12 15:00:00', 'test', '批改错误', 33);   -- 关联test成绩(grade_id=33)

-- 插入课程申请
INSERT INTO application (sec_id, reason, teacher, suggestion, final) VALUES
(1, '课程容量不足', '张教授', '同意扩容至70人', 1),
(2, '更换上课时间', '李教授', '建议调整到周四', 0),
(3, '更换教室', '王教授', '物理楼201室更合适', 1),
(4, '增加实验设备', '赵教授', '已批准采购', 1),
(5, '调整考核方式', '刘教授', '建议增加实验考核', 0),
(6, '更换教材', '陈教授', '新版教材已订购', 1),
(7, '增加助教', '杨教授', '已分配研究生助教', 1),
(8, '调整学分', '周教授', '维持原学分', 0),
(9, '课程时间冲突', '吴教授', '建议调整到周三', 1),
(10, '增加实践环节', '张教授', '已安排企业参观', 1);