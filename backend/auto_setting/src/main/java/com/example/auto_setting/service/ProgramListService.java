package com.example.auto_setting.service;

import com.example.auto_setting.entity.ProgramList;
import com.example.auto_setting.persistence.ProgramListJdbcRepository;
import com.example.auto_setting.util.ProgramListConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgramListService {

    private final ProgramListJdbcRepository programListRepository;

    // 직원 번호로 Program List를 조회하여 프로그램 ID 리스트 반환
    public List<Long> getProgramIdListByEmpNo(@NonNull final String empNo) {
        ProgramList programList = programListRepository.findByEmpNo(empNo);
        List<Integer> programIdList = ProgramListConverter.convertStringToList(programList.getProgramIdList());

        return programIdList.stream()
                .map(Integer::longValue) // Integer를 Long으로 변환
                .collect(Collectors.toList());
    }

    // 직원 번호로 Program List와 Program Name을 조합하여 반환하는 서비스 메서드
    public List<ProgramDto> getProgramListWithNameByEmpNo(@NonNull String empNo) {
        return programListRepository.findProgramListWithNameByEmpNo(empNo);
    }

    // 직원 번호로 Program List를 생성하는 메서드
    public void createProgramList(@NonNull final String empNo, @NonNull final List<Long> programList) {
        // 빈 리스트나 null이 전달될 경우 "N/A"로 기본값 설정
        String convertedProgramIdList = (programList == null || programList.isEmpty())
                ? "N/A"
                : ProgramListConverter.convertListToString(
                programList.stream().map(Long::intValue).collect(Collectors.toList())
        );

        programListRepository.create(empNo, convertedProgramIdList);
    }

    // 직원 번호로 Program List를 업데이트하는 메서드
    public void updateProgramExecutionOrder(String empNo, List<Long> newProgramOrder) {
        // 로깅을 추가하여 empNo와 newProgramOrder 값 확인
        log.info("Received update request for empNo: {}", empNo);

        if (newProgramOrder == null) {
            log.warn("Program order is NULL for empNo: {}", empNo);
        } else if (newProgramOrder.isEmpty()) {
            log.warn("Program order is empty for empNo: {}", empNo);
        } else {
            log.info("Program order to update: {}", newProgramOrder);
        }

        if (newProgramOrder == null || newProgramOrder.isEmpty()) {
            return; // 빈 리스트일 경우 업데이트를 중단하고 기존 데이터 유지
        }

        String updatedOrder = ProgramListConverter.convertListToString(
                newProgramOrder.stream().map(Long::intValue).collect(Collectors.toList())
        );

        log.info("Final updated order string for empNo {}: {}", empNo, updatedOrder);
        programListRepository.update(empNo, updatedOrder);
    }

    // 직원 번호로 Program List를 삭제하는 메서드
    public void deleteProgramList(@NonNull final String empNo) {
        programListRepository.deleteByEmpNo(empNo);
    }
}
