package com.example.auto_setting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auto_setting.persistence.ClientPersistence;
import com.example.auto_setting.persistence.ClientPersistence.ProgramInfo;

@Service
public class ClientService {

    @Autowired
    private ClientPersistence clientPersistence;

    // 직원 번호를 사용하여 프로그램 정보 목록을 조회하는 메서드
    public List<ProgramInfo> getProgramInfoByEmpNo(String empNo) {
        System.out.println("Retrieving program information for employee: " + empNo);
        return clientPersistence.clientSelect(empNo);
    }
}