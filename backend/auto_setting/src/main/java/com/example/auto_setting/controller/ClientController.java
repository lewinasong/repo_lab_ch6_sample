package com.example.auto_setting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auto_setting.persistence.ClientPersistence.ProgramInfo;
import com.example.auto_setting.service.ClientService;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // 직원 번호로 프로그램 정보를 조회하는 엔드포인트
    @GetMapping("/programs/{empNo}")
    public List<ProgramInfo> getProgramsByEmpNo(@PathVariable String empNo) {
        System.out.println("Fetching program information for employee number: " + empNo);
        return clientService.getProgramInfoByEmpNo(empNo);
    }
}