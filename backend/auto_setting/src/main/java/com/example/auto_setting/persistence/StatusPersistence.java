package com.example.auto_setting.persistence;

import com.example.auto_setting.service.ProgramDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

@Slf4j
@Component
public class StatusPersistence {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatusPersistence(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 프로그램 실행 여부 적재
    public void insertStatus(String empNo, Long pgmId, Integer scssYn) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        try {
            String sql = "INSERT INTO PGM_EXEC_PTCL (EMP_NO, PGM_ID, PGM_STR_DT, SCSS_YN, PGM_STR_DTM) " +
                    "VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, empNo, pgmId, today, scssYn, currentTimestamp);
        } catch (Exception e) {
            log.error("Error while inserting status: ", e);
            throw new RuntimeException(e);
        }
    }

    // empNo와 오늘 날짜를 기반으로 데이터를 조회하는 메서드
    public List<PGM_EXEC_PTCL> findByEmpNoAndPgmStrDt(String empNo, LocalDate pgmStrDt) {
        String sql = "SELECT * FROM PGM_EXEC_PTCL WHERE EMP_NO = ? AND PGM_STR_DT = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{empNo, pgmStrDt},
                (rs, rowNum) -> new PGM_EXEC_PTCL(
                        rs.getLong("HIST_ID"),
                        rs.getString("EMP_NO"),
                        rs.getString("PGM_ID"),
                        rs.getDate("PGM_STR_DT").toLocalDate(),
                        rs.getInt("SCSS_YN"),
                        rs.getTimestamp("PGM_STR_DTM").toLocalDateTime()
                )
        );
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PGM_EXEC_PTCL {
        private Long histId;        // HIST_ID 필드 추가
        private String empNo;
        private String pgmId;
        private LocalDate pgmStrDt;
        private Integer scssYn;
        private LocalDateTime pgmStrDtm;
    }
}
