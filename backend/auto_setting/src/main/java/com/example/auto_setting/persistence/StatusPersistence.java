package com.example.auto_setting.persistence;

import com.example.auto_setting.service.ProgramDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    //프로그램 실행 여부 적재
    public void insertStatus(String empNo, String pgmId, Integer scssYn) {
        //현재 일자를 가져오기 위한 LocalDate 사용
        LocalDate today = LocalDate.now();

        //현재일시를 가져오기 위한 LocalDateTime 사용
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        //HIST_ID의 최대값에 1을 더해 새로운 ID 생성
        String histIdQuery = "SELECT COALESCE(MAX(HIST_ID), 0) + 1 FROM PGM_EXEC_PTCL";
        Integer histId = jdbcTemplate.queryForObject(histIdQuery, Integer.class);

        //INSERT 쿼리 작성 (HIST_ID, EMP_NO, PGM_ID, PGM_STR_DT, SCSS_YN, PGM_STR_DTM)
        try {
            String sql = "insert into PGM_EXEC_PTCL (HIST_ID, EMP_NO, PGM_ID, PGM_STR_DT, SCSS_YN, PGM_STR_DTM)" +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, histId, empNo, pgmId, today, scssYn, currentTimestamp);  // 상태값을 DB에 업데이트
        }catch (Exception e) {
            //throw new RuntimeException(e);
            log.error("Error while inserting status: ", e);
            throw new RuntimeException(e);
        }
            //ALTER TABLE PGM_EXEC_PTCL MODIFY HIST_ID INT AUTO_INCREMENT; 수행 시 HIST_ID 생략 가능
    }



}
