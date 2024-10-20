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

@Slf4j
@Component

public class StatusPersistence {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatusPersistence(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //프로그램 실행 여부 적재
    public void updateStatus(String pgmId, Integer scssYn) {
        String sql = "UPDATE PGM_EXEC_PTCL SET SCSS_YN = ? WHERE PGM_NM = ?";
        jdbcTemplate.update(sql, scssYn, pgmId);  // 상태값을 DB에 업데이트
    }



}
