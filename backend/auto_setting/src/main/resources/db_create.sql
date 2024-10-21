CREATE TABLE EMP_INFO_BASE (
                               EMP_NO VARCHAR(7) PRIMARY KEY NOT NULL,
                               EMP_NM VARCHAR(50) NOT NULL,
                               HDOFC_YN INT(1) NOT NULL,
                               SYS_REG_DTM DATETIME NOT NULL,
                               SYS_UPD_DTM DATETIME NOT NULL
);
CREATE TABLE PGM_BASE (
                          PGM_ID VARCHAR(50) PRIMARY KEY NOT NULL,
                          PGM_NM VARCHAR(100) NOT NULL,
                          EMP_NO VARCHAR(7) NOT NULL,
                          FILE_PATH VARCHAR(2000) NOT NULL,
                          REG_YN INT(1) NOT NULL,
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