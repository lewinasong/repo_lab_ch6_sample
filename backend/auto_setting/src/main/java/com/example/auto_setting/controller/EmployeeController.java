package com.example.auto_setting.controller;


import com.example.auto_setting.service.EmployeeService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //@CrossOrigin(origins = "http://localhost:8089") // 프론트엔드 주소
    @CrossOrigin(origins = "http://3.209.94.202:8089") // 프론트엔드 주소
    @GetMapping(path = "/login/{empNo}")
    public String login(
            @PathVariable String empNo
    ) {
        System.out.println("Please enter empNo:");

        ProgramDto programDto = ProgramDto.builder()
                .empNo(empNo)
                .build();

        boolean loginSuccess = employeeService.validateEmployee(programDto);

        if (loginSuccess) {
            //로그인 성공 시 직원 이름을 조회
            String employeeName = employeeService.getEmployeeName(empNo);
            System.out.println("empNo: " + empNo);
            return "로그인 성공. 직원 이름: " + employeeName;
        } else {
            System.out.println("empNo: " + empNo);
            return "로그인 실패";
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProgramDto {
        private String empNo;


    }
}


