package com.example.auto_setting.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ClientPersistence {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 프로그램 순서, 프로그램명, 경로, SLEEP_TIME 조회
    public List<ProgramInfo> clientSelect(String empNo) {
        System.out.println("CONNECT TO MYSQL");
        
        String sql = """
                WITH RECURSIVE EXEC_LIST_SPLIT AS (
                    -- 초기 쿼리: 첫 번째 프로그램 ID와 나머지 문자열 분리
                    SELECT EXEC_LIST_ID, EMP_NO,
                        CAST(SUBSTRING_INDEX(EXEC_LIST, ',', 1) AS UNSIGNED) AS PGM_ID,
                        TRIM(SUBSTRING(EXEC_LIST, LOCATE(',', EXEC_LIST) + 1)) AS REMAINING,
                        1 AS SEQUENCE -- 순서 값을 1로 설정
                    FROM PGM_EXEC_BASE
                    WHERE EMP_NO = ?
                     AND EXEC_LIST_ID = ( SELECT MAX(EXEC_LIST_ID)
                                            FROM PGM_EXEC_BASE
                                              WHERE EMP_NO = ?
                                          )
                    UNION ALL

                    -- 재귀 쿼리: 남은 문자열에서 다음 프로그램 ID 추출
                    SELECT EXEC_LIST_ID, EMP_NO,
                        CAST(SUBSTRING_INDEX(REMAINING, ',', 1) AS UNSIGNED) AS PGM_ID,
                        IF(LOCATE(',', REMAINING) > 0, TRIM(SUBSTRING(REMAINING, LOCATE(',', REMAINING) + 1)), NULL) AS REMAINING,
                        SEQUENCE + 1 AS SEQUENCE -- 순서 값을 1씩 증가
                    FROM EXEC_LIST_SPLIT
                    WHERE REMAINING IS NOT NULL
                )

                -- 결과: 순서대로 프로그램 정보 조회
                SELECT E.SEQUENCE, E.PGM_ID, P.PGM_NM, P.FILE_PATH, P.SLEEP_TIME 
                FROM EXEC_LIST_SPLIT E
                INNER JOIN PGM_BASE P ON E.PGM_ID = P.PGM_ID
                WHERE P.REG_YN = '1'
                ORDER BY E.SEQUENCE
                """;

        return jdbcTemplate.query(sql, new Object[]{empNo, empNo}, new ProgramInfoRowMapper());
    }

    // ProgramInfo 클래스 정의 
    public static class ProgramInfo {
        private int sequence;  
        private int pgmId;
        private String pgmNm;
        private String filePath;
        private int sleepTime;  

        public ProgramInfo(int sequence, int pgmId, String pgmNm, String filePath, int sleepTime) {
            this.sequence = sequence;
            this.pgmId = pgmId;
            this.pgmNm = pgmNm;
            this.filePath = filePath;
            this.sleepTime = sleepTime;  
        }

        // Getters
        public int getSequence() {
            return sequence;
        }

        public int getPgmId() {
            return pgmId;
        }

        public String getPgmNm() {
            return pgmNm;
        }

        public String getFilePath() {
            return filePath;
        }

        public int getSleepTime() {
            return sleepTime;  
        }

        @Override
        public String toString() {
            return "ProgramInfo{" +
                    "sequence=" + sequence +
                    ", pgmId=" + pgmId +
                    ", pgmNm='" + pgmNm + '\'' +
                    ", filePath='" + filePath + '\'' +
                    ", sleepTime=" + sleepTime +  
                    '}';
        }
    }

    // RowMapper 클래스 정의 (SLEEP_TIME 필드 추가)
    public static class ProgramInfoRowMapper implements RowMapper<ProgramInfo> {
        @Override
        public ProgramInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            int sequence = rs.getInt("SEQUENCE");  
            int pgmId = rs.getInt("PGM_ID");
            String pgmNm = rs.getString("PGM_NM");
            String filePath = rs.getString("FILE_PATH");
            int sleepTime = rs.getInt("SLEEP_TIME");  
            return new ProgramInfo(sequence, pgmId, pgmNm, filePath, sleepTime);
        }
    }
}
