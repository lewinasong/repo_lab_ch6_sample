package com.example.auto_setting.persistence;

import com.example.auto_setting.entity.ProgramList;
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
    public void update(@NonNull final String empNo, @NonNull final String programIdList) {
        final String sql = "UPDATE PGM_EXEC_BASE SET EXEC_LIST = ? WHERE EMP_NO = ?";
        jdbcTemplate.update(sql, programIdList, empNo);
    }

    @Override
    public void deleteByEmpNo(@NonNull final String empNo) {
        final String sql = "DELETE FROM PGM_EXEC_BASE WHERE EMP_NO = ?";
        jdbcTemplate.update(sql, empNo);
    }

    // Program List와 Program Name을 조합하여 반환하는 메서드
    public List<Map<String, Object>> findProgramListWithNameByEmpNo(@NonNull String empNo) {
        // SQL 쿼리로 Program List와 Program Name을 조회
        String sql = "SELECT pb.PGM_NM, pl.EXEC_LIST " +
                     "FROM PGM_EXEC_BASE pl " +
                     "JOIN PGM_BASE pb ON FIND_IN_SET(pb.PGM_ID, pl.EXEC_LIST) > 0 " +
                     "WHERE pl.EMP_NO = ?";
        
        // SQL 쿼리 실행 및 결과 반환
        return jdbcTemplate.queryForList(sql, empNo);
    }
}
