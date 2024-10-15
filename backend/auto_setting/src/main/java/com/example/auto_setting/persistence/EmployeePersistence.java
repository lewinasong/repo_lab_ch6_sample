package com.example.auto_setting.persistence;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeePersistence {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean existsByEmpNo(String empNo) {
        System.out.println("CONNECT TO MYSSQL");
        String sql = "SELECT COUNT(*) FROM EMP_INFO_BASE WHERE EMP_NO = ?";

        // Query를 실행하고, 결과 값을 count로 반환합니다.
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{empNo}, Integer.class);

        return count != null && count > 0;
    }
}