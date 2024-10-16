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
                    .pgmId(rs.getString("PGM_ID"))
                    .pgmNm(rs.getString("PGM_NM"))
                    .filePath(rs.getString("FILE_PATH"))
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
    public void saveProgram(String pgmNm, String empNo, String filePath, Integer regYn) {
        // 현재일시를 가져오기 위한 LocalDateTime 사용
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        System.out.println("filePath");
        //log.info("Hi {}", now);

        // PGM_ID의 최대값에 1을 더해 새로운 ID 생성
        String pgmIdQuery = "SELECT COALESCE(MAX(PGM_ID), 0) + 1 FROM PGM_BASE";
        Integer pgmId = jdbcTemplate.queryForObject(pgmIdQuery, Integer.class);
        // pgmIdString = pgmId.toString();

        // INSERT 쿼리 작성 (PGM_ID, PGM_NM, EMP_NO, FILE_PATH, REG_YN, SYS_REG_DTM, SYS_UPD_DTM)
        try {
            String sql = "INSERT INTO PGM_BASE (PGM_ID, PGM_NM, EMP_NO, FILE_PATH, REG_YN, SYS_REG_DTM, SYS_UPD_DTM) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql, pgmId, pgmNm, empNo, filePath, regYn, currentTimestamp, currentTimestamp);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error("Error while saving program: ", e);
            throw new RuntimeException(e);
        }

    }

    // 프로그램 수정
    public void updateProgram(String pgmId, String pgmNm, String empNo, String filePath) {
        // 현재일시를 가져오기 위한 LocalDateTime 사용
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        try{
        // UPDATE 쿼리 작성
        String sql = "UPDATE PGM_BASE SET PGM_NM = ?, FILE_PATH = ?, SYS_UPD_DTM = ? WHERE PGM_ID = ? AND EMP_NO = ?";
        jdbcTemplate.update(sql, pgmNm, filePath, currentTimestamp, pgmId, empNo);
        } catch (Exception e) {
            //throw new RuntimeException(e);
            log.error("Error while saving program: ", e);
            throw new RuntimeException(e);
        }


        /*
        // EMP_NO로 데이터가 있는지 확인
        String checkQuery = "SELECT COUNT(*) FROM PGM_EXEC_BASE WHERE EMP_NO = ?";
        Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, empNo);

        // PGM_ID 리스트 업데이트를 위한 SELECT
        String execListQuery = "SELECT EXEC_LIST FROM PGM_EXEC_BASE WHERE EMP_NO = ?";
        String execList = jdbcTemplate.queryForObject(execListQuery, String.class, empNo);

        if (count == 0) {
            // 데이터가 없으면 INSERT
            String insertQuery = "INSERT INTO PGM_EXEC_BASE (EXEC_LIST_ID, EMP_NO, EXEC_LIST) VALUES (?, ?, ?)";
            String newExecListId = generateExecListId(); // EXEC_LIST_ID 생성 로직
            jdbcTemplate.update(insertQuery, newExecListId, empNo, pgmId); // 초기 EXEC_LIST는 pgmId로 시작
        } else {
            // 데이터가 있으면 UPDATE
            String updatedExecList = execList + "," + pgmId; // 기존 리스트에 새로운 pgmId 추가
            String updateQuery = "UPDATE PGM_EXEC_BASE SET EXEC_LIST = ? WHERE EMP_NO = ?";
            jdbcTemplate.update(updateQuery, updatedExecList, empNo);
        }
        */

    }

    // EXEC_LIST_ID 생성 로직 (예시)
    private String generateExecListId() {
        String query = "SELECT COALESCE(MAX(EXEC_LIST_ID), 0) + 1 FROM PGM_EXEC_BASE";
        String maxId = jdbcTemplate.queryForObject(query, String.class);

        return maxId;
    }

    // 프로그램 삭제 (REG_YN을 0으로 업데이트)
    public void deleteProgram(String pgmId) {
        String sql = "UPDATE PGM_BASE SET REG_YN = 0, SYS_UPD_DTM = NOW() WHERE PGM_ID = ?";
        try {
            jdbcTemplate.update(sql, pgmId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to update REG_YN: " + e.getMessage());
        }
    }




}