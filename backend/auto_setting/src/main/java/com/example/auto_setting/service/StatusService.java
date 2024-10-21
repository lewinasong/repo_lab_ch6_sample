package com.example.auto_setting.service;

import com.example.auto_setting.persistence.StatusPersistence;
import com.example.auto_setting.service.ProgramDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusPersistence statusPersistence;

    //프로그램 실행 여부 적재
    public void updateProgramStatus(String pgmId, Integer scssYn) {
        try {
            statusPersistence.updateStatus(pgmId, scssYn);
        } catch (DataAccessException e) {
            throw new RuntimeException("INSERT operation failed: " + e.getMessage());
        }
    }


}
