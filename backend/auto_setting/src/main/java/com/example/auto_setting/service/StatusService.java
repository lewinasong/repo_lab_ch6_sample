package com.example.auto_setting.service;

import com.example.auto_setting.controller.StatusController;
import com.example.auto_setting.persistence.StatusPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusPersistence statusPersistence;

    //프로그램 실행 여부 적재
    public void insertProgramStatus(String empNo, String pgmId, Integer scssYn) {
        System.out.println("Insert Status Service");

        try {
            statusPersistence.insertStatus(empNo, pgmId, scssYn);
        } catch (DataAccessException e) {
            throw new RuntimeException("INSERT operation failed: " + e.getMessage());
        }
    }

    public List<StatusPersistence.PGM_EXEC_PTCL> findRecordsByEmpNoAndToday(String empNo) {
        LocalDate today = LocalDate.now();
        return statusPersistence.findByEmpNoAndPgmStrDt(empNo, today);
    }


}