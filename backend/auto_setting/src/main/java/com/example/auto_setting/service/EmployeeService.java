package com.example.auto_setting.service;


import com.example.auto_setting.controller.EmployeeController;
import com.example.auto_setting.persistence.EmployeePersistence;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class EmployeeService {

    @Autowired
    private EmployeePersistence employeePersistence;

    //직원 검증 메서드
    public boolean validateEmployee(EmployeeController.ProgramDto programDto) {
        if (employeePersistence.existsByEmpNo(programDto.getEmpNo())) {
            return employeePersistence.isHeadOffice(programDto.getEmpNo());  // HDOFC_YN 값이 1이면 true 반환
        }
        return false;  // 직원이 존재하지 않거나 HDOFC_YN이 1이 아니면 false
    }

    //empNo로 직원 이름을 조회하는 메서드
    public String getEmployeeName(String empNo) {
        return employeePersistence.getEmpNameByEmpNo(empNo);
    }

}
