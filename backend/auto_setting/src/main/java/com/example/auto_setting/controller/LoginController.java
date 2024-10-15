package com.example.auto_setting.controller;

import com.example.auto_setting.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private ProgramService programService;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> verifyLogin(@RequestBody Map<String, String> empInfo) {
        String empNo = empInfo.get("EMP_NO");

        // 서비스에서 로그인 검증 및 EMP_NM 반환
        // @minsoo: 실행을 위해 잠시 주석 처리 해두었습니다.
//        String empName = programService.verifyLogin(empNo);
        String empName = null;

        if (empName != null) {
            // 정상 처리 시 EMP_NM 반환
            Map<String, String> response = new HashMap<>();
            response.put("EMP_NO", empNo);
            response.put("EMP_NM", empName);
            return ResponseEntity.ok(response);
        } else {
            // 실패 시 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
