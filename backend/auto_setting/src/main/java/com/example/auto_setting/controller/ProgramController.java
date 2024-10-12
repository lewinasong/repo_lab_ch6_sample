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
@RequestMapping("/api/program")
public class ProgramController {
    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }


    //프로그램 등록
    @PostMapping("/register")
    public void InsertProgram(@RequestBody ProgramDto programDto){
        String pgmNm = programDto.getPgmNm();
        String empNo = programDto.getEmpNo();
        String filePath = programDto.getFilePath();
        Integer regYn = 1; //등록시 1, 해제시 0

        System.out.println(pgmNm);
        System.out.println(empNo);
        System.out.println(filePath);

        // 서비스 호출하여 업데이트 처리
        programService.saveProgram(pgmNm, empNo, filePath, regYn);

    }

    //프로그램 수정
    @PostMapping("/modify")
    public void UpdateProgram(@RequestBody ProgramDto programDto){
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
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ProgramDto {
        private String pgmId;
        private String pgmNm;
        private String empNo;
        private String filePath;

        // @minsoo: TODO: Getter와 Setter는 @Data에 포함되어 있으니 제거하셔도 됩니다!
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


}
