package com.example.auto_setting.controller;

import com.example.auto_setting.service.ProgramDto;
import com.example.auto_setting.service.ProgramService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService programService;

    @CrossOrigin(origins = "http://10.233.4.217:8089") // 프론트엔드 주소

    // 프로그램 조회
    @GetMapping("/PagePgmBase/{empNo}")
    public ResponseEntity<?> getProgramByEmpNo(@PathVariable String empNo) {
        try {
            // 서비스 호출하여 데이터 조회
            List<ProgramDto> programs = programService.getProgramByEmpNo(empNo);
            return new ResponseEntity<>(programs, HttpStatus.OK);
        } catch (RuntimeException e) {
            // 예외 발생 시 500 반환
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //프로그램 등록
    @PostMapping("/register")
    public ResponseEntity<?> InsertProgram(@RequestBody ProgramDto programDto){
        try{
            String pgmNm = programDto.getPgmNm();
            String empNo = programDto.getEmpNo();
            String filePath = programDto.getFilePath();
            Integer regYn = 1; //등록시 1, 해제시 0
            Integer sleepTime = programDto.getSleepTime();

            System.out.println(pgmNm);
            System.out.println(empNo);
            System.out.println(filePath);

            // 서비스 호출하여 업데이트 처리
            programService.saveProgram(pgmNm, empNo, filePath, regYn, sleepTime);
            return new ResponseEntity<>("Program registered successfully", HttpStatus.OK);
        } catch (RuntimeException e) {//
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //프로그램 수정
    @PostMapping("/modify")
    public ResponseEntity<?> UpdateProgram(@RequestBody ProgramDto programDto){
        try{
            Long pgmId = programDto.getPgmId();
            String pgmNm = programDto.getPgmNm();
            String empNo = programDto.getEmpNo();
            String filePath = programDto.getFilePath();
            Integer sleepTime = programDto.getSleepTime();

            System.out.println(pgmId);
            System.out.println(pgmNm);
            System.out.println(empNo);
            System.out.println(filePath);

            // 서비스 호출하여 업데이트 처리
            programService.updateProgram(pgmId, pgmNm, empNo, filePath, sleepTime);
            return new ResponseEntity<>("Program updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 프로그램 삭제 (REG_YN을 0으로 업데이트)
    @PostMapping("/delete")
    public ResponseEntity<?> deleteProgram(@RequestBody ProgramDto programDto) {
        try {
            Long pgmId = programDto.getPgmId();
            String empNo = programDto.getEmpNo();


            // 서비스 호출하여 REG_YN을 0으로 업데이트
            System.out.println(pgmId);
            programService.deleteProgram(empNo, pgmId);
            return new ResponseEntity<>("Program deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}