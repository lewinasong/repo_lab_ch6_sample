package com.example.auto_setting.service;


import com.example.auto_setting.persistence.EmployeePersistence;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class EmployeeService {

    @Autowired
    private EmployeePersistence employeePersistence;

    public boolean validateEmployee(String empNo) {
        return employeePersistence.existsByEmpNo(empNo);
    }
}
