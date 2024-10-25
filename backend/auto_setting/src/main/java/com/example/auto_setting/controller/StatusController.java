package com.example.auto_setting.controller;

import com.example.auto_setting.service.ProgramDto;
import com.example.auto_setting.service.StatusService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StatusController {

    @Autowired
    private final StatusService statusService;

    //프로그램 실행 여부 적재
    @PostMapping("/insertStatus")
    public ResponseEntity<?> insertProgramStatus(@RequestBody ProgramDto programDto) {
        try {
            String empNo = programDto.getEmpNo();  // 직원 번호
            String pgmId = programDto.getPgmId();  // 프로그램 ID
            Integer scssYn = programDto.getScssYn();     // 상태값 (0 또는 1)

            // 서비스 호출하여 상태 업데이트 처리
            statusService.insertProgramStatus(empNo, pgmId, scssYn);

            return new ResponseEntity<>("Status inserted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
