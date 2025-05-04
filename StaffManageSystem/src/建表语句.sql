CREATE DATABASE staff;
USE staff;
CREATE TABLE user(
    username VARCHAR(15) PRIMARY KEY,
    timestamp TIMESTAMP,
    password CHAR(64),
    level TINYINT DEFAULT 0
);
CREATE TABLE department(
    id INT PRIMARY KEY,
    name VARCHAR(10)
);
CREATE TABLE staff(
    id INT PRIMARY KEY,
    name VARCHAR(10),
    gender ENUM('男','女'),
    birthday DATE,
    telephone CHAR(11),
    department INT,
    position VARCHAR(10),
    salary DOUBLE,
    CONSTRAINT FOREIGN KEY(department) REFERENCES department(id)
);

CREATE VIEW visitor AS SELECT username,timestamp,password FROM user;

CREATE USER 'public'@'%' IDENTIFIED BY '123456';
GRANT SELECT ON user TO 'public'@'%';
GRANT SELECT, INSERT ON visitor TO 'public'@'%';

CREATE USER 'default'@'%' IDENTIFIED BY '123456';
GRANT SELECT, UPDATE ON visitor TO 'default'@'%';
GRANT SELECT ON user TO 'default'@'%';
GRANT SELECT ON staff TO 'default'@'%';
GRANT SELECT ON department TO 'default'@'%';

CREATE USER 'operator'@'%' IDENTIFIED BY '123456';
GRANT SELECT ON user TO 'operator'@'%';
GRANT SELECT, UPDATE, DELETE ON visitor TO 'operator'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON staff TO 'operator'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON department TO 'operator'@'%';

CREATE USER 'admin'@'%' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON staff.* TO 'admin'@'%';


INSERT INTO department VALUES (1, '运营部');
INSERT INTO department VALUES (2, '行政部');
INSERT INTO department VALUES (3, '信息技术部');
INSERT INTO department VALUES (4, '销售部');
INSERT INTO department VALUES (5, '市场部');
INSERT INTO department VALUES (6, '人力资源部');
INSERT INTO department VALUES (7, '财务部');



INSERT INTO staff VALUES (1, '徐杰', '男', '2003-11-30', '18990505342', '3', '工程师', 6137.85);
INSERT INTO staff VALUES (2, '刘红梅', '男', '2003-08-29', '18262469714', '5', '工程师', 5363.07);
INSERT INTO staff VALUES (3, '颜帆', '女', '1977-05-28', '15898098793', '6', '助理', 7985.32);
INSERT INTO staff VALUES (4, '周萍', '男', '1978-07-26', '18502817328', '7', '总监', 4866.04);
INSERT INTO staff VALUES (5, '田静', '女', '1986-05-18', '13483247733', '6', '总监', 3604.99);
INSERT INTO staff VALUES (6, '李瑜', '男', '1990-05-06', '18609890803', '5', '工程师', 7894.84);
INSERT INTO staff VALUES (7, '欧阳敏', '女', '1973-09-02', '18541405895', '3', '助理', 3673.48);
INSERT INTO staff VALUES (8, '莫玉', '男', '2002-08-03', '18840781383', '6', '销售员', 5839.31);
INSERT INTO staff VALUES (9, '贺海燕', '女', '1995-07-26', '18760027152', '3', '经理', 9083.77);
INSERT INTO staff VALUES (10, '纪小红', '女', '1978-07-07', '15307386309', '5', '总监', 6967.07);
INSERT INTO staff VALUES (11, '吕飞', '女', '1991-12-05', '14596198849', '7', '总监', 7898.62);
INSERT INTO staff VALUES (12, '林燕', '男', '1978-05-25', '13184579944', '3', '经理', 6239.08);
INSERT INTO staff VALUES (13, '董倩', '男', '1992-09-24', '18185667735', '4', '顾问', 6050.61);
INSERT INTO staff VALUES (14, '白建', '男', '1991-07-02', '13475374464', '7', '专员', 10972.25);
INSERT INTO staff VALUES (15, '孙丹丹', '女', '1996-08-26', '18075374571', '3', '总监', 6016.92);
INSERT INTO staff VALUES (16, '何峰', '女', '1965-09-23', '18179613187', '4', '总监', 8863.02);
INSERT INTO staff VALUES (17, '张峰', '女', '1964-07-04', '13612129358', '4', '专员', 3861.71);
INSERT INTO staff VALUES (18, '廖建', '男', '1980-08-06', '18991576817', '7', '顾问', 11332.31);
INSERT INTO staff VALUES (19, '梁建军', '女', '1964-10-29', '13031972364', '7', '总监', 9858.03);
INSERT INTO staff VALUES (20, '曾桂荣', '女', '1993-11-18', '15608465396', '7', '经理', 3090.8);
INSERT INTO staff VALUES (21, '吴英', '男', '1978-06-25', '13570623418', '4', '经理', 11932.28);
INSERT INTO staff VALUES (22, '苏想', '男', '2000-06-23', '15801218931', '1', '工程师', 8985.38);
INSERT INTO staff VALUES (23, '王洁', '女', '1973-01-09', '13115099090', '2', '实习生', 6563.15);
INSERT INTO staff VALUES (24, '徐丽华', '男', '1989-11-12', '15671560466', '6', '工程师', 8614.32);
INSERT INTO staff VALUES (25, '王阳', '女', '1978-11-27', '13425596145', '2', '工程师', 11775.15);
INSERT INTO staff VALUES (26, '黄婷', '女', '1980-06-15', '18696620972', '7', '工程师', 4827.21);
INSERT INTO staff VALUES (27, '许淑珍', '男', '2000-04-01', '18169771040', '2', '总监', 4253.69);
INSERT INTO staff VALUES (28, '许华', '男', '1999-04-20', '18509315923', '2', '顾问', 10823.79);
INSERT INTO staff VALUES (29, '王文', '男', '1990-07-29', '15010274381', '1', '工程师', 10210.49);
INSERT INTO staff VALUES (30, '莫海燕', '男', '2004-03-23', '13402382865', '1', '实习生', 11660.18);
INSERT INTO staff VALUES (31, '王小红', '男', '1992-03-09', '15170700621', '6', '助理', 9044.83);
INSERT INTO staff VALUES (32, '刘明', '女', '1987-03-05', '13178093650', '6', '实习生', 3715.08);
INSERT INTO staff VALUES (33, '张丽丽', '男', '1979-02-28', '15729291712', '3', '顾问', 10248.67);
INSERT INTO staff VALUES (34, '夏婷婷', '女', '1982-06-19', '15207867906', '2', '实习生', 10743.73);
INSERT INTO staff VALUES (35, '周辉', '男', '1988-12-29', '18077260524', '2', '经理', 11773.97);
INSERT INTO staff VALUES (36, '刘荣', '男', '1978-01-02', '15027538691', '1', '实习生', 4501.66);
INSERT INTO staff VALUES (37, '叶志强', '男', '1995-05-18', '15608293690', '1', '助理', 6780.36);
INSERT INTO staff VALUES (38, '杨小红', '女', '1984-08-17', '13185227016', '5', '经理', 7120.61);
INSERT INTO staff VALUES (39, '刘洁', '女', '1996-03-15', '15118966489', '3', '总监', 10816.43);
INSERT INTO staff VALUES (40, '王东', '男', '2006-05-16', '18297830241', '4', '专员', 4220.75);
INSERT INTO staff VALUES (41, '杨利', '女', '1986-09-23', '18246504212', '7', '实习生', 9270.86);
INSERT INTO staff VALUES (42, '覃雷', '女', '1992-09-05', '18041680667', '6', '专员', 3690.11);
INSERT INTO staff VALUES (43, '赵萍', '女', '1972-03-14', '15535674537', '7', '总监', 6677.73);
INSERT INTO staff VALUES (44, '江亮', '女', '2000-11-14', '13006675491', '3', '专员', 5890.82);
INSERT INTO staff VALUES (45, '李鑫', '男', '1977-12-21', '15238443930', '1', '总监', 3462.99);
INSERT INTO staff VALUES (46, '郭欢', '女', '1979-02-04', '18784832405', '5', '专员', 11644.62);
INSERT INTO staff VALUES (47, '吴晶', '男', '2005-06-22', '15797239751', '2', '工程师', 5808.48);
INSERT INTO staff VALUES (48, '魏斌', '女', '2007-03-17', '18692580153', '1', '顾问', 10546.59);
INSERT INTO staff VALUES (49, '吴秀珍', '女', '1964-07-09', '13344692348', '5', '专员', 5464.79);
INSERT INTO staff VALUES (50, '陈文', '女', '1988-05-08', '13002266837', '4', '总监', 4844.94);
INSERT INTO staff VALUES (51, '李云', '女', '2004-04-17', '18045256617', '4', '助理', 4451.39);
INSERT INTO staff VALUES (52, '廖建', '男', '2006-07-13', '15356123213', '4', '专员', 7855.17);
INSERT INTO staff VALUES (53, '韩玉', '女', '1978-01-27', '15903164813', '3', '顾问', 3782.01);
INSERT INTO staff VALUES (54, '蔡兵', '男', '1976-01-29', '13787644721', '4', '顾问', 11743.5);
INSERT INTO staff VALUES (55, '吴璐', '女', '1992-12-23', '15076724697', '6', '顾问', 9189.64);
INSERT INTO staff VALUES (56, '高斌', '女', '1969-06-29', '18001116170', '1', '实习生', 8302.36);
INSERT INTO staff VALUES (57, '邓丹丹', '男', '1971-02-11', '14592236801', '3', '总监', 4467.53);
INSERT INTO staff VALUES (58, '董玉珍', '女', '2000-11-18', '13076294571', '4', '经理', 4612.36);
INSERT INTO staff VALUES (59, '冷燕', '女', '1964-07-01', '13974922904', '7', '助理', 5996.46);
INSERT INTO staff VALUES (60, '黄畅', '男', '1990-08-08', '14716645894', '2', '实习生', 5269.67);
INSERT INTO staff VALUES (61, '王红梅', '女', '2002-01-17', '13279218814', '5', '专员', 10414.88);
INSERT INTO staff VALUES (62, '郭琴', '男', '1971-02-25', '13996773194', '6', '顾问', 10015.81);
INSERT INTO staff VALUES (63, '张军', '男', '1996-09-08', '13706479450', '3', '专员', 9822.31);
INSERT INTO staff VALUES (64, '陈玉', '女', '1998-11-24', '13571757011', '5', '总监', 9692.28);
INSERT INTO staff VALUES (65, '高琴', '男', '1996-08-25', '18923631374', '7', '总监', 11000.56);
INSERT INTO staff VALUES (66, '徐玉兰', '女', '1965-12-21', '15043445590', '2', '专员', 10626.84);
INSERT INTO staff VALUES (67, '万瑜', '男', '1971-07-26', '18075894705', '4', '专员', 6542.14);
INSERT INTO staff VALUES (68, '唐彬', '女', '1968-07-22', '18611786880', '1', '顾问', 7985.45);
INSERT INTO staff VALUES (69, '林涛', '男', '1969-01-17', '13944996471', '1', '经理', 5386.16);
INSERT INTO staff VALUES (70, '李建军', '男', '1994-10-11', '13978725983', '7', '总监', 5212.26);
INSERT INTO staff VALUES (71, '丁阳', '男', '1967-08-26', '15222920641', '5', '助理', 5497.74);
INSERT INTO staff VALUES (72, '李金凤', '男', '1989-08-04', '15207164164', '6', '工程师', 3440.03);
INSERT INTO staff VALUES (73, '赵伟', '女', '1998-05-24', '18592056496', '7', '专员', 10323.19);
INSERT INTO staff VALUES (74, '李鹏', '男', '1991-10-27', '15878961207', '2', '实习生', 6573.97);
INSERT INTO staff VALUES (75, '程宇', '女', '1994-06-19', '13772961341', '1', '顾问', 4012.41);
INSERT INTO staff VALUES (76, '王萍', '女', '2005-02-12', '13017678000', '4', '顾问', 7313.85);
INSERT INTO staff VALUES (77, '杜华', '男', '2005-08-02', '13964611883', '6', '专员', 11504.1);
INSERT INTO staff VALUES (78, '罗亮', '女', '1981-07-05', '18125786026', '7', '工程师', 7240.94);
INSERT INTO staff VALUES (79, '杨凯', '男', '1971-10-26', '14508908966', '3', '顾问', 7587.17);
INSERT INTO staff VALUES (80, '姜想', '女', '1981-04-02', '13804676623', '3', '专员', 5285.62);
INSERT INTO staff VALUES (81, '陈勇', '男', '1970-11-14', '18649227748', '7', '实习生', 3108.33);
INSERT INTO staff VALUES (82, '王凤英', '男', '1989-06-26', '15647090903', '7', '实习生', 11046.53);
INSERT INTO staff VALUES (83, '刘艳', '女', '1990-09-19', '14734841714', '7', '实习生', 7936.14);
INSERT INTO staff VALUES (84, '黄秀珍', '男', '1987-01-25', '18771945133', '6', '经理', 10195.89);
INSERT INTO staff VALUES (85, '王阳', '女', '1994-11-30', '13496171021', '3', '助理', 6970.54);
INSERT INTO staff VALUES (86, '刘洁', '男', '1982-06-09', '18926054858', '1', '总监', 5763.07);
INSERT INTO staff VALUES (87, '葛欣', '男', '1978-04-15', '13161285363', '7', '总监', 9142.45);
INSERT INTO staff VALUES (88, '孙秀荣', '男', '1983-10-11', '15755900140', '7', '顾问', 3830.46);
INSERT INTO staff VALUES (89, '薛兰英', '男', '1990-08-21', '13136103190', '1', '顾问', 6920.42);
INSERT INTO staff VALUES (90, '张丽华', '女', '1972-05-31', '15961421180', '2', '专员', 8638.55);
INSERT INTO staff VALUES (91, '周凯', '男', '1976-11-02', '15542922525', '5', '实习生', 3470.05);
INSERT INTO staff VALUES (92, '王浩', '女', '1988-05-31', '13559496925', '1', '专员', 5599.38);
INSERT INTO staff VALUES (93, '刘丽', '男', '1971-12-22', '18042193770', '1', '总监', 3458.3);
INSERT INTO staff VALUES (94, '王峰', '男', '1997-01-05', '13724602064', '5', '经理', 7994.56);
INSERT INTO staff VALUES (95, '陈雪', '男', '1976-02-04', '15735818601', '1', '实习生', 9137.15);
INSERT INTO staff VALUES (96, '程佳', '女', '1994-10-22', '18565488560', '3', '经理', 5192.9);
INSERT INTO staff VALUES (97, '毕利', '女', '1977-09-18', '15838483350', '5', '顾问', 11742.05);
INSERT INTO staff VALUES (98, '张丹', '男', '1967-09-30', '13160149149', '4', '专员', 11582.41);
INSERT INTO staff VALUES (99, '蒋超', '女', '2000-06-28', '18698034864', '4', '专员', 7900.55);
INSERT INTO staff VALUES (100, '马佳', '男', '1967-07-01', '15858335311', '1', '实习生', 7420.75);
