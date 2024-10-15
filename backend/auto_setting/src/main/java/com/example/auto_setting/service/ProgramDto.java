package com.example.auto_setting.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDto {
    private String pgmId;
    private String pgmNm;
    private String empNo;
    private String filePath;
    private LocalDateTime sysRegDtm;
    private LocalDateTime sysUpdDtm;

    // Getters and Setters
    /*
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

     */
}
