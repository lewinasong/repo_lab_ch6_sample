package com.example.auto_setting.service;

import com.example.auto_setting.entity.ProgramList;
import com.example.auto_setting.persistence.ProgramListJdbcRepository;
import com.example.auto_setting.persistence.ProgramPersistence;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramPersistence programPersistence;
    private final ProgramListService programListService;
    private final ProgramListJdbcRepository programListRepository;

    // 프로그램 조회
    public List<ProgramDto> getProgramByEmpNo(String empNo) {
        return programPersistence.getProgramByEmpNo(empNo);
    }

    // 프로그램 등록
    public void saveProgram(String pgmNm, String empNo, String filePath, Integer regYn, Integer sleepTime) {
        Long pgmId;
        try {
            pgmId = programPersistence.saveProgram(pgmNm, empNo, filePath, regYn, sleepTime);
        } catch (DataAccessException e) {
            throw new RuntimeException("INSERT operation failed: " + e.getMessage());
        }

        // PGM_EXEC_BASE 처리
        // 직원 번호로 Program ID 리스트 조회
        ProgramList chkProgramList = programListRepository.findByEmpNo(empNo);
        System.out.println("existingProgramIdList" + chkProgramList);

        if (chkProgramList != null) {
            System.out.println("existingProgramIdList is not null");
            // 직원 번호로 Program ID 리스트 조회
            List<Long> existingProgramIdList = programListService.getProgramIdListByEmpNo(empNo);

            // 기존 데이터가 있는 경우: 새로운 Program ID를 리스트에 추가
            System.out.println("existingProgramIdList1 " + existingProgramIdList);
            try {
                existingProgramIdList.add(pgmId);
                System.out.println("pgmId " + pgmId);
                System.out.println("existingProgramIdList2 " + existingProgramIdList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 업데이트 메서드 호출
            programListService.updateProgramExecutionOrder(empNo, existingProgramIdList);
        } else {
            // 데이터가 없는 경우: 새로 생성
            List<Long> programIdList = Collections.singletonList(pgmId);
            programListService.createProgramList(empNo, programIdList);
            System.out.println("programIdlist" + programIdList);
        }
    }

    // 프로그램 수정
    public void updateProgram(Long pgmId, String pgmNm, String empNo, String filePath, Integer sleepTime) {
        try {
            programPersistence.updateProgram(pgmId, pgmNm, empNo, filePath, sleepTime);
        } catch (DataAccessException e) {
            throw new RuntimeException("UPDATE operation failed: " + e.getMessage());
        }
    }

    // 프로그램 삭제 (REG_YN을 0으로 업데이트)
    public void deleteProgram(String empNo, Long pgmId) {
        try {
            // persistence 레이어 호출
            programPersistence.deleteProgram(pgmId.intValue());
        } catch (DataAccessException e) {
            throw new RuntimeException("Delete operation failed: " + e.getMessage());
        }

        // PGM_EXEC_BASE 처리
        // 직원 번호로 Program ID 리스트 조회
        List<Long> existingProgramIdList = programListService.getProgramIdListByEmpNo(empNo);

        if (existingProgramIdList.contains(pgmId)) {
            existingProgramIdList.remove(pgmId); // pgmId 값을 리스트에서 제거
        } else {
            throw new RuntimeException("해당 프로그램 ID가 실행 테이블에 존재하지 않습니다: " + pgmId);
        }

        // 삭제한 값을 updateProgramExecutionOrder를 통해 업데이트
        programListService.updateProgramExecutionOrder(empNo, existingProgramIdList);
    }
}
