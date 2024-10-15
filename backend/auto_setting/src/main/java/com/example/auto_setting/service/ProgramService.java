package com.example.auto_setting.service;

import com.example.auto_setting.persistence.ProgramPersistence;
import com.example.auto_setting.service.ProgramDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramPersistence programPersistence;

    // 프로그램 조회
    public List<ProgramDto> getProgramByEmpNo(String empNo) {
        return programPersistence.getProgramByEmpNo(empNo);
    }

    // 프로그램 등록
    public void saveProgram(String pgmNm, String empNo, String filePath, Integer regYn) {
        try {
            programPersistence.saveProgram(pgmNm, empNo, filePath, regYn);
        } catch (DataAccessException e) {
            throw new RuntimeException("INSERT operation failed: " + e.getMessage());
        }
    }

    // 프로그램 수정
    public void updateProgram(String pgmId, String pgmNm, String empNo, String filePath) {
        try {
            programPersistence.updateProgram(pgmId, pgmNm, empNo, filePath);
        } catch (DataAccessException e) {
            throw new RuntimeException("UPDATE operation failed: " + e.getMessage());
        }
    }
}