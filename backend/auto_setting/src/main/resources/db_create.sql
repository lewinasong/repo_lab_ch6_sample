CREATE DATABASE PGM_MGNT_DB;
USE  PGM_MGNT_DB;
CREATE TABLE EMP_INFO_BASE (
                               EMP_NO VARCHAR(7) PRIMARY KEY NOT NULL,
                               EMP_NM VARCHAR(50) NOT NULL,
                               HDOFC_YN INT(1) NOT NULL,
                               SYS_REG_DTM DATETIME NOT NULL,
                               SYS_UPD_DTM DATETIME NOT NULL
);
CREATE TABLE PGM_BASE (
                          PGM_ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                          PGM_NM VARCHAR(100) NOT NULL,
                          EMP_NO VARCHAR(7) NOT NULL,
                          FILE_PATH VARCHAR(2000) NOT NULL,
                          REG_YN INT(1) NOT NULL,
                          SLEEP_TIME INT(100) NOT NULL,
                          SYS_REG_DTM DATETIME NOT NULL,
                          SYS_UPD_DTM DATETIME NOT NULL
);
CREATE TABLE PGM_EXEC_BASE (
                               EXEC_LIST_ID BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                               EMP_NO VARCHAR(7) NOT NULL,
                               EXEC_LIST VARCHAR(255) NOT NULL
);
CREATE TABLE PGM_EXEC_PTCL (
                               HIST_ID VARCHAR(50) PRIMARY KEY NOT NULL, -- SEQUENCE
                               EMP_NO VARCHAR(7) NOT NULL,
                               PGM_ID VARCHAR(50) NOT NULL,
                               PGM_STR_DT DATE NOT NULL,
                               SCSS_YN INT(1) NOT NULL,
                               PGM_STR_DTM DATETIME NOT NULL
);

--INSERT INTO EMP_INFO_BASE (EMP_NO, EMP_NM, HDOFC_YN, SYS_REG_DTM, SYS_UPD_DTM)
--VALUES
--('1657429', '박보경', 1, '2024-10-10 00:00:00', '2024-10-10 00:00:00'),
--('2349856', '정유진', 1, '2024-10-11 00:00:00', '2024-10-11 00:00:00'),
--('3415982', '정민수', 0, '2024-10-12 00:00:00', '2024-10-12 00:00:00'),
--('4523698', '최예진', 1, '2024-10-12 00:00:00', '2024-10-12 00:00:00'),
--('5136987', '송지현', 1, '2024-10-11 00:00:00', '2024-10-11 00:00:00'),
--('9871529', '김은아', 1, '2024-10-10 00:00:00', '2024-10-10 00:00:00'),
--('1234567', '홍길동', 1, '2024-10-10 00:00:00', '2024-10-10 00:00:00');
--INSERT INTO PGM_EXEC_PTCL (HIST_ID, EMP_NO, PGM_ID, PGM_STR_DT, SCSS_YN, PGM_STR_DTM)
--VALUES ('1', '1234567', '1', CURDATE(), 1, NOW());
--INSERT INTO PGM_EXEC_PTCL (HIST_ID, EMP_NO, PGM_ID, PGM_STR_DT, SCSS_YN, PGM_STR_DTM)
--VALUES ('2', '1234567', '2', CURDATE(), 1, NOW());
--INSERT INTO PGM_EXEC_PTCL (HIST_ID, EMP_NO, PGM_ID, PGM_STR_DT, SCSS_YN, PGM_STR_DTM)
--VALUES ('3', '1234567', '6', CURDATE(), 1, NOW());