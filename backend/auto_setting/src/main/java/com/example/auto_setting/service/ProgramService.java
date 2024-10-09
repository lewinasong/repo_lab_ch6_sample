package com.example.auto_setting.service;

import com.example.auto_setting.persistence.ProgramPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramPersistence programPersistence;

    // 프로그램 등록
    public void saveProgram(String pgmNm, String empNo, String filePath, Integer regYn) {
        programPersistence.saveProgram(pgmNm, empNo, filePath, regYn);
    }

    // 프로그램 수정
    public void updateProgram(String pgmId, String pgmNm, String empNo, String filePath) {
        programPersistence.updateProgram(pgmId, pgmNm, empNo, filePath);
    }



}
