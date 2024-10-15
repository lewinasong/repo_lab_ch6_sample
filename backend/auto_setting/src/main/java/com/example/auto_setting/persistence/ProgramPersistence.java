package com.example.auto_setting.persistence;

import com.example.auto_setting.service.ProgramDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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
            ProgramDto dto = new ProgramDto();
            dto.setPgmId(rs.getString("PGM_ID"));
            dto.setPgmNm(rs.getString("PGM_NM"));
            dto.setFilePath(rs.getString("FILE_PATH"));
            dto.setSysRegDtm(rs.getTimestamp("SYS_REG_DTM").toLocalDateTime());
            dto.setSysUpdDtm(rs.getTimestamp("SYS_UPD_DTM").toLocalDateTime());
            return dto;
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

        System.out.println("tst");

        // PGM_ID의 최대값에 1을 더해 새로운 ID 생성
        String pgmIdQuery = "SELECT COALESCE(MAX(PGM_ID), 0) + 1 FROM PGM_BASE";
        Integer pgmId = jdbcTemplate.queryForObject(pgmIdQuery, Integer.class);

        // INSERT 쿼리 작성 (PGM_ID, PGM_NM, EMP_NO, FILE_PATH, REG_YN, SYS_REG_DTM, SYS_UPD_DTM)
        String sql = "INSERT INTO PGM_BASE (PGM_ID, PGM_NM, EMP_NO, FILE_PATH, REG_YN, SYS_REG_DTM, SYS_UPD_DTM) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, pgmId, pgmNm, empNo, filePath, regYn, currentTimestamp, currentTimestamp);
    }

    // 프로그램 수정
    public void updateProgram(String pgmId, String pgmNm, String empNo, String filePath) {
        // 현재일시를 가져오기 위한 LocalDateTime 사용
        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        // UPDATE 쿼리 작성
        String sql = "UPDATE PGM_BASE SET PGM_NM = ?, EMP_NO = ?, FILE_PATH = ?, SYS_UPD_DTM = ? WHERE PGM_ID = ?";
        jdbcTemplate.update(sql, pgmNm, empNo, filePath, currentTimestamp, pgmId);
    }
}