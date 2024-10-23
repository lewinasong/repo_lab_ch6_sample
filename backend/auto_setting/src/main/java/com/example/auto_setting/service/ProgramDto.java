package com.example.auto_setting.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDto {
    private Long pgmId;
    private String pgmNm;
    private String empNo;
    private String filePath;
    private Integer sleepTime;
    private LocalDateTime sysRegDtm;
    private LocalDateTime sysUpdDtm;
    private Integer scssYn;

}
