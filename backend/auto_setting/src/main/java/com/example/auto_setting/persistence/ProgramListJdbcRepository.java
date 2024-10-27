package com.example.auto_setting.persistence;

import com.example.auto_setting.entity.ProgramList;
import com.example.auto_setting.service.ProgramDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProgramListJdbcRepository implements ProgramListRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public ProgramList findByEmpNo(@NonNull final String empNo) {
        // empNo는 unique하기 때문에 empNo로 단일값 조회 가능
        final String sql = "SELECT EXEC_LIST_ID, EMP_NO, EXEC_LIST FROM PGM_EXEC_BASE WHERE EMP_NO = ?";

        // SQL 쿼리 실행 및 결과 받아오기
        final List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, empNo);

        // 결과가 존재하는지 확인
        if (rows.isEmpty()) {
            return null; // 결과가 없을 때 null 반환
        }

        // 첫 번째 결과를 가져와서 ProgramList 객체로 변환
        final Map<String, Object> row = rows.get(0);

        return ProgramList.builder()
                .id((Long) row.get("EXEC_LIST_ID"))
                .programIdList((String) row.get("EXEC_LIST"))
                .empNo((String) row.get("EMP_NO"))
                .build();
    }

    @Override
    public Long create(@NonNull final String empNo, @NonNull final String programIdList) {
        final String sql = "INSERT INTO PGM_EXEC_BASE (EMP_NO, EXEC_LIST) VALUES (?, ?)";
        jdbcTemplate.update(sql, empNo, programIdList);
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public void update(String empNo, String programIdList) {
        final String sql = "UPDATE PGM_EXEC_BASE SET EXEC_LIST = ? WHERE EMP_NO = ?";
        int rowsAffected = jdbcTemplate.update(sql, programIdList, empNo);
        if (rowsAffected == 0) {
            throw new RuntimeException("Update failed: no rows affected.");
        }
    }

    @Override
    public void deleteByEmpNo(@NonNull final String empNo) {
        final String sql = "DELETE FROM PGM_EXEC_BASE WHERE EMP_NO = ?";
        jdbcTemplate.update(sql, empNo);
    }

    public List<ProgramDto> findProgramListWithNameByEmpNo(@NonNull String empNo) {
        // SQL 쿼리로 Program List와 Program Name을 조회
        String sql = "SELECT pb.PGM_ID, pb.PGM_NM, pl.EXEC_LIST, pl.EMP_NO " +
                "FROM PGM_EXEC_BASE pl " +
                "JOIN PGM_BASE pb ON FIND_IN_SET(pb.PGM_ID, pl.EXEC_LIST) > 0 " +
                "WHERE pl.EMP_NO = ?";

        // 쿼리 실행 및 결과를 ProgramDto로 매핑
        List<ProgramDto> result = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return ProgramDto.builder()
                    .empNo(rs.getString("EMP_NO"))
                    .pgmId(rs.getLong("PGM_ID"))
                    .pgmNm(rs.getString("PGM_NM"))
                    .build();
        }, empNo);

        // 결과가 없으면 예외 처리
        if (result.isEmpty()) {
            throw new RuntimeException("해당 직원번호에 대한 프로그램이 존재하지 않습니다.");
        }

        return result;
    }
}
