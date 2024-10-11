package com.example.auto_setting.controller;

import com.example.auto_setting.service.ProgramService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> verifyLogin(@RequestBody Map<String, String> empInfo) {
        String empNo = empInfo.get("EMP_NO");

        // 서비스에서 로그인 검증 및 EMP_NM 반환
        String empName = programService.verifyLogin(empNo);

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
