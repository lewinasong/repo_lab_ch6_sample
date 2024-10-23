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
public class ProgramPersistence {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProgramPersistence(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 프로그램 조회
    public List<ProgramDto> getProgramByEmpNo(String empNo) {
        // SELECT 쿼리 작성
        String sql = "SELECT PGM_ID, PGM_NM, FILE_PATH, SYS_REG_DTM, SYS_UPD_DTM " +
                "FROM PGM_BASE " +
                "WHERE EMP_NO = ? AND REG_YN = '1'";

        // 쿼리 결과를 ProgramDto 객체로 매핑
        List<ProgramDto> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return ProgramDto.builder()
                    .pgmId(rs.getLong("PGM_ID"))
                    .pgmNm(rs.getString("PGM_NM"))
                    .filePath(rs.getString("FILE_PATH"))
                    .sleepTime(rs.getInt("SLEEP_TIME"))
                    .sysRegDtm(rs.getTimestamp("SYS_REG_DTM").toLocalDateTime())
                    .sysUpdDtm(rs.getTimestamp("SYS_UPD_DTM").toLocalDateTime())
                    .build();
        }, empNo);

        // 결과가 없으면 예외 처리
        if (result.isEmpty()) {
            throw new RuntimeException("해당 직원번호에 대한 프로그램이 존재하지 않습니다.");
        }

        return result;
    }

    // 프로그램 등록
    public Long saveProgram(String pgmNm, String empNo, String filePath, Integer regYn, Integer sleepTime) {
        // 현재일시를 가져오기 위한 LocalDateTime 사용
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        System.out.println("filePath");
        //log.info("Hi {}", now);

        // PGM_ID의 최대값에 1을 더해 새로운 ID 생성
        //pgmIdQuery = "SELECT COALESCE(MAX(PGM_ID), 0) + 1 FROM PGM_BASE";
        //Integer pgmId = jdbcTemplate.queryForObject(pgmIdQuery, Integer.class);

        // INSERT 쿼리 작성 (PGM_ID, PGM_NM, EMP_NO, FILE_PATH, REG_YN, SYS_REG_DTM, SYS_UPD_DTM)
        try {
            String sql = "INSERT INTO PGM_BASE (PGM_NM, EMP_NO, FILE_PATH, REG_YN, SLEEP_TIME, SYS_REG_DTM, SYS_UPD_DTM) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql, pgmNm, empNo, filePath, regYn, sleepTime, currentTimestamp, currentTimestamp);
        } catch (Exception e) {
            log.error("Error while data insert PGM_BASE: ", e);
            throw new RuntimeException(e);
        }
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    // 프로그램 수정
    public void updateProgram(Long pgmId, String pgmNm, String empNo, String filePath, Integer sleepTime) {
        // 현재일시를 가져오기 위한 LocalDateTime 사용
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        try{
            // UPDATE 쿼리 작성
            String sql = "UPDATE PGM_BASE SET PGM_NM = ?, FILE_PATH = ?, SLEEP_TIME = ?, SYS_UPD_DTM = ? WHERE PGM_ID = ? AND EMP_NO = ?";
            jdbcTemplate.update(sql, pgmNm, filePath, sleepTime, currentTimestamp, pgmId, empNo);

            // 업데이트된 행의 개수를 확인
            int rowsAffected = jdbcTemplate.update(sql, pgmNm, filePath, sleepTime, currentTimestamp, pgmId, empNo);
            // 업데이트된 행이 없을 경우 예외 발생
            if (rowsAffected == 0) {
                throw new RuntimeException("No rows updated. Check if the pgmId and empNo are correct.");
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error("Error while saving program: ", e);
            throw new RuntimeException(e);
        }
    }
    // 프로그램 삭제 (REG_YN을 0으로 업데이트)
    public void deleteProgram(Integer pgmId) {
        String sql = "UPDATE PGM_BASE SET REG_YN = 0, SYS_UPD_DTM = NOW() WHERE PGM_ID = ?";
        try {
            jdbcTemplate.update(sql, pgmId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to update REG_YN: " + e.getMessage());
        }
    }




}