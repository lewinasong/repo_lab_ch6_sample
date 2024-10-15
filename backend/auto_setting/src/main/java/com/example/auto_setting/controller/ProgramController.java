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
@RequestMapping("/api/program")
public class ProgramController {
    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    // 프로그램 조회
    @GetMapping("PagePgmBase/{empNo}")
    public ResponseEntity<?> getProgramByEmpNo(@PathVariable String empNo) {
        try {
            // 서비스 호출하여 데이터 조회
            List<ProgramDto> programs = programService.getProgramByEmpNo(empNo);
            return new ResponseEntity<>(programs, HttpStatus.OK);
        } catch (RuntimeException e) {
            // 예외 발생 시 404 반환
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
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

            System.out.println(pgmNm);
            System.out.println(empNo);
            System.out.println(filePath);

            // 서비스 호출하여 업데이트 처리
            programService.saveProgram(pgmNm, empNo, filePath, regYn);
            return new ResponseEntity<>("Program registered successfully", HttpStatus.OK);
        } catch (RuntimeException e) {//
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //프로그램 수정
    @PostMapping("/modify")
    public ResponseEntity<?> UpdateProgram(@RequestBody ProgramDto programDto){
        try{
            String pgmId = programDto.getPgmId();
            String pgmNm = programDto.getPgmNm();
            String empNo = programDto.getEmpNo();
            String filePath = programDto.getFilePath();

            System.out.println(pgmId);
            System.out.println(pgmNm);
            System.out.println(empNo);
            System.out.println(filePath);

            // 서비스 호출하여 업데이트 처리
            programService.updateProgram(pgmId, pgmNm, empNo, filePath);
            return new ResponseEntity<>("Program updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

/*
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProgramDto {
        private String pgmId;
        private String pgmNm;
        private String empNo;
        private String filePath;
        private LocalDateTime sysRegDtm;
        private LocalDateTime sysUpdDtm;

        // Getters and Setters
        public String getPgmId() { return pgmId; }

        public void setPgmId(String pgmId) { this.pgmId = pgmId; }

        public String getPgmNm() {
            return pgmNm;
        }

        public void setPgmNm(String pgmNm) {
            this.pgmNm = pgmNm;
        }

        public String getEmpNo() {
            return empNo;
        }

        public void setEmpNo(String empNo) {
            this.empNo = empNo;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {this.filePath = filePath; }
    }
*/

}