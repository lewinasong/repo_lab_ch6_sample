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

    //empNo로 직원이 존재하는지 확인하는 메서드
    public boolean existsByEmpNo(String empNo) {
        System.out.println("CONNECT TO MYSQL");
        String sql = "SELECT COUNT(*) FROM EMP_INFO_BASE WHERE EMP_NO = ?";

        //Query를 실행하고, 결과 값을 count로 반환
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{empNo}, Integer.class);

        return count != null && count > 0;
    }

    //empNo로 직원 이름을 조회하는 메서드
    public String getEmpNameByEmpNo(String empNo) {
        String sql = "SELECT EMP_NM FROM EMP_INFO_BASE WHERE EMP_NO = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{empNo}, String.class);
    }

    //empNo로 HDOFC_YN 값을 조회하는 메서드
    public boolean isHeadOffice(String empNo) {
        String sql = "SELECT HDOFC_YN FROM EMP_INFO_BASE WHERE EMP_NO = ?";
        Integer hdofcYn = jdbcTemplate.queryForObject(sql, new Object[]{empNo}, Integer.class);
        return hdofcYn != null && hdofcYn == 1;  // HDOFC_YN이 1인 경우 true 반환
    }

}