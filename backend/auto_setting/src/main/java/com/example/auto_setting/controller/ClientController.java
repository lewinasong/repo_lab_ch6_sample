package com.example.auto_setting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.auto_setting.persistence.ClientPersistence.ProgramInfo;
import com.example.auto_setting.service.ClientService;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;
    //@CrossOrigin(origins = "http://localhost:8089") // 프론트엔드 주소
    @CrossOrigin(origins = "http://3.209.94.202:8089") // 프론트엔드 주소
    // 직원 번호로 프로그램 정보를 조회하는 엔드포인트
    @GetMapping("/programs/{empNo}")
    public List<ProgramInfo> getProgramsByEmpNo(@PathVariable String empNo) {
        System.out.println("Fetching program information for employee number: " + empNo);
        return clientService.getProgramInfoByEmpNo(empNo);
    }
}