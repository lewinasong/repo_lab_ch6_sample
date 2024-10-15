package com.example.auto_setting.controller;


import com.example.auto_setting.service.EmployeeService;
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

    @GetMapping(path = "/login/{empNo}")
    public String login(
            @PathVariable String empNo
    ){
        System.out.println("Please enter empNo:");
        boolean loginSuccess = employeeService.validateEmployee(empNo);



        if (loginSuccess) {
            System.out.println("empNo: "+empNo);
            return "로그인 성공";
        } else {
            System.out.println("empNo: "+empNo);
            return "로그인 실패";
        }
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class ProgramDto {
    private String empNo;

    // @minsoo: TODO: Getter와 Setter는 @Data에 포함되어 있으니 제거하셔도 됩니다!

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

}


